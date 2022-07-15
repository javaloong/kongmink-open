package org.javaloong.kongmink.open.data.jpa.internal;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.CompareOperation;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.javaloong.kongmink.open.data.UserRepository;
import org.javaloong.kongmink.open.data.domain.UserEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DBUnit(mergeDataSets = true)
@DataSet(value = {"userData.xml"}, cleanAfter = true, disableConstraints = true)
public class JpaUserRepositoryIT extends RepositoryTestSupport {

    private JpaUserRepository userRepository;

    @Test
    @DataSet(transactional = true)
    @ExpectedDataSet(value = "createUserDataExpected.xml", compareOperation = CompareOperation.CONTAINS)
    public void createUser() {
        UserEntity user = new UserEntity();
        user.setId("4");
        user.setUsername("user4");
        user.setCreatedAt(LocalDateTime.parse("2021-12-30T10:25:12"));
        UserEntity result = getUserRepository().create(user);
        assertThat(result).isNotNull();
    }

    @Test
    @DataSet(transactional = true)
    @ExpectedDataSet(value = "updateUserDataExpected.xml", compareOperation = CompareOperation.CONTAINS)
    public void updateUser() {
        getUserRepository().findById("2").ifPresent(user -> {
            user.setUsername("user22");
            user.setUpdatedAt(LocalDateTime.parse("2021-12-13T13:13:13"));
            UserEntity result = getUserRepository().update(user);
            assertThat(result).returns("user22", UserEntity::getUsername);
        });
    }

    @Test
    @DataSet(transactional = true)
    public void deleteUser() {
        getUserRepository().delete("3");
        assertThat(getUserRepository().findById("3")).isEmpty();
    }

    @Test
    public void findUserById() {
        Optional<UserEntity> result = getUserRepository().findById("1");
        assertThat(result).isPresent().hasValueSatisfying(user -> {
            assertThat(user.getUsername()).isEqualTo("user1");
        });
    }

    @Test
    public void findUserByUsername() {
        Optional<UserEntity> result = getUserRepository().findByUsername("user2");
        assertThat(result).isPresent().hasValueSatisfying(user -> {
            assertThat(user.getId()).isEqualTo("2");
        });
    }

    @Test
    public void findAllUsers() {
        List<UserEntity> users = getUserRepository().findAll();
        assertThat(users).isNotEmpty().hasSize(3)
                .extracting(UserEntity::getUsername)
                .containsExactly("user3", "user2", "user1");
    }

    private synchronized UserRepository getUserRepository() {
        if (userRepository == null) {
            userRepository = new JpaUserRepository();
            userRepository.setEntityManager(em());
        }
        return userRepository;
    }
}
