package org.javaloong.kongmink.open.rest.core.internal.resource;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.javaloong.kongmink.open.apim.model.ApiKey;
import org.javaloong.kongmink.open.apim.model.Application;
import org.javaloong.kongmink.open.apim.model.application.ApplicationSettings;
import org.javaloong.kongmink.open.apim.model.application.OAuthClientSettings;
import org.javaloong.kongmink.open.common.application.ApplicationType;
import org.javaloong.kongmink.open.common.client.ClientSecret;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.rest.core.internal.dto.ApplicationDTO;
import org.javaloong.kongmink.open.service.ApplicationService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class ApplicationResourceIT extends AbstractResourceTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext context = FrameworkUtil.getBundle(ApplicationResourceIT.class).getBundleContext();
        registerUserContextProvider(context);
        context.registerService(ApplicationService.class, Mockito.mock(ApplicationService.class), null);
    }

    @Inject
    ApplicationService applicationService;

    @Test
    public void createApplication_NameInvalid_ShouldReturnValidationErrors() {
        assertThat(given().contentType(ContentType.JSON).body(new ApplicationDTO())
                .post("/applications").then().assertThat()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .extract().body().jsonPath())
                .satisfies(path -> {
                    assertThat(path.getList("errors.field")).containsAnyOf("name");
                });
    }

    @Test
    public void createApplication_ShouldAddApplicationAndReturnHttpStatusCreated() {
        ArgumentCaptor<Application> applicationCapture = ArgumentCaptor.forClass(Application.class);
        when(applicationService.create(any(User.class), applicationCapture.capture())).thenReturn(new Application());
        ApplicationDTO applicationDto = new ApplicationDTO();
        applicationDto.setName("application1");
        applicationDto.setApplicationType(ApplicationType.WEB);
        setOAuthApplicationSettings(applicationDto, Collections.singletonList("authorization_code"),
                Collections.singletonList("http://localhost/redirect"));
        given().contentType(ContentType.JSON).body(applicationDto)
                .post("/applications").then().assertThat()
                .statusCode(Response.Status.CREATED.getStatusCode());
        assertThat(applicationCapture.getValue())
                .returns(applicationDto.getName(), Application::getName)
                .returns(applicationDto.getApplicationType(), Application::getApplicationType)
                .extracting(app -> app.getSettings().getOauth())
                .returns(applicationDto.getSettings().getOauth().getGrantTypes(), OAuthClientSettings::getGrantTypes)
                .returns(applicationDto.getSettings().getOauth().getRedirectUris(), OAuthClientSettings::getRedirectUris);
    }

    @Test
    public void updateApplication_ShouldReturnHttpStatusOk() {
        ArgumentCaptor<Application> applicationCapture = ArgumentCaptor.forClass(Application.class);
        doNothing().when(applicationService).update(applicationCapture.capture());
        ApplicationDTO applicationDto = new ApplicationDTO();
        applicationDto.setName("application11");
        applicationDto.setApplicationType(ApplicationType.WEB);
        setOAuthApplicationSettings(applicationDto, Collections.singletonList("authorization_code"),
                Collections.singletonList("http://localhost/redirect"));
        given().contentType(ContentType.JSON).body(applicationDto)
                .put("/applications/{id}", "1").then().assertThat()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        assertThat(applicationCapture.getValue())
                .returns("1", Application::getId)
                .returns(applicationDto.getName(), Application::getName)
                .returns(applicationDto.getApplicationType(), Application::getApplicationType)
                .extracting(app -> app.getSettings().getOauth())
                .returns(applicationDto.getSettings().getOauth().getGrantTypes(), OAuthClientSettings::getGrantTypes)
                .returns(applicationDto.getSettings().getOauth().getRedirectUris(), OAuthClientSettings::getRedirectUris);
    }

    @Test
    public void deleteApplication_ShouldReturnHttpStatusOk() {
        ArgumentCaptor<String> idCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(applicationService).delete(idCapture.capture());
        delete("/applications/{id}", "1").then().assertThat()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        assertThat(idCapture.getValue()).isEqualTo("1");
    }

    @Test
    public void getApplication_ApplicationNotFound_ShouldReturnHttpStatusNotFound() {
        when(applicationService.findById(anyString())).thenReturn(Optional.empty());
        get("/applications/{id}", "1").then().assertThat()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void getApplication_ShouldReturnHttpStatusOk() {
        Application application = createApplication("1", "application1", ApplicationType.WEB);
        when(applicationService.findById(anyString())).thenReturn(Optional.of(application));
        Application result = get("/applications/{id}", "1").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(Application.class);
        assertThat(result)
                .returns(application.getId(), Application::getId)
                .returns(application.getName(), Application::getName)
                .returns(application.getApplicationType(), Application::getApplicationType);
    }

    @Test
    public void getApplications_ShouldReturnHttpStatusOk() {
        when(applicationService.findAll(any(User.class), anyInt(), anyInt())).thenReturn(createApplications());
        Page<Application> page = given().param("size", 2)
                .get("/applications").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(new TypeRef<Page<Application>>() {
                });
        assertThat(page).isNotNull()
                .returns(3L, Page::getTotalCount)
                .extracting(Page::getData)
                .satisfies(data -> {
                    assertThat(data).hasSize(2)
                            .extracting(Application::getName)
                            .containsExactly("application1", "application2");
                });
    }

    @Test
    public void getClientSecret_ShouldReturnHttpStatusOk() {
        ClientSecret clientSecret = new ClientSecret("client1", "secret1");
        when(applicationService.getClientSecret(anyString())).thenReturn(clientSecret);
        ClientSecret result = get("/applications/{id}/client-secret", "1").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(ClientSecret.class);
        assertThat(result)
                .returns(clientSecret.getClientId(), ClientSecret::getClientId)
                .returns(clientSecret.getSecret(), ClientSecret::getSecret);
    }

    @Test
    public void regenerateClientSecret_ShouldReturnHttpStatusOk() {
        ClientSecret clientSecret = new ClientSecret("client1", "secret2");
        when(applicationService.regenerateClientSecret(anyString())).thenReturn(clientSecret);
        ClientSecret result = post("/applications/{id}/client-secret", "1").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(ClientSecret.class);
        assertThat(result)
                .returns(clientSecret.getClientId(), ClientSecret::getClientId)
                .returns(clientSecret.getSecret(), ClientSecret::getSecret);
    }

    @Test
    public void renewSharedKey_ShouldReturnHttpStatusOk() {
        ApiKey apiKey = createApiKey("1", "key1");
        when(applicationService.renewSharedKey(anyString())).thenReturn(apiKey);
        ApiKey result = post("/applications/{id}/keys/_renew", "1").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(ApiKey.class);
        assertThat(result)
                .returns(apiKey.getId(), ApiKey::getId)
                .returns(apiKey.getKey(), ApiKey::getKey);
    }

    @Test
    public void revokeKeySubscription_ShouldReturnHttpStatusOk() {
        ArgumentCaptor<String> idCapture = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(applicationService).revokeKey(idCapture.capture(), keyCapture.capture());
        post("/applications/{id}/keys/{apiKey}/_revoke", "1", "key1").then().assertThat()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        assertThat(idCapture.getValue()).isEqualTo("1");
        assertThat(keyCapture.getValue()).isEqualTo("key1");
    }

    private Application createApplication(String id, String name, ApplicationType applicationType) {
        Application application = new Application();
        application.setId(id);
        application.setName(name);
        application.setApplicationType(applicationType);
        application.setCreatedAt(LocalDateTime.now());
        return application;
    }

    private void setOAuthApplicationSettings(ApplicationDTO applicationDto,
                                             List<String> grantTypes, List<String> redirectUris) {
        ApplicationSettings applicationSettings = new ApplicationSettings();
        OAuthClientSettings oauthSettings = new OAuthClientSettings();
        oauthSettings.setApplicationType(applicationDto.getApplicationType().toString());
        oauthSettings.setGrantTypes(grantTypes);
        oauthSettings.setRedirectUris(redirectUris);
        applicationSettings.setOauth(oauthSettings);
        applicationDto.setSettings(applicationSettings);
    }

    private Page<Application> createApplications() {
        List<Application> applications = new ArrayList<>();
        applications.add(createApplication("1", "application1", ApplicationType.WEB));
        applications.add(createApplication("2", "application2", ApplicationType.BROWSER));
        return new Page<>(applications, 3);
    }

    private ApiKey createApiKey(String id, String key) {
        ApiKey apiKey = new ApiKey();
        apiKey.setId(id);
        apiKey.setKey(key);
        return apiKey;
    }
}
