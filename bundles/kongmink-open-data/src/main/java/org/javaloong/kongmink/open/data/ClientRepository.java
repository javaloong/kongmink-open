package org.javaloong.kongmink.open.data;

import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.data.domain.Client;
import org.javaloong.kongmink.open.data.domain.User;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, String> {

    List<Client> findAllByUser(User user);

    Page<Client> findAllByUser(User user, int page, int size);
}
