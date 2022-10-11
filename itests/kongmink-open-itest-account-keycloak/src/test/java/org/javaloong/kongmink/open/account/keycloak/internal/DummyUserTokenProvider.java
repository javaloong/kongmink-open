package org.javaloong.kongmink.open.account.keycloak.internal;

import org.javaloong.kongmink.open.core.auth.UserToken;
import org.javaloong.kongmink.open.core.auth.UserTokenProvider;

public class DummyUserTokenProvider implements UserTokenProvider {

    @Override
    public UserToken getUserToken() {
        UserToken userToken = new UserToken();
        userToken.setToken("test_access_token");
        return userToken;
    }
}
