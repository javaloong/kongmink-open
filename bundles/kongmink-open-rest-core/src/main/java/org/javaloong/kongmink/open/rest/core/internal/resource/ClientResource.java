package org.javaloong.kongmink.open.rest.core.internal.resource;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.javaloong.kongmink.open.rest.core.model.ClientDto;
import org.javaloong.kongmink.open.rest.core.security.Roles;
import org.javaloong.kongmink.open.service.ClientService;
import org.javaloong.kongmink.open.service.model.ComplexClient;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JSONRequired;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsName;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsResource;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Component(service = ClientResource.class)
@JaxrsResource
@JaxrsName(ClientResource.RESOURCE_NAME)
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")")
@JSONRequired
@RequiresRoles(Roles.MANAGE_CLIENTS)
@SecurityRequirement(name = RESTConstants.SECURITY_BEARER_AUTH)
@Path("/clients")
@Produces(MediaType.APPLICATION_JSON)
public class ClientResource {

    static final String RESOURCE_NAME = "client";

    @Reference
    ClientService clientService;

    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response createClient(@Valid ClientDto clientDto, @Context User user) {
        ComplexClient result = clientService.create(user, clientDto.toClient());
        return Response.ok(result).status(Response.Status.CREATED).build();
    }

    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Response updateClient(@PathParam("id") String id, @Valid ClientDto clientDto,
                                 @Context User user) {
        clientService.update(clientDto.toClient(id));
        return Response.noContent().build();
    }

    @Path("/{id}")
    @DELETE
    public Response deleteClient(@PathParam("id") String id, @Context User user) {
        clientService.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    public Response getClient(@PathParam("id") String id, @Context User user) {
        Optional<ComplexClient> result = clientService.findById(id);
        return result.map(client -> Response.ok(client).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    public Response getClients(@DefaultValue("1") @QueryParam("page") int page,
                               @DefaultValue("10") @QueryParam("size") int size,
                               @Context User user) {
        Page<ComplexClient> result = clientService.findAllByUser(user, page, size);
        return Response.ok(result).status(Response.Status.OK).build();
    }
}
