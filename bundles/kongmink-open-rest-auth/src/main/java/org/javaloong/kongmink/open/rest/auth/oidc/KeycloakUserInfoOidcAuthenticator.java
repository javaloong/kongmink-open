package org.javaloong.kongmink.open.rest.auth.oidc;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.oidc.credentials.authenticator.UserInfoOidcAuthenticator;
import org.pac4j.oidc.profile.OidcProfile;
import org.pac4j.oidc.profile.OidcProfileDefinition;
import org.pac4j.oidc.profile.keycloak.KeycloakOidcProfile;

public class KeycloakUserInfoOidcAuthenticator extends UserInfoOidcAuthenticator {

    @Override
    public void validate(TokenCredentials credentials, WebContext context) {
        super.validate(credentials, context);

        OidcProfile profile = (OidcProfile) credentials.getUserProfile();

        OidcProfileDefinition<KeycloakOidcProfile> profileDefinition = new OidcProfileDefinition<>(x -> new KeycloakOidcProfile());
        KeycloakOidcProfile keycloakOidcProfile = profileDefinition.newProfile();
        keycloakOidcProfile.setAccessToken(profile.getAccessToken());
        keycloakOidcProfile.addAttributes(profile.getAttributes());
        keycloakOidcProfile.addAuthenticationAttributes(profile.getAuthenticationAttributes());

        credentials.setUserProfile(keycloakOidcProfile);
    }
}
