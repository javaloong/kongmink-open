package org.javaloong.kongmink.open.apim.gravitee.internal.resource;

import org.javaloong.kongmink.open.apim.gravitee.internal.resource.auth.OAuth2AuthenticationResource;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public interface AuthResource {

    @Path("/oauth2/{identity}")
    OAuth2AuthenticationResource getOAuth2AuthenticationResource(
            @PathParam(value = "identity") final String identity);
}
