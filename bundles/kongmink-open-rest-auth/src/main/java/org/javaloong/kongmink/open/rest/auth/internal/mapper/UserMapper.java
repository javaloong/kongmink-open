package org.javaloong.kongmink.open.rest.auth.internal.mapper;

import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.common.user.UserConstants;
import org.javaloong.kongmink.open.common.user.UserProfile;
import org.pac4j.oidc.profile.OidcProfile;

public class UserMapper {

    public static User mapToUser(OidcProfile oidcProfile) {
        User user = new User();
        user.setId(oidcProfile.getId());
        user.setUsername(oidcProfile.getUsername());
        user.setEmail(oidcProfile.getEmail());
        user.setEmailVerified(oidcProfile.getEmailVerified());
        user.setEnabled(true);
        user.setProfile(createUserProfile(oidcProfile.getId(), oidcProfile));
        return user;
    }

    private static UserProfile createUserProfile(String userId, OidcProfile oidcProfile) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(userId);
        userProfile.setCompanyName(oidcProfile.getAttribute(UserConstants.COMPANY_NAME_KEY, String.class));
        userProfile.setCompanyProvince(oidcProfile.getAttribute(UserConstants.COMPANY_PROVINCE_KEY, String.class));
        userProfile.setCompanyCity(oidcProfile.getAttribute(UserConstants.COMPANY_CITY_KEY, String.class));
        userProfile.setCompanyAddress(oidcProfile.getAttribute(UserConstants.COMPANY_ADDRESS_KEY, String.class));
        userProfile.setContactName(oidcProfile.getAttribute(UserConstants.CONTACT_NAME_KEY, String.class));
        userProfile.setContactPhone(oidcProfile.getAttribute(UserConstants.CONTACT_PHONE_KEY, String.class));
        return userProfile;
    }
}
