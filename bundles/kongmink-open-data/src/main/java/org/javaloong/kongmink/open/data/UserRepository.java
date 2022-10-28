package org.javaloong.kongmink.open.data;

import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.model.user.query.UserQuery;
import org.javaloong.kongmink.open.data.domain.UserEntity;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, String> {

    Optional<UserEntity> findByUsername(String username);

    Page<UserEntity> findAll(UserQuery query, int page, int size);
}
