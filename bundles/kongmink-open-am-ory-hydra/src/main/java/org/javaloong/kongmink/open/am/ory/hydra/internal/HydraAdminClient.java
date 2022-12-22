package org.javaloong.kongmink.open.am.ory.hydra.internal;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.ResponseExceptionMapper;
import org.apache.cxf.jaxrs.utils.ExceptionUtils;
import org.javaloong.kongmink.open.am.ory.hydra.internal.resource.AdminResource;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.lang.reflect.Constructor;
import java.util.Arrays;

import static java.lang.Thread.currentThread;

@Component(service = HydraAdminClient.class,
        configurationPid = HydraAdminClient.CONFIGURATION_PID)
@Designate(ocd = HydraAdminClient.Configuration.class)
public class HydraAdminClient {

    public static final String CONFIGURATION_PID = "org.javaloong.kongmink.open.am.ory.hydra";

    @ObjectClassDefinition(name = "Ory Hydra Clients Admin Configuration")
    @interface Configuration {

        String serverUrl() default "http://localhost:4445";

        String username() default "";

        String password() default "";

        String apiKeyHeaderName() default "X-API-KEY";

        String apiKey() default "";
    }

    AdminResource adminResource;

    Bus bus;

    @Activate
    public HydraAdminClient(Configuration config) {
        this.adminResource = createJAXRSResource(config, AdminResource.class);
    }

    public AdminResource getAdminResource() {
        return adminResource;
    }

    public <T> T createJAXRSResource(Configuration config, Class<T> cls) {
        bus = createBus();
        JAXRSClientFactoryBean bean = new JAXRSClientFactoryBean();
        bean.setBus(bus);
        bean.setAddress(config.serverUrl());
        bean.setResourceClass(cls);
        bean.setProvider(new JacksonJsonProvider(createObjectMapper()));
        bean.setProvider(new RestClientExceptionMapper());
        if (StringUtils.isNotEmpty(config.username())) {
            bean.setUsername(config.username());
            bean.setPassword(config.password());
        }
        if (StringUtils.isNotEmpty(config.apiKey())) {
            bean.getHeaders().put(config.apiKeyHeaderName(), Arrays.asList(config.apiKey()));
        }
        return bean.create(cls);
    }

    private Bus createBus() {
        final ClassLoader ldr = currentThread().getContextClassLoader();
        ClassLoaderUtils.setThreadContextClassloader(getClass().getClassLoader());
        try {
            return BusFactory.newInstance().createBus();
        } finally {
            currentThread().setContextClassLoader(ldr);
        }
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Deactivate
    public void deactivate() {
        bus.shutdown(false);
    }

    static class RestClientExceptionMapper implements ResponseExceptionMapper<Exception> {

        @Override
        public Exception fromResponse(Response response) {
            int status = response.getStatus();
            if (status >= 400) {
                String message = response.readEntity(String.class);
                return toWebApplicationException(
                        new RestClientException(message), response);
            }
            return null;
        }

        private WebApplicationException toWebApplicationException(Throwable cause, Response response) {
            try {
                final Class<?> exceptionClass = ExceptionUtils.getWebApplicationExceptionClass(response,
                        WebApplicationException.class);
                final Constructor<?> ctr = exceptionClass.getConstructor(Response.class, Throwable.class);
                return (WebApplicationException) ctr.newInstance(response, cause);
            } catch (Throwable ex) {
                return new WebApplicationException(cause, response);
            }
        }
    }

    static class RestClientException extends RuntimeException {

        RestClientException(String message) {
            super(message);
        }
    }
}
