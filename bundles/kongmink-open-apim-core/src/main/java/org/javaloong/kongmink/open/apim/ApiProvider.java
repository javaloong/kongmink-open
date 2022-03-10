package org.javaloong.kongmink.open.apim;

import org.javaloong.kongmink.open.apim.model.Api;
import org.javaloong.kongmink.open.apim.model.ApiMetrics;
import org.javaloong.kongmink.open.apim.model.Plan;
import org.javaloong.kongmink.open.common.model.Page;

import java.util.Optional;

public interface ApiProvider {

    ApiMetrics getMetrics(String apiId);

    Page<Plan> getPlans(String apiId, int page, int size);

    Optional<Api> findById(String id);

    Page<Api> findAll(int page, int size);

    Page<Api> search(String query, int page, int size);
}
