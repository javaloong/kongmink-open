package org.javaloong.kongmink.open.am.clients.keycloak.internal;

import org.javaloong.kongmink.open.am.clients.model.Client;
import org.keycloak.OAuth2Constants;
import org.keycloak.representations.idm.ClientRepresentation;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.*;

public class ClientMapper {

    public static final String OIDC = "openid-connect";
    public static final String OAUTH2_DEVICE_AUTHORIZATION_GRANT_ENABLED = "oauth2.device.authorization.grant.enabled";
    public static final String USE_REFRESH_TOKEN = "use.refresh.tokens";

    private static final ModelMapper modelMapper = createModelMapper();

    public static Client mapToClient(ClientRepresentation clientRepresentation) {
        Client client = modelMapper.map(clientRepresentation, Client.class);
        client.setGrantTypes(getGrantTypes(clientRepresentation));
        return client;
    }

    public static ClientRepresentation mapToClientRepresentation(Client client) {
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setProtocol(OIDC);
        map(client, clientRepresentation);
        return clientRepresentation;
    }

    public static void map(Client source, ClientRepresentation destination) {
        modelMapper.map(source, destination);
        if (source.getGrantTypes() != null) {
            setGrantTypes(destination, source.getGrantTypes());
        }
    }

    private static ModelMapper createModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        // Client scopes
        modelMapper.createTypeMap(Client.class, ClientRepresentation.class)
                .addMapping(Client::getDefaultScopes, ClientRepresentation::setDefaultClientScopes)
                .addMapping(Client::getAllowedScopes, ClientRepresentation::setOptionalClientScopes);
        modelMapper.createTypeMap(ClientRepresentation.class, Client.class)
                .addMapping(ClientRepresentation::getDefaultClientScopes, Client::setDefaultScopes)
                .addMapping(ClientRepresentation::getOptionalClientScopes, Client::setAllowedScopes);
        return modelMapper;
    }

    private static List<String> getGrantTypes(ClientRepresentation clientRepresentation) {
        Set<String> grantTypes = new HashSet<>();
        // Authorization Code
        if (clientRepresentation.isStandardFlowEnabled()) {
            grantTypes.add(OAuth2Constants.AUTHORIZATION_CODE);
        }
        // Client Credentials
        if (clientRepresentation.isServiceAccountsEnabled()) {
            grantTypes.add(OAuth2Constants.CLIENT_CREDENTIALS);
        }
        // Password Grant
        if (clientRepresentation.isDirectAccessGrantsEnabled()) {
            grantTypes.add(OAuth2Constants.PASSWORD);
        }
        // Implicit Flow
        if (clientRepresentation.isImplicitFlowEnabled()) {
            grantTypes.add(OAuth2Constants.IMPLICIT);
        }
        // Device Code
        if (Boolean.parseBoolean(clientRepresentation.getAttributes().getOrDefault(OAUTH2_DEVICE_AUTHORIZATION_GRANT_ENABLED, "false"))) {
            grantTypes.add(OAuth2Constants.DEVICE_CODE_GRANT_TYPE);
        }
        // Refresh Token
        if (Boolean.parseBoolean(clientRepresentation.getAttributes().getOrDefault(USE_REFRESH_TOKEN, "false"))) {
            grantTypes.add(OAuth2Constants.REFRESH_TOKEN);
        }
        return new ArrayList<>(grantTypes);
    }

    private static void setGrantTypes(ClientRepresentation clientRepresentation, List<String> grantTypes) {
        // Clear Client Defaults
        clientRepresentation.setStandardFlowEnabled(false);
        clientRepresentation.setDirectAccessGrantsEnabled(false);
        // Authorization Code
        if (grantTypes.contains(OAuth2Constants.AUTHORIZATION_CODE)) {
            clientRepresentation.setStandardFlowEnabled(true);
            clientRepresentation.setPublicClient(false);
        }
        // Client Credentials
        if (grantTypes.contains(OAuth2Constants.CLIENT_CREDENTIALS)) {
            clientRepresentation.setServiceAccountsEnabled(true);
            clientRepresentation.setPublicClient(false);
        }
        // Password Grant
        if (grantTypes.contains(OAuth2Constants.PASSWORD)) {
            clientRepresentation.setDirectAccessGrantsEnabled(true);
            clientRepresentation.setPublicClient(false);
        }
        // Implicit Flow
        if (grantTypes.contains(OAuth2Constants.IMPLICIT)) {
            clientRepresentation.setImplicitFlowEnabled(true);
            clientRepresentation.setPublicClient(true);
        }
        // Device Code
        if (grantTypes.contains(OAuth2Constants.DEVICE_CODE_GRANT_TYPE)) {
            getClientAttributes(clientRepresentation).put(OAUTH2_DEVICE_AUTHORIZATION_GRANT_ENABLED, "true");
            clientRepresentation.setPublicClient(true);
        }
        // Refresh Token
        if (grantTypes.contains(OAuth2Constants.REFRESH_TOKEN)) {
            getClientAttributes(clientRepresentation).put(USE_REFRESH_TOKEN, "true");
        }
    }

    private static Map<String, String> getClientAttributes(ClientRepresentation clientRepresentation) {
        if (clientRepresentation.getAttributes() == null) {
            clientRepresentation.setAttributes(new HashMap<>());
        }
        return clientRepresentation.getAttributes();
    }
}
