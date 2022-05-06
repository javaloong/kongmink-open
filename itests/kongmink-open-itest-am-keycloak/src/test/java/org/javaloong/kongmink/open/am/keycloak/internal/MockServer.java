package org.javaloong.kongmink.open.am.keycloak.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class MockServer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static WireMockServer wireMockServer;

    public static void start(int port) {
        wireMockServer = new WireMockServer(options().port(port));
        wireMockServer.start();
        initServer();
    }

    private static void initServer() {
        // OIDC Token
        wireMockServer.stubFor(post(urlMatching(".*/protocol/openid-connect/token"))
                .willReturn(aResponse().withBody(writeValueAsString(createAccessToken()))));
        // User
        wireMockServer.stubFor(get(urlMatching(".*/users/1"))
                .withHeader(HttpHeaders.AUTHORIZATION, matching("Bearer test_access_token"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBody(writeValueAsString(createUserRepresentation()))));
        // Client
        wireMockServer.stubFor(get(urlMatching(".*/clients/1"))
                .withHeader(HttpHeaders.AUTHORIZATION, matching("Bearer test_access_token"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBody(writeValueAsString(createClientRepresentation()))));
    }

    public static void stop() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    private static String writeValueAsString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static Map<String, Object> createAccessToken() {
        Map<String, Object> token = new HashMap<>();
        token.put("access_token", "test_access_token");
        token.put("expires_in", 600);
        token.put("refresh_token", "test_refresh_token");
        token.put("refresh_expires_in", 1800);
        token.put("token_type", "bearer");
        return token;
    }

    private static UserRepresentation createUserRepresentation() {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId("1");
        userRepresentation.setUsername("user1");
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(false);
        userRepresentation.setCreatedTimestamp(new Date().getTime());
        return userRepresentation;
    }

    private static ClientRepresentation createClientRepresentation() {
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setId("1");
        clientRepresentation.setClientId("example-client");
        return clientRepresentation;
    }
}
