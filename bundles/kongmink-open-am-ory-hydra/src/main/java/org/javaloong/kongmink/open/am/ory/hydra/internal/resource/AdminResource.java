package org.javaloong.kongmink.open.am.ory.hydra.internal.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/admin")
@Consumes(MediaType.APPLICATION_JSON)
public interface AdminResource {

    @Path("clients")
    ClientsResource getClientsResource();
}
