package org.javaloong.kongmink.open.service.impl;

import org.javaloong.kongmink.open.account.UserProvider;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.common.user.UserEmail;
import org.javaloong.kongmink.open.common.user.UserPassword;
import org.javaloong.kongmink.open.common.user.UserProfile;
import org.javaloong.kongmink.open.service.AccountService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = AccountService.class)
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Reference
    UserProvider userProvider;

    @Override
    public User getDetails(User user) {
        User result = userProvider.getDetails();
        if (result.getCreatedAt() == null) {
            result.setCreatedAt(user.getCreatedAt());
        }
        return result;
    }

    @Override
    public void updateProfile(UserProfile userProfile) {
        userProvider.updateProfile(userProfile);
        logger.debug("User {} profile successfully updated", userProfile.getUserId());
    }

    @Override
    public void updatePassword(UserPassword userPassword) {
        userProvider.updatePassword(userPassword);
        logger.debug("User {} password successfully updated", userPassword.getUserId());
    }

    @Override
    public void updateEmail(UserEmail userEmail) {
        userProvider.updateEmail(userEmail);
        logger.debug("User {} email successfully updated", userEmail.getUserId());
    }
}
