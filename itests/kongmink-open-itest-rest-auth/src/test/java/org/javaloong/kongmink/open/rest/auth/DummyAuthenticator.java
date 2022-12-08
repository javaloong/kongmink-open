package org.javaloong.kongmink.open.rest.auth;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.profile.CommonProfile;

public class DummyAuthenticator implements Authenticator<TokenCredentials> {

    @Override
    public void validate(TokenCredentials credentials, WebContext context) {
        String token = credentials.getToken();
        if ("user1_token".equals(token)) {
            credentials.setUserProfile(createUser1Profile());
        } else if ("user2_token".equals(token)) {
            credentials.setUserProfile(createUser2Profile());
        } else {
            credentials.setUserProfile(createUser3Profile());
        }
    }

    private CommonProfile createUser1Profile() {
        CommonProfile profile = new CommonProfile();
        profile.setId("1");
        profile.addAttribute("username", "user1");
        profile.addAttribute("email", "user1@example.com");
        profile.addAttribute("email_verified", true);
        profile.addAttribute("roles", new String[]{"manage-clients", "manage-users"});
        profile.addAttribute("permissions", new String[]{"client:*"});
        return profile;
    }

    private CommonProfile createUser2Profile() {
        CommonProfile profile = new CommonProfile();
        profile.setId("2");
        profile.addAttribute("username", "user2");
        profile.addAttribute("email", "user2@example.com");
        profile.addAttribute("email_verified", true);
        profile.addAttribute("roles", new String[]{"manage-clients"});
        profile.addAttribute("permissions", new String[]{"client:read"});
        return profile;
    }

    private CommonProfile createUser3Profile() {
        CommonProfile profile = new CommonProfile();
        profile.setId("3");
        profile.addAttribute("username", "user3");
        profile.addAttribute("email", "user3@example.com");
        profile.addAttribute("email_verified", true);
        return profile;
    }
}
