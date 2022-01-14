package org.javaloong.kongmink.open.am.user;

import org.javaloong.kongmink.open.common.model.user.User;
import org.javaloong.kongmink.open.common.model.user.UserEmail;
import org.javaloong.kongmink.open.common.model.user.UserPassword;
import org.javaloong.kongmink.open.common.model.user.UserProfile;

import java.util.Optional;

public interface UserProvider {

    Optional<User> findById(String userId);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User create(User user);

    void updateProfile(UserProfile userProfile);

    void updatePassword(UserPassword userPassword);

    void updateEmail(UserEmail userEmail);

    void delete(String userId);
}
