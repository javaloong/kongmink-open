package org.javaloong.kongmink.open.rest.auth.jwt.internal.mapper;

import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.common.user.UserConstants;
import org.javaloong.kongmink.open.common.user.UserProfile;
import org.javaloong.kongmink.open.rest.auth.jwt.profile.JwtUserProfile;

public class UserMapper {

    public static User mapToUser(JwtUserProfile profile) {
        User user = new User();
        user.setId(profile.getId());
        user.setUsername(profile.getUsername());
        user.setEmail(profile.getEmail());
        user.setEmailVerified(profile.getEmailVerified());
        user.setEnabled(true);
        user.setProfile(createUserProfile(profile.getId(), profile));
        return user;
    }

    private static UserProfile createUserProfile(String userId, JwtUserProfile profile) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(userId);
        userProfile.setCompanyName(profile.getAttribute(UserConstants.COMPANY_NAME_KEY, String.class));
        userProfile.setCompanyProvince(profile.getAttribute(UserConstants.COMPANY_PROVINCE_KEY, String.class));
        userProfile.setCompanyCity(profile.getAttribute(UserConstants.COMPANY_CITY_KEY, String.class));
        userProfile.setCompanyAddress(profile.getAttribute(UserConstants.COMPANY_ADDRESS_KEY, String.class));
        userProfile.setContactName(profile.getAttribute(UserConstants.CONTACT_NAME_KEY, String.class));
        userProfile.setContactPhone(profile.getAttribute(UserConstants.CONTACT_PHONE_KEY, String.class));
        return userProfile;
    }
}
