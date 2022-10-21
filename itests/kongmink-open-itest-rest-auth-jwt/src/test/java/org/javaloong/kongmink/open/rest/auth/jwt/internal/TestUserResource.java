package org.javaloong.kongmink.open.rest.auth.jwt.internal;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.javaloong.kongmink.open.common.model.User;
import org.javaloong.kongmink.open.core.auth.UserTokenProvider;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JSONRequired;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component(service = TestUserResource.class)
@JaxrsResource
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")")
@JSONRequired
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class TestUserResource {

    @Reference
    UserTokenProvider userTokenProvider;

    @RequiresUser
    @GET
    public Response getUser(@Context User user) {
        return Response.ok(user).build();
    }

    @RequiresUser
    @GET
    @Path("/token")
    public Response getUserToken() {
        String token = userTokenProvider.getUserToken().getToken();
        return Response.ok(token).build();
    }
}
