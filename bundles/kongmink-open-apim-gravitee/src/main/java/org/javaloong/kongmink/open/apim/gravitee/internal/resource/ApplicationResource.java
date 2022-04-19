package org.javaloong.kongmink.open.apim.gravitee.internal.resource;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.ApplicationEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface ApplicationResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    ApplicationEntity getApplicationByApplicationId();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void updateApplicationByApplicationId(ApplicationEntity application);

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    void deleteApplicationByApplicationId();

    @GET
    @Path("picture")
    @Produces({ MediaType.WILDCARD, MediaType.APPLICATION_JSON })
    Response getPictureByApplicationId();

    @GET
    @Path("background")
    @Produces({ MediaType.WILDCARD, MediaType.APPLICATION_JSON })
    Response getBackgroundByApplicationId();

    @Path("analytics")
    ApplicationAnalyticsResource getApplicationAnalyticsResource();
}
