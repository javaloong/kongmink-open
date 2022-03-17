package org.javaloong.kongmink.open.apim.model.application;

public class ApplicationSettings {

    private SimpleApplicationSettings app;
    private OAuthClientSettings oauth;

    public SimpleApplicationSettings getApp() {
        return app;
    }

    public void setApp(SimpleApplicationSettings app) {
        this.app = app;
    }

    public OAuthClientSettings getOauth() {
        return oauth;
    }

    public void setOauth(OAuthClientSettings oauth) {
        this.oauth = oauth;
    }
}
