package org.javaloong.kongmink.open.rest.core.internal.resource;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.javaloong.kongmink.open.apim.model.ApiKey;
import org.javaloong.kongmink.open.apim.model.Subscription;
import org.javaloong.kongmink.open.apim.model.SubscriptionStatus;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.javaloong.kongmink.open.rest.core.dto.SubscriptionDTO;
import org.javaloong.kongmink.open.rest.core.internal.security.Roles;
import org.javaloong.kongmink.open.service.SubscriptionService;
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
import java.util.List;
import java.util.Optional;

@Component(service = SubscriptionResource.class)
@JaxrsResource
@JaxrsName(SubscriptionResource.RESOURCE_NAME)
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")")
@JSONRequired
@RequiresRoles(Roles.MANAGE_SUBSCRIPTIONS)
@SecurityRequirement(name = RESTConstants.SECURITY_BEARER_AUTH)
@Path("/subscriptions")
@Produces(MediaType.APPLICATION_JSON)
public class SubscriptionResource {

    static final String RESOURCE_NAME = "subscription";

    @Reference
    SubscriptionService subscriptionService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSubscription(@Valid SubscriptionDTO subscriptionDto, @Context User user) {
        Subscription newSubscription = subscriptionService.create(subscriptionDto.toSubscription());
        return Response.ok(newSubscription).status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/{id}/_close")
    public Response closeSubscription(@PathParam("id") String id, @Context User user) {
        subscriptionService.close(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    public Response getSubscription(@PathParam("id") String id, @Context User user) {
        Optional<Subscription> result = subscriptionService.findById(id);
        return result.map(subscription -> Response.ok(subscription).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    public Response getSubscriptions(@QueryParam("apiId") String apiId,
                                     @QueryParam("applicationId") String applicationId,
                                     @QueryParam("statuses") List<SubscriptionStatus> statuses,
                                     @DefaultValue("1") @QueryParam("page") int page,
                                     @DefaultValue("10") @QueryParam("size") int size,
                                     @Context User user) {
        Page<Subscription> result = subscriptionService.findAll(apiId, applicationId, statuses, page, size);
        return Response.ok(result).build();
    }

    @POST
    @Path("/{id}/keys/_renew")
    public Response renewKeySubscription(@PathParam("id") String id) {
        ApiKey result = subscriptionService.renewKey(id);
        return Response.ok(result).build();
    }

    @POST
    @Path("/{id}/keys/{apiKey}/_revoke")
    public Response revokeKeySubscription(@PathParam("id") String id, @PathParam("apiKey") String apiKey) {
        subscriptionService.revokeKey(id, apiKey);
        return Response.noContent().build();
    }
}
