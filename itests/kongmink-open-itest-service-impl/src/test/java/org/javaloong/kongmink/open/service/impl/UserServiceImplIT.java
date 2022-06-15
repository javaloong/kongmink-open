package org.javaloong.kongmink.open.service.impl;

import org.javaloong.kongmink.open.account.UserProvider;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.common.user.UserEmail;
import org.javaloong.kongmink.open.common.user.UserPassword;
import org.javaloong.kongmink.open.common.user.UserProfile;
import org.javaloong.kongmink.open.service.UserService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javax.inject.Inject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        when(userProvider.getUser()).thenReturn(user);
        userService.get(user);
        verify(userProvider).getUser();
        userService.create(user);
        user.setUsername("user11");
        userService.save(user);
        doNothing().when(userProvider).updateProfile(any(UserProfile.class));
        UserProfile userProfile = new UserProfile();
        userProfile.setCompanyName("company1");
        userService.updateProfile(userProfile);
        verify(userProvider).updateProfile(userProfile);
        doNothing().when(userProvider).updateEmail(any(UserEmail.class));
        UserEmail userEmail = new UserEmail();
        userEmail.setEmail("user1@test.com");
        userService.updateEmail(userEmail);
        verify(userProvider).updateEmail(userEmail);
        doNothing().when(userProvider).updatePassword(any(UserPassword.class));
        UserPassword userPassword = new UserPassword();
        userPassword.setPassword("111111");
        userPassword.setPasswordNew("222222");
        userPassword.setPasswordConfirm("222222");
        userService.updatePassword(userPassword);
        verify(userProvider).updatePassword(userPassword);
    }
}
