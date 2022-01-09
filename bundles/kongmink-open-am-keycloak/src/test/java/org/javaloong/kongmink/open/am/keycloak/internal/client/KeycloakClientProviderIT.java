package org.javaloong.kongmink.open.am.keycloak.internal.client;

import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import org.javaloong.kongmink.open.am.client.ClientException;
import org.javaloong.kongmink.open.am.client.ClientProvider;
import org.javaloong.kongmink.open.am.client.model.Client;
import org.javaloong.kongmink.open.am.embedded.keycloak.KeycloakServerProperties;
import org.javaloong.kongmink.open.am.keycloak.internal.EmbeddedKeycloakTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.OAuth2Constants;
import org.osgi.util.converter.Converters;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KeycloakClientProviderIT extends EmbeddedKeycloakTestSupport {

    @TestServerConfiguration
    static EmbeddedJettyConfiguration jettyConfiguration() {
        return EmbeddedJettyConfiguration.builder()
                .withOverrideDescriptor("src/test/resources/web.xml")
                .withProperty(KeycloakServerProperties.REALM_CONFIGURATION_PATH, "demo-realm.json")
                .build();
    }

    private ClientProvider clientProvider;

    @BeforeEach
    public void setUp(EmbeddedJetty jetty) {
        KeycloakAdminClient adminClient = new KeycloakAdminClient(createAdminClientConfig(jetty));
        clientProvider = new KeycloakClientProvider(adminClient);
    }

    @Test
    public void createClient_ClientExists_ThrowException() {
        ClientException exception = assertThrows(ClientException.class, () -> {
            Client client = new Client();
            client.setClientId("example-client");
            clientProvider.create(client);
        });
        assertThat(exception.getErrorCode()).isEqualTo(ClientException.CLIENT_EXISTS);
    }

    @Test
    public void createClient() {
        Client client = new Client();
        client.setClientId("new-client");
        client.setName("New Client");
        client.setDescription("New Client");
        client.setRedirectUris(Collections.singletonList("/new-client/*"));
        client.setGrantTypes(Arrays.asList(OAuth2Constants.AUTHORIZATION_CODE, OAuth2Constants.REFRESH_TOKEN));
        client.setDefaultScopes(Arrays.asList("profile", "email"));
        client.setAllowedScopes(Arrays.asList("address", "phone"));
        Client savedClient = clientProvider.create(client);
        assertThat(savedClient.getClientId()).isEqualTo("new-client");
        assertThat(savedClient.getGrantTypes()).contains(
                OAuth2Constants.AUTHORIZATION_CODE, OAuth2Constants.REFRESH_TOKEN);
        assertThat(savedClient.getDefaultScopes()).contains("profile", "email");
        assertThat(savedClient.getAllowedScopes()).contains("address", "phone");
    }

    @Test
    public void updateClient() {
        String id = "b88ce206-63d6-43b6-87c9-ea09d8c02f32";
        Client client = new Client();
        client.setId(id);
        client.setClientId("example-client");
        client.setName("Example Client");
        client.setDescription("Example Description");
        client.setEnabled(true);
        client.setSecret("example-client-secret");
        client.setGrantTypes(Collections.singletonList(OAuth2Constants.CLIENT_CREDENTIALS));
        clientProvider.updateClient(client);
        Optional<Client> result = clientProvider.findById(id);
        result.ifPresent(c -> {
            assertThat(c.getDescription()).isEqualTo("Example Description");
            assertThat(c.getGrantTypes()).containsExactly(OAuth2Constants.CLIENT_CREDENTIALS);
        });
    }

    @Test
    public void deleteClient() {
        String id = "6a4bfbd0-576d-4778-af56-56f876647355";
        clientProvider.deleteClient(id);
        Optional<Client> result = clientProvider.findById(id);
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void getClientSecret() {
        String id = "b88ce206-63d6-43b6-87c9-ea09d8c02f32";
        String secret = clientProvider.getSecret(id);
        assertThat(secret).isEqualTo("example-client-secret");
    }

    @Test
    public void regenerateClientSecret() {
        String id = "b88ce206-63d6-43b6-87c9-ea09d8c02f32";
        String secret = clientProvider.regenerateSecret(id);
        assertThat(secret).isNotEmpty();
    }

    @Test
    public void findClientById() {
        String id = "b88ce206-63d6-43b6-87c9-ea09d8c02f32";
        Optional<Client> result = clientProvider.findById(id);
        assertThat(result).hasValueSatisfying(client -> {
            assertThat(client.getClientId()).isEqualTo("example-client");
        });
    }

    @Test
    public void findClientByClientId() {
        String clientId = "example-client";
        Optional<Client> result = clientProvider.findByClientId(clientId);
        assertThat(result).hasValueSatisfying(client -> {
            assertThat(client.getName()).isEqualTo("Example Client");
        });
    }

    private KeycloakAdminClientConfiguration createAdminClientConfig(EmbeddedJetty jetty) {
        Map<String, Object> props = new HashMap<>();
        props.put("serverUrl", jetty.getUrl() + "auth");
        return Converters.standardConverter().convert(props).to(KeycloakAdminClientConfiguration.class);
    }
}
