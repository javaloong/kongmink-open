package org.javaloong.kongmink.open.service.impl;

import org.javaloong.kongmink.open.account.UserProvider;
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

import java.time.LocalDateTime;

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
    public ComplexUser get(User user) {
        User result = userProvider.getUser();
        return ComplexUser.fromUser(result);
    }

    @Override
    public ComplexUser create(User user) {
        return transactionControl.required(() -> {
            UserEntity result = userRepository.create(mapToUserEntity(user));
            log.debug("User {} successfully added to repository", result.getId());
            return mapToComplexUser(result, user);
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

    private UserEntity mapToUserEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setEnabled(user.isEnabled());
        entity.setCreatedDate(LocalDateTime.now());
        return entity;
    }

    private ComplexUser mapToComplexUser(UserEntity userEntity, User user) {
        ComplexUser complexUser = ComplexUser.fromUser(user);
        complexUser.setCreatedTimestamp(userEntity.getCreatedDate());
        return complexUser;
    }
}
