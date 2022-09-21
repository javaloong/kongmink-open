package org.javaloong.kongmink.open.core.config.internal;

import org.javaloong.kongmink.open.core.config.ConfigProperties;
import org.javaloong.kongmink.open.data.UserRepository;
import org.javaloong.kongmink.open.data.domain.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.javaloong.kongmink.open.core.config.ConfigConstants.USER_APPLICATIONS_LIMIT;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AppConfigFactoryTest {

    private UserRepository userRepository;

    private AppConfigFactory appConfigFactory;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        appConfigFactory = new AppConfigFactory();
        appConfigFactory.userRepository = userRepository;
        appConfigFactory.activate(Collections.singletonMap(USER_APPLICATIONS_LIMIT, 1));
    }

    @Test
    public void testGetConfig() {
        ConfigProperties configProperties = appConfigFactory.getConfig();
        assertThat(configProperties).contains(entry(USER_APPLICATIONS_LIMIT, 1));
    }

    @Test
    public void testGetConfigByUser() {
        UserEntity userEntity = createUserEntity();
        when(userRepository.findById(anyString())).thenReturn(Optional.of(userEntity));
        ConfigProperties configProperties = appConfigFactory.getConfig("1");
        assertThat(configProperties).contains(entry(USER_APPLICATIONS_LIMIT, 10));
    }

    private UserEntity createUserEntity() {
        UserEntity entity = new UserEntity();
        entity.setConfigData(Collections.singletonMap(USER_APPLICATIONS_LIMIT, 10));
        return entity;
    }
}
