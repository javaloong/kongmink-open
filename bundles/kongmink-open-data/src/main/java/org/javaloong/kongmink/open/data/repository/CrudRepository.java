package org.javaloong.kongmink.open.data.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {

    <S extends T> S create(S entity);

    <S extends T> S update(S entity);

    void delete(ID id);

    Optional<T> findById(ID id);

    List<T> findAll();
}
