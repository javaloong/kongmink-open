package org.javaloong.kongmink.open.core.service;

import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.common.user.UserEmail;
import org.javaloong.kongmink.open.common.user.UserPassword;
import org.javaloong.kongmink.open.common.user.UserProfile;

public interface AccountService {

    User getDetails(User user);

    void updateProfile(UserProfile userProfile);

    void updatePassword(UserPassword userPassword);

    void updateEmail(UserEmail userEmail);
}
