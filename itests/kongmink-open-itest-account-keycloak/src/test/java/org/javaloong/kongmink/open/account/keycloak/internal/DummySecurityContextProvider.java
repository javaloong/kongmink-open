package org.javaloong.kongmink.open.account.keycloak.internal;

import org.javaloong.kongmink.open.common.auth.SecurityContext;
import org.javaloong.kongmink.open.common.auth.SecurityContextProvider;

public class DummySecurityContextProvider implements SecurityContextProvider {

    @Override
    public SecurityContext getContext() {
        SecurityContext securityContext = new SecurityContext();
        securityContext.setToken("test_access_token");
        return securityContext;
    }
}
