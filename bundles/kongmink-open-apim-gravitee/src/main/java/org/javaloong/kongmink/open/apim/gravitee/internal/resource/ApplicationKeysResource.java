package org.javaloong.kongmink.open.apim.gravitee.internal.resource;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.KeyEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface ApplicationKeysResource {

    @POST
    @Path("/_renew")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    KeyEntity renewSharedKey();

    @POST
    @Path("/{apiKey}/_revoke")
    @Produces(MediaType.APPLICATION_JSON)
    Response revokeKeySubscription(@PathParam("apiKey") String apiKey);
}
