package org.javaloong.kongmink.open.am.keycloak.internal;

import org.javaloong.kongmink.open.am.keycloak.internal.resource.ClientsResource;
import org.keycloak.OAuth2Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Keycloak Clients Admin Configuration")
@interface KeycloakClientAdminClientConfiguration {

    String serverUrl() default "http://localhost:8080/auth";

    String realm() default "demo";

    String clientId() default "keycloak-admin";

    String clientSecret() default "keycloak-admin-secret";
}

@Component(service = KeycloakClientAdminClient.class,
        configurationPid = KeycloakClientAdminClient.KEYCLOAK_CLIENT_CONFIGURATION_PID)
@Designate(ocd = KeycloakClientAdminClientConfiguration.class)
public class KeycloakClientAdminClient extends KeycloakAdminClient {

    public static final String KEYCLOAK_CLIENT_CONFIGURATION_PID = "org.javaloong.kongmink.open.am.keycloak.client";

    private final Config config;

    @Activate
    public KeycloakClientAdminClient(KeycloakClientAdminClientConfiguration adminClientConfiguration) {
        this.config = createConfig(adminClientConfiguration);
    }

    public ClientsResource getClientsResource() {
        return getRealmResource(config).clients();
    }

    private Config createConfig(KeycloakClientAdminClientConfiguration configuration) {
        return new Config(configuration.serverUrl(), configuration.realm(), null, null,
                configuration.clientId(), configuration.clientSecret(), OAuth2Constants.CLIENT_CREDENTIALS);
    }
}
