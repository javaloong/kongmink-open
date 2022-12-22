package org.javaloong.kongmink.open.am;

import org.javaloong.kongmink.open.common.model.Client;

import java.util.Optional;

public interface ClientProvider {

    Optional<Client> findByClientId(String clientId);

    Client create(Client client);

    void update(Client client);

    void delete(String id);

    String getSecret(String id);

    String regenerateSecret(String id);
}
