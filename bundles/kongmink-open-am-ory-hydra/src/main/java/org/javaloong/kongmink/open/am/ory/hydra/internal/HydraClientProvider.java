package org.javaloong.kongmink.open.am.ory.hydra.internal;

import org.apache.commons.lang3.RandomStringUtils;
import org.javaloong.kongmink.open.am.ClientProvider;
import org.javaloong.kongmink.open.am.ory.hydra.internal.mapper.ClientMapper;
import org.javaloong.kongmink.open.am.ory.hydra.internal.model.OAuth2Client;
import org.javaloong.kongmink.open.am.ory.hydra.internal.resource.ClientResource;
import org.javaloong.kongmink.open.am.ory.hydra.internal.resource.ClientsResource;
import org.javaloong.kongmink.open.common.model.Client;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Component(service = ClientProvider.class)
public class HydraClientProvider implements ClientProvider {

    private static final Logger log = LoggerFactory.getLogger(HydraClientProvider.class);

    private final HydraAdminClient adminClient;

    @Activate
    public HydraClientProvider(@Reference HydraAdminClient adminClient) {
        this.adminClient = adminClient;
    }

    @Override
    public Optional<Client> findById(String clientId) {
        Optional<ClientResource> clientResource = getClientResource(clientId);
        return clientResource.flatMap(
                resource -> getOAuth2Client(resource)
                        .map(ClientMapper::mapToClient));
    }

    @Override
    public Client create(Client client) {
        ClientsResource clientsResource = adminClient.getAdminResource().getClientsResource();
        OAuth2Client oauth2Client = ClientMapper.mapToOAuth2Client(client);
        Response response = clientsResource.create(oauth2Client);
        log.info("Response Code: " + response.getStatusInfo().getStatusCode());
        if (response.getStatusInfo().toEnum().equals(Response.Status.CREATED)) {
            log.info("Response Location: " + response.getLocation());
        }
        return ClientMapper.mapToClient(response.readEntity(OAuth2Client.class));
    }

    @Override
    public void update(Client client) {
        Optional<ClientResource> clientResource = getClientResource(client.getClientId());
        clientResource.ifPresent(resource -> {
            OAuth2Client oauth2Client = resource.getClientById();
            ClientMapper.map(client, oauth2Client);
            resource.updateClientById(oauth2Client);
        });
    }

    @Override
    public void delete(String id) {
        Optional<ClientResource> clientResource = getClientResource(id);
        clientResource.ifPresent(ClientResource::deleteClientById);
    }

    @Override
    public String getSecret(String id) {
        return null;
    }

    @Override
    public String regenerateSecret(String id) {
        Optional<ClientResource> clientResource = getClientResource(id);
        return clientResource.map(resource -> {
            OAuth2Client oauth2Client = resource.getClientById();
            oauth2Client.setClientSecret(RandomStringUtils.random(26, true, true));
            Response response = resource.updateClientById(oauth2Client);
            return response.readEntity(OAuth2Client.class).getClientSecret();
        }).orElse(null);
    }

    private Optional<ClientResource> getClientResource(String id) {
        return Optional.ofNullable(adminClient.getAdminResource().getClientsResource().get(id));
    }

    private Optional<OAuth2Client> getOAuth2Client(ClientResource resource) {
        try {
            return Optional.ofNullable(resource.getClientById());
        } catch (NotFoundException ex) {
            return Optional.empty();
        }
    }
}
