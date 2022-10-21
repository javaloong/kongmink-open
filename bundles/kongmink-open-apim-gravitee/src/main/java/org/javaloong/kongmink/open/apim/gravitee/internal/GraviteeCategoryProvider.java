package org.javaloong.kongmink.open.apim.gravitee.internal;

import org.javaloong.kongmink.open.apim.CategoryProvider;
import org.javaloong.kongmink.open.apim.gravitee.internal.mapper.CategoryMapper;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.CategoryEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.DataResponse;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.param.PaginationParam;
import org.javaloong.kongmink.open.common.model.Category;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Collection;

@Component(service = CategoryProvider.class)
public class GraviteeCategoryProvider implements CategoryProvider {

    private final GraviteePortalClient client;

    @Activate
    public GraviteeCategoryProvider(@Reference GraviteePortalClient client) {
        this.client = client;
    }

    @Override
    public Collection<Category> findAll(int size) {
        PaginationParam paginationParam = new PaginationParam();
        paginationParam.setPage(1);
        paginationParam.setSize(size);
        DataResponse<CategoryEntity> dataResponse = client.getCategoriesResource().getCategories(paginationParam);
        return CategoryMapper.mapToCategories(dataResponse.getData());
    }
}
