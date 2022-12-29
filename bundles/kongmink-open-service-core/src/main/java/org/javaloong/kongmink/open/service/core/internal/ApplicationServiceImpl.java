package org.javaloong.kongmink.open.service.core.internal;

import org.javaloong.kongmink.open.am.ClientProvider;
import org.javaloong.kongmink.open.apim.ApplicationProvider;
import org.javaloong.kongmink.open.common.model.*;
import org.javaloong.kongmink.open.common.model.application.ApplicationSettings;
import org.javaloong.kongmink.open.common.model.application.ApplicationType;
import org.javaloong.kongmink.open.common.model.application.OAuthClientSettings;
import org.javaloong.kongmink.open.common.model.application.SimpleApplicationSettings;
import org.javaloong.kongmink.open.service.ApplicationService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Optional;

@Component(service = ApplicationService.class)
public class ApplicationServiceImpl implements ApplicationService {

    @Reference
    ApplicationProvider applicationProvider;
    @Reference
    ClientProvider clientProvider;

    @Override
    public Optional<Application> findById(String id) {
        return applicationProvider.findById(id).map(application -> {
            if (application.getApplicationType() == ApplicationType.SIMPLE) {
                String clientId = application.getSettings().getApp().getClientId();
                clientProvider.findById(clientId).ifPresent(client -> {
                    ApplicationSettings applicationSettings = createApplicationSettings(application, client);
                    application.setSettings(applicationSettings);
                });
            }
            return application;
        });
    }

    @Override
    public Application create(User user, Application application) {
        if (application.getApplicationType() == ApplicationType.SIMPLE && application.getSettings().getOauth() != null) {
            Client client = createClient(application);
            ApplicationSettings applicationSettings = createApplicationSettings(client.getClientId(),
                    application.getSettings().getOauth().getApplicationType());
            application.setSettings(applicationSettings);
        }
        return applicationProvider.create(application);
    }

    @Override
    public void update(Application application) {
        applicationProvider.findById(application.getId()).ifPresent(updateApplication -> {
            if (updateApplication.getSettings().getApp() != null) {
                if (application.getApplicationType() == ApplicationType.SIMPLE && application.getSettings().getOauth() != null) {
                    String clientId = updateApplication.getSettings().getApp().getClientId();
                    clientProvider.findById(clientId).ifPresent(updateClient -> {
                        updateClient(application, updateClient);
                        ApplicationSettings applicationSettings = createApplicationSettings(updateClient.getClientId(),
                                application.getSettings().getOauth().getApplicationType());
                        application.setSettings(applicationSettings);
                    });
                }
            }
            updateApplication.setName(application.getName());
            updateApplication.setDescription(application.getDescription());
            updateApplication.setPicture(application.getPicture());
            if (application.getSettings() != null) {
                updateApplication.setSettings(application.getSettings());
            }
            applicationProvider.update(updateApplication);
        });
    }

    @Override
    public void delete(String id) {
        applicationProvider.findById(id).ifPresent(application -> {
            if (application.getSettings().getApp() != null) {
                String clientId = application.getSettings().getApp().getClientId();
                clientProvider.delete(clientId);
            }
            applicationProvider.delete(id);
        });
    }

    @Override
    public Page<Application> findAll(User user, int page, int size) {
        return applicationProvider.findAll(page, size);
    }

    @Override
    public ClientSecret getClientSecret(String applicationId) {
        return applicationProvider.findById(applicationId).map(application -> findClient(application)
                .map(client -> new ClientSecret(client.getClientId(), clientProvider.getSecret(client.getClientId())))
                .orElseGet(() -> createClientSecret(application.getSettings().getOauth()))).orElse(null);
    }

    @Override
    public ClientSecret regenerateClientSecret(String applicationId) {
        return applicationProvider.findById(applicationId).map(application -> findClient(application)
                .map(client -> new ClientSecret(client.getClientId(), clientProvider.regenerateSecret(client.getClientId())))
                .orElseThrow(() -> new UnsupportedOperationException("Not implemented yet."))).orElse(null);
    }

    @Override
    public ApiKey renewSharedKey(String applicationId) {
        return applicationProvider.renewSharedKey(applicationId);
    }

    @Override
    public void revokeKey(String applicationId, String apiKey) {
        applicationProvider.revokeKey(applicationId, apiKey);
    }

    private Client createClient(Application application) {
        Client client = new Client();
        client.setClientName(application.getName());
        client.setGrantTypes(application.getSettings().getOauth().getGrantTypes());
        client.setRedirectUris(application.getSettings().getOauth().getRedirectUris());
        return clientProvider.create(client);
    }

    private void updateClient(Application application, Client client) {
        client.setClientName(application.getName());
        client.setGrantTypes(application.getSettings().getOauth().getGrantTypes());
        client.setRedirectUris(application.getSettings().getOauth().getRedirectUris());
        clientProvider.update(client);
    }

    private ApplicationSettings createApplicationSettings(String clientId, String type) {
        ApplicationSettings applicationSettings = new ApplicationSettings();
        SimpleApplicationSettings simpleApp = new SimpleApplicationSettings();
        simpleApp.setClientId(clientId);
        simpleApp.setType(type);
        applicationSettings.setApp(simpleApp);
        return applicationSettings;
    }

    private ApplicationSettings createApplicationSettings(Application application, Client client) {
        ApplicationSettings applicationSettings = new ApplicationSettings();
        OAuthClientSettings oauthSettings = new OAuthClientSettings();
        oauthSettings.setApplicationType(application.getSettings().getApp().getType());
        oauthSettings.setGrantTypes(client.getGrantTypes());
        oauthSettings.setRedirectUris(client.getRedirectUris());
        oauthSettings.setClientId(client.getClientId());
        oauthSettings.setClientSecret(client.getClientSecret());
        applicationSettings.setOauth(oauthSettings);
        return applicationSettings;
    }

    private Optional<Client> findClient(Application application) {
        if (application.getSettings().getApp() != null) {
            String clientId = application.getSettings().getApp().getClientId();
            return clientProvider.findById(clientId);
        }
        return Optional.empty();
    }

    private ClientSecret createClientSecret(OAuthClientSettings oauthSettings) {
        return oauthSettings != null ? new ClientSecret(oauthSettings.getClientId(),
                oauthSettings.getClientSecret()) : null;
    }
}
