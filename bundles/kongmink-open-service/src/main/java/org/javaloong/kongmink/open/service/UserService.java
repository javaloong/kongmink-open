package org.javaloong.kongmink.open.service;

import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.common.user.UserEmail;
import org.javaloong.kongmink.open.common.user.UserPassword;
import org.javaloong.kongmink.open.common.user.UserProfile;
import org.javaloong.kongmink.open.service.model.OPUser;

public interface UserService {

    OPUser get(User user);

    OPUser loadByUser(User user);

    void updateProfile(UserProfile userProfile);

    void updatePassword(UserPassword userPassword);

    void updateEmail(UserEmail userEmail);
}
