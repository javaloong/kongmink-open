package org.javaloong.kongmink.open.apim.gravitee.internal.resource.auth;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.TokenEntity;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

public interface OAuth2AuthenticationResource {

    @POST
    @Path("_exchange")
    @Produces(MediaType.APPLICATION_JSON)
    TokenEntity tokenExchange(@QueryParam(value = "token") final String token);
}
