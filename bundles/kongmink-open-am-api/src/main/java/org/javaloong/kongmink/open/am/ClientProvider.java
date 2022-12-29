package org.javaloong.kongmink.open.am;

import org.javaloong.kongmink.open.common.model.Client;

import java.util.Optional;

public interface ClientProvider {

    Optional<Client> findById(String clientId);

    Client create(Client client);

    void update(Client client);

    void delete(String clientId);

    String getSecret(String clientId);

    String regenerateSecret(String clientId);
}
