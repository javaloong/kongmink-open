package org.javaloong.kongmink.open.account.keycloak.internal.resource;

import org.keycloak.representations.account.UserRepresentation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface AccountRestService {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    UserRepresentation account(@QueryParam("userProfileMetadata") Boolean userProfileMetadata);

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updateAccount(UserRepresentation rep);
}
