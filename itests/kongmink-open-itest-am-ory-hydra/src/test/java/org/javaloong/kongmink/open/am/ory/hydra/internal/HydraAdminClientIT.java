package org.javaloong.kongmink.open.am.ory.hydra.internal;

import org.javaloong.kongmink.open.am.ClientProvider;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HydraAdminClientIT extends HydraMockTestSupport {

    @Test
    public void test_client_provider() {
        assertThat(getService(ClientProvider.class)).isNotNull().satisfies(
                provider -> assertThat(provider.findById("example-client")).hasValueSatisfying(
                        client -> assertThat(client.getClientName()).isEqualTo("Example Client")));
    }
}
