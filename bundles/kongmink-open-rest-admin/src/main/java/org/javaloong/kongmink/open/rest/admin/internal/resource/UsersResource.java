package org.javaloong.kongmink.open.rest.admin.internal.resource;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.javaloong.kongmink.open.rest.admin.dto.UserQueryDto;
import org.javaloong.kongmink.open.rest.admin.internal.security.Roles;
import org.javaloong.kongmink.open.service.UserService;
import org.javaloong.kongmink.open.service.dto.UserDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.Optional;

@RequiresRoles(Roles.MANAGE_USERS)
@SecurityRequirement(name = RESTConstants.SECURITY_BEARER_AUTH)
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {

    private final UserService userService;

    public UsersResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Path("/{id}/config")
    public Response getUserConfig(@PathParam("id") String userId) {
        Map<String, Object> userConfig = userService.getConfig(userId);
        return Response.ok(userConfig).build();
    }

    @PUT
    @Path("/{id}/config")
    public Response setUserConfig(@PathParam("id") String userId, Map<String, Object> userConfig) {
        userService.setConfig(userId, userConfig);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/config")
    public Response updateUserConfig(@PathParam("id") String userId, Map<String, Object> userConfig) {
        userService.updateConfig(userId, userConfig);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") String userId) {
        Optional<UserDTO> result = userService.findById(userId);
        return result.map(user -> Response.ok(user).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    public Response getUsers(@BeanParam UserQueryDto userQueryDto,
                             @DefaultValue("1") @QueryParam("page") int page,
                             @DefaultValue("10") @QueryParam("size") int size) {
        Page<UserDTO> result = userService.findAll(userQueryDto.toUserQuery(), page, size);
        return Response.ok(result).build();
    }
}
