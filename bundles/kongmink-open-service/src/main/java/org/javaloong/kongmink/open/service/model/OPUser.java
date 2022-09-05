package org.javaloong.kongmink.open.service.model;

import org.javaloong.kongmink.open.common.user.User;

public class OPUser extends User {

    public static OPUser fromUser(User base) {
        OPUser user = new OPUser();
        user.setId(base.getId());
        user.setUsername(base.getUsername());
        user.setPassword(base.getPassword());
        user.setEmail(base.getEmail());
        user.setEmailVerified(base.isEmailVerified());
        user.setEnabled(base.isEnabled());
        user.setProfile(base.getProfile());
        user.setCreatedAt(base.getCreatedAt());
        user.setAttributes(base.getAttributes());
        return user;
    }
}
