package org.javaloong.kongmink.open.am.ory.hydra.internal.resource;

import org.javaloong.kongmink.open.am.ory.hydra.internal.model.OAuth2Client;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public interface ClientsResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response create(OAuth2Client client);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<OAuth2Client> findAll();

    @Path("{id}")
    ClientResource get(@PathParam("id") String id);
}
