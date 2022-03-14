package org.javaloong.kongmink.open.apim.gravitee.internal.resource;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.ApiEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.CategoryEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.param.ApisParam;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.param.PaginationParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface ApisResource {

    @GET
    @Path("categories")
    @Produces(MediaType.APPLICATION_JSON)
    DataResponse<CategoryEntity> listCategories(@BeanParam ApisParam apisParam);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    DataResponse<ApiEntity> getApis(@BeanParam PaginationParam paginationParam, @BeanParam ApisParam apisParam);

    @POST
    @Path("_search")
    @Produces(MediaType.APPLICATION_JSON)
    DataResponse<ApiEntity> searchApis(@QueryParam("q") String query, @BeanParam PaginationParam paginationParam);

    @Path("{apiId}")
    ApiResource getApiResource(@PathParam("apiId") String apiId);
}
