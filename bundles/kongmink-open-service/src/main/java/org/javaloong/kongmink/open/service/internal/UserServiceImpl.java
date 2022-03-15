package org.javaloong.kongmink.open.service.internal;

import org.javaloong.kongmink.open.am.UserProvider;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.common.user.UserEmail;
import org.javaloong.kongmink.open.common.user.UserPassword;
import org.javaloong.kongmink.open.common.user.UserProfile;
import org.javaloong.kongmink.open.data.UserRepository;
import org.javaloong.kongmink.open.data.domain.UserEntity;
import org.javaloong.kongmink.open.service.UserService;
import org.javaloong.kongmink.open.service.model.ComplexUser;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.transaction.control.TransactionControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Component(service = UserService.class)
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Reference
    TransactionControl transactionControl;
    @Reference
    UserProvider userProvider;
    @Reference
    UserRepository userRepository;

    @Override
    public ComplexUser create(User user) {
        User result = userProvider.create(user);
        log.debug("User {} successfully created", result.getId());
        return transactionControl.required(() -> {
            userRepository.create(mapToUserEntity(user));
            log.debug("User {} successfully added to repository", result.getId());
            return ComplexUser.fromUser(result);
        });
    }

    @Override
    public void save(User user) {
        transactionControl.required(() -> {
            userRepository.update(mapToUserEntity(user));
            log.debug("User {} successfully saved to repository", user.getId());
            return null;
        });
    }

    @Override
    public void updateProfile(UserProfile userProfile) {
        userProvider.updateProfile(userProfile);
        log.debug("User {} profile successfully updated", userProfile.getUserId());
    }

    @Override
    public void updatePassword(UserPassword userPassword) {
        userProvider.updatePassword(userPassword);
        log.debug("User {} password successfully updated", userPassword.getUserId());
    }

    @Override
    public void updateEmail(UserEmail userEmail) {
        userProvider.updateEmail(userEmail);
        log.debug("User {} email successfully updated", userEmail.getUserId());
    }

    @Override
    public void delete(String id) {
        transactionControl.required(() -> {
            userRepository.delete(id);
            log.debug("User {} successfully removed from repository", id);
            userProvider.delete(id);
            log.debug("User {} successfully deleted", id);
            return null;
        });
    }

    @Override
    public Optional<ComplexUser> findById(String id) {
        return transactionControl.supports(() -> userRepository.findById(id).map(entity -> {
            Optional<User> result = userProvider.findById(id);
            return result.map(ComplexUser::fromUser).orElse(null);
        }));
    }

    @Override
    public Optional<ComplexUser> findByUsername(String username) {
         return userProvider.findByUsername(username).map(ComplexUser::fromUser);
    }

    @Override
    public Optional<ComplexUser> findByEmail(String email) {
        return userProvider.findByEmail(email).map(ComplexUser::fromUser);
    }

    private UserEntity mapToUserEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setEnabled(user.isEnabled());
        entity.setCreatedDate(user.getCreatedTimestamp());
        return entity;
    }
}
