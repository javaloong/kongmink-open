package org.javaloong.kongmink.open.rest.auth.jwt;

import com.nimbusds.jwt.JWT;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.jwt.profile.JwtProfile;
import org.pac4j.oidc.profile.OidcProfileDefinition;
import org.pac4j.oidc.profile.keycloak.KeycloakOidcProfile;

import java.text.ParseException;

public class KeycloakJwtAuthenticator extends JwtAuthenticator {

    @Override
    protected void createJwtProfile(TokenCredentials credentials, JWT jwt, WebContext context) throws ParseException {
        super.createJwtProfile(credentials, jwt, context);

        JwtProfile profile = (JwtProfile) credentials.getUserProfile();
        if (profile != null) {
            OidcProfileDefinition<KeycloakOidcProfile> profileDefinition = new OidcProfileDefinition<>(x -> new KeycloakOidcProfile());
            KeycloakOidcProfile keycloakOidcProfile = profileDefinition.newProfile();
            AccessToken accessToken = new BearerAccessToken(jwt.getParsedString());
            keycloakOidcProfile.setAccessToken(accessToken);
            keycloakOidcProfile.addAttributes(profile.getAttributes());
            keycloakOidcProfile.addAuthenticationAttributes(profile.getAuthenticationAttributes());

            credentials.setUserProfile(keycloakOidcProfile);
        }
    }
}
