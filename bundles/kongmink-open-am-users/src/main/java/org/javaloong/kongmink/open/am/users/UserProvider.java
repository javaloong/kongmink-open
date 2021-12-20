package org.javaloong.kongmink.open.am.users;

import org.javaloong.kongmink.open.am.users.model.User;
import org.javaloong.kongmink.open.am.users.model.UserEmail;
import org.javaloong.kongmink.open.am.users.model.UserPassword;
import org.javaloong.kongmink.open.am.users.model.UserProfile;

import java.util.Optional;

public interface UserProvider {

    Optional<User> findById(String userId);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User create(User user);

    void updateProfile(UserProfile userProfile);

    void updatePassword(UserPassword userPassword);

    void updateEmail(UserEmail userEmail);
}
