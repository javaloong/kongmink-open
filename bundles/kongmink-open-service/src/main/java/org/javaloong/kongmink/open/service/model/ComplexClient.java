package org.javaloong.kongmink.open.service.model;

import org.javaloong.kongmink.open.common.model.client.Client;

import java.time.LocalDateTime;

public class ComplexClient extends Client {

    private LocalDateTime createdDate;

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public static ComplexClient fromClient(Client source) {
        ComplexClient client = new ComplexClient();
        client.setId(source.getId());
        client.setClientId(source.getClientId());
        client.setName(source.getName());
        client.setDescription(source.getDescription());
        client.setEnabled(source.isEnabled());
        client.setRootUrl(source.getRootUrl());
        client.setBaseUrl(source.getBaseUrl());
        client.setSecret(source.getSecret());
        client.setRedirectUris(source.getRedirectUris());
        client.setConsentRequired(source.isConsentRequired());
        client.setDefaultScopes(source.getDefaultScopes());
        client.setAllowedScopes(source.getAllowedScopes());
        client.setGrantTypes(source.getGrantTypes());
        return client;
    }
}
