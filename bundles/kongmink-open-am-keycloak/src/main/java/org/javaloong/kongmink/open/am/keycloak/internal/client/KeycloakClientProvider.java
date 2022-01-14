package org.javaloong.kongmink.open.am.keycloak.internal.client;

import org.javaloong.kongmink.open.am.client.ClientException;
import org.javaloong.kongmink.open.am.client.ClientProvider;
import org.javaloong.kongmink.open.common.model.client.Client;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Component(service = ClientProvider.class, immediate = true)
public class KeycloakClientProvider implements ClientProvider {

    private static final Logger log = LoggerFactory.getLogger(KeycloakClientProvider.class);

    private final KeycloakAdminClient adminClient;

    @Activate
    public KeycloakClientProvider(@Reference KeycloakAdminClient adminClient) {
        this.adminClient = adminClient;
    }

    @Override
    public Optional<Client> findById(String id) {
        Optional<ClientResource> clientResource = getClientResource(id);
        return clientResource.flatMap(
                resource -> getClientRepresentation(resource)
                        .map(ClientMapper::mapToClient));
    }

    @Override
    public Optional<Client> findByClientId(String clientId) {
        ClientsResource clientsResource = adminClient.getClientsResource();
        List<ClientRepresentation> clientList = clientsResource.findByClientId(clientId);
        Optional<Client> result = Optional.empty();
        if (clientList.size() > 0) {
            result = Optional.of(ClientMapper.mapToClient(clientList.get(0)));
        }
        return result;
    }

    @Override
    public Client create(Client client) {
        verifyClient(client.getClientId());
        ClientsResource clientsResource = adminClient.getClientsResource();
        ClientRepresentation clientRepresentation = ClientMapper.mapToClientRepresentation(client);
        Response response = clientsResource.create(clientRepresentation);
        log.info("Response Code: " + response.getStatusInfo());
        if (response.getStatusInfo().equals(Response.Status.CREATED)) {
            log.info("Response Location: " + response.getLocation());
        }
        String id = CreatedResponseUtil.getCreatedId(response);
        return findById(id).orElseThrow();
    }

    @Override
    public void update(Client client) {
        Optional<ClientResource> clientResource = getClientResource(client.getId());
        clientResource.ifPresent(resource -> {
            ClientRepresentation clientRepresentation = resource.toRepresentation();
            String clientId = clientRepresentation.getClientId();
            ClientMapper.map(client, clientRepresentation);
            clientRepresentation.setClientId(clientId);
            resource.update(clientRepresentation);
        });
    }

    @Override
    public void delete(String id) {
        Optional<ClientResource> clientResource = getClientResource(id);
        clientResource.ifPresent(ClientResource::remove);
    }

    @Override
    public String getSecret(String id) {
        Optional<ClientResource> clientResource = getClientResource(id);
        return clientResource.map(resource -> resource.getSecret().getValue()).orElse(null);
    }

    @Override
    public String regenerateSecret(String id) {
        Optional<ClientResource> clientResource = getClientResource(id);
        return clientResource.map(resource -> resource.generateNewSecret().getValue()).orElse(null);
    }

    private Optional<ClientResource> getClientResource(String id) {
        return Optional.ofNullable(adminClient.getClientsResource().get(id));
    }

    private Optional<ClientRepresentation> getClientRepresentation(ClientResource resource) {
        try {
            return Optional.ofNullable(resource.toRepresentation());
        } catch (NotFoundException ex) {
            return Optional.empty();
        }
    }

    private void verifyClient(String clientId) {
        Optional<Client> result = findByClientId(clientId);
        if (result.isPresent()) {
            throw ClientException.clientExistsException();
        }
    }
}
