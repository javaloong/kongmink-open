package org.javaloong.kongmink.open.am.ory.hydra.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.javaloong.kongmink.open.am.ory.hydra.internal.model.OAuth2Client;

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
        // Client
        wireMockServer.stubFor(get(urlMatching(".*/clients/example-client"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBody(writeValueAsString(createOAuth2Client()))));
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

    private static OAuth2Client createOAuth2Client() {
        OAuth2Client client = new OAuth2Client();
        client.setClientId("example-client");
        client.setClientName("Example Client");
        return client;
    }
}
