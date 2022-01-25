package org.javaloong.kongmink.open.service.model;

import org.javaloong.kongmink.open.common.user.User;

public class ComplexUser extends User {

    public static ComplexUser fromUser(User source) {
        ComplexUser user = new ComplexUser();
        user.setId(source.getId());
        user.setUsername(source.getUsername());
        user.setPassword(source.getPassword());
        user.setEmail(source.getEmail());
        user.setEmailVerified(source.isEmailVerified());
        user.setEnabled(source.isEnabled());
        user.setProfile(source.getProfile());
        user.setCreatedTimestamp(source.getCreatedTimestamp());
        return user;
    }
}
