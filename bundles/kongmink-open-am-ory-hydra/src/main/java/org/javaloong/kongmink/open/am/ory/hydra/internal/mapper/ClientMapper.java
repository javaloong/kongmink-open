package org.javaloong.kongmink.open.am.ory.hydra.internal.mapper;

import org.apache.commons.lang3.StringUtils;
import org.javaloong.kongmink.open.am.ory.hydra.internal.model.OAuth2Client;
import org.javaloong.kongmink.open.common.model.Client;

import java.util.Arrays;

public class ClientMapper {

    public static Client mapToClient(OAuth2Client oauth2Client) {
        Client client = new Client();
        client.setClientId(oauth2Client.getClientId());
        client.setName(oauth2Client.getClientName());
        client.setSecret(oauth2Client.getClientSecret());
        client.setRootUrl(oauth2Client.getClientUri());
        client.setBaseUrl(oauth2Client.getClientUri());
        client.setRedirectUris(oauth2Client.getRedirectUris());
        client.setGrantTypes(oauth2Client.getGrantTypes());
        if (StringUtils.isNotEmpty(oauth2Client.getScope())) {
            client.setAllowedScopes(Arrays.asList(StringUtils.split(oauth2Client.getScope())));
        }
        return client;
    }

    public static OAuth2Client mapToOAuth2Client(Client client) {
        OAuth2Client oauth2Client = new OAuth2Client();
        oauth2Client.setClientId(client.getClientId());
        oauth2Client.setClientSecret(client.getSecret());
        map(client, oauth2Client);
        return oauth2Client;
    }

    public static void map(Client source, OAuth2Client destination) {
        destination.setClientName(source.getName());
        destination.setClientUri(source.getRootUrl());
        destination.setRedirectUris(source.getRedirectUris());
        destination.setGrantTypes(source.getGrantTypes());
        if (source.getAllowedScopes() != null) {
            destination.setScope(StringUtils.join(source.getAllowedScopes(), " "));
        }
    }
}
