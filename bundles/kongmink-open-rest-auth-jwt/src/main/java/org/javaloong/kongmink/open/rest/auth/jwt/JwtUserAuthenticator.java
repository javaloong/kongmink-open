package org.javaloong.kongmink.open.rest.auth.jwt;

import com.nimbusds.jwt.JWT;
import org.javaloong.kongmink.open.rest.auth.jwt.profile.JwtUserProfile;
import org.javaloong.kongmink.open.rest.auth.jwt.profile.JwtUserProfileDefinition;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;

import java.text.ParseException;

public class JwtUserAuthenticator extends JwtAuthenticator {

    public JwtUserAuthenticator() {
        setProfileDefinition(new JwtUserProfileDefinition());
    }

    @Override
    protected void createJwtProfile(TokenCredentials credentials, JWT jwt, WebContext context) throws ParseException {
        super.createJwtProfile(credentials, jwt, context);

        JwtUserProfile profile = (JwtUserProfile) credentials.getUserProfile();
        if (profile != null) {
            profile.setAccessToken(jwt.getParsedString());
        }
    }
}
