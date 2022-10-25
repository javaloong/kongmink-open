package org.javaloong.kongmink.open.service.core.internal;

import org.javaloong.kongmink.open.common.model.User;
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

public class UserDetailsServiceImplTest {

    private UserRepository userRepository;
    private TransactionControl transactionControl;

    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        transactionControl = mock(TransactionControl.class);
        userDetailsService = new UserDetailsServiceImpl(createConfig());
        userDetailsService.userRepository = userRepository;
        userDetailsService.transactionControl = transactionControl;
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
        userDetailsService.loadByUser(user, false);
        ArgumentCaptor<UserEntity> createEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).create(createEntityCaptor.capture());
        assertThat(createEntityCaptor.getValue())
                .returns("1", UserEntity::getId)
                .returns("user1", UserEntity::getUsername);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(entity));
        when(userRepository.update(any(UserEntity.class))).thenReturn(entity);
        User user1 = createUser("1", "user11");
        userDetailsService.loadByUser(user1, false);
        ArgumentCaptor<UserEntity> updateEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).update(updateEntityCaptor.capture());
        assertThat(updateEntityCaptor.getValue())
                .returns("1", UserEntity::getId)
                .returns("user11", UserEntity::getUsername);
        userDetailsService.loadByUser(user1, true);
        verify(userRepository, times(3)).findById("1");
        userDetailsService.loadByUser(user1, true);
        verify(userRepository, times(3)).findById("1");
        userDetailsService.loadByUser(user1, true);
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

    private UserDetailsServiceImpl.Config createConfig() {
        Map<String, Object> props = new HashMap<>();
        return Converters.standardConverter().convert(props).to(UserDetailsServiceImpl.Config.class);
    }
}
