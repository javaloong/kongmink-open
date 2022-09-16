package org.javaloong.kongmink.open.rest.auth.jwt.internal;

import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.core.service.UserService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.ops4j.pax.exam.Option;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.ops4j.pax.exam.CoreOptions.composite;
import static org.ops4j.pax.tinybundles.core.TinyBundles.bundle;

public class ShiroSecurityIT extends SecurityTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext context = FrameworkUtil.getBundle(ShiroSecurityIT.class).getBundleContext();
        context.registerService(UserService.class, Mockito.mock(UserService.class), null);
    }

    @Inject
    UserService userService;

    @Test
    public void testGetUser() {
        when(userService.loadByUser(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertThat(given().auth().oauth2(getAccessToken())
                .get("/user")
                .then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(User.class))
                .returns("da71a97e-5c99-4868-a0a2-690ff2a44563", User::getId)
                .returns("user1", User::getUsername)
                .returns("user1@example.com", User::getEmail);
    }

    @Test
    public void testGetUserToken() {
        assertThat(given().auth().oauth2(getAccessToken())
                .get("/user/token")
                .then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().asString())
                .isEqualTo(getAccessToken());
    }

    @Override
    protected Option testBundles() {
        return composite(super.testBundles(),
                BndDSOptions.dsBundle("test.resources",
                        bundle().add(TestUserResource.class))
        );
    }
}
