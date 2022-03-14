package org.javaloong.kongmink.open.apim.gravitee.internal;

import org.javaloong.kongmink.open.apim.*;
import org.javaloong.kongmink.open.apim.model.ApiUser;
import org.javaloong.kongmink.open.apim.model.Category;
import org.javaloong.kongmink.open.common.auth.SecurityContextProvider;
import org.javaloong.kongmink.open.common.user.User;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class GraviteePortalClientIT extends GraviteeMockTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext bundleContext = FrameworkUtil.getBundle(GraviteePortalClientIT.class).getBundleContext();
        bundleContext.registerService(SecurityContextProvider.class, new DummySecurityContextProvider(), null);
    }

    @Test
    public void test_api_user_provider() {
        assertThat(getService(ApiUserProvider.class)).isNotNull().satisfies(
                provider -> assertThat(provider.connectUser(new User()))
                        .returns("0a807fb8-7f9e-4ed7-807f-b87f9e9ed76f", ApiUser::getId));
    }

    @Test
    public void test_api_provider() {
        assertThat(getService(ApiProvider.class)).isNotNull().satisfies(
                provider -> assertThat(provider.findById("1")).hasValueSatisfying(
                        api -> assertThat(api.getName()).isEqualTo("example-api")));
    }

    @Test
    public void test_application_provider() {
        assertThat(getService(ApplicationProvider.class)).isNotNull().satisfies(
                provider -> assertThat(provider.findById("1")).hasValueSatisfying(
                        api -> assertThat(api.getName()).isEqualTo("example-app")));
    }

    @Test
    public void test_category_provider() {
        assertThat(getService(CategoryProvider.class)).isNotNull().satisfies(
                provider -> assertThat(provider.findAll(-1)).hasSize(1)
                        .extracting(Category::getName).containsExactly("example-category"));
    }

    @Test
    public void test_subscription_provider() {
        assertThat(getService(SubscriptionProvider.class)).isNotNull().satisfies(
                provider -> assertThat(provider.findById("1")).hasValueSatisfying(
                        api -> assertThat(api.getApi()).isEqualTo("api1")));
    }
}
