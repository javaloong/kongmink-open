package org.javaloong.kongmink.open.rest.auth.jwt.profile;

import org.pac4j.jwt.profile.JwtProfile;

public class JwtUserProfile extends JwtProfile {

    public JwtUserProfile() {
    }

    @Override
    public String getUsername() {
        return (String) getAttribute(JwtUserProfileDefinition.PREFERRED_USERNAME);
    }

    public Boolean getEmailVerified() {
        return (Boolean) getAttribute(JwtUserProfileDefinition.EMAIL_VERIFIED);
    }

    public String getPhoneNumber() {
        return (String) getAttribute(JwtUserProfileDefinition.PHONE_NUMBER);
    }

    public Boolean getPhoneNumberVerified() {
        return (Boolean) getAttribute(JwtUserProfileDefinition.PHONE_NUMBER_VERIFIED);
    }

    public String getAccessToken() {
        return (String) getAttribute(JwtUserProfileDefinition.ACCESS_TOKEN);
    }

    public void setAccessToken(String accessToken) {
        addAttribute(JwtUserProfileDefinition.ACCESS_TOKEN, accessToken);
    }
}
