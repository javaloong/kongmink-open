package org.javaloong.kongmink.open.rest.auth.internal;

import io.restassured.http.ContentType;
import org.javaloong.kongmink.open.common.model.client.Client;
import org.javaloong.kongmink.open.common.model.user.User;
import org.junit.Test;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.tinybundles.core.InnerClassStrategy;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.ops4j.pax.exam.CoreOptions.composite;
import static org.ops4j.pax.tinybundles.core.TinyBundles.bundle;

public class ShiroSecurityIT extends SecurityTestSupport {

    @Test
    public void testAuthenticatedNoAuthPresent() {
        get("/user").then().assertThat()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void testAuthenticatedAuthPresent() {
        assertThat(given().auth().oauth2("user1_token")
                .get("/user")
                .then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(User.class))
                .returns("1", User::getId)
                .returns("user1", User::getUsername)
                .returns("user1@example.com", User::getEmail);
    }

    @Test
    public void testRoleAuthPresent() {
        given().auth().oauth2("user3_token")
                .get("/clients/{id}", 1).then().assertThat()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    public void testRoleAuthWithRolePresent() {
        given().auth().oauth2("user2_token")
                .get("/clients/{id}", 1)
                .then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    public void testPermissionNoAuthPermissionPresent() {
        given().auth().oauth2("user2_token")
                .contentType(ContentType.JSON).body(new Client())
                .post("/clients").then().assertThat()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    public void testPermissionAuthWithPermissionPresent() {
        given().auth().oauth2("user1_token")
                .contentType(ContentType.JSON).body(new Client())
                .post("/clients").then().assertThat()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Override
    protected Option testBundles() {
        return composite(super.testBundles(),
                BndDSOptions.dsBundle("test.resources",
                        bundle().add(TestResources.class, InnerClassStrategy.ALL))
        );
    }
}
