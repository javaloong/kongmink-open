package org.javaloong.kongmink.open.rest.core.internal.resource;

import io.restassured.common.mapper.TypeRef;
import org.javaloong.kongmink.open.apim.model.Api;
import org.javaloong.kongmink.open.apim.model.ApiMetrics;
import org.javaloong.kongmink.open.apim.model.Category;
import org.javaloong.kongmink.open.apim.model.Plan;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.service.ApiService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.*;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ApiResourceIT extends AbstractResourceTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext context = FrameworkUtil.getBundle(ApiResourceIT.class).getBundleContext();
        registerUserContextProvider(context);
        context.registerService(ApiService.class, Mockito.mock(ApiService.class), null);
    }

    @Inject
    ApiService apiService;

    @Test
    public void getCategories_ShouldReturnHttpStatusOk() {
        when(apiService.getCategories()).thenReturn(createCategories());
        Collection<Category> result = get("/apis/categories").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(new TypeRef<Collection<Category>>() {
                });
        assertThat(result).isNotNull().satisfies(data -> {
            assertThat(data).hasSize(2)
                    .extracting(Category::getName)
                    .containsExactly("Officials Apis", "Partner Apis");
        });
    }

    @Test
    public void getApi_ApiNotFound_ShouldReturnHttpStatusNotFound() {
        when(apiService.findById(anyString())).thenReturn(Optional.empty());
        get("/apis/{id}", "1").then().assertThat()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void getApi_ShouldReturnHttpStatusOk() {
        Api api = createApi("1", "api1", "1.0.0", Collections.singletonList("Officials Apis"));
        when(apiService.findById(anyString())).thenReturn(Optional.of(api));
        Api result = get("/apis/{id}", "1").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(Api.class);
        assertThat(result)
                .returns(api.getId(), Api::getId)
                .returns(api.getName(), Api::getName)
                .returns(api.getVersion(), Api::getVersion);
    }

    @Test
    public void getApis_ShouldReturnHttpStatusOk() {
        when(apiService.findAll(nullable(String.class), anyInt(), anyInt())).thenReturn(createApis());
        Page<Api> page = given().param("size", 2)
                .get("/apis").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(new TypeRef<Page<Api>>() {
                });
        assertThat(page).isNotNull()
                .returns(3L, Page::getTotalCount)
                .extracting(Page::getData)
                .satisfies(data -> {
                    assertThat(data).hasSize(2)
                            .extracting(Api::getName)
                            .containsExactly("api1", "api2");
                });
    }

    @Test
    public void searchApis_ShouldReturnHttpStatusOk() {
        when(apiService.search(nullable(String.class), anyInt(), anyInt())).thenReturn(createApis());
        Page<Api> page = given().param("size", 2)
                .post("/apis/_search").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(new TypeRef<Page<Api>>() {
                });
        assertThat(page).isNotNull()
                .returns(3L, Page::getTotalCount)
                .extracting(Page::getData)
                .satisfies(data -> {
                    assertThat(data).hasSize(2)
                            .extracting(Api::getName)
                            .containsExactly("api1", "api2");
                });
    }

    @Test
    public void getMetrics_ShouldReturnHttpStatusOk() {
        ApiMetrics apiMetrics = createApiMetrics("1");
        when(apiService.getMetrics(anyString())).thenReturn(apiMetrics);
        ApiMetrics result = get("/apis/{id}/metrics", "1").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(ApiMetrics.class);
        assertThat(result)
                .returns(apiMetrics.getId(), ApiMetrics::getId)
                .returns(apiMetrics.getHits(), ApiMetrics::getHits)
                .returns(apiMetrics.getSubscribers(), ApiMetrics::getSubscribers)
                .returns(apiMetrics.getHealth(), ApiMetrics::getHealth);
    }

    @Test
    public void getPlans_ShouldReturnHttpStatusOk() {
        when(apiService.getPlans(anyString(), anyInt(), anyInt())).thenReturn(createPlans());
        Page<Plan> page = given().param("size", 2)
                .get("/apis/{id}/plans", "1").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(new TypeRef<Page<Plan>>() {
                });
        assertThat(page).isNotNull()
                .returns(3L, Page::getTotalCount)
                .extracting(Page::getData)
                .satisfies(data -> {
                    assertThat(data).hasSize(2)
                            .extracting(Plan::getName)
                            .containsExactly("plan1", "plan2");
                });
    }

    private Category createCategory(String id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;
    }

    private Collection<Category> createCategories() {
        Collection<Category> categories = new ArrayList<>();
        categories.add(createCategory("1", "Officials Apis"));
        categories.add(createCategory("2", "Partner Apis"));
        return categories;
    }

    private Api createApi(String id, String name, String version, List<String> categories) {
        Api api = new Api();
        api.setId(id);
        api.setName(name);
        api.setCategories(categories);
        api.setVersion(version);
        api.setCreatedAt(LocalDateTime.now());
        return api;
    }

    private Page<Api> createApis() {
        List<Api> apis = new ArrayList<>();
        apis.add(createApi("1", "api1", "1.0.0", Collections.singletonList("Officials Apis")));
        apis.add(createApi("2", "api2", "2.1.0", Collections.singletonList("Partner Apis")));
        return new Page<>(apis, 3);
    }

    private ApiMetrics createApiMetrics(String id) {
        ApiMetrics apiMetrics = new ApiMetrics();
        apiMetrics.setId(id);
        apiMetrics.setHits(0);
        apiMetrics.setSubscribers(0);
        apiMetrics.setHealth(0);
        return apiMetrics;
    }

    private Plan createPlan(String id, String name, Plan.Security security, Plan.Validation validation) {
        Plan plan = new Plan();
        plan.setId(id);
        plan.setName(name);
        plan.setSecurity(security);
        plan.setValidation(validation);
        return plan;
    }

    private Page<Plan> createPlans() {
        List<Plan> plans = new ArrayList<>();
        plans.add(createPlan("1", "plan1", Plan.Security.API_KEY, Plan.Validation.AUTO));
        plans.add(createPlan("2", "plan2", Plan.Security.OAUTH2, Plan.Validation.MANUAL));
        return new Page<>(plans, 3);
    }
}
