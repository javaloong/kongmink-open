package org.javaloong.kongmink.open.service;

import org.javaloong.kongmink.open.apim.model.*;
import org.javaloong.kongmink.open.common.model.Page;

import java.util.Collection;
import java.util.Optional;

public interface ApiService {

    Collection<Category> getCategories();

    ApiMetrics getMetrics(String apiId);

    ApiPage getPage(String apiId, String pageId, String lang);

    Page<ApiPage> getPages(String apiId, String lang, String parent, int page, int size);

    Page<Plan> getPlans(String apiId, int page, int size);

    Optional<Api> findById(String id);

    Page<Api> findAll(String category, int page, int size);

    Page<Api> search(String query, int page, int size);
}
