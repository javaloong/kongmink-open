package org.javaloong.kongmink.open.apim.gravitee.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserPermissions {

    @JsonProperty("APPLICATION")
    private List<String> applications;
    @JsonProperty("USER")
    private List<String> users;

    public List<String> getApplications() {
        return applications;
    }

    public void setApplications(List<String> applications) {
        this.applications = applications;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
