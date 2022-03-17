package org.javaloong.kongmink.open.apim.model.application;

import java.util.List;

public class OAuthClientSettings {

    private String clientId;
    private String clientSecret;
    private List<String> redirectUris;
    private String clientUri;
    private String logoUri;
    private List<String> responseTypes;
    private List<String> grantTypes;
    private String applicationType;
    private boolean renewClientSecretSupported;

    public List<String> getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(List<String> redirectUris) {
        this.redirectUris = redirectUris;
    }

    public String getClientUri() {
        return clientUri;
    }

    public void setClientUri(String clientUri) {
        this.clientUri = clientUri;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public List<String> getResponseTypes() {
        return responseTypes;
    }

    public void setResponseTypes(List<String> responseTypes) {
        this.responseTypes = responseTypes;
    }

    public List<String> getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(List<String> grantTypes) {
        this.grantTypes = grantTypes;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public boolean isRenewClientSecretSupported() {
        return renewClientSecretSupported;
    }

    public void setRenewClientSecretSupported(boolean renewClientSecretSupported) {
        this.renewClientSecretSupported = renewClientSecretSupported;
    }
}
