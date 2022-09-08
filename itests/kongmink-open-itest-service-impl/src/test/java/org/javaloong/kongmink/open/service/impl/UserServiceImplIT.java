package org.javaloong.kongmink.open.service.impl;

import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.service.UserService;
import org.junit.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

public class UserServiceImplIT extends AbstractServiceTestSupport {

    @Inject
    UserService userService;

    @Test
    public void testLoadByUser() {
        User user = TestUtils.createUser("1", "user1");
        User result1 = userService.loadByUser(user);
        assertThat(result1).returns("user1", User::getUsername);
        User result2 = userService.loadByUser(user);
        assertThat(result2).returns("user1", User::getUsername);
        User result3 = userService.loadByUser(user);
        assertThat(result3).returns("user1", User::getUsername);
    }
}
