package org.javaloong.kongmink.open.service.core.internal;

import org.javaloong.kongmink.open.common.model.User;
import org.javaloong.kongmink.open.service.UserDetailsService;
import org.junit.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDetailsServiceImplIT extends AbstractServiceTestSupport {

    @Inject
    UserDetailsService userDetailsService;

    @Test
    public void testLoadByUser() {
        User user = TestUtils.createUser("1", "user1");
        User result1 = userDetailsService.loadByUser(user);
        assertThat(result1).returns("user1", User::getUsername);
        User result2 = userDetailsService.loadByUser(user);
        assertThat(result2).returns("user1", User::getUsername);
        User result3 = userDetailsService.loadByUser(user);
        assertThat(result3).returns("user1", User::getUsername);
    }
}
