package org.javaloong.kongmink.open.apim.gravitee.internal.model;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.application.ApplicationSettings;

import java.util.Set;

public class NewApplicationEntity {

    private String name;
    private String description;
    private Set<String> groups;
    private ApplicationSettings settings;
    private String picture;
    private String background;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ApplicationSettings getSettings() {
        return settings;
    }

    public void setSettings(ApplicationSettings settings) {
        this.settings = settings;
    }

    public Set<String> getGroups() {
        return groups;
    }

    public void setGroups(Set<String> groups) {
        this.groups = groups;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
