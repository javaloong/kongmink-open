package org.javaloong.kongmink.open.data.jpa.internal.repository;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.CompareOperation;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.model.user.query.UserQuery;
import org.javaloong.kongmink.open.data.repository.UserRepository;
import org.javaloong.kongmink.open.data.domain.UserEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

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
        UserEntity result = getUserRepository().create(user);
        assertThat(result).isNotNull();
    }

    @Test
    @DataSet(transactional = true)
    @ExpectedDataSet(value = "updateUserDataExpected.xml", compareOperation = CompareOperation.CONTAINS)
    public void updateUser() {
        getUserRepository().findById("2").ifPresent(user -> {
            user.setUsername("user22");
            UserEntity result = getUserRepository().update(user);
            assertThat(result).returns("user22", UserEntity::getUsername);
        });
    }

    @Test
    @DataSet(transactional = true)
    @ExpectedDataSet(value = "updateUserConfigDataExpected.xml", compareOperation = CompareOperation.CONTAINS)
    public void updateUserConfigData() {
        getUserRepository().findById("1").ifPresent(user -> {
            user.setConfigData(Collections.singletonMap("user.application.count", 10));
            UserEntity result = getUserRepository().update(user);
            assertThat(result.getConfigData()).contains(entry("user.application.count", 10));
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
        assertThat(result).isPresent().hasValueSatisfying(user ->
                assertThat(user.getUsername()).isEqualTo("user1"));
    }

    @Test
    public void findUserByUsername() {
        Optional<UserEntity> result = getUserRepository().findByUsername("user2");
        assertThat(result).isPresent().hasValueSatisfying(user ->
                assertThat(user.getId()).isEqualTo("2"));
    }

    @Test
    public void findAllUsers() {
        List<UserEntity> users = getUserRepository().findAll();
        assertThat(users).isNotEmpty().hasSize(3)
                .extracting(UserEntity::getUsername)
                .containsExactly("user3", "user2", "user1");
    }

    @Test
    public void findAllUsersByQueryByPagination() {
        UserQuery query = new UserQuery();
        query.setFrom(LocalDateTime.parse("2021-12-01T00:00:00")
                .toInstant(ZoneOffset.UTC).toEpochMilli());
        query.setTo(LocalDateTime.parse("2021-12-31T23:59:59")
                .toInstant(ZoneOffset.UTC).toEpochMilli());
        query.setField("username");
        query.setQuery("user");
        Page<UserEntity> result = getUserRepository().findAll(query, 1, 2);
        assertThat(result.getTotalCount()).isEqualTo(2);
        assertThat(result.getData()).isNotEmpty().hasSize(2)
                .extracting(UserEntity::getUsername).containsExactly("user3", "user2");
    }

    private synchronized UserRepository getUserRepository() {
        if (userRepository == null) {
            userRepository = new JpaUserRepository();
            userRepository.setEntityManager(em());
        }
        return userRepository;
    }
}
