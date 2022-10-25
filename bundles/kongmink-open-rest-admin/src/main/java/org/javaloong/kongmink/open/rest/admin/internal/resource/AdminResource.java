package org.javaloong.kongmink.open.rest.admin.internal.resource;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.javaloong.kongmink.open.service.UserConfigService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JSONRequired;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsName;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsResource;

import javax.ws.rs.Path;

@Component(service = AdminResource.class)
@JaxrsResource
@JaxrsName(AdminResource.RESOURCE_NAME)
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")")
@JSONRequired
@SecurityRequirement(name = RESTConstants.SECURITY_BEARER_AUTH)
@Tag(name = AdminResource.RESOURCE_NAME)
@Path(AdminResource.RESOURCE_NAME)
public class AdminResource {

    static final String RESOURCE_NAME = "admin";

    @Reference
    UserConfigService userConfigService;

    @Path("users")
    public UsersResource getUsersResource() {
        return new UsersResource(userConfigService);
    }
}
