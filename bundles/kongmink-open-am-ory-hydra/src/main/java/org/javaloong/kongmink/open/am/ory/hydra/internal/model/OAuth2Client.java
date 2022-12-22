package org.javaloong.kongmink.open.am.ory.hydra.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class OAuth2Client {

    @JsonProperty("allowed_cors_origins")
    private List<String> allowedCorsOrigins;
    @JsonProperty("audience")
    private List<String> audience;
    @JsonProperty("authorization_code_grant_access_token_lifespan")
    private String authorizationCodeGrantAccessTokenLifespan;
    @JsonProperty("authorization_code_grant_id_token_lifespan")
    private String authorizationCodeGrantIdTokenLifespan;
    @JsonProperty("authorization_code_grant_refresh_token_lifespan")
    private String authorizationCodeGrantRefreshTokenLifespan;
    @JsonProperty("backchannel_logout_session_required")
    private Boolean backchannelLogoutSessionRequired;
    @JsonProperty("backchannel_logout_uri")
    private String backchannelLogoutUri;
    @JsonProperty("client_credentials_grant_access_token_lifespan")
    private String clientCredentialsGrantAccessTokenLifespan;
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_name")
    private String clientName;
    @JsonProperty("client_secret")
    private String clientSecret;
    @JsonProperty("client_secret_expires_at")
    private Long clientSecretExpiresAt;
    @JsonProperty("client_uri")
    private String clientUri;
    @JsonProperty("contacts")
    private List<String> contacts;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("frontchannel_logout_session_required")
    private Boolean frontchannelLogoutSessionRequired;
    @JsonProperty("frontchannel_logout_uri")
    private String frontchannelLogoutUri;
    @JsonProperty("grant_types")
    private List<String> grantTypes;
    @JsonProperty("implicit_grant_access_token_lifespan")
    private String implicitGrantAccessTokenLifespan;
    @JsonProperty("implicit_grant_id_token_lifespan")
    private String implicitGrantIdTokenLifespan;
    @JsonProperty("jwks")
    private Object jwks;
    @JsonProperty("jwks_uri")
    private String jwksUri;
    @JsonProperty("jwt_bearer_grant_access_token_lifespan")
    private String jwtBearerGrantAccessTokenLifespan;
    @JsonProperty("logo_uri")
    private String logoUri;
    @JsonProperty("metadata")
    private Object metadata;
    @JsonProperty("owner")
    private String owner;
    @JsonProperty("policy_uri")
    private String policyUri;
    @JsonProperty("post_logout_redirect_uris")
    private List<String> postLogoutRedirectUris;
    @JsonProperty("redirect_uris")
    private List<String> redirectUris;
    @JsonProperty("refresh_token_grant_access_token_lifespan")
    private String refreshTokenGrantAccessTokenLifespan;
    @JsonProperty("refresh_token_grant_id_token_lifespan")
    private String refreshTokenGrantIdTokenLifespan;
    @JsonProperty("refresh_token_grant_refresh_token_lifespan")
    private String refreshTokenGrantRefreshTokenLifespan;
    @JsonProperty("registration_access_token")
    private String registrationAccessToken;
    @JsonProperty("registration_client_uri")
    private String registrationClientUri;
    @JsonProperty("request_object_signing_alg")
    private String requestObjectSigningAlg;
    @JsonProperty("request_uris")
    private List<String> requestUris;
    @JsonProperty("response_types")
    private List<String> responseTypes;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("sector_identifier_uri")
    private String sectorIdentifierUri;
    @JsonProperty("subject_type")
    private String subjectType;
    @JsonProperty("token_endpoint_auth_method")
    private String tokenEndpointAuthMethod;
    @JsonProperty("token_endpoint_auth_signing_alg")
    private String tokenEndpointAuthSigningAlg;
    @JsonProperty("tos_uri")
    private String tosUri;
    @JsonProperty("updated_at")
    private Date updatedAt;
    @JsonProperty("userinfo_signed_response_alg")
    private String userinfoSignedResponseAlg;

    public List<String> getAllowedCorsOrigins() {
        return allowedCorsOrigins;
    }

    public void setAllowedCorsOrigins(List<String> allowedCorsOrigins) {
        this.allowedCorsOrigins = allowedCorsOrigins;
    }

    public List<String> getAudience() {
        return audience;
    }

    public void setAudience(List<String> audience) {
        this.audience = audience;
    }

    public String getAuthorizationCodeGrantAccessTokenLifespan() {
        return authorizationCodeGrantAccessTokenLifespan;
    }

    public void setAuthorizationCodeGrantAccessTokenLifespan(String authorizationCodeGrantAccessTokenLifespan) {
        this.authorizationCodeGrantAccessTokenLifespan = authorizationCodeGrantAccessTokenLifespan;
    }

    public String getAuthorizationCodeGrantIdTokenLifespan() {
        return authorizationCodeGrantIdTokenLifespan;
    }

    public void setAuthorizationCodeGrantIdTokenLifespan(String authorizationCodeGrantIdTokenLifespan) {
        this.authorizationCodeGrantIdTokenLifespan = authorizationCodeGrantIdTokenLifespan;
    }

    public String getAuthorizationCodeGrantRefreshTokenLifespan() {
        return authorizationCodeGrantRefreshTokenLifespan;
    }

    public void setAuthorizationCodeGrantRefreshTokenLifespan(String authorizationCodeGrantRefreshTokenLifespan) {
        this.authorizationCodeGrantRefreshTokenLifespan = authorizationCodeGrantRefreshTokenLifespan;
    }

    public Boolean getBackchannelLogoutSessionRequired() {
        return backchannelLogoutSessionRequired;
    }

    public void setBackchannelLogoutSessionRequired(Boolean backchannelLogoutSessionRequired) {
        this.backchannelLogoutSessionRequired = backchannelLogoutSessionRequired;
    }

    public String getBackchannelLogoutUri() {
        return backchannelLogoutUri;
    }

    public void setBackchannelLogoutUri(String backchannelLogoutUri) {
        this.backchannelLogoutUri = backchannelLogoutUri;
    }

    public String getClientCredentialsGrantAccessTokenLifespan() {
        return clientCredentialsGrantAccessTokenLifespan;
    }

    public void setClientCredentialsGrantAccessTokenLifespan(String clientCredentialsGrantAccessTokenLifespan) {
        this.clientCredentialsGrantAccessTokenLifespan = clientCredentialsGrantAccessTokenLifespan;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Long getClientSecretExpiresAt() {
        return clientSecretExpiresAt;
    }

    public void setClientSecretExpiresAt(Long clientSecretExpiresAt) {
        this.clientSecretExpiresAt = clientSecretExpiresAt;
    }

    public String getClientUri() {
        return clientUri;
    }

    public void setClientUri(String clientUri) {
        this.clientUri = clientUri;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getFrontchannelLogoutSessionRequired() {
        return frontchannelLogoutSessionRequired;
    }

    public void setFrontchannelLogoutSessionRequired(Boolean frontchannelLogoutSessionRequired) {
        this.frontchannelLogoutSessionRequired = frontchannelLogoutSessionRequired;
    }

    public String getFrontchannelLogoutUri() {
        return frontchannelLogoutUri;
    }

    public void setFrontchannelLogoutUri(String frontchannelLogoutUri) {
        this.frontchannelLogoutUri = frontchannelLogoutUri;
    }

    public List<String> getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(List<String> grantTypes) {
        this.grantTypes = grantTypes;
    }

    public String getImplicitGrantAccessTokenLifespan() {
        return implicitGrantAccessTokenLifespan;
    }

    public void setImplicitGrantAccessTokenLifespan(String implicitGrantAccessTokenLifespan) {
        this.implicitGrantAccessTokenLifespan = implicitGrantAccessTokenLifespan;
    }

    public String getImplicitGrantIdTokenLifespan() {
        return implicitGrantIdTokenLifespan;
    }

    public void setImplicitGrantIdTokenLifespan(String implicitGrantIdTokenLifespan) {
        this.implicitGrantIdTokenLifespan = implicitGrantIdTokenLifespan;
    }

    public Object getJwks() {
        return jwks;
    }

    public void setJwks(Object jwks) {
        this.jwks = jwks;
    }

    public String getJwksUri() {
        return jwksUri;
    }

    public void setJwksUri(String jwksUri) {
        this.jwksUri = jwksUri;
    }

    public String getJwtBearerGrantAccessTokenLifespan() {
        return jwtBearerGrantAccessTokenLifespan;
    }

    public void setJwtBearerGrantAccessTokenLifespan(String jwtBearerGrantAccessTokenLifespan) {
        this.jwtBearerGrantAccessTokenLifespan = jwtBearerGrantAccessTokenLifespan;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPolicyUri() {
        return policyUri;
    }

    public void setPolicyUri(String policyUri) {
        this.policyUri = policyUri;
    }

    public List<String> getPostLogoutRedirectUris() {
        return postLogoutRedirectUris;
    }

    public void setPostLogoutRedirectUris(List<String> postLogoutRedirectUris) {
        this.postLogoutRedirectUris = postLogoutRedirectUris;
    }

    public List<String> getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(List<String> redirectUris) {
        this.redirectUris = redirectUris;
    }

    public String getRefreshTokenGrantAccessTokenLifespan() {
        return refreshTokenGrantAccessTokenLifespan;
    }

    public void setRefreshTokenGrantAccessTokenLifespan(String refreshTokenGrantAccessTokenLifespan) {
        this.refreshTokenGrantAccessTokenLifespan = refreshTokenGrantAccessTokenLifespan;
    }

    public String getRefreshTokenGrantIdTokenLifespan() {
        return refreshTokenGrantIdTokenLifespan;
    }

    public void setRefreshTokenGrantIdTokenLifespan(String refreshTokenGrantIdTokenLifespan) {
        this.refreshTokenGrantIdTokenLifespan = refreshTokenGrantIdTokenLifespan;
    }

    public String getRefreshTokenGrantRefreshTokenLifespan() {
        return refreshTokenGrantRefreshTokenLifespan;
    }

    public void setRefreshTokenGrantRefreshTokenLifespan(String refreshTokenGrantRefreshTokenLifespan) {
        this.refreshTokenGrantRefreshTokenLifespan = refreshTokenGrantRefreshTokenLifespan;
    }

    public String getRegistrationAccessToken() {
        return registrationAccessToken;
    }

    public void setRegistrationAccessToken(String registrationAccessToken) {
        this.registrationAccessToken = registrationAccessToken;
    }

    public String getRegistrationClientUri() {
        return registrationClientUri;
    }

    public void setRegistrationClientUri(String registrationClientUri) {
        this.registrationClientUri = registrationClientUri;
    }

    public String getRequestObjectSigningAlg() {
        return requestObjectSigningAlg;
    }

    public void setRequestObjectSigningAlg(String requestObjectSigningAlg) {
        this.requestObjectSigningAlg = requestObjectSigningAlg;
    }

    public List<String> getRequestUris() {
        return requestUris;
    }

    public void setRequestUris(List<String> requestUris) {
        this.requestUris = requestUris;
    }

    public List<String> getResponseTypes() {
        return responseTypes;
    }

    public void setResponseTypes(List<String> responseTypes) {
        this.responseTypes = responseTypes;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSectorIdentifierUri() {
        return sectorIdentifierUri;
    }

    public void setSectorIdentifierUri(String sectorIdentifierUri) {
        this.sectorIdentifierUri = sectorIdentifierUri;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getTokenEndpointAuthMethod() {
        return tokenEndpointAuthMethod;
    }

    public void setTokenEndpointAuthMethod(String tokenEndpointAuthMethod) {
        this.tokenEndpointAuthMethod = tokenEndpointAuthMethod;
    }

    public String getTokenEndpointAuthSigningAlg() {
        return tokenEndpointAuthSigningAlg;
    }

    public void setTokenEndpointAuthSigningAlg(String tokenEndpointAuthSigningAlg) {
        this.tokenEndpointAuthSigningAlg = tokenEndpointAuthSigningAlg;
    }

    public String getTosUri() {
        return tosUri;
    }

    public void setTosUri(String tosUri) {
        this.tosUri = tosUri;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserinfoSignedResponseAlg() {
        return userinfoSignedResponseAlg;
    }

    public void setUserinfoSignedResponseAlg(String userinfoSignedResponseAlg) {
        this.userinfoSignedResponseAlg = userinfoSignedResponseAlg;
    }
}

