package org.javaloong.kongmink.open.rest.admin.internal.resource;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.javaloong.kongmink.open.rest.admin.internal.security.Roles;
import org.javaloong.kongmink.open.service.UserConfigService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@RequiresRoles(Roles.MANAGE_USERS)
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {

    private final UserConfigService userConfigService;

    public UsersResource(UserConfigService userConfigService) {
        this.userConfigService = userConfigService;
    }

    @GET
    @Path("/{id}/config")
    public Response getUserConfig(@PathParam("id") String userId) {
        Map<String, Object> userConfig = userConfigService.getConfig(userId);
        return Response.ok(userConfig).build();
    }

    @PUT
    @Path("/{id}/config")
    public Response setUserConfig(@PathParam("id") String userId, Map<String, Object> userConfig) {
        userConfigService.setConfig(userId, userConfig);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/config")
    public Response updateUserConfig(@PathParam("id") String userId, Map<String, Object> userConfig) {
        userConfigService.updateConfig(userId, userConfig);
        return Response.noContent().build();
    }
}
