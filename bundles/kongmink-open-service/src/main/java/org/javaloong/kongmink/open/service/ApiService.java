package org.javaloong.kongmink.open.service;

import org.javaloong.kongmink.open.apim.model.Api;
import org.javaloong.kongmink.open.apim.model.ApiMetrics;
import org.javaloong.kongmink.open.apim.model.Category;
import org.javaloong.kongmink.open.apim.model.Plan;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.user.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface ApiService {

    Collection<Category> getCategories();

    ApiMetrics getMetrics(String apiId);

    Page<Plan> getPlans(String apiId, int page, int size);

    Optional<Api> findById(String id);

    Page<Api> findAll(String category, int page, int size);

    Page<Api> search(String query, int page, int size);
}
