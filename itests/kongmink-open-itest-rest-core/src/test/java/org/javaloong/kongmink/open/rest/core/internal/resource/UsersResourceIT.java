package org.javaloong.kongmink.open.rest.core.internal.resource;

import io.restassured.http.ContentType;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.rest.core.model.UserDto;
import org.javaloong.kongmink.open.service.UserService;
import org.javaloong.kongmink.open.service.model.ComplexUser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.head;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UsersResourceIT extends AbstractResourceTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext context = FrameworkUtil.getBundle(UsersResourceIT.class).getBundleContext();
        context.registerService(UserService.class, Mockito.mock(UserService.class), null);
    }

    @Inject
    UserService userService;

    @Test
    public void create_UsernameInvalid_ShouldReturnValidationErrors() {
        UserDto userDto = new UserDto();
        userDto.setEmail("user1@test.com");
        userDto.setPasswordNew("111111");
        userDto.setPasswordConfirm("111111");
        assertThat(given().contentType(ContentType.JSON).body(userDto)
                .post("/users").then().assertThat()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .extract().body().jsonPath())
                .returns("username", path -> path.get("errors[0].field"));
    }

    @Test
    public void create_EmailInvalid_ShouldReturnValidationErrors() {
        UserDto userDto = new UserDto();
        userDto.setUsername("user1");
        userDto.setEmail("user1.test.com");
        userDto.setPasswordNew("111111");
        userDto.setPasswordConfirm("111111");
        assertThat(given().contentType(ContentType.JSON).body(userDto)
                .post("/users").then().assertThat()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .extract().body().jsonPath())
                .returns("email", path -> path.get("errors[0].field"));
    }

    @Test
    public void create_PasswordConfirmNotMatch_ShouldReturnValidationErrors() {
        UserDto userDto = new UserDto();
        userDto.setUsername("user1");
        userDto.setEmail("user1@test.com");
        userDto.setPasswordNew("111111");
        userDto.setPasswordConfirm("222222");
        given().contentType(ContentType.JSON).body(userDto)
                .post("/users").then().assertThat()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void create_ShouldAddUserAndReturnHttpStatusCreated() {
        ArgumentCaptor<User> userCapture = ArgumentCaptor.forClass(User.class);
        when(userService.create(userCapture.capture())).thenReturn(new ComplexUser());
        UserDto userDto = new UserDto();
        userDto.setUsername("user1");
        userDto.setEmail("user1@test.com");
        userDto.setPasswordNew("111111");
        userDto.setPasswordConfirm("111111");
        given().contentType(ContentType.JSON).body(userDto)
                .post("/users").then().assertThat()
                .statusCode(Response.Status.CREATED.getStatusCode());
        assertThat(userCapture.getValue()).isNotNull()
                .extracting(User::getUsername, User::getEmail,
                        user -> user.getPassword().getPasswordNew(),
                        user -> user.getPassword().getPasswordConfirm())
                .containsExactly(userDto.getUsername(), userDto.getEmail(),
                        userDto.getPasswordNew(), userDto.getPasswordConfirm());
    }

    @Test
    public void verifyUsernameExists_UserEmpty_ShouldReturnHttpStatusNotFound() {
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());
        head("/users/username/{username}", "user1").then().assertThat()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void verifyUsernameExists_UserFound_ShouldReturnHttpStatusOk() {
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(new ComplexUser()));
        head("/users/username/{username}", "user1").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    public void verifyEmailExists_UserEmpty_ShouldReturnHttpStatusNotFound() {
        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
        head("/users/email/{email}", "user1@example.com").then().assertThat()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void verifyEmailExists_UserFound_ShouldReturnHttpStatusOk() {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(new ComplexUser()));
        head("/users/email/{email}", "user1@example.com").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode());
    }
}
