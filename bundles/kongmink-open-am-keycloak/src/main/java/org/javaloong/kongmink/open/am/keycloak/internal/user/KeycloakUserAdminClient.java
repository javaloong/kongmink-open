package org.javaloong.kongmink.open.am.keycloak.internal.user;

import org.javaloong.kongmink.open.am.keycloak.internal.KeycloakAdminClient;
import org.javaloong.kongmink.open.am.keycloak.internal.Config;
import org.javaloong.kongmink.open.am.keycloak.internal.resource.RealmResource;
import org.javaloong.kongmink.open.am.keycloak.internal.resource.RealmsResource;
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

    private final RealmResource realmResource;

    @Activate
    public KeycloakUserAdminClient(KeycloakUserAdminClientConfiguration adminClientConfiguration) {
        Config config = createConfig(adminClientConfiguration);
        RealmsResource realmsResource = createJAXRSResource(config, RealmsResource.class);
        realmResource = realmsResource.realm(config.getRealm());
    }

    public UsersResource getUsersResource() {
        return realmResource.users();
    }

    private Config createConfig(KeycloakUserAdminClientConfiguration configuration) {
        return new Config(configuration.serverUrl(), configuration.realm(), null, null,
                configuration.clientId(), configuration.clientSecret(), OAuth2Constants.CLIENT_CREDENTIALS);
    }
}
