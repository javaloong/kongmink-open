package org.javaloong.kongmink.open.account.keycloak.internal;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.ResponseExceptionMapper;
import org.apache.cxf.jaxrs.utils.ExceptionUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.HTTPConduitConfigurer;
import org.apache.cxf.transport.http.auth.HttpAuthSupplier;
import org.javaloong.kongmink.open.account.keycloak.internal.resource.AccountRealmsResource;
import org.javaloong.kongmink.open.account.keycloak.internal.resource.AccountRestService;
import org.javaloong.kongmink.open.core.auth.UserTokenProvider;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.lang.reflect.Constructor;
import java.net.URI;

import static java.lang.Thread.currentThread;

@ObjectClassDefinition(name = "Keycloak Account Management Configuration")
@interface KeycloakAccountClientConfiguration {

    String serverUrl() default "http://localhost:8080/auth";

    String realm() default "kongmink-open";
}

@Component(service = KeycloakAccountClient.class,
        configurationPid = KeycloakAccountClient.KEYCLOAK_ACCOUNT_CONFIGURATION_PID)
@Designate(ocd = KeycloakAccountClientConfiguration.class)
public class KeycloakAccountClient {

    public static final String KEYCLOAK_ACCOUNT_CONFIGURATION_PID = "org.javaloong.kongmink.open.account.keycloak";

    private final KeycloakAccountClientConfiguration config;

    @Reference
    UserTokenProvider userTokenProvider;

    AccountRealmsResource accountRealmsResource;

    Bus bus;

    @Activate
    public KeycloakAccountClient(KeycloakAccountClientConfiguration config) {
        this.config = config;
        this.accountRealmsResource = createJAXRSResource(AccountRealmsResource.class, config.realm());
    }

    public AccountRealmsResource getAccountRealmsResource() {
        return accountRealmsResource;
    }

    public AccountRestService getAccountRestService() {
        return getAccountRealmsResource().getAccountRestService();
    }

    public <T> T createJAXRSResource(Class<T> cls, Object... varValues) {
        bus = createBus();
        HTTPConduitConfigurer httpConduitConfigurer = (name, address, c) -> c.setAuthSupplier(new BearerAuthSupplier());
        bus.setExtension(httpConduitConfigurer, HTTPConduitConfigurer.class);
        JAXRSClientFactoryBean bean = new JAXRSClientFactoryBean();
        bean.setBus(bus);
        bean.setAddress(config.serverUrl());
        bean.setResourceClass(cls);
        bean.setProvider(new JacksonJsonProvider(createObjectMapper()));
        bean.setProvider(new RestClientExceptionMapper());
        return bean.create(cls, varValues);
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
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Deactivate
    public void deactivate() {
        bus.shutdown(false);
    }

    class BearerAuthSupplier implements HttpAuthSupplier {

        @Override
        public boolean requiresRequestCaching() {
            return true;
        }

        @Override
        public String getAuthorization(AuthorizationPolicy authPolicy, URI uri, Message message, String fullHeader) {
            return "Bearer " + userTokenProvider.getUserToken().getToken();
        }
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
