package org.javaloong.kongmink.open.rest.admin.internal.resource;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.javaloong.kongmink.open.rest.admin.internal.security.Roles;
import org.javaloong.kongmink.open.service.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@RequiresRoles(Roles.MANAGE_USERS)
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
}
