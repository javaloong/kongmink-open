package org.javaloong.kongmink.open.rest.auth.policy.internal;

import org.javaloong.kongmink.open.common.model.Client;
import org.javaloong.kongmink.open.core.auth.policy.annotation.Fact;
import org.javaloong.kongmink.open.core.auth.policy.annotation.Policy;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JSONRequired;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsResource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component(service = TestClientResource.class)
@JaxrsResource
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")")
@JSONRequired
@Path("/clients")
@Produces(MediaType.APPLICATION_JSON)
public class TestClientResource {

    @Policy("client_create")
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response createClient(@Fact("client") Client client) {
        return Response.ok(client).status(Response.Status.CREATED).build();
    }
}
