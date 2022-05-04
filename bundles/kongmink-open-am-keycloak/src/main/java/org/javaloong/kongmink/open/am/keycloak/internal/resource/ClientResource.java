package org.javaloong.kongmink.open.am.keycloak.internal.resource;

import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface ClientResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    ClientRepresentation toRepresentation();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    Response update(ClientRepresentation clientRepresentation);

    @DELETE
    Response remove();

    @POST
    @Path("client-secret")
    @Produces(MediaType.APPLICATION_JSON)
    CredentialRepresentation generateNewSecret();

    @GET
    @Path("client-secret")
    @Produces(MediaType.APPLICATION_JSON)
    CredentialRepresentation getSecret();
}
