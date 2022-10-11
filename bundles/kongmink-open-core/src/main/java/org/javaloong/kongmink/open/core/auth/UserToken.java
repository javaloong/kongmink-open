package org.javaloong.kongmink.open.core.auth;

import org.javaloong.kongmink.open.common.user.User;

import java.util.Objects;

public class UserToken {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserToken that = (UserToken) o;
        return Objects.equals(user, that.user) && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, token);
    }
}
