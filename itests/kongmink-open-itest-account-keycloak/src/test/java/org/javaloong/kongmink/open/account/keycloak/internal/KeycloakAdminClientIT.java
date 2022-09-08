package org.javaloong.kongmink.open.account.keycloak.internal;

import org.javaloong.kongmink.open.account.UserProvider;
import org.javaloong.kongmink.open.common.auth.SecurityContextProvider;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class KeycloakAdminClientIT extends KeycloakMockTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext bundleContext = FrameworkUtil.getBundle(KeycloakAdminClientIT.class).getBundleContext();
        bundleContext.registerService(SecurityContextProvider.class, new DummySecurityContextProvider(), null);
    }

    @Test
    public void test_user_provider() {
        assertThat(getService(UserProvider.class)).isNotNull().satisfies(
                provider -> assertThat(provider.getDetails().getUsername()).isEqualTo("user1"));
    }
}
