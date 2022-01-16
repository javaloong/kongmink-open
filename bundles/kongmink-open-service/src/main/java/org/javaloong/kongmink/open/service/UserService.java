package org.javaloong.kongmink.open.service;

import org.javaloong.kongmink.open.common.model.user.User;
import org.javaloong.kongmink.open.common.model.user.UserEmail;
import org.javaloong.kongmink.open.common.model.user.UserPassword;
import org.javaloong.kongmink.open.common.model.user.UserProfile;
import org.javaloong.kongmink.open.service.model.ComplexUser;

import java.util.Optional;

public interface UserService {

    ComplexUser create(User user);

    void save(User user);

    void updateProfile(UserProfile userProfile);

    void updatePassword(UserPassword userPassword);

    void updateEmail(UserEmail userEmail);

    void delete(String id);

    Optional<ComplexUser> findById(String id);

    Optional<ComplexUser> findByUsername(String username);

    Optional<ComplexUser> findByEmail(String email);
}
