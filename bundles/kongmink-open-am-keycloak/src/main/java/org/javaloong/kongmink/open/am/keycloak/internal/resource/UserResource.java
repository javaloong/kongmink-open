package org.javaloong.kongmink.open.am.keycloak.internal.resource;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UserResource {

    @GET
    UserRepresentation toRepresentation();

    @PUT
    void update(UserRepresentation userRepresentation);

    @DELETE
    void remove();

    @PUT
    @Path("reset-password")
    void resetPassword(CredentialRepresentation credentialRepresentation);
}
