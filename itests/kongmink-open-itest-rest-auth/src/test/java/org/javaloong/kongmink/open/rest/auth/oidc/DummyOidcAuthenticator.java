package org.javaloong.kongmink.open.rest.auth.oidc;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.oidc.profile.OidcProfile;

public class DummyOidcAuthenticator implements Authenticator<TokenCredentials> {

    @Override
    public void validate(TokenCredentials credentials, WebContext context) {
        String token = credentials.getToken();
        if("user1_token".equals(token)) {
            credentials.setUserProfile(createUser1Profile());
        }
        else if("user2_token".equals(token)) {
            credentials.setUserProfile(createUser2Profile());
        }
        else {
            credentials.setUserProfile(createUser3Profile());
        }
    }

    private OidcProfile createUser1Profile() {
        OidcProfile profile = new OidcProfile();
        profile.setId("1");
        profile.addAttribute("preferred_username", "user1");
        profile.addAttribute("email", "user1@example.com");
        profile.addAttribute("email_verified", true);
        profile.addAttribute("roles", new String[]{"manage-client"});
        profile.addAttribute("permissions", new String[]{"client:*"});
        return profile;
    }

    private OidcProfile createUser2Profile() {
        OidcProfile profile = new OidcProfile();
        profile.setId("2");
        profile.addAttribute("preferred_username", "user2");
        profile.addAttribute("email", "user2@example.com");
        profile.addAttribute("email_verified", true);
        profile.addAttribute("roles", new String[]{"manage-client"});
        profile.addAttribute("permissions", new String[]{"client:read"});
        return profile;
    }

    private OidcProfile createUser3Profile() {
        OidcProfile profile = new OidcProfile();
        profile.setId("3");
        profile.addAttribute("preferred_username", "user3");
        profile.addAttribute("email", "user3@example.com");
        profile.addAttribute("email_verified", true);
        return profile;
    }
}
