package org.javaloong.kongmink.open.core.config.internal;

import org.javaloong.kongmink.open.core.config.ConfigFactory;
import org.javaloong.kongmink.open.core.config.ConfigProperties;
import org.javaloong.kongmink.open.data.UserRepository;
import org.javaloong.kongmink.open.data.domain.UserEntity;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.javaloong.kongmink.open.core.config.ConfigConstants.USER_APPLICATIONS_LIMIT;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AppConfigFactoryIT extends AbstractTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext context = FrameworkUtil.getBundle(AppConfigFactoryIT.class).getBundleContext();
        context.registerService(UserRepository.class, Mockito.mock(UserRepository.class), null);
    }

    @Inject
    UserRepository userRepository;
    @Inject
    ConfigFactory configFactory;

    @Test
    public void testGetConfig() {
        ConfigProperties configProperties = configFactory.getConfig();
        assertThat(configProperties).contains(entry(USER_APPLICATIONS_LIMIT, 1));
    }

    @Test
    public void testGetConfigByUser() {
        UserEntity userEntity = createUserEntity();
        when(userRepository.findById(anyString())).thenReturn(Optional.of(userEntity));
        ConfigProperties configProperties = configFactory.getConfig("1");
        assertThat(configProperties).contains(entry(USER_APPLICATIONS_LIMIT, 10));
    }

    private UserEntity createUserEntity() {
        UserEntity entity = new UserEntity();
        entity.setConfigData(Collections.singletonMap(USER_APPLICATIONS_LIMIT, 10));
        return entity;
    }
}
