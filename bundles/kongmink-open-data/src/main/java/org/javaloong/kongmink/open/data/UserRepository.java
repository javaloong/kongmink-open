package org.javaloong.kongmink.open.data;

import org.javaloong.kongmink.open.data.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findByUsername(String username);
}
