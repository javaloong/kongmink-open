package org.javaloong.kongmink.open.am.keycloak.internal.resource;

import org.keycloak.representations.idm.ClientRepresentation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public interface ClientsResource {

    @Path("{id}")
    ClientResource get(@PathParam("id") String id);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response create(ClientRepresentation clientRepresentation);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<ClientRepresentation> findAll();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<ClientRepresentation> findAll(@QueryParam("viewableOnly") boolean viewableOnly);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<ClientRepresentation> findAll(@QueryParam("clientId") String clientId,
                                       @QueryParam("viewableOnly") Boolean viewableOnly,
                                       @QueryParam("search") Boolean search,
                                       @QueryParam("first") Integer firstResult,
                                       @QueryParam("max") Integer maxResults);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<ClientRepresentation> findByClientId(@QueryParam("clientId") String clientId);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<ClientRepresentation> query(@QueryParam("q") String searchQuery);
}
