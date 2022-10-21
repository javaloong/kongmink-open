package org.javaloong.kongmink.open.apim.gravitee.internal;

import org.javaloong.kongmink.open.apim.ApiProvider;
import org.javaloong.kongmink.open.apim.gravitee.internal.mapper.ApiMapper;
import org.javaloong.kongmink.open.apim.gravitee.internal.mapper.CategoryMapper;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.ApiEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.CategoryEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.PageEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.PlanEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.*;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.param.ApisParam;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.param.PaginationParam;
import org.javaloong.kongmink.open.common.model.*;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.ws.rs.NotFoundException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component(service = ApiProvider.class)
public class GraviteeApiProvider implements ApiProvider {

    private static final String INCLUDE_CONTENT = "content";
    private static final String INCLUDE_PLANS = "plans";

    private final GraviteePortalClient client;

    @Activate
    public GraviteeApiProvider(@Reference GraviteePortalClient client) {
        this.client = client;
    }

    @Override
    public Collection<Category> getCategories() {
        ApisParam apisParam = new ApisParam();
        DataResponse<CategoryEntity> dataResponse = client.getApisResource().listCategories(apisParam);
        return CategoryMapper.mapToCategories(dataResponse.getData());
    }

    @Override
    public ApiMetrics getMetrics(String apiId) {
        ApiMetricsResource apiMetricsResource = getApiResource(apiId).getApiMetricsResource();
        ApiMetrics apiMetrics = apiMetricsResource.getApiMetricsByApiId();
        apiMetrics.setId(apiId);
        return apiMetrics;
    }

    @Override
    public ApiPage getPage(String apiId, String pageId, String lang) {
        ApiPageResource apiPageResource = getApiResource(apiId).getApiPagesResource()
                .getApiPageResource(pageId);
        List<String> include = Collections.singletonList(INCLUDE_CONTENT);
        PageEntity pageEntity = apiPageResource.getPageByApiIdAndPageId(lang, include);
        return ApiMapper.mapToApiPage(pageEntity);
    }

    @Override
    public Page<ApiPage> getPages(String apiId, String lang, String parent, int page, int size) {
        ApiPagesResource apiPagesResource = getApiResource(apiId).getApiPagesResource();
        PaginationParam paginationParam = createPaginationParam(page, size);
        DataResponse<PageEntity> dataResponse = apiPagesResource.getPagesByApiId(
                lang, paginationParam, false, parent);
        return ApiMapper.mapToPaginationApiPages(dataResponse);
    }

    @Override
    public Page<Plan> getPlans(String apiId, int page, int size) {
        PaginationParam paginationParam = createPaginationParam(page, size);
        ApiPlansResource apiPlansResource = getApiResource(apiId).getApiPlansResource();
        DataResponse<PlanEntity> dataResponse = apiPlansResource.getApiPlansByApiId(paginationParam);
        return ApiMapper.mapToPaginationPlans(dataResponse);
    }

    @Override
    public Optional<Api> findById(String id) {
        ApiResource apiResource = getApiResource(id);
        List<String> include = Collections.singletonList(INCLUDE_PLANS);
        return getApiEntity(apiResource, include).map(ApiMapper::mapToApi);
    }

    @Override
    public Page<Api> findAll(String category, int page, int size) {
        PaginationParam paginationParam = createPaginationParam(page, size);
        ApisParam apisParam = new ApisParam();
        apisParam.setCategory(category);
        DataResponse<ApiEntity> dataResponse = client.getApisResource().getApis(paginationParam, apisParam);
        return ApiMapper.mapToPaginationApis(dataResponse);
    }

    @Override
    public Page<Api> search(String query, int page, int size) {
        PaginationParam paginationParam = createPaginationParam(page, size);
        DataResponse<ApiEntity> dataResponse = client.getApisResource().searchApis(query, paginationParam);
        return ApiMapper.mapToPaginationApis(dataResponse);
    }

    private ApiResource getApiResource(String id) {
        return client.getApisResource().getApiResource(id);
    }

    private Optional<ApiEntity> getApiEntity(ApiResource resource, List<String> include) {
        try {
            return Optional.ofNullable(resource.getApiByApiId(include));
        } catch (NotFoundException ex) {
            return Optional.empty();
        }
    }

    private PaginationParam createPaginationParam(int page, int size) {
        PaginationParam paginationParam = new PaginationParam();
        paginationParam.setPage(page);
        paginationParam.setSize(size);
        return paginationParam;
    }
}
