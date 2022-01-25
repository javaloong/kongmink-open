package org.javaloong.kongmink.open.rest.core.internal.resource;

import io.restassured.http.ContentType;
import org.javaloong.kongmink.open.common.user.UserEmail;
import org.javaloong.kongmink.open.common.user.UserPassword;
import org.javaloong.kongmink.open.common.user.UserProfile;
import org.javaloong.kongmink.open.rest.core.model.EmailDto;
import org.javaloong.kongmink.open.rest.core.model.ProfileDto;
import org.javaloong.kongmink.open.rest.core.model.UpdatePasswordDto;
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
import java.time.LocalDateTime;
import java.util.Optional;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class UserResourceIT extends AbstractResourceTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext context = FrameworkUtil.getBundle(UserResourceIT.class).getBundleContext();
        registerUserContextProvider(context);
        context.registerService(UserService.class, Mockito.mock(UserService.class), null);
    }

    @Inject
    UserService userService;

    @Test
    public void updateProfile_ShouldReturnHttpStatusOk() {
        ArgumentCaptor<UserProfile> profileCapture = ArgumentCaptor.forClass(UserProfile.class);
        doNothing().when(userService).updateProfile(profileCapture.capture());
        ProfileDto profileDto = new ProfileDto();
        profileDto.setCompanyName("company1");
        given().contentType(ContentType.JSON).body(profileDto)
                .put("/user/profile").then().assertThat()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        assertThat(profileCapture.getValue()).isNotNull()
                .returns(profileDto.getCompanyName(), UserProfile::getCompanyName);
    }

    @Test
    public void updatePassword_PasswordConfirmNotMatch_ShouldReturnValidationErrors() {
        UpdatePasswordDto passwordDto = new UpdatePasswordDto();
        passwordDto.setPassword("111111");
        passwordDto.setPasswordNew("222222");
        passwordDto.setPasswordConfirm("333333");
        given().contentType(ContentType.JSON).body(passwordDto)
                .put("/user/password").then().assertThat()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .log().all();
    }

    @Test
    public void updatePassword_ShouldReturnHttpStatusOk() {
        ArgumentCaptor<UserPassword> passwordCapture = ArgumentCaptor.forClass(UserPassword.class);
        doNothing().when(userService).updatePassword(passwordCapture.capture());
        UpdatePasswordDto passwordDto = new UpdatePasswordDto();
        passwordDto.setPassword("111111");
        passwordDto.setPasswordNew("222222");
        passwordDto.setPasswordConfirm("222222");
        given().contentType(ContentType.JSON).body(passwordDto)
                .put("/user/password").then().assertThat()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        assertThat(passwordCapture.getValue()).isNotNull()
                .returns(passwordDto.getPassword(), UserPassword::getPassword);
    }

    @Test
    public void updateEmail_EmailInvalid_ShouldReturnValidationErrors() {
        EmailDto emailDto = new EmailDto();
        emailDto.setEmail("user1.test.com");
        assertThat(given().contentType(ContentType.JSON).body(emailDto)
                .put("/user/email").then().assertThat()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .extract().body().jsonPath())
                .returns("email",path -> path.get("errors[0].field"));
    }

    @Test
    public void updateEmail_ShouldReturnHttpStatusOk() {
        ArgumentCaptor<UserEmail> emailCapture = ArgumentCaptor.forClass(UserEmail.class);
        doNothing().when(userService).updateEmail(emailCapture.capture());
        EmailDto emailDto = new EmailDto();
        emailDto.setEmail("user1@test.com");
        given().contentType(ContentType.JSON).body(emailDto)
                .put("/user/email").then().assertThat()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        assertThat(emailCapture.getValue()).isNotNull()
                .returns(emailDto.getEmail(), UserEmail::getEmail);
    }

    @Test
    public void getUser_UserNotFound_ShouldReturnHttpStatusForbidden() {
        when(userService.findById(anyString())).thenReturn(Optional.empty());
        get("/user").then().assertThat()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    public void getUser_ShouldReturnHttpStatusOk() {
        ComplexUser complexUser = createComplexUser("1", "user1");
        when(userService.findById(anyString())).thenReturn(Optional.of(complexUser));
        ComplexUser result = get("/user").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(ComplexUser.class);
        assertThat(result).isNotNull()
                .extracting(ComplexUser::getId, ComplexUser::getUsername)
                .containsExactly(complexUser.getId(), complexUser.getUsername());
    }

    private ComplexUser createComplexUser(String id, String username) {
        ComplexUser user = new ComplexUser();
        user.setId(id);
        user.setUsername(username);
        user.setCreatedTimestamp(LocalDateTime.now());
        return user;
    }
}
