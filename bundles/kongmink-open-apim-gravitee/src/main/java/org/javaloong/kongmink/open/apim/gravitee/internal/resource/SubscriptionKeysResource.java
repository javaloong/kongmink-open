package org.javaloong.kongmink.open.apim.gravitee.internal.resource;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.Key;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface SubscriptionKeysResource {

    @POST
    @Path("/_renew")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Key renewKeySubscription();

    @POST
    @Path("/{apiKey}/_revoke")
    @Produces(MediaType.APPLICATION_JSON)
    Response revokeKeySubscription(@PathParam("apiKey") String apiKey);
}
