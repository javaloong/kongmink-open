package org.javaloong.kongmink.open.apim.gravitee.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.*;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.DataResponse;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.javaloong.kongmink.open.apim.gravitee.internal.resource.DataResponse.METADATA_DATA_KEY;
import static org.javaloong.kongmink.open.apim.gravitee.internal.resource.DataResponse.METADATA_DATA_TOTAL_KEY;

public class MockServer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static WireMockServer wireMockServer;

    public static void start(int port) {
        wireMockServer = new WireMockServer(options().port(port));
        wireMockServer.start();
        initServer();
    }

    private static void initServer() {
        // Auth
        wireMockServer.stubFor(post(urlMatching(".*/auth/oauth2/.*"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBody(writeValueAsString(createTokenEntity()))));
        // Api
        wireMockServer.stubFor(get(urlPathMatching(".*/apis/1"))
                .withHeader(HttpHeaders.AUTHORIZATION, matching("Bearer " + jwtToken()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBody(writeValueAsString(createApiEntity()))));
        // Application
        wireMockServer.stubFor(get(urlMatching(".*/applications/1"))
                .withHeader(HttpHeaders.AUTHORIZATION, matching("Bearer " + jwtToken()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBody(writeValueAsString(createApplicationEntity()))));

        // Category
        wireMockServer.stubFor(get(urlPathMatching(".*/categories"))
                .withHeader(HttpHeaders.AUTHORIZATION, matching("Bearer " + jwtToken()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBody(writeValueAsString(createCategoryEntities()))));

        // Subscription
        wireMockServer.stubFor(get(urlPathMatching(".*/subscriptions/1"))
                .withHeader(HttpHeaders.AUTHORIZATION, matching("Bearer " + jwtToken()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBody(writeValueAsString(createSubscriptionEntity()))));
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

    private static String jwtToken() {
        return "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwYTgwN2ZiOC03ZjllLTRlZDctODA3Zi1iODdmOWU5ZWQ3NmYiLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5IjoiRU5WSVJPTk1FTlQ6VVNFUiJ9LHsiYXV0aG9yaXR5IjoiT1JHQU5JWkFUSU9OOlVTRVIifV0sImlzcyI6ImdyYXZpdGVlLW1hbmFnZW1lbnQtYXV0aCIsImV4cCI6MTY0NzUxMjYwNiwiaWF0IjoxNjQ2OTA3ODA2LCJlbWFpbCI6InVzZXIxQGV4YW1wbGUuY29tIiwianRpIjoiNjA5ZTc0NDktYzJmNC00MGE0LThjMzMtYjA4YmU1ZmE3MjIyIn0.m9RPdMG4NinA-xFky1Ml5wvY9m0weTPRJXuh-UoQfs0";
    }

    private static TokenEntity createTokenEntity() {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(jwtToken());
        return tokenEntity;
    }

    private static ApiEntity createApiEntity() {
        ApiEntity apiEntity = new ApiEntity();
        apiEntity.setId("1");
        apiEntity.setName("example-api");
        apiEntity.setCreatedAt(new Date());
        apiEntity.setUpdatedAt(new Date());
        return apiEntity;
    }

    private static ApplicationEntity createApplicationEntity() {
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setId("1");
        applicationEntity.setName("example-app");
        return applicationEntity;
    }

    private static DataResponse<CategoryEntity> createCategoryEntities() {
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId("1");
        categoryEntity.setName("example-category");
        categoryEntities.add(categoryEntity);
        DataResponse<CategoryEntity> dataResponse = new DataResponse<>();
        Map<String, Map<String, Object>> metaData = Collections.singletonMap(METADATA_DATA_KEY,
                Collections.singletonMap(METADATA_DATA_TOTAL_KEY, 1));
        dataResponse.metadata(metaData);
        dataResponse.data(categoryEntities);
        return dataResponse;
    }

    private static SubscriptionEntity createSubscriptionEntity() {
        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
        subscriptionEntity.setId("1");
        subscriptionEntity.setApi("api1");
        subscriptionEntity.setApplication("app1");
        return subscriptionEntity;
    }
}
