package org.javaloong.kongmink.open.account;

import org.javaloong.kongmink.open.common.model.User;
import org.javaloong.kongmink.open.common.model.UserEmail;
import org.javaloong.kongmink.open.common.model.UserPassword;
import org.javaloong.kongmink.open.common.model.UserProfile;

public interface UserProvider {

    User getDetails();

    void updateProfile(UserProfile userProfile);

    void updatePassword(UserPassword userPassword);

    void updateEmail(UserEmail userEmail);
}
