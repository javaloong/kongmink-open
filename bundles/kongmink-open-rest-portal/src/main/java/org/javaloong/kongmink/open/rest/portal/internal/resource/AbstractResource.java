package org.javaloong.kongmink.open.rest.portal.internal.resource;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.javaloong.kongmink.open.rest.RESTConstants;

@SecurityRequirement(name = RESTConstants.SECURITY_BEARER_AUTH)
@Tag(name = AbstractResource.TAG_NAME)
public abstract class AbstractResource {

    public static final String TAG_NAME = "portal";
}
