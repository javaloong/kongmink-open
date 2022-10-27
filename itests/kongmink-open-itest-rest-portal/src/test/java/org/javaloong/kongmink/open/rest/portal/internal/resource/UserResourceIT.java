package org.javaloong.kongmink.open.rest.portal.internal.resource;

import io.restassured.http.ContentType;
import org.javaloong.kongmink.open.common.model.User;
import org.javaloong.kongmink.open.common.model.UserEmail;
import org.javaloong.kongmink.open.common.model.UserPassword;
import org.javaloong.kongmink.open.common.model.UserProfile;
import org.javaloong.kongmink.open.rest.portal.dto.EmailDTO;
import org.javaloong.kongmink.open.rest.portal.dto.ProfileDTO;
import org.javaloong.kongmink.open.rest.portal.dto.UpdatePasswordDTO;
import org.javaloong.kongmink.open.service.AccountService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class UserResourceIT extends AbstractResourceTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext context = FrameworkUtil.getBundle(UserResourceIT.class).getBundleContext();
        registerUserContextProvider(context);
        context.registerService(AccountService.class, Mockito.mock(AccountService.class), null);
    }

    @Inject
    AccountService accountService;

    @Test
    public void updateProfile_ShouldReturnHttpStatusOk() {
        ArgumentCaptor<UserProfile> profileCapture = ArgumentCaptor.forClass(UserProfile.class);
        doNothing().when(accountService).updateProfile(profileCapture.capture());
        ProfileDTO profileDto = new ProfileDTO();
        profileDto.setCompanyName("company1");
        given().contentType(ContentType.JSON).body(profileDto)
                .put("/user/profile").then().assertThat()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        assertThat(profileCapture.getValue()).isNotNull()
                .returns(profileDto.getCompanyName(), UserProfile::getCompanyName);
    }

    @Test
    public void updatePassword_PasswordConfirmNotMatch_ShouldReturnValidationErrors() {
        UpdatePasswordDTO passwordDto = new UpdatePasswordDTO();
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
        doNothing().when(accountService).updatePassword(passwordCapture.capture());
        UpdatePasswordDTO passwordDto = new UpdatePasswordDTO();
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
        EmailDTO emailDto = new EmailDTO();
        emailDto.setEmail("user1.test.com");
        assertThat(given().contentType(ContentType.JSON).body(emailDto)
                .put("/user/email").then().assertThat()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .extract().body().jsonPath())
                .returns("email", path -> path.get("errors[0].field"));
    }

    @Test
    public void updateEmail_ShouldReturnHttpStatusOk() {
        ArgumentCaptor<UserEmail> emailCapture = ArgumentCaptor.forClass(UserEmail.class);
        doNothing().when(accountService).updateEmail(emailCapture.capture());
        EmailDTO emailDto = new EmailDTO();
        emailDto.setEmail("user1@test.com");
        given().contentType(ContentType.JSON).body(emailDto)
                .put("/user/email").then().assertThat()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        assertThat(emailCapture.getValue()).isNotNull()
                .returns(emailDto.getEmail(), UserEmail::getEmail);
    }

    @Test
    public void getUser_ShouldReturnHttpStatusOk() {
        User user = createUser("1", "user1");
        when(accountService.getDetails(any(User.class))).thenReturn(user);
        User result = get("/user").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(User.class);
        assertThat(result).isNotNull()
                .extracting(User::getId, User::getUsername)
                .containsExactly(user.getId(), user.getUsername());
    }

    private User createUser(String id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}
