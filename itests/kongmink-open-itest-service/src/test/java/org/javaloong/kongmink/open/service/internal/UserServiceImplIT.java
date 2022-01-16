package org.javaloong.kongmink.open.service.internal;

import org.javaloong.kongmink.open.am.user.UserProvider;
import org.javaloong.kongmink.open.common.model.user.User;
import org.javaloong.kongmink.open.service.UserService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javax.inject.Inject;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class UserServiceImplIT extends AbstractServiceTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext context = FrameworkUtil.getBundle(UserServiceImplIT.class).getBundleContext();
        context.registerService(UserProvider.class, Mockito.mock(UserProvider.class), null);
    }

    @Inject
    UserProvider userProvider;
    @Inject
    UserService userService;

    @Test
    public void testCRUD() {
        User user = TestUtils.createUser("1", "user1");
        when(userProvider.create(any(User.class))).thenReturn(user);
        userService.create(user);
        user.setUsername("user11");
        userService.save(user);
        when(userProvider.findById(anyString())).thenReturn(Optional.of(user));
        assertThat(userService.findById("1")).isPresent();
        doNothing().when(userProvider).delete(anyString());
        userService.delete("1");
        assertThat(userService.findById(anyString())).isEmpty();
    }
}
