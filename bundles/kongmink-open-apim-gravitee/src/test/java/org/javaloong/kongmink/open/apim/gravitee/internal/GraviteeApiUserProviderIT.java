package org.javaloong.kongmink.open.apim.gravitee.internal;

import org.javaloong.kongmink.open.apim.ApiUserProvider;
import org.javaloong.kongmink.open.common.model.ApiUser;
import org.javaloong.kongmink.open.common.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("gravitee")
public class GraviteeApiUserProviderIT extends GraviteePortalClientTestSupport {

    private ApiUserProvider apiUserProvider;

    @BeforeEach
    public void setUp() {
        apiUserProvider = new GraviteeApiUserProvider(createPortalClient());
    }

    @Test
    public void connectUser() {
        ApiUser newUser = apiUserProvider.connectUser(createUser());
        assertThat(newUser.getId()).isNotEmpty();
    }

    private User createUser() {
        User user = new User();
        user.setId("30f3efcc-c3ff-43f8-9b9a-ec3d8d9f6bc9");
        user.setEmail("user1@example.com");
        return user;
    }
}
