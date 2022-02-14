package org.javaloong.kongmink.open.am.keycloak.internal.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Path("/admin/realms")
@Consumes(MediaType.APPLICATION_JSON)
public interface RealmsResource {

    @Path("/{realm}")
    RealmResource realm(@PathParam("realm") String realm);
}
