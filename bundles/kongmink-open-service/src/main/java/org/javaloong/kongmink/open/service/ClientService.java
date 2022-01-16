package org.javaloong.kongmink.open.service;

import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.model.client.Client;
import org.javaloong.kongmink.open.common.model.user.User;
import org.javaloong.kongmink.open.service.model.ComplexClient;

import java.util.Collection;
import java.util.Optional;

public interface ClientService {

    ComplexClient create(User user, Client client);

    void update(Client client);

    void delete(String id);

    Optional<ComplexClient> findById(String id);

    Collection<ComplexClient> findAllByUser(User user, int size);

    Page<ComplexClient> findAllByUser(User user, int page, int size);
}
