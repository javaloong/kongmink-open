package org.javaloong.kongmink.open.am.users.keycloak.internal;

import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import com.github.mjeanroy.junit.servers.jetty.jupiter.JettyServerExtension;
import org.javaloong.kongmink.open.am.embedded.keycloak.KeycloakServerProperties;
import org.junit.jupiter.api.extension.RegisterExtension;

public abstract class EmbeddedKeycloakTestSupport {

    static {
        System.setProperty(KeycloakServerProperties.REALM_CONFIGURATION_PATH, "open-admin-realm.json");
    }

    @RegisterExtension
    static JettyServerExtension jettyServerExtension = new JettyServerExtension(createJettyConfiguration());

    private static EmbeddedJettyConfiguration createJettyConfiguration() {
        return EmbeddedJettyConfiguration.builder()
                .withOverrideDescriptor("src/test/resources/web.xml")
                .build();
    }
}
