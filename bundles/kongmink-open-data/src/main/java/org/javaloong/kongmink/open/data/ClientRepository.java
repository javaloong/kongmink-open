package org.javaloong.kongmink.open.data;

import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.data.domain.ClientEntity;
import org.javaloong.kongmink.open.data.domain.UserEntity;

import java.util.List;

public interface ClientRepository extends CrudRepository<ClientEntity, String> {

    List<ClientEntity> findAllByUser(UserEntity user, int size);

    Page<ClientEntity> findAllByUser(UserEntity user, int page, int size);
}
