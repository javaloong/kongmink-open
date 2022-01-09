package org.javaloong.kongmink.open.am.client;

import org.javaloong.kongmink.open.am.client.model.Client;

import java.util.Optional;

public interface ClientProvider {

    Optional<Client> findById(String id);

    Optional<Client> findByClientId(String clientId);

    Client create(Client client);

    void updateClient(Client client);

    void deleteClient(String id);

    String getSecret(String id);

    String regenerateSecret(String id);
}
