package org.javaloong.kongmink.open.apim.gravitee.internal;

import org.javaloong.kongmink.open.apim.ApiProvider;
import org.javaloong.kongmink.open.common.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("gravitee")
public class GraviteeApiProviderIT extends GraviteePortalClientTestSupport {

    private ApiProvider apiProvider;

    @BeforeEach
    public void setUp() {
        apiProvider = new GraviteeApiProvider(createPortalClient());
    }

    @Test
    public void getMetrics() {
        String apiId = "ebbb4e21-9bbc-400d-bb4e-219bbc400d97";
        ApiMetrics apiMetrics = apiProvider.getMetrics(apiId);
        assertThat(apiMetrics).returns(1, ApiMetrics::getSubscribers);
    }

    @Test
    public void getPage() {
        String apiId = "ebbb4e21-9bbc-400d-bb4e-219bbc400d97";
        String pageId = "9d574190-f95b-4b18-9741-90f95bfb1844";
        ApiPage apiPage = apiProvider.getPage(apiId, pageId, null);
        assertThat(apiPage).isNotNull().returns("Swagger", ApiPage::getName);
    }

    @Test
    public void getPages() {
        String apiId = "ebbb4e21-9bbc-400d-bb4e-219bbc400d97";
        Page<ApiPage> result = apiProvider.getPages(apiId, null, null, 1, 10);
        assertThat(result.getTotalCount()).isGreaterThan(1);
        assertThat(result.getData()).isNotEmpty()
                .extracting(ApiPage::getName).contains("Swagger");
    }

    @Test
    public void getPlans() {
        String apiId = "ebbb4e21-9bbc-400d-bb4e-219bbc400d97";
        Page<Plan> result = apiProvider.getPlans(apiId, 1, 10);
        assertThat(result.getTotalCount()).isEqualTo(1);
        assertThat(result.getData()).isNotEmpty().hasSize(1)
                .extracting(Plan::getName).contains("Basic");
    }

    @Test
    public void getCategories() {
        Collection<Category> result = apiProvider.getCategories();
        assertThat(result).isNotEmpty().hasSize(1);
    }

    @Test
    public void findById() {
        String id = "ebbb4e21-9bbc-400d-bb4e-219bbc400d97";
        Optional<Api> result = apiProvider.findById(id);
        assertThat(result).isPresent();
    }

    @Test
    public void findAll() {
        Page<Api> result = apiProvider.findAll(null, 1, 10);
        assertThat(result.getTotalCount()).isEqualTo(2);
        assertThat(result.getData()).isNotEmpty().hasSize(2)
                .extracting(Api::getName).contains("Swagger Petstore - OpenAPI 3.0");
    }

    @Test
    public void search() {
        String query = "pet";
        Page<Api> result = apiProvider.search(query, 1, 10);
        assertThat(result.getTotalCount()).isEqualTo(1);
        assertThat(result.getData()).isNotEmpty().hasSize(1)
                .extracting(Api::getName).contains("Swagger Petstore - OpenAPI 3.0");
    }
}
