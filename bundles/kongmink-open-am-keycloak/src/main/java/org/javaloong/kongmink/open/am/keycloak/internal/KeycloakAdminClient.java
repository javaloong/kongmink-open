package org.javaloong.kongmink.open.am.keycloak.internal;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.rs.security.oauth2.client.BearerAuthSupplier;
import org.apache.cxf.rs.security.oauth2.client.Consumer;
import org.apache.cxf.rs.security.oauth2.client.OAuthClientUtils;
import org.apache.cxf.rs.security.oauth2.common.ClientAccessToken;
import org.apache.cxf.rs.security.oauth2.grants.clientcred.ClientCredentialsGrant;
import org.apache.cxf.transport.http.HTTPConduitConfigurer;
import org.javaloong.kongmink.open.am.keycloak.internal.resource.RealmResource;
import org.javaloong.kongmink.open.am.keycloak.internal.resource.RealmsResource;

import static java.lang.Thread.currentThread;

public abstract class KeycloakAdminClient {

    public static final String TOKEN_URI_FORMAT = "%s/realms/%s/protocol/openid-connect/token";

    private final Object lock = new Object();

    private RealmResource realmResource;

    public RealmResource getRealmResource(Config config) {
        if (realmResource == null) {
            synchronized (lock) {
                if (realmResource == null) {
                    RealmsResource realmsResource = createJAXRSResource(config, RealmsResource.class);
                    realmResource = realmsResource.realm(config.getRealm());
                }
            }
        }
        return realmResource;
    }

    public <T> T createJAXRSResource(Config config, Class<T> cls) {
        Bus bus = getBus();
        BearerAuthSupplier supplier = createBearerAuthSupplier(config);
        HTTPConduitConfigurer httpConduitConfigurer = (name, address, c) -> c.setAuthSupplier(supplier);
        bus.setExtension(httpConduitConfigurer, HTTPConduitConfigurer.class);
        JAXRSClientFactoryBean bean = new JAXRSClientFactoryBean();
        bean.setAddress(config.getServerUrl());
        bean.setResourceClass(cls);
        bean.setProvider(new JacksonJsonProvider());
        return bean.create(cls);
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

    private BearerAuthSupplier createBearerAuthSupplier(Config config) {
        String tokenUri = String.format(TOKEN_URI_FORMAT, config.getServerUrl(), config.getRealm());
        Consumer consumer = new Consumer(config.getClientId(), config.getClientSecret());
        BearerAuthSupplier supplier = new BearerAuthSupplier();
        supplier.setConsumer(consumer);
        supplier.setAccessTokenServiceUri(tokenUri);
        ClientAccessToken token = getClientAccessToken(tokenUri, consumer);
        supplier.setAccessToken(token.getTokenKey());
        supplier.setRefreshToken(token.getRefreshToken());
        return supplier;
    }

    private ClientAccessToken getClientAccessToken(String tokenUri, Consumer consumer) {
        ClientCredentialsGrant grant = new ClientCredentialsGrant();
        grant.setClientId(consumer.getClientId());
        grant.setClientSecret(consumer.getClientSecret());
        return OAuthClientUtils.getAccessToken(tokenUri, consumer, grant, true);
    }
}
