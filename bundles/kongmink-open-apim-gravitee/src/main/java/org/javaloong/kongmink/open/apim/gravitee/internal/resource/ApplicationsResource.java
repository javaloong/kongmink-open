package org.javaloong.kongmink.open.apim.gravitee.internal.resource;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.ApplicationEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.NewApplicationEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.param.PaginationParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface ApplicationsResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    ApplicationEntity createApplication(NewApplicationEntity newApplicationEntity);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    DataResponse<ApplicationEntity> getApplications(
            @BeanParam PaginationParam paginationParam,
            @QueryParam("forSubscription") final boolean forSubscription,
            @QueryParam("order") @DefaultValue("name") final String order);

    @Path("{applicationId}")
    ApplicationResource getApplicationResource(@PathParam("applicationId") String applicationId);
}
