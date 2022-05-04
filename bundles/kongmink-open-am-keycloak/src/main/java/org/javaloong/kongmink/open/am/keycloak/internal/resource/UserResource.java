package org.javaloong.kongmink.open.am.keycloak.internal.resource;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UserResource {

    @GET
    UserRepresentation toRepresentation();

    @PUT
    Response update(UserRepresentation userRepresentation);

    @DELETE
    Response remove();

    @PUT
    @Path("reset-password")
    Response resetPassword(CredentialRepresentation credentialRepresentation);
}
