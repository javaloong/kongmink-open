package org.javaloong.kongmink.open.rest.admin.internal.resource;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.model.user.query.UserQuery;
import org.javaloong.kongmink.open.service.UserService;
import org.javaloong.kongmink.open.service.dto.UserDTO;
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
import java.time.LocalDateTime;
import java.util.*;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class UsersResourceIT extends AbstractResourceTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext context = FrameworkUtil.getBundle(UsersResourceIT.class).getBundleContext();
        context.registerService(UserService.class, Mockito.mock(UserService.class), null);
    }

    @Inject
    UserService userService;

    @Captor
    ArgumentCaptor<Map<String, Object>> userConfigCapture;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUserConfig_ShouldReturnHttpStatusOk() {
        when(userService.getConfig(anyString())).thenReturn(createUserConfig());
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
        doNothing().when(userService).setConfig(anyString(), userConfigCapture.capture());
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
        doNothing().when(userService).updateConfig(anyString(), userConfigCapture.capture());
        Map<String, Object> userConfig = new HashMap<>();
        userConfig.put("user.config.key1", 10);
        given().contentType(ContentType.JSON).body(userConfig)
                .patch("/users/{id}/config", "1").then().assertThat()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        assertThat(userConfigCapture.getValue()).contains(
                entry("user.config.key1", 10));
    }

    @Test
    public void getUser_UserNotFound_ShouldReturnHttpStatusNotFound() {
        when(userService.findById(anyString())).thenReturn(Optional.empty());
        get("/users/{id}", "1").then().assertThat()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void getUser_ShouldReturnHttpStatusOk() {
        UserDTO user = createUser("1", "user1");
        when(userService.findById(anyString())).thenReturn(Optional.of(user));
        UserDTO result = get("/users/{id}", "1").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(UserDTO.class);
        assertThat(result)
                .returns(user.getId(), UserDTO::getId)
                .returns(user.getUsername(), UserDTO::getUsername);
    }

    @Test
    public void getUsers_ShouldReturnHttpStatusOk() {
        when(userService.findAll(any(UserQuery.class), anyInt(), anyInt())).thenReturn(createUsers());
        Page<UserDTO> page = given().param("size", 2)
                .get("/users").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(new TypeRef<>() {
                });
        assertThat(page).isNotNull()
                .returns(3L, Page::getTotalCount)
                .extracting(Page::getData)
                .satisfies(data -> {
                    assertThat(data).hasSize(2)
                            .extracting(UserDTO::getUsername)
                            .containsExactly("user1", "user2");
                });
    }

    private Map<String, Object> createUserConfig() {
        Map<String, Object> userConfig = new HashMap<>();
        userConfig.put("user.config.key1", 1);
        userConfig.put("user.config.key2", true);
        return userConfig;
    }

    private UserDTO createUser(String id, String username) {
        UserDTO user = new UserDTO();
        user.setId(id);
        user.setUsername(username);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    private Page<UserDTO> createUsers() {
        List<UserDTO> users = new ArrayList<>();
        users.add(createUser("1", "user1"));
        users.add(createUser("2", "user2"));
        return new Page<>(users, 3);
    }
}
