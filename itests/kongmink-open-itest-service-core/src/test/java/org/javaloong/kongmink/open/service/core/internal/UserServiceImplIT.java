package org.javaloong.kongmink.open.service.core.internal;

import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.model.user.query.UserQuery;
import org.javaloong.kongmink.open.data.repository.UserRepository;
import org.javaloong.kongmink.open.data.domain.UserEntity;
import org.javaloong.kongmink.open.service.UserService;
import org.javaloong.kongmink.open.service.dto.UserDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.service.transaction.control.TransactionControl;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

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
        Optional<UserDTO> result = userService.findById("1");
        assertThat(result).isPresent().hasValueSatisfying(user ->
                assertThat(user.getUsername()).isEqualTo("user1"));
        UserQuery query = new UserQuery();
        Page<UserDTO> page = userService.findAll(query, 1, 10);
        assertThat(page).isNotNull()
                .returns(1L, Page::getTotalCount)
                .extracting(Page::getData)
                .satisfies(data -> {
                    assertThat(data).hasSize(1)
                            .extracting(UserDTO::getUsername)
                            .containsExactly("user1");
                });
    }

    private UserEntity createUser(String id, String name) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setUsername(name);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}
