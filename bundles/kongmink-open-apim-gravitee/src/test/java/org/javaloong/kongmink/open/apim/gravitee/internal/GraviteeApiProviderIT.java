package org.javaloong.kongmink.open.apim.gravitee.internal;

import org.javaloong.kongmink.open.apim.ApiProvider;
import org.javaloong.kongmink.open.apim.model.Api;
import org.javaloong.kongmink.open.apim.model.ApiMetrics;
import org.javaloong.kongmink.open.apim.model.Plan;
import org.javaloong.kongmink.open.common.model.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
    public void getPlans() {
        String apiId = "167f6b66-da50-4c39-bf6b-66da505c391a";
        Page<Plan> result = apiProvider.getPlans(apiId, 1, 10);
        assertThat(result.getTotalCount()).isEqualTo(1);
        assertThat(result.getData()).isNotEmpty().hasSize(1)
                .extracting(Plan::getName).contains("Basic");
    }

    @Test
    public void findById() {
        String id = "167f6b66-da50-4c39-bf6b-66da505c391a";
        Optional<Api> result = apiProvider.findById(id);
        assertThat(result).isPresent();
    }

    @Test
    public void findAll() {
        Page<Api> result = apiProvider.findAll(1, 10);
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
