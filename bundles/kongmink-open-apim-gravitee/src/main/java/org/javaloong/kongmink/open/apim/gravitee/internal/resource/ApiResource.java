package org.javaloong.kongmink.open.apim.gravitee.internal.resource;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.ApiEntity;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public interface ApiResource {

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    ApiEntity getApiByApiId(@QueryParam("include") List<String> include);

    @GET
    @Path("picture")
    @Produces({ MediaType.WILDCARD, MediaType.APPLICATION_JSON })
    Response getPictureByApiId();

    @GET
    @Path("background")
    @Produces({ MediaType.WILDCARD, MediaType.APPLICATION_JSON })
    Response getBackgroundByApiId();

    @Path("metrics")
    ApiMetricsResource getApiMetricsResource();

    @Path("plans")
    ApiPlansResource getApiPlansResource();
}
