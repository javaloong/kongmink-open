package org.javaloong.kongmink.open.apim.gravitee.internal.model.application;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleApplicationSettings {

    private String type;
    @JsonProperty("client_id")
    private String clientId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
