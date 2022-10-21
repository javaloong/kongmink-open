package org.javaloong.kongmink.open.service.core.internal;

import org.javaloong.kongmink.open.account.UserProvider;
import org.javaloong.kongmink.open.common.model.User;
import org.javaloong.kongmink.open.common.model.UserEmail;
import org.javaloong.kongmink.open.common.model.UserPassword;
import org.javaloong.kongmink.open.common.model.UserProfile;
import org.javaloong.kongmink.open.service.AccountService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javax.inject.Inject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceImplIT extends AbstractServiceTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext context = FrameworkUtil.getBundle(UserServiceImplIT.class).getBundleContext();
        context.registerService(UserProvider.class, Mockito.mock(UserProvider.class), null);
    }

    @Inject
    UserProvider userProvider;
    @Inject
    AccountService accountService;

    @Test
    public void testGetDetails() {
        User user = TestUtils.createUser("1", "user1");
        when(userProvider.getDetails()).thenReturn(user);
        accountService.getDetails(user);
        verify(userProvider).getDetails();
    }

    @Test
    public void testUpdateProfile() {
        doNothing().when(userProvider).updateProfile(any(UserProfile.class));
        UserProfile userProfile = new UserProfile();
        userProfile.setCompanyName("company1");
        accountService.updateProfile(userProfile);
        verify(userProvider).updateProfile(userProfile);
    }

    @Test
    public void testUpdateEmail() {
        doNothing().when(userProvider).updateEmail(any(UserEmail.class));
        UserEmail userEmail = new UserEmail();
        userEmail.setEmail("user1@test.com");
        accountService.updateEmail(userEmail);
        verify(userProvider).updateEmail(userEmail);
    }

    @Test
    public void testUpdatePassword() {
        doNothing().when(userProvider).updatePassword(any(UserPassword.class));
        UserPassword userPassword = new UserPassword();
        userPassword.setPassword("111111");
        userPassword.setPasswordNew("222222");
        userPassword.setPasswordConfirm("222222");
        accountService.updatePassword(userPassword);
        verify(userProvider).updatePassword(userPassword);
    }
}
