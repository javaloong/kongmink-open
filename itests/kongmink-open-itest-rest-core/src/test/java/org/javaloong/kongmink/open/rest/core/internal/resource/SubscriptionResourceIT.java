package org.javaloong.kongmink.open.rest.core.internal.resource;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.javaloong.kongmink.open.common.model.ApiKey;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.model.Subscription;
import org.javaloong.kongmink.open.rest.core.dto.SubscriptionDTO;
import org.javaloong.kongmink.open.service.SubscriptionService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class SubscriptionResourceIT extends AbstractResourceTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext context = FrameworkUtil.getBundle(SubscriptionResourceIT.class).getBundleContext();
        registerUserContextProvider(context);
        context.registerService(SubscriptionService.class, Mockito.mock(SubscriptionService.class), null);
    }

    @Inject
    SubscriptionService subscriptionService;

    @Test
    public void createSubscription_InputInvalid_ShouldReturnValidationErrors() {
        assertThat(given().contentType(ContentType.JSON).body(new SubscriptionDTO())
                .post("/subscriptions").then().assertThat()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .extract().body().jsonPath())
                .satisfies(path -> {
                    assertThat(path.getList("errors.field")).containsAnyOf("application");
                });
    }

    @Test
    public void createSubscription_ShouldAddSubscriptionAndReturnHttpStatusCreated() {
        ArgumentCaptor<Subscription> subscriptionArgumentCaptor = ArgumentCaptor.forClass(Subscription.class);
        when(subscriptionService.create(subscriptionArgumentCaptor.capture())).thenReturn(new Subscription());
        SubscriptionDTO subscriptionDto = new SubscriptionDTO();
        subscriptionDto.setApplication("application1");
        subscriptionDto.setPlan("plan1");
        given().contentType(ContentType.JSON).body(subscriptionDto)
                .post("/subscriptions").then().assertThat()
                .statusCode(Response.Status.CREATED.getStatusCode());
        assertThat(subscriptionArgumentCaptor.getValue())
                .returns(subscriptionDto.getApplication(), Subscription::getApplication)
                .returns(subscriptionDto.getPlan(), Subscription::getPlan);
    }

    @Test
    public void closeSubscription_ShouldReturnHttpStatusOk() {
        ArgumentCaptor<String> idCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(subscriptionService).close(idCapture.capture());
        post("/subscriptions/{id}/_close", "1").then().assertThat()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        assertThat(idCapture.getValue()).isEqualTo("1");
    }

    @Test
    public void getSubscription_SubscriptionNotFound_ShouldReturnHttpStatusNotFound() {
        when(subscriptionService.findById(anyString())).thenReturn(Optional.empty());
        get("/subscriptions/{id}", "1").then().assertThat()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void getSubscription_ShouldReturnHttpStatusOk() {
        Subscription subscription = createSubscription("1", "application1", "plan1");
        when(subscriptionService.findById(anyString())).thenReturn(Optional.of(subscription));
        Subscription result = get("/subscriptions/{id}", "1").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(Subscription.class);
        assertThat(result)
                .returns(subscription.getId(), Subscription::getId)
                .returns(subscription.getApplication(), Subscription::getApplication)
                .returns(subscription.getPlan(), Subscription::getPlan);
    }

    @Test
    public void getSubscriptions_ShouldReturnHttpStatusOk() {
        when(subscriptionService.findAll(nullable(String.class), nullable(String.class), anyList(), anyInt(), anyInt())).thenReturn(createSubscriptions());
        Page<Subscription> page = given().param("size", 2)
                .get("/subscriptions").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(new TypeRef<Page<Subscription>>() {
                });
        assertThat(page).isNotNull()
                .returns(3L, Page::getTotalCount)
                .extracting(Page::getData)
                .satisfies(data -> {
                    assertThat(data).hasSize(2)
                            .extracting(Subscription::getId)
                            .containsExactly("1", "2");
                });
    }

    @Test
    public void renewKeySubscription_ShouldReturnHttpStatusOk() {
        ApiKey apiKey = createApiKey("1", "key1");
        when(subscriptionService.renewKey(anyString())).thenReturn(apiKey);
        ApiKey result = post("/subscriptions/{id}/keys/_renew", "1").then().assertThat()
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
        doNothing().when(subscriptionService).revokeKey(idCapture.capture(), keyCapture.capture());
        post("/subscriptions/{id}/keys/{apiKey}/_revoke", "1", "key1").then().assertThat()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        assertThat(idCapture.getValue()).isEqualTo("1");
        assertThat(keyCapture.getValue()).isEqualTo("key1");
    }

    private Subscription createSubscription(String id, String application, String plan) {
        Subscription subscription = new Subscription();
        subscription.setId(id);
        subscription.setApplication(application);
        subscription.setPlan(plan);
        return subscription;
    }

    private Page<Subscription> createSubscriptions() {
        List<Subscription> subscriptions = new ArrayList<>();
        subscriptions.add(createSubscription("1", "application1", "plan1"));
        subscriptions.add(createSubscription("2", "application2", "plan2"));
        return new Page<>(subscriptions, 3);
    }

    private ApiKey createApiKey(String id, String key) {
        ApiKey apiKey = new ApiKey();
        apiKey.setId(id);
        apiKey.setKey(key);
        return apiKey;
    }
}
