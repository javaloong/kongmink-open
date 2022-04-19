package org.javaloong.kongmink.open.apim.gravitee.internal.resource;

import org.javaloong.kongmink.open.apim.gravitee.internal.resource.param.AnalyticsParam;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface ApplicationAnalyticsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response hits(@BeanParam AnalyticsParam analyticsParam);
}
