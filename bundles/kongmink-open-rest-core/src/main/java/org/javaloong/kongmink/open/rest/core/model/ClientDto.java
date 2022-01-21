package org.javaloong.kongmink.open.rest.core.model;

import org.javaloong.kongmink.open.common.model.client.Client;

import javax.validation.constraints.NotEmpty;

public class ClientDto {

    @NotEmpty
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Client toClient() {
        return toClient(null);
    }

    public Client toClient(String id) {
        Client client = new Client();
        client.setId(id);
        client.setName(name);
        client.setDescription(description);
        return client;
    }
}
