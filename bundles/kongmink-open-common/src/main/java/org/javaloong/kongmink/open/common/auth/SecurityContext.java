package org.javaloong.kongmink.open.common.auth;

import org.javaloong.kongmink.open.common.user.User;

public class SecurityContext {

    private User user;
    private String token;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
