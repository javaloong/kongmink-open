package org.javaloong.kongmink.open.service.core.internal.user;

import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.data.UserRepository;
import org.javaloong.kongmink.open.data.domain.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.osgi.service.transaction.control.TransactionControl;
import org.osgi.util.converter.Converters;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserManagerTest {

    private UserRepository userRepository;
    private TransactionControl transactionControl;

    private UserManager userManager;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        transactionControl = mock(TransactionControl.class);
        userManager = new UserManager(createConfig());
        userManager.userRepository = userRepository;
        userManager.transactionControl = transactionControl;
    }

    @Test
    public void testLoadByUser() {
        when(transactionControl.required(ArgumentMatchers.any())).thenAnswer(invocation -> {
            Callable<UserEntity> work = invocation.getArgument(0);
            return work.call();
        });
        UserEntity entity = createUserEntity("1", "user1");
        when(userRepository.create(any(UserEntity.class))).thenReturn(entity);
        User user = createUser("1", "user1");
        userManager.loadByUser(user, false);
        ArgumentCaptor<UserEntity> createEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).create(createEntityCaptor.capture());
        assertThat(createEntityCaptor.getValue())
                .returns("1", UserEntity::getId)
                .returns("user1", UserEntity::getUsername);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(entity));
        when(userRepository.update(any(UserEntity.class))).thenReturn(entity);
        User user1 = createUser("1", "user11");
        userManager.loadByUser(user1, false);
        ArgumentCaptor<UserEntity> updateEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).update(updateEntityCaptor.capture());
        assertThat(updateEntityCaptor.getValue())
                .returns("1", UserEntity::getId)
                .returns("user11", UserEntity::getUsername);
        userManager.loadByUser(user1, true);
        verify(userRepository, times(3)).findById("1");
        userManager.loadByUser(user1, true);
        verify(userRepository, times(3)).findById("1");
        userManager.loadByUser(user1, true);
        verify(userRepository, times(3)).findById("1");
    }

    private User createUser(String id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        return user;
    }

    private UserEntity createUserEntity(String id, String username) {
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setUsername(username);
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

    private UserManager.Config createConfig() {
        Map<String, Object> props = new HashMap<>();
        return Converters.standardConverter().convert(props).to(UserManager.Config.class);
    }
}
