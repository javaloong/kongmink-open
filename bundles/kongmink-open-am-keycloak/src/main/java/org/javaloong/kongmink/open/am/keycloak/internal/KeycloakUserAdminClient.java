package org.javaloong.kongmink.open.am.keycloak.internal;

import org.javaloong.kongmink.open.am.keycloak.internal.resource.UsersResource;
import org.keycloak.OAuth2Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Keycloak Users Admin Configuration")
@interface KeycloakUserAdminClientConfiguration {

    String serverUrl() default "http://localhost:8080/auth";

    String realm() default "kongmink-open";

    String clientId() default "keycloak-admin";

    String clientSecret() default "keycloak-admin-secret";
}

@Component(service = KeycloakUserAdminClient.class,
        configurationPid = KeycloakUserAdminClient.KEYCLOAK_USER_CONFIGURATION_PID)
@Designate(ocd = KeycloakUserAdminClientConfiguration.class)
public class KeycloakUserAdminClient extends KeycloakAdminClient {

    public static final String KEYCLOAK_USER_CONFIGURATION_PID = "org.javaloong.kongmink.open.am.keycloak.user";

    private final Config config;

    @Activate
    public KeycloakUserAdminClient(KeycloakUserAdminClientConfiguration adminClientConfiguration) {
        this.config = createConfig(adminClientConfiguration);
    }

    public UsersResource getUsersResource() {
        return getRealmResource(config).users();
    }

    private Config createConfig(KeycloakUserAdminClientConfiguration configuration) {
        return new Config(configuration.serverUrl(), configuration.realm(), null, null,
                configuration.clientId(), configuration.clientSecret(), OAuth2Constants.CLIENT_CREDENTIALS);
    }
}
