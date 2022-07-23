package org.javaloong.kongmink.open.rest.auth.internal;

import io.buji.pac4j.subject.Pac4jPrincipal;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.javaloong.kongmink.open.common.client.Client;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JSONRequired;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsResource;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public abstract class TestResources {

    @Component(service = TestUserResource.class)
    @JaxrsResource
    @JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")")
    @JSONRequired
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public static class TestUserResource {

        @RequiresUser
        @GET
        public Response getUser(@Context SecurityContext securityContext) {
            Pac4jPrincipal principal = (Pac4jPrincipal) securityContext.getUserPrincipal();
            return Response.ok(principal.getProfile()).build();
        }
    }

    @Component(service = TestClientResource.class)
    @JaxrsResource
    @JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")")
    @JSONRequired
    @RequiresRoles("manage-clients")
    @Path("/clients")
    @Produces(MediaType.APPLICATION_JSON)
    public static class TestClientResource {

        @RequiresPermissions("client:create")
        @Consumes(MediaType.APPLICATION_JSON)
        @POST
        public Response createClient(Client client) {
            return Response.ok(client).status(Response.Status.CREATED).build();
        }

        @RequiresPermissions("client:edit")
        @Path("/{id}")
        @Consumes(MediaType.APPLICATION_JSON)
        @PUT
        public Response updateClient(@PathParam("id") String id, Client client) {
            return Response.noContent().build();
        }

        @RequiresPermissions("client:delete")
        @Path("/{id}")
        @DELETE
        public Response deleteClient(@PathParam("id") String id) {
            return Response.noContent().build();
        }

        @RequiresPermissions("client:read")
        @GET
        @Path("/{id}")
        public Response getClient(@PathParam("id") String id) {
            return Response.ok(dummyClient()).build();
        }

        private Client dummyClient() {
            Client client = new Client();
            client.setId("1");
            client.setName("client1");
            return client;
        }
    }
}
