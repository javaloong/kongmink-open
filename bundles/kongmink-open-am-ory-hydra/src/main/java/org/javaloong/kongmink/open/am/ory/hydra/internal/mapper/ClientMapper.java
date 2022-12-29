package org.javaloong.kongmink.open.am.ory.hydra.internal.mapper;

import org.javaloong.kongmink.open.am.ory.hydra.internal.model.OAuth2Client;
import org.javaloong.kongmink.open.common.model.Client;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ClientMapper {

    public static Client mapToClient(OAuth2Client oauth2Client) {
        Client client = new Client();
        client.setClientId(oauth2Client.getClientId());
        client.setClientName(oauth2Client.getClientName());
        client.setClientSecret(oauth2Client.getClientSecret());
        client.setClientUri(oauth2Client.getClientUri());
        client.setScope(oauth2Client.getScope());
        client.setGrantTypes(oauth2Client.getGrantTypes());
        client.setRedirectUris(oauth2Client.getRedirectUris());
        client.setResponseTypes(oauth2Client.getResponseTypes());
        client.setEnabled(true);
        client.setConsentRequired(false);
        client.setCreatedAt(convertToLocalDatetime(oauth2Client.getCreatedAt()));
        client.setUpdatedAt(convertToLocalDatetime(oauth2Client.getUpdatedAt()));
        return client;
    }

    public static OAuth2Client mapToOAuth2Client(Client client) {
        OAuth2Client oauth2Client = new OAuth2Client();
        oauth2Client.setClientId(client.getClientId());
        oauth2Client.setClientSecret(client.getClientSecret());
        map(client, oauth2Client);
        return oauth2Client;
    }

    public static void map(Client source, OAuth2Client destination) {
        destination.setClientName(source.getClientName());
        destination.setClientUri(source.getClientUri());
        destination.setScope(source.getScope());
        destination.setGrantTypes(source.getGrantTypes());
        destination.setRedirectUris(source.getRedirectUris());
        destination.setResponseTypes(source.getResponseTypes());
    }

    private static LocalDateTime convertToLocalDatetime(Date date) {
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private static Date convertToDate(LocalDateTime dateTime) {
        return dateTime == null ? null : Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
