package org.javaloong.kongmink.open.am.clients.keycloak.internal;

import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import com.github.mjeanroy.junit.servers.jetty.jupiter.JettyServerExtension;
import org.junit.jupiter.api.extension.RegisterExtension;

public abstract class EmbeddedKeycloakTestSupport {

    @RegisterExtension
    static JettyServerExtension jettyServerExtension = new JettyServerExtension(createJettyConfiguration());

    private static EmbeddedJettyConfiguration createJettyConfiguration() {
        return EmbeddedJettyConfiguration.builder()
                .withOverrideDescriptor("src/test/resources/web.xml")
                .build();
    }
}
