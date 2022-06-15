package org.javaloong.kongmink.open.account.keycloak.internal;

import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import org.apache.cxf.rs.security.oauth2.client.Consumer;
import org.apache.cxf.rs.security.oauth2.client.OAuthClientUtils;
import org.apache.cxf.rs.security.oauth2.common.ClientAccessToken;
import org.apache.cxf.rs.security.oauth2.grants.owner.ResourceOwnerGrant;
import org.javaloong.kongmink.open.account.UserProvider;
import org.javaloong.kongmink.open.account.exception.UserException;
import org.javaloong.kongmink.open.account.keycloak.internal.exception.NotImplementedException;
import org.javaloong.kongmink.open.am.embedded.keycloak.KeycloakServerProperties;
import org.javaloong.kongmink.open.common.auth.SecurityContext;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.common.user.UserEmail;
import org.javaloong.kongmink.open.common.user.UserPassword;
import org.javaloong.kongmink.open.common.user.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.osgi.util.converter.Converters;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KeycloakUserProviderIT extends EmbeddedKeycloakTestSupport {

    public static final String TOKEN_URI_FORMAT = "%s/realms/%s/protocol/openid-connect/token";

    @TestServerConfiguration
    static EmbeddedJettyConfiguration jettyConfiguration() {
        return EmbeddedJettyConfiguration.builder()
                .withOverrideDescriptor("src/test/resources/web.xml")
                .withProperty(KeycloakServerProperties.REALM_CONFIGURATION_PATH, "kongmink-open-realm.json")
                .build();
    }

    private UserProvider userProvider;

    @BeforeEach
    public void setUp(EmbeddedJetty jetty) {
        KeycloakAccountClientConfiguration config = createAccountClientConfig(jetty);
        String token = getToken(config);
        KeycloakAccountClient accountClient = new KeycloakAccountClient(config);
        accountClient.securityContextProvider = () -> {
            SecurityContext securityContext = new SecurityContext();
            securityContext.setToken(token);
            return securityContext;
        };
        userProvider = new KeycloakUserProvider(accountClient);
    }

    @Test
    public void getUser() {
        User user = userProvider.getUser();
        assertThat(user)
                .returns("30f3efcc-c3ff-43f8-9b9a-ec3d8d9f6bc9", User::getId)
                .returns("user1", User::getUsername);
    }

    @Test
    public void updateUserProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setCompanyName("company1");
        userProvider.updateProfile(userProfile);
        User user = userProvider.getUser();
        assertThat(user.getProfile().getCompanyName()).isEqualTo("company1");
    }

    @Test
    public void updateUserPassword() {
        NotImplementedException exception = assertThrows(NotImplementedException.class, () -> {
            String userId = "30f3efcc-c3ff-43f8-9b9a-ec3d8d9f6bc9";
            UserPassword userPassword = new UserPassword();
            userPassword.setUserId(userId);
            userPassword.setPassword("11111111");
            userPassword.setPasswordNew("22222222");
            userPassword.setPasswordConfirm("22222222");
            userProvider.updatePassword(userPassword);
        });
        assertThat(exception.getMessage()).isEqualTo("HTTP 501 Not Implemented");
    }

    @Test
    public void updateUserEmail_emailExists_ThrowException() {
        UserException exception = assertThrows(UserException.class, () -> {
            String userId = "30f3efcc-c3ff-43f8-9b9a-ec3d8d9f6bc9";
            UserEmail userEmail = new UserEmail(userId, "user2@example.com");
            userProvider.updateEmail(userEmail);
        });
        assertThat(exception.getErrorCode()).isEqualTo(UserException.EMAIL_EXISTS);
    }

    @Test
    public void updateUserEmail() {
        String userId = "30f3efcc-c3ff-43f8-9b9a-ec3d8d9f6bc9";
        String email = "user11@example.com";
        UserEmail userEmail = new UserEmail(userId, email);
        userProvider.updateEmail(userEmail);
        User user = userProvider.getUser();
        assertThat(user)
                .returns(email, User::getEmail)
                .returns(false, User::isEmailVerified);
    }

    private String getToken(KeycloakAccountClientConfiguration config) {
        String tokenUri = String.format(TOKEN_URI_FORMAT, config.serverUrl(), config.realm());
        Consumer consumer = new Consumer("keycloak-account");
        ResourceOwnerGrant grant = new ResourceOwnerGrant("user1", "11111111");
        ClientAccessToken clientAccessToken = OAuthClientUtils.getAccessToken(tokenUri, consumer, grant, false);
        return clientAccessToken.getTokenKey();
    }

    private KeycloakAccountClientConfiguration createAccountClientConfig(EmbeddedJetty jetty) {
        Map<String, Object> props = new HashMap<>();
        props.put("serverUrl", jetty.getUrl() + "auth");
        return Converters.standardConverter().convert(props).to(KeycloakAccountClientConfiguration.class);
    }
}
