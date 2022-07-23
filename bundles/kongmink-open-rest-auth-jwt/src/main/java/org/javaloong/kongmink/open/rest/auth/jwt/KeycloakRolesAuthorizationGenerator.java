package org.javaloong.kongmink.open.rest.auth.jwt;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.javaloong.kongmink.open.rest.auth.jwt.profile.JwtUserProfile;
import org.pac4j.core.authorization.generator.AuthorizationGenerator;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.profile.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class KeycloakRolesAuthorizationGenerator implements AuthorizationGenerator {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakRolesAuthorizationGenerator.class);

    private String clientId;

    public KeycloakRolesAuthorizationGenerator() {}

    public KeycloakRolesAuthorizationGenerator(final String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Optional<UserProfile> generate(final WebContext context, final UserProfile profile) {

        if (profile instanceof JwtUserProfile) {
            try {
                final JWT jwt = SignedJWT.parse(((JwtUserProfile) profile).getAccessToken());
                final JWTClaimsSet jwtClaimsSet = jwt.getJWTClaimsSet();

                final Map<String, Object> realmRolesJsonObject = jwtClaimsSet.getJSONObjectClaim("realm_access");
                if (realmRolesJsonObject != null) {
                    final JSONArray realmRolesJsonArray = (JSONArray) realmRolesJsonObject.get("roles");
                    if (realmRolesJsonArray != null) {
                        realmRolesJsonArray.forEach(role -> profile.addRole((String) role));
                    }
                }

                if (clientId != null) {
                    final Map<String, Object> resourceAccess = jwtClaimsSet.getJSONObjectClaim("resource_access");
                    if (resourceAccess != null) {
                        final JSONObject clientRolesJsonObject = (JSONObject) resourceAccess.get(clientId);
                        if (clientRolesJsonObject != null) {
                            final JSONArray clientRolesJsonArray = (JSONArray) clientRolesJsonObject.get("roles");
                            if (clientRolesJsonArray != null) {
                                clientRolesJsonArray.forEach(role -> profile.addRole((String) role));
                            }
                        }
                    }
                }
            } catch (final Exception e) {
                logger.warn("Cannot parse Keycloak roles", e);
            }
        }

        return Optional.of(profile);
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }
}
