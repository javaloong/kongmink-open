package org.javaloong.kongmink.open.service.impl;

import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.service.UserService;
import org.javaloong.kongmink.open.service.impl.user.UserManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = UserService.class)
public class UserServiceImpl implements UserService {

    @Reference
    UserManager userManager;

    @Override
    public User loadByUser(User user) {
        return userManager.loadByUser(user, true);
    }
}
