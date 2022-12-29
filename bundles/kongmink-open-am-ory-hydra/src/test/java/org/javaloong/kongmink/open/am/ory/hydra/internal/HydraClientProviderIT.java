package org.javaloong.kongmink.open.am.ory.hydra.internal;

import org.javaloong.kongmink.open.am.ClientProvider;
import org.javaloong.kongmink.open.common.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("hydra")
public class HydraClientProviderIT extends HydraAdminClientTestSupport {

    private ClientProvider clientProvider;

    @BeforeEach
    public void setUp() {
        clientProvider = new HydraClientProvider(createClient());
    }

    @Test
    public void testOAuth2ClientCRUD() {
        // create client
        Client client = new Client();
        client.setClientName("New Client");
        client.setScope("profile email");
        client.setRedirectUris(Collections.singletonList("/new-client/*"));
        client.setGrantTypes(Arrays.asList("authorization_code", "refresh_token"));
        Client newClient = clientProvider.create(client);
        assertThat(newClient)
                .returns("New Client", Client::getClientName)
                .returns(Collections.singletonList("/new-client/*"), Client::getRedirectUris)
                .returns(Arrays.asList("authorization_code", "refresh_token"), Client::getGrantTypes)
                .returns("profile email", Client::getScope);
        // update client
        newClient.setClientName("New Client2");
        newClient.setRedirectUris(Arrays.asList("/new-client/*", "/new-client2/*"));
        newClient.setGrantTypes(Collections.singletonList("implicit"));
        newClient.setScope("profile phone");
        clientProvider.update(newClient);
        Optional<Client> result = clientProvider.findById(newClient.getClientId());
        assertThat(result).hasValueSatisfying(value -> {
            assertThat(value)
                    .returns("New Client2", Client::getClientName)
                    .returns(Arrays.asList("/new-client/*", "/new-client2/*"), Client::getRedirectUris)
                    .returns(Collections.singletonList("implicit"), Client::getGrantTypes)
                    .returns("profile phone", Client::getScope);
        });
        // regenerate secret
        String clientSecret = clientProvider.regenerateSecret(newClient.getClientId());
        assertThat(clientSecret).isNotEmpty();
        // delete client
        clientProvider.delete(newClient.getClientId());
        assertThat(clientProvider.findById(newClient.getClientId())).isEmpty();
    }
}
