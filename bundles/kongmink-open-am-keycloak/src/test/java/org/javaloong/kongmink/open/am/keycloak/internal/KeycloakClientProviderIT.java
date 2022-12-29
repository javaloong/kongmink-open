package org.javaloong.kongmink.open.am.keycloak.internal;

import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import org.javaloong.kongmink.keycloak.embedded.KeycloakServerProperties;
import org.javaloong.kongmink.open.am.ClientProvider;
import org.javaloong.kongmink.open.common.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.OAuth2Constants;
import org.osgi.util.converter.Converters;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

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
        KeycloakClientAdminClient adminClient = new KeycloakClientAdminClient(createAdminClientConfig(jetty));
        clientProvider = new KeycloakClientProvider(adminClient);
    }

    @Test
    public void createClient() {
        Client client = new Client();
        client.setClientId("new-client");
        client.setClientName("New Client");
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
        Client client = new Client();
        client.setClientId("example-client");
        client.setClientUri("https://example.com");
        client.setClientSecret("example-client-secret");
        client.setGrantTypes(Collections.singletonList(OAuth2Constants.CLIENT_CREDENTIALS));
        client.setEnabled(true);
        clientProvider.update(client);
        Optional<Client> result = clientProvider.findById("example-client");
        result.ifPresent(c -> {
            assertThat(c.getClientUri()).isEqualTo("https://example.com");
            assertThat(c.getGrantTypes()).containsExactly(OAuth2Constants.CLIENT_CREDENTIALS);
        });
    }

    @Test
    public void deleteClient() {
        String clientId = "other-client";
        clientProvider.delete(clientId);
        Optional<Client> result = clientProvider.findById(clientId);
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void getClientSecret() {
        String clientId = "example-client";
        String secret = clientProvider.getSecret(clientId);
        assertThat(secret).isEqualTo("example-client-secret");
    }

    @Test
    public void regenerateClientSecret() {
        String clientId = "example-client";
        String secret = clientProvider.regenerateSecret(clientId);
        assertThat(secret).isNotEmpty();
    }

    @Test
    public void findClientById() {
        String clientId = "example-client";
        Optional<Client> result = clientProvider.findById(clientId);
        assertThat(result).hasValueSatisfying(client -> {
            assertThat(client.getClientName()).isEqualTo("Example Client");
        });
    }

    private KeycloakClientAdminClientConfiguration createAdminClientConfig(EmbeddedJetty jetty) {
        Map<String, Object> props = new HashMap<>();
        props.put("serverUrl", jetty.getUrl() + "auth");
        return Converters.standardConverter().convert(props).to(KeycloakClientAdminClientConfiguration.class);
    }
}
