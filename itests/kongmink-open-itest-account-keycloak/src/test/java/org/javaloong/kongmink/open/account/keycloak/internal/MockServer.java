package org.javaloong.kongmink.open.account.keycloak.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.keycloak.representations.account.UserRepresentation;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

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
        // User
        wireMockServer.stubFor(get(urlPathMatching(".*/account"))
                .withHeader(HttpHeaders.AUTHORIZATION, matching("Bearer test_access_token"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBody(writeValueAsString(createUserRepresentation()))));
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

    private static UserRepresentation createUserRepresentation() {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId("1");
        userRepresentation.setUsername("user1");
        userRepresentation.setEmailVerified(false);
        return userRepresentation;
    }
}
