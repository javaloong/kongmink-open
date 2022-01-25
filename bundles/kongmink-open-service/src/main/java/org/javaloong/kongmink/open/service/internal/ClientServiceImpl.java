package org.javaloong.kongmink.open.service.internal;

import org.javaloong.kongmink.open.am.client.ClientProvider;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.client.Client;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.data.ClientRepository;
import org.javaloong.kongmink.open.data.domain.ClientEntity;
import org.javaloong.kongmink.open.data.domain.UserEntity;
import org.javaloong.kongmink.open.service.ClientService;
import org.javaloong.kongmink.open.service.model.ComplexClient;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.transaction.control.TransactionControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Component(service = ClientService.class)
public class ClientServiceImpl implements ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Reference
    TransactionControl transactionControl;
    @Reference
    ClientProvider clientProvider;
    @Reference
    ClientRepository clientRepository;

    @Override
    public ComplexClient create(User user, Client client) {
        Client result = clientProvider.create(client);
        log.debug("Client {} successfully created", result.getId());
        return transactionControl.required(() -> {
            ClientEntity entity = clientRepository.create(mapToClientEntity(user, client));
            log.debug("Client {} successfully added to repository", result.getId());
            return mapToComplexClient(entity, result);
        });
    }

    @Override
    public void update(Client client) {
        transactionControl.required(() -> {
            clientRepository.findById(client.getId()).ifPresent(entity -> {
                entity.setName(client.getName());
                clientRepository.update(entity);
                log.debug("Client {} successfully saved to repository", client.getId());
            });
            clientProvider.update(client);
            log.debug("Client {} successfully updated", client.getId());
            return null;
        });
    }

    @Override
    public void delete(String id) {
        transactionControl.required(() -> {
            clientRepository.delete(id);
            log.debug("Client {} successfully removed from repository", id);
            clientProvider.delete(id);
            log.debug("Client {} successfully deleted", id);
            return null;
        });
    }

    @Override
    public Optional<ComplexClient> findById(String id) {
        return transactionControl.supports(() -> clientRepository.findById(id).map(entity -> {
            Optional<Client> result = clientProvider.findById(entity.getId());
            return result.map(client ->mapToComplexClient(entity, client)).orElse(null);
        }));
    }

    @Override
    public Collection<ComplexClient> findAllByUser(User user, int size) {
        return transactionControl.notSupported(() -> {
            Collection<ClientEntity> entities = clientRepository.findAllByUser(getUserEntity(user.getId()), size);
            return mapToComplexClients(entities);
        });
    }

    @Override
    public Page<ComplexClient> findAllByUser(User user, int page, int size) {
        return transactionControl.notSupported(() -> {
            Page<ClientEntity> result = clientRepository.findAllByUser(getUserEntity(user.getId()), page, size);
            Collection<ComplexClient> items = mapToComplexClients(result.getData());
            return new Page<>(items, result.getTotalCount());
        });
    }

    private ClientEntity mapToClientEntity(User user, Client client) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(client.getId());
        clientEntity.setName(client.getName());
        clientEntity.setCreatedDate(LocalDateTime.now());
        clientEntity.setUser(getUserEntity(user.getId()));
        return clientEntity;
    }

    private ComplexClient mapToComplexClient(ClientEntity entity, Client client) {
        ComplexClient complexClient = ComplexClient.fromClient(client);
        complexClient.setCreatedDate(entity.getCreatedDate());
        return complexClient;
    }

    private Collection<ComplexClient> mapToComplexClients(Collection<ClientEntity> entities) {
        return entities.stream().map(entity -> {
            ComplexClient client = new ComplexClient();
            client.setId(entity.getId());
            client.setName(entity.getName());
            client.setCreatedDate(entity.getCreatedDate());
            return client;
        }).collect(Collectors.toList());
    }

    private UserEntity getUserEntity(String userId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        return userEntity;
    }
}
