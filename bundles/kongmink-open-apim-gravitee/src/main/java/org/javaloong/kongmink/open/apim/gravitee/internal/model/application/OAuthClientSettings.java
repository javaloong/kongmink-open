package org.javaloong.kongmink.open.apim.gravitee.internal.model.application;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OAuthClientSettings {

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_secret")
    private String clientSecret;

    @JsonProperty("redirect_uris")
    private List<String> redirectUris;

    @JsonProperty("client_uri")
    private String clientUri;

    @JsonProperty("logo_uri")
    private String logoUri;

    @JsonProperty("response_types")
    private List<String> responseTypes;

    @JsonProperty("grant_types")
    private List<String> grantTypes;

    @JsonProperty("application_type")
    private String applicationType;

    @JsonProperty("renew_client_secret_supported")
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
