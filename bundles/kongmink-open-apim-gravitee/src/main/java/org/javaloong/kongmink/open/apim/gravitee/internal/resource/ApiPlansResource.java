package org.javaloong.kongmink.open.apim.gravitee.internal.resource;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.PlanEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.param.PaginationParam;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public interface ApiPlansResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    DataResponse<PlanEntity> getApiPlansByApiId(@BeanParam PaginationParam paginationParam);
}
