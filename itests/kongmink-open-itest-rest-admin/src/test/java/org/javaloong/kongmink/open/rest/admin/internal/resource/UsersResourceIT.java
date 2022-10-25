package org.javaloong.kongmink.open.rest.admin.internal.resource;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.javaloong.kongmink.open.service.UserConfigService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class UsersResourceIT extends AbstractResourceTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext context = FrameworkUtil.getBundle(UsersResourceIT.class).getBundleContext();
        context.registerService(UserConfigService.class, Mockito.mock(UserConfigService.class), null);
    }

    @Inject
    UserConfigService userConfigService;

    @Captor
    ArgumentCaptor<Map<String, Object>> userConfigCapture;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUserConfig_ShouldReturnHttpStatusOk() {
        when(userConfigService.getConfig(anyString())).thenReturn(createUserConfig());
        Map<String, Object> result = get("/users/{id}/config", "1").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(new TypeRef<>() {
                });
        assertThat(result).contains(
                entry("user.config.key1", 1),
                entry("user.config.key2", true));
    }

    @Test
    public void setUserConfig_ShouldReturnHttpStatusOk() {
        doNothing().when(userConfigService).setConfig(anyString(), userConfigCapture.capture());
        Map<String, Object> userConfig = new HashMap<>();
        userConfig.put("user.config.key1", 10);
        userConfig.put("user.config.key2", false);
        given().contentType(ContentType.JSON).body(userConfig)
                .put("/users/{id}/config", "1").then().assertThat()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        assertThat(userConfigCapture.getValue()).contains(
                entry("user.config.key1", 10),
                entry("user.config.key2", false));

    }

    @Test
    public void updateUserConfig_ShouldReturnHttpStatusOk() {
        doNothing().when(userConfigService).updateConfig(anyString(), userConfigCapture.capture());
        Map<String, Object> userConfig = new HashMap<>();
        userConfig.put("user.config.key1", 10);
        given().contentType(ContentType.JSON).body(userConfig)
                .patch("/users/{id}/config", "1").then().assertThat()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        assertThat(userConfigCapture.getValue()).contains(
                entry("user.config.key1", 10));
    }

    private Map<String, Object> createUserConfig() {
        Map<String, Object> userConfig = new HashMap<>();
        userConfig.put("user.config.key1", 1);
        userConfig.put("user.config.key2", true);
        return userConfig;
    }
}
