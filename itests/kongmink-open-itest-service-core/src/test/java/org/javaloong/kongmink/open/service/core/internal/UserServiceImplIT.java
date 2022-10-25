package org.javaloong.kongmink.open.service.core.internal;

import org.javaloong.kongmink.open.data.UserRepository;
import org.javaloong.kongmink.open.data.domain.UserEntity;
import org.javaloong.kongmink.open.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.service.transaction.control.TransactionControl;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class UserServiceImplIT extends AbstractServiceTestSupport {

    @Inject
    TransactionControl transactionControl;
    @Inject
    UserRepository userRepository;
    @Inject
    UserService userService;

    @Before
    public void setUp() {
        transactionControl.required(() -> userRepository.create(createUser("1", "user1")));
    }

    @After
    public void tearDown() {
        transactionControl.required(() -> {
            userRepository.delete("1");
            return null;
        });
    }

    @Test
    public void testCRUD() {
        Map<String, Object> userConfigMap = userService.getConfig("1");
        assertThat(userConfigMap).isEmpty();
        userService.setConfig("1", Collections.singletonMap("test", 1));
        userConfigMap = userService.getConfig("1");
        assertThat(userConfigMap).contains(entry("test", 1));
        userService.updateConfig("1", Collections.singletonMap("test2", 0));
        userConfigMap = userService.getConfig("1");
        assertThat(userConfigMap).contains(entry("test", 1), entry("test2", 0));
    }

    private UserEntity createUser(String id, String name) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setUsername(name);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}
