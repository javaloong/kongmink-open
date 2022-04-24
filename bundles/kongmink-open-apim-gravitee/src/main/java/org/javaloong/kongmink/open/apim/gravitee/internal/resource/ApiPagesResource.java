package org.javaloong.kongmink.open.apim.gravitee.internal.resource;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.PageEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.param.PaginationParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface ApiPagesResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    DataResponse<PageEntity> getPagesByApiId(
            @HeaderParam("Accept-Language") String acceptLang,
            @BeanParam PaginationParam paginationParam,
            @QueryParam("homepage") Boolean homepage,
            @QueryParam("parent") String parent);

    @Path("{pageId}")
    ApiPageResource getApiPageResource(@PathParam("pageId") String pageId);
}
