package org.javaloong.kongmink.open.rest.core.internal.resource;

import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.javaloong.kongmink.open.rest.core.model.EmailDto;
import org.javaloong.kongmink.open.rest.core.model.ProfileDto;
import org.javaloong.kongmink.open.rest.core.model.UpdatePasswordDto;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Component(service = UserResource.class)
@JaxrsResource
@JaxrsName(UserResource.RESOURCE_NAME)
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")")
@JSONRequired
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    static final String RESOURCE_NAME = "user";

    @Reference
    UserService userService;

    @Path("/profile")
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Response updateProfile(@Valid ProfileDto profileDto, @Context User user) {
        userService.updateProfile(profileDto.toUserProfile(user));
        return Response.noContent().build();
    }

    @Path("/password")
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Response updatePassword(@Valid UpdatePasswordDto passwordDto, @Context User user) {
        userService.updatePassword(passwordDto.toUserPassword(user));
        return Response.noContent().build();
    }

    @Path("/email")
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Response updateEmail(@Valid EmailDto emailDto, @Context User user) {
        userService.updateEmail(emailDto.toUserEmail(user));
        return Response.noContent().build();
    }

    @GET
    public Response getUser(@Context User user) {
        Optional<ComplexUser> result = userService.findById(user.getId());
        return result.map(u -> Response.ok(u).build()).orElseThrow(ForbiddenException::new);
    }
}
