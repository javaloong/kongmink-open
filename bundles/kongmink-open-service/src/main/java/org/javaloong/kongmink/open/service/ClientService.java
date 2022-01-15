package org.javaloong.kongmink.open.service;

import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.service.model.ComplexClient;

import java.util.Collection;
import java.util.Optional;

public interface ClientService {

    ComplexClient create(ComplexClient client);

    void update(ComplexClient client);

    void delete(String id);

    Optional<ComplexClient> findById(String id);

    Collection<ComplexClient> findAllByUser(String userId, int size);

    Page<ComplexClient> findAllByUser(String userId, int page, int size);
}
