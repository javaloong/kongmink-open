package org.javaloong.kongmink.open.rest.auth.internal.mapper;

import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.common.user.UserConstants;
import org.javaloong.kongmink.open.common.user.UserProfile;
import org.pac4j.core.profile.CommonProfile;

public class UserMapper {

    public static final String EMAIL_VERIFIED = "email_verified";

    public static User mapToUser(CommonProfile profile) {
        User user = new User();
        user.setId(profile.getId());
        user.setUsername(profile.getUsername());
        user.setEmail(profile.getEmail());
        user.setEmailVerified((Boolean) profile.getAttribute(EMAIL_VERIFIED));
        user.setEnabled(true);
        user.setProfile(createUserProfile(profile.getId(), profile));
        return user;
    }

    private static UserProfile createUserProfile(String userId, CommonProfile profile) {
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
