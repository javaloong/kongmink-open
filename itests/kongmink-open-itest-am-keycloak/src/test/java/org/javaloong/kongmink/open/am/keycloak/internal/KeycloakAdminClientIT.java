package org.javaloong.kongmink.open.am.keycloak.internal;

import org.javaloong.kongmink.open.am.ClientProvider;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class KeycloakAdminClientIT extends KeycloakMockTestSupport {

    @Test
    public void test_client_provider() {
        assertThat(getService(ClientProvider.class)).isNotNull().satisfies(
                provider -> assertThat(provider.findById("1")).hasValueSatisfying(
                        client -> assertThat(client.getClientId()).isEqualTo("example-client")));
    }
}
