package org.javaloong.kongmink.open.apim.gravitee.internal.model.application;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApplicationSettings {

    @JsonProperty("app")
    private SimpleApplicationSettings app;

    public SimpleApplicationSettings getApp() {
        return app;
    }

    public void setApp(SimpleApplicationSettings app) {
        this.app = app;
    }
}
