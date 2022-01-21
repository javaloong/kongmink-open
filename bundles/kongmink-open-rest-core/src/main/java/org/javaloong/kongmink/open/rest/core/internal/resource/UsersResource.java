package org.javaloong.kongmink.open.rest.core.internal.resource;

import org.javaloong.kongmink.open.rest.RESTConstants;
import org.javaloong.kongmink.open.rest.core.model.UserDto;
import org.javaloong.kongmink.open.service.UserService;
import org.javaloong.kongmink.open.service.model.ComplexUser;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JSONRequired;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsName;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsResource;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Component(service = UsersResource.class)
@JaxrsResource
@JaxrsName(UsersResource.RESOURCE_NAME)
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")")
@JSONRequired
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {

    static final String RESOURCE_NAME = "users";

    @Reference
    UserService userService;

    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response createUser(@Valid UserDto userDto) {
        ComplexUser result = userService.create(userDto.toUser());
        return Response.ok(result).status(Response.Status.CREATED).build();
    }

    @HEAD
    @Path("/username/{username}")
    public Response verifyUsernameExists(@PathParam("username") String username) {
        Optional<ComplexUser> result = userService.findByUsername(username);
        return result.map(user -> Response.ok().build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @HEAD
    @Path("/email/{email}")
    public Response verifyEmailExists(@PathParam("email") String email) {
        Optional<ComplexUser> result = userService.findByEmail(email);
        return result.map(user -> Response.ok().build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }
}
