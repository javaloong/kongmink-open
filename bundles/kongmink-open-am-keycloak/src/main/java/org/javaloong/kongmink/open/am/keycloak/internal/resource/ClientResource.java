package org.javaloong.kongmink.open.am.keycloak.internal.resource;

import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface ClientResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    ClientRepresentation toRepresentation();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    void update(ClientRepresentation clientRepresentation);

    @DELETE
    void remove();

    @POST
    @Path("client-secret")
    @Produces(MediaType.APPLICATION_JSON)
    CredentialRepresentation generateNewSecret();

    @GET
    @Path("client-secret")
    @Produces(MediaType.APPLICATION_JSON)
    CredentialRepresentation getSecret();
}
