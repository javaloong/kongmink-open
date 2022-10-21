package org.javaloong.kongmink.open.rest.core.dto;

import org.javaloong.kongmink.open.common.model.Application;
import org.javaloong.kongmink.open.common.model.application.ApplicationSettings;
import org.javaloong.kongmink.open.common.model.application.ApplicationType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ApplicationDTO {

    @NotEmpty
    private String name;
    private String description;
    private String picture;
    @NotNull
    private ApplicationType applicationType;
    private ApplicationSettings settings;

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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    public ApplicationSettings getSettings() {
        return settings;
    }

    public void setSettings(ApplicationSettings settings) {
        this.settings = settings;
    }

    public Application toApplication() {
        return toApplication(null);
    }

    public Application toApplication(String id) {
        Application application = new Application();
        application.setId(id);
        application.setName(name);
        application.setDescription(description);
        application.setPicture(picture);
        application.setApplicationType(applicationType);
        application.setSettings(settings);
        return application;
    }
}
