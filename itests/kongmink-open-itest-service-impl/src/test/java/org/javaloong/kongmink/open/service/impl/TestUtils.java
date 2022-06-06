package org.javaloong.kongmink.open.service.impl;

import org.javaloong.kongmink.open.apim.model.Api;
import org.javaloong.kongmink.open.apim.model.Application;
import org.javaloong.kongmink.open.apim.model.Category;
import org.javaloong.kongmink.open.apim.model.application.ApplicationSettings;
import org.javaloong.kongmink.open.apim.model.application.OAuthClientSettings;
import org.javaloong.kongmink.open.apim.model.application.SimpleApplicationSettings;
import org.javaloong.kongmink.open.common.application.ApplicationType;
import org.javaloong.kongmink.open.common.client.Client;
import org.javaloong.kongmink.open.common.user.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class TestUtils {

    public static Category createCategory(String id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;
    }

    public static Api createApi(String id, String name, String version, String category) {
        Api api = new Api();
        api.setId(id);
        api.setName(name);
        api.setVersion(version);
        api.setCategories(Collections.singletonList(category));
        return api;
    }

    public static Application createApplication(
            String id, String name, ApplicationType applicationType, ApplicationSettings settings) {
        Application application = new Application();
        application.setId(id);
        application.setName(name);
        application.setApplicationType(applicationType);
        application.setSettings(settings);
        return application;
    }

    public static ApplicationSettings createApplicationSettings(String clientId, String type) {
        ApplicationSettings applicationSettings = new ApplicationSettings();
        SimpleApplicationSettings simpleSettings = new SimpleApplicationSettings();
        simpleSettings.setClientId(clientId);
        simpleSettings.setType(type);
        applicationSettings.setApp(simpleSettings);
        return applicationSettings;
    }

    public static ApplicationSettings createOAuthApplicationSettings(
            ApplicationType applicationType, List<String> grantTypes, List<String> redirectUris) {
        ApplicationSettings applicationSettings = new ApplicationSettings();
        OAuthClientSettings oauthSettings = new OAuthClientSettings();
        oauthSettings.setApplicationType(applicationType.toString());
        oauthSettings.setGrantTypes(grantTypes);
        oauthSettings.setRedirectUris(redirectUris);
        applicationSettings.setOauth(oauthSettings);
        return applicationSettings;
    }

    public static Client createClient(String id, String name) {
        Client client = new Client();
        client.setId(id);
        client.setName(name);
        client.setClientId(UUID.randomUUID().toString());
        return client;
    }

    public static User createUser(String id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setCreatedTimestamp(LocalDateTime.now());
        return user;
    }
}
