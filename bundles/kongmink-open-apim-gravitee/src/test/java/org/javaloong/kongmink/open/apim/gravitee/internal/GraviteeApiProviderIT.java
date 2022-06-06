package org.javaloong.kongmink.open.apim.gravitee.internal;

import org.javaloong.kongmink.open.apim.ApiProvider;
import org.javaloong.kongmink.open.apim.model.*;
import org.javaloong.kongmink.open.common.model.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
public class GraviteeApiProviderIT extends GraviteePortalClientTestSupport {

    private ApiProvider apiProvider;

    @BeforeEach
    public void setUp() {
        apiProvider = new GraviteeApiProvider(createPortalClient());
    }

    @Test
    public void getMetrics() {
        String apiId = "167f6b66-da50-4c39-bf6b-66da505c391a";
        ApiMetrics apiMetrics = apiProvider.getMetrics(apiId);
        assertThat(apiMetrics).returns(1, ApiMetrics::getSubscribers);
    }

    @Test
    public void getPage() {
        String apiId = "167f6b66-da50-4c39-bf6b-66da505c391a";
        String pageId = "9d574190-f95b-4b18-9741-90f95bfb1844";
        ApiPage apiPage = apiProvider.getPage(apiId, pageId, null);
        assertThat(apiPage).isNotNull().returns("Swagger", ApiPage::getName);
    }

    @Test
    public void getPages() {
        String apiId = "167f6b66-da50-4c39-bf6b-66da505c391a";
        Page<ApiPage> result = apiProvider.getPages(apiId, null, null, 1, 10);
        assertThat(result.getTotalCount()).isGreaterThan(1);
        assertThat(result.getData()).isNotEmpty()
                .extracting(ApiPage::getName).contains("Swagger");
    }

    @Test
    public void getPlans() {
        String apiId = "167f6b66-da50-4c39-bf6b-66da505c391a";
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
        String id = "167f6b66-da50-4c39-bf6b-66da505c391a";
        Optional<Api> result = apiProvider.findById(id);
        assertThat(result).isPresent();
    }

    @Test
    public void findAll() {
        Page<Api> result = apiProvider.findAll(null,1, 10);
        assertThat(result.getTotalCount()).isEqualTo(1);
        assertThat(result.getData()).isNotEmpty().hasSize(1)
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
