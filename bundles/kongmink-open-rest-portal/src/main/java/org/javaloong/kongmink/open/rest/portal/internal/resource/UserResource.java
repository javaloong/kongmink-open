package org.javaloong.kongmink.open.rest.portal.internal.resource;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.javaloong.kongmink.open.common.model.User;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.javaloong.kongmink.open.rest.portal.dto.EmailDTO;
import org.javaloong.kongmink.open.rest.portal.dto.ProfileDTO;
import org.javaloong.kongmink.open.rest.portal.dto.UpdatePasswordDTO;
import org.javaloong.kongmink.open.service.AccountService;
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

@Component(service = UserResource.class)
@JaxrsResource
@JaxrsName(UserResource.RESOURCE_NAME)
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")")
@JSONRequired
@SecurityRequirement(name = RESTConstants.SECURITY_BEARER_AUTH)
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    static final String RESOURCE_NAME = "user";

    @Reference
    AccountService accountService;

    @PUT
    @Path("/profile")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProfile(@Valid ProfileDTO profileDto, @Context User user) {
        accountService.updateProfile(profileDto.toUserProfile(user));
        return Response.noContent().build();
    }

    @PUT
    @Path("/password")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePassword(@Valid UpdatePasswordDTO passwordDto, @Context User user) {
        accountService.updatePassword(passwordDto.toUserPassword(user));
        return Response.noContent().build();
    }

    @PUT
    @Path("/email")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEmail(@Valid EmailDTO emailDto, @Context User user) {
        accountService.updateEmail(emailDto.toUserEmail(user));
        return Response.noContent().build();
    }

    @GET
    public Response getUser(@Context User user) {
        User result = accountService.getDetails(user);
        return Response.ok(result).build();
    }
}
