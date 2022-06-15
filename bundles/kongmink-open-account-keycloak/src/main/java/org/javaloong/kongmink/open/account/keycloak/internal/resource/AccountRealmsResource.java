package org.javaloong.kongmink.open.account.keycloak.internal.resource;

import javax.ws.rs.Path;

@Path("realms/{realm}")
public interface AccountRealmsResource {

    @Path("account")
    AccountRestService getAccountRestService();
}
