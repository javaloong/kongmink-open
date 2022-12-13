package org.javaloong.kongmink.open.account.keycloak.internal;

import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;
import org.javaloong.kongmink.keycloak.embedded.support.SimplePlatformProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.platform.Platform;

@ExtendWith(JunitServerExtension.class)
public abstract class EmbeddedKeycloakTestSupport {

    @AfterAll
    public static void tearDown() {
        SimplePlatformProvider platform = (SimplePlatformProvider) Platform.getPlatform();
        platform.shutdown();
    }
}
