package org.javaloong.kongmink.open.apim.gravitee.internal;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.HTTPConduitConfigurer;
import org.apache.cxf.transport.http.auth.HttpAuthSupplier;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.*;
import org.javaloong.kongmink.open.common.auth.SecurityContextProvider;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.TokenEntity;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import java.net.URI;

import static java.lang.Thread.currentThread;

@ObjectClassDefinition(name = "Gravitee Portal Client Configuration")
@interface GraviteePortalClientConfiguration {

    String serverUrl() default "http://localhost:8083/portal";

    String environment() default "DEFAULT";

    String identityProvider() default "keycloak";

    long cacheExpireAfterAccess() default 600_000;

    long cacheMaximumSize() default 10_1000;
}

@Component(service = GraviteePortalClient.class,
        configurationPid = GraviteePortalClient.GRAVITEE_PORTAL_CONFIGURATION_PID)
@Designate(ocd = GraviteePortalClientConfiguration.class)
public class GraviteePortalClient {

    public static final String GRAVITEE_PORTAL_CONFIGURATION_PID = "org.javaloong.kongmink.open.apim.gravitee.portal";

    private final GraviteePortalClientConfiguration config;

    @Reference
    SecurityContextProvider securityContextProvider;

    GraviteeTokenManager tokenManager;

    EnvironmentsResource environmentsResource;

    @Activate
    public GraviteePortalClient(GraviteePortalClientConfiguration portalClientConfiguration) {
        this.config = portalClientConfiguration;
        this.tokenManager = new GraviteeTokenManager(config);
        this.environmentsResource = createJAXRSResource(EnvironmentsResource.class, config.environment());
    }

    public EnvironmentsResource getEnvironmentsResource() {
        return environmentsResource;
    }

    public String getSource() {
        return config.identityProvider();
    }

    public ApisResource getApisResource() {
        return getEnvironmentsResource().getApisResource();
    }

    public ApplicationsResource getApplicationsResource() {
        return getEnvironmentsResource().getApplicationsResource();
    }

    public CategoriesResource getCategoriesResource() {
        return getEnvironmentsResource().getCategoriesResource();
    }

    public SubscriptionsResource getSubscriptionsResource() {
        return getEnvironmentsResource().getSubscriptionsResource();
    }

    public UserResource getUserResource() {
        return getEnvironmentsResource().getUserResource();
    }

    public TokenEntity tokenExchange() {
        String accessToken = securityContextProvider.getContext().getToken();
        return tokenManager.getToken(accessToken, token -> {
            AuthResource authResource = environmentsResource.getAuthResource();
            return authResource.getOAuth2AuthenticationResource(config.identityProvider())
                    .tokenExchange(token);
        });
    }

    public <T> T createJAXRSResource(Class<T> cls, Object... varValues) {
        Bus bus = getBus();
        HTTPConduitConfigurer httpConduitConfigurer = (name, address, c) -> c.setAuthSupplier(new BearerAuthSupplier());
        bus.setExtension(httpConduitConfigurer, HTTPConduitConfigurer.class);
        JAXRSClientFactoryBean bean = new JAXRSClientFactoryBean();
        bean.setAddress(config.serverUrl());
        bean.setResourceClass(cls);
        bean.setProvider(new JacksonJsonProvider(createObjectMapper()));
        return bean.create(cls, varValues);
    }

    private Bus getBus() {
        final ClassLoader ldr = currentThread().getContextClassLoader();
        ClassLoaderUtils.setThreadContextClassloader(getClass().getClassLoader());
        try {
            return BusFactory.getThreadDefaultBus();
        } finally {
            currentThread().setContextClassLoader(ldr);
        }
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    class BearerAuthSupplier implements HttpAuthSupplier {

        @Override
        public boolean requiresRequestCaching() {
            return true;
        }

        @Override
        public String getAuthorization(AuthorizationPolicy authPolicy, URI uri, Message message, String fullHeader) {
            URI authURI = WebClient.client(getEnvironmentsResource().getAuthResource()).getCurrentURI();
            if (uri.getPath().contains(authURI.getPath())) {
                return null;
            }

            return "Bearer " + tokenExchange().getToken();
        }
    }
}
