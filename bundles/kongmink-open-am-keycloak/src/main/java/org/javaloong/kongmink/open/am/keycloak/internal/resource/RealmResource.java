package org.javaloong.kongmink.open.am.keycloak.internal.resource;

import javax.ws.rs.Path;

public interface RealmResource {

    @Path("clients")
    ClientsResource clients();

    @Path("users")
    UsersResource users();
}
