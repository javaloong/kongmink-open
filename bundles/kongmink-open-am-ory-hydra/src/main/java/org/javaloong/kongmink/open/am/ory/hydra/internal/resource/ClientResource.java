package org.javaloong.kongmink.open.am.ory.hydra.internal.resource;

import org.javaloong.kongmink.open.am.ory.hydra.internal.model.OAuth2Client;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface ClientResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    OAuth2Client getClientById();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateClientById(OAuth2Client client);

    @DELETE
    Response deleteClientById();
}
