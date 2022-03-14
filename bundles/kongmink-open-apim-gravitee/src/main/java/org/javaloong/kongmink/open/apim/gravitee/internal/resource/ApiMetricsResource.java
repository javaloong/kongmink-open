package org.javaloong.kongmink.open.apim.gravitee.internal.resource;

import org.javaloong.kongmink.open.apim.model.ApiMetrics;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public interface ApiMetricsResource {

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    ApiMetrics getApiMetricsByApiId();
}
