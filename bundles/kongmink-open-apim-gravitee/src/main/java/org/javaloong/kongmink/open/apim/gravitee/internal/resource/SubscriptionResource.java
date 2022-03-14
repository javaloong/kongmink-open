package org.javaloong.kongmink.open.apim.gravitee.internal.resource;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.SubscriptionEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public interface SubscriptionResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    SubscriptionEntity getSubscriptionBySubscriptionId(@QueryParam("include") List<String> include);

    @POST
    @Path("/_close")
    @Produces(MediaType.APPLICATION_JSON)
    Response closeSubscription();

    @Path("keys")
    SubscriptionKeysResource getSubscriptionKeysResource();
}
