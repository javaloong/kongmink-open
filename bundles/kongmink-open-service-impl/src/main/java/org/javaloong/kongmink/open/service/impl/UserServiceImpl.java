package org.javaloong.kongmink.open.service.impl;

import org.javaloong.kongmink.open.account.UserProvider;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.common.user.UserEmail;
import org.javaloong.kongmink.open.common.user.UserPassword;
import org.javaloong.kongmink.open.common.user.UserProfile;
import org.javaloong.kongmink.open.service.UserService;
import org.javaloong.kongmink.open.service.impl.user.UserManager;
import org.javaloong.kongmink.open.service.model.OPUser;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = UserService.class)
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Reference
    UserProvider userProvider;
    @Reference
    UserManager userManager;

    @Override
    public OPUser get(User user) {
        User result = userProvider.getUser();
        return OPUser.fromUser(result);
    }

    @Override
    public OPUser loadByUser(User user) {
        return userManager.loadByUser(user, true);
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
