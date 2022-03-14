package org.javaloong.kongmink.open.apim.gravitee.internal.resource;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.UserEntity;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public interface UserResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    UserEntity getCurrentUser();
}
