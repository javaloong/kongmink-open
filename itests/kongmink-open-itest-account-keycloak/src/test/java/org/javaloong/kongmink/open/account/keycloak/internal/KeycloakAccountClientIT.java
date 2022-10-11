package org.javaloong.kongmink.open.account.keycloak.internal;

import org.javaloong.kongmink.open.account.UserProvider;
import org.javaloong.kongmink.open.core.auth.UserTokenProvider;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class KeycloakAccountClientIT extends KeycloakMockTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext bundleContext = FrameworkUtil.getBundle(KeycloakAccountClientIT.class).getBundleContext();
        bundleContext.registerService(UserTokenProvider.class, new DummyUserTokenProvider(), null);
    }

    @Test
    public void test_user_provider() {
        assertThat(getService(UserProvider.class)).isNotNull().satisfies(
                provider -> assertThat(provider.getDetails().getUsername()).isEqualTo("user1"));
    }
}
