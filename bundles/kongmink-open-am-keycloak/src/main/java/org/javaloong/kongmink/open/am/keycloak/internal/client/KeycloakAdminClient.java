package org.javaloong.kongmink.open.am.keycloak.internal.client;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Keycloak Admin Client Configuration")
@interface KeycloakAdminClientConfiguration {

    String serverUrl() default "http://localhost:8080/auth";

    String realm() default "demo";

    String clientId() default "keycloak-admin";

    String clientSecret() default "keycloak-admin-secret";
}

@Component(service = KeycloakAdminClient.class,
        configurationPid = KeycloakAdminClient.KEYCLOAK_CLIENT_CONFIGURATION_PID)
@Designate(ocd = KeycloakAdminClientConfiguration.class)
public class KeycloakAdminClient {

    public static final String KEYCLOAK_CLIENT_CONFIGURATION_PID = "org.javaloong.kongmink.open.am.keycloak.client";

    private final Keycloak keycloak;
    private final RealmResource realmResource;

    @Activate
    public KeycloakAdminClient(KeycloakAdminClientConfiguration adminClientConfiguration) {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(adminClientConfiguration.serverUrl())
                .realm(adminClientConfiguration.realm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(adminClientConfiguration.clientId())
                .clientSecret(adminClientConfiguration.clientSecret())
                .build();

        this.realmResource = keycloak.realm(adminClientConfiguration.realm());
    }

    public Keycloak getKeycloak() {
        return keycloak;
    }

    public ClientsResource getClientsResource() {
        return realmResource.clients();
    }
}
