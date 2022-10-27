package org.javaloong.kongmink.open.rest.portal.internal.resource;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.javaloong.kongmink.open.common.model.*;
import org.javaloong.kongmink.open.core.auth.policy.AccessControlPolicies;
import org.javaloong.kongmink.open.core.auth.policy.annotation.Policy;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.javaloong.kongmink.open.rest.portal.dto.ApplicationDTO;
import org.javaloong.kongmink.open.rest.portal.internal.security.Roles;
import org.javaloong.kongmink.open.service.ApplicationService;
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

@Component(service = ApplicationResource.class)
@JaxrsResource
@JaxrsName(ApplicationResource.RESOURCE_NAME)
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")")
@JSONRequired
@RequiresRoles(Roles.MANAGE_APPLICATIONS)
@SecurityRequirement(name = RESTConstants.SECURITY_BEARER_AUTH)
@Path("/applications")
@Produces(MediaType.APPLICATION_JSON)
public class ApplicationResource {

    static final String RESOURCE_NAME = "application";

    @Reference
    ApplicationService applicationService;

    @Policy(value = AccessControlPolicies.USER_APPLICATION_CREATION, message = "{error.userApplicationCreationLimit}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createApplication(@Valid ApplicationDTO applicationDto, @Context User user) {
        Application newApplication = applicationService.create(user, applicationDto.toApplication());
        return Response.ok(newApplication).status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateApplication(@PathParam("id") String id, @Valid ApplicationDTO applicationDto,
                                      @Context User user) {
        applicationService.update(applicationDto.toApplication(id));
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteApplication(@PathParam("id") String id, @Context User user) {
        applicationService.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    public Response getApplication(@PathParam("id") String id, @Context User user) {
        Optional<Application> result = applicationService.findById(id);
        return result.map(application -> Response.ok(application).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    public Response getApplications(@DefaultValue("1") @QueryParam("page") int page,
                                    @DefaultValue("10") @QueryParam("size") int size,
                                    @Context User user) {
        Page<Application> result = applicationService.findAll(user, page, size);
        return Response.ok(result).build();
    }

    @GET
    @Path("/{id}/client-secret")
    public Response getClientSecret(@PathParam("id") String id) {
        ClientSecret result = applicationService.getClientSecret(id);
        return Optional.ofNullable(result).map(clientSecret -> Response.ok(clientSecret).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/{id}/client-secret")
    public Response regenerateClientSecret(@PathParam("id") String id) {
        ClientSecret result = applicationService.regenerateClientSecret(id);
        return Optional.ofNullable(result).map(clientSecret -> Response.ok(clientSecret).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/{id}/keys/_renew")
    public Response renewSharedKey(@PathParam("id") String id) {
        ApiKey result = applicationService.renewSharedKey(id);
        return Response.ok(result).build();
    }

    @POST
    @Path("/{id}/keys/{apiKey}/_revoke")
    public Response revokeKeySubscription(@PathParam("id") String id, @PathParam("apiKey") String apiKey) {
        applicationService.revokeKey(id, apiKey);
        return Response.noContent().build();
    }
}
