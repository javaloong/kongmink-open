package org.javaloong.kongmink.open.service.impl;

import org.javaloong.kongmink.open.apim.ApiProvider;
import org.javaloong.kongmink.open.apim.model.Api;
import org.javaloong.kongmink.open.apim.model.Category;
import org.javaloong.kongmink.open.apim.model.Plan;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.service.ApiService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Collection;
import java.util.Optional;

@Component(service = ApiService.class)
public class ApiServiceImpl implements ApiService {

    @Reference
    ApiProvider apiProvider;

    @Override
    public Collection<Category> getCategories() {
        return apiProvider.getCategories();
    }

    @Override
    public Page<Plan> getPlans(String apiId, int page, int size) {
        return apiProvider.getPlans(apiId, page, size);
    }

    @Override
    public Optional<Api> findById(String id) {
        return apiProvider.findById(id);
    }

    @Override
    public Page<Api> findAll(String category, int page, int size) {
        return apiProvider.findAll(category, page, size);
    }

    @Override
    public Page<Api> search(String query, int page, int size) {
        return apiProvider.search(query, page, size);
    }
}
