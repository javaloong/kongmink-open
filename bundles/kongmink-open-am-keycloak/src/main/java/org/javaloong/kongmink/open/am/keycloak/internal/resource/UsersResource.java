package org.javaloong.kongmink.open.am.keycloak.internal.resource;

import org.keycloak.representations.idm.UserRepresentation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public interface UsersResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<UserRepresentation> search(@QueryParam("username") String username,
                                    @QueryParam("firstName") String firstName,
                                    @QueryParam("lastName") String lastName,
                                    @QueryParam("email") String email,
                                    @QueryParam("first") Integer firstResult,
                                    @QueryParam("max") Integer maxResults);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<UserRepresentation> search(@QueryParam("username") String username, @QueryParam("exact") Boolean exact);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response create(UserRepresentation userRepresentation);

    @Path("{id}")
    UserResource get(@PathParam("id") String id);
}
