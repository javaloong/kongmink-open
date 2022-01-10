package org.javaloong.kongmink.open.am.keycloak.internal.user;

import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import org.javaloong.kongmink.open.am.embedded.keycloak.KeycloakServerProperties;
import org.javaloong.kongmink.open.am.keycloak.internal.EmbeddedKeycloakTestSupport;
import org.javaloong.kongmink.open.am.user.UserException;
import org.javaloong.kongmink.open.am.user.UserProvider;
import org.javaloong.kongmink.open.common.model.user.User;
import org.javaloong.kongmink.open.common.model.user.UserEmail;
import org.javaloong.kongmink.open.common.model.user.UserPassword;
import org.javaloong.kongmink.open.common.model.user.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.osgi.util.converter.Converters;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KeycloakUserProviderIT extends EmbeddedKeycloakTestSupport {

    @TestServerConfiguration
    static EmbeddedJettyConfiguration jettyConfiguration() {
        return EmbeddedJettyConfiguration.builder()
                .withOverrideDescriptor("src/test/resources/web.xml")
                .withProperty(KeycloakServerProperties.REALM_CONFIGURATION_PATH, "open-admin-realm.json")
                .build();
    }

    private UserProvider userProvider;

    @BeforeEach
    public void setUp(EmbeddedJetty jetty) {
        KeycloakAdminClient adminClient = new KeycloakAdminClient(createAdminClientConfig(jetty));
        userProvider = new KeycloakUserProvider(adminClient);
    }

    @Test
    public void createUser_UsernameExists_ThrowException() {
        UserException exception = assertThrows(UserException.class, () -> {
            User user = new User();
            user.setUsername("user1");
            userProvider.create(user);
        });
        assertThat(exception.getErrorCode()).isEqualTo(UserException.USERNAME_EXISTS);
    }

    @Test
    public void createUser_emailExists_ThrowException() {
        UserException exception = assertThrows(UserException.class, () -> {
            User user = new User();
            user.setUsername("user3");
            user.setEmail("user1@example.com");
            userProvider.create(user);
        });
        assertThat(exception.getErrorCode()).isEqualTo(UserException.EMAIL_EXISTS);
    }

    @Test
    public void createUser() {
        User user = new User();
        user.setUsername("user3");
        User saved = userProvider.create(user);
        assertThat(saved.getId()).isNotEmpty();
    }

    @Test
    public void updateUserProfile() {
        String userId = "30f3efcc-c3ff-43f8-9b9a-ec3d8d9f6bc9";
        UserProfile userProfile = new UserProfile(userId);
        userProfile.setCompanyName("company1");
        userProvider.updateProfile(userProfile);
        Optional<User> result = userProvider.findById(userId);
        result.ifPresent(user -> {
            assertThat(user.getProfile().getCompanyName()).isEqualTo("company1");
        });
    }

    @Test
    public void updateUserPassword() {
        String userId = "30f3efcc-c3ff-43f8-9b9a-ec3d8d9f6bc9";
        UserPassword userPassword = new UserPassword();
        userPassword.setUserId(userId);
        userPassword.setPassword("11111111");
        userPassword.setPasswordNew("22222222");
        userPassword.setPasswordConfirm("22222222");
        userProvider.updatePassword(userPassword);
    }

    @Test
    public void updateUserEmail_emailExists_ThrowException() {
        UserException exception = assertThrows(UserException.class, () -> {
            String userId = "45e4c8f8-98bc-43c0-93e8-1a2a3aec6bf7";
            UserEmail userEmail = new UserEmail(userId, "user1@example.com");
            userProvider.updateEmail(userEmail);
        });
        assertThat(exception.getErrorCode()).isEqualTo(UserException.EMAIL_EXISTS);
    }

    @Test
    public void updateUserEmail() {
        String userId = "45e4c8f8-98bc-43c0-93e8-1a2a3aec6bf7";
        String email = "user2@example.com";
        UserEmail userEmail = new UserEmail(userId, email);
        userProvider.updateEmail(userEmail);
        Optional<User> result = userProvider.findById(userId);
        result.ifPresent(user -> {
            assertThat(user.getEmail()).isEqualTo(email);
        });
    }

    @Test
    public void findUserById() {
        String userId = "30f3efcc-c3ff-43f8-9b9a-ec3d8d9f6bc9";
        Optional<User> result = userProvider.findById(userId);
        assertThat(result).hasValueSatisfying(user -> {
            assertThat(user.getUsername()).isEqualTo("user1");
        });
    }

    @Test
    public void findUserByUsername() {
        String username = "user1";
        Optional<User> result = userProvider.findByUsername(username);
        assertThat(result).hasValueSatisfying(user -> {
            assertThat(user.getId()).isNotEmpty();
            assertThat(user.getUsername()).isEqualTo("user1");
        });
    }

    @Test
    public void findUserByEmail() {
        String email = "user1@example.com";
        Optional<User> result = userProvider.findByEmail(email);
        assertThat(result).hasValueSatisfying(user -> {
            assertThat(user.getId()).isNotEmpty();
            assertThat(user.getUsername()).isEqualTo("user1");
        });
    }

    private KeycloakAdminClientConfiguration createAdminClientConfig(EmbeddedJetty jetty) {
        Map<String, Object> props = new HashMap<>();
        props.put("serverUrl", jetty.getUrl() + "auth");
        return Converters.standardConverter().convert(props).to(KeycloakAdminClientConfiguration.class);
    }
}
