package org.javaloong.kongmink.open.core.service.impl;

import org.javaloong.kongmink.open.am.ClientProvider;
import org.javaloong.kongmink.open.apim.ApplicationProvider;
import org.javaloong.kongmink.open.apim.model.Application;
import org.javaloong.kongmink.open.apim.model.application.ApplicationSettings;
import org.javaloong.kongmink.open.common.application.ApplicationType;
import org.javaloong.kongmink.open.common.client.Client;
import org.javaloong.kongmink.open.common.client.ClientSecret;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.core.service.ApplicationService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ApplicationServiceImplIT extends AbstractServiceTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext context = FrameworkUtil.getBundle(ApplicationServiceImplIT.class).getBundleContext();
        context.registerService(ApplicationProvider.class, Mockito.mock(ApplicationProvider.class), null);
        context.registerService(ClientProvider.class, Mockito.mock(ClientProvider.class), null);
    }

    @Inject
    ApplicationProvider applicationProvider;
    @Inject
    ClientProvider clientProvider;
    @Inject
    ApplicationService applicationService;

    @Before
    public void setUp() {
        reset(applicationProvider);
        reset(clientProvider);
    }

    @Test
    public void testSimpleCRUD() {
        ApplicationType applicationType = ApplicationType.SIMPLE;
        ApplicationSettings applicationSettings = TestUtils.createApplicationSettings("client1", "WEB");
        Application application = TestUtils.createApplication("1", "application1", applicationType, applicationSettings);
        User user = TestUtils.createUser("1", "user1");
        when(applicationProvider.create(any(Application.class))).thenReturn(new Application());
        applicationService.create(user, application);
        ArgumentCaptor<Application> applicationArgumentCaptor = ArgumentCaptor.forClass(Application.class);
        verify(applicationProvider).create(applicationArgumentCaptor.capture());
        assertThat(applicationArgumentCaptor.getValue())
                .returns(ApplicationType.SIMPLE, Application::getApplicationType)
                .returns("client1", app -> app.getSettings().getApp().getClientId())
                .returns("WEB", app -> app.getSettings().getApp().getType());
        when(applicationProvider.findById(anyString())).thenReturn(Optional.of(application));
        when(clientProvider.findByClientId(anyString())).thenReturn(Optional.empty());
        application.setName("application11");
        application.getSettings().getApp().setClientId("client11");
        application.getSettings().getApp().setType("NATIVE");
        applicationService.update(application);
        verify(applicationProvider).update(applicationArgumentCaptor.capture());
        assertThat(applicationArgumentCaptor.getValue())
                .returns("application11", Application::getName)
                .returns("client11", app -> app.getSettings().getApp().getClientId())
                .returns("NATIVE", app -> app.getSettings().getApp().getType());
        applicationService.delete(application.getId());
        verify(applicationProvider).delete(application.getId());
    }

    @Test
    public void testOauthCRUD() {
        ApplicationType applicationType = ApplicationType.WEB;
        List<String> grantTypes = Collections.singletonList("authorization_code");
        List<String> redirectUris = Collections.singletonList("https://localhost/redirect");
        ApplicationSettings applicationSettings = TestUtils.createOAuthApplicationSettings(applicationType,
                "client1", "secret1", grantTypes, redirectUris);
        Application application = TestUtils.createApplication("1", "application1", applicationType, applicationSettings);
        User user = TestUtils.createUser("1", "user1");
        when(applicationProvider.create(any(Application.class))).thenReturn(new Application());
        applicationService.create(user, application);
        ArgumentCaptor<Application> applicationArgumentCaptor = ArgumentCaptor.forClass(Application.class);
        verify(applicationProvider).create(applicationArgumentCaptor.capture());
        assertThat(applicationArgumentCaptor.getValue())
                .returns(ApplicationType.WEB, Application::getApplicationType)
                .returns(grantTypes, app -> app.getSettings().getOauth().getGrantTypes())
                .returns(redirectUris, app -> app.getSettings().getOauth().getRedirectUris());
        when(applicationProvider.findById(anyString())).thenReturn(Optional.of(application));
        application.setName("application11");
        application.getSettings().getOauth().setGrantTypes(Collections.singletonList("client_credentials"));
        application.getSettings().getOauth().setRedirectUris(Collections.singletonList("https://localhost/redirect2"));
        applicationService.update(application);
        verify(applicationProvider).update(applicationArgumentCaptor.capture());
        assertThat(applicationArgumentCaptor.getValue())
                .returns("application11", Application::getName)
                .returns(Collections.singletonList("client_credentials"), app -> app.getSettings().getOauth().getGrantTypes())
                .returns(Collections.singletonList("https://localhost/redirect2"), app -> app.getSettings().getOauth().getRedirectUris());
        when(clientProvider.findByClientId(anyString())).thenReturn(Optional.empty());
        ClientSecret clientSecret = applicationService.getClientSecret(application.getId());
        assertThat(clientSecret)
                .returns("client1", ClientSecret::getClientId)
                .returns("secret1", ClientSecret::getSecret);
        assertThatThrownBy(() -> applicationService.regenerateClientSecret(application.getId()))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Not implemented yet.");
        applicationService.delete(application.getId());
        verify(applicationProvider).delete(application.getId());
    }

    @Test
    public void testSimpleOauthCRUD() {
        ApplicationType applicationType = ApplicationType.SIMPLE;
        List<String> grantTypes = Collections.singletonList("authorization_code");
        List<String> redirectUris = Collections.singletonList("https://localhost/redirect");
        ApplicationSettings applicationSettings = TestUtils.createOAuthApplicationSettings(ApplicationType.WEB,
                null, null, grantTypes, redirectUris);
        Application application = TestUtils.createApplication("1", "application1", applicationType, applicationSettings);
        User user = TestUtils.createUser("1", "user1");
        Client client = TestUtils.createClient("1", "client1", "application1");
        when(clientProvider.create(any(Client.class))).thenReturn(client);
        when(applicationProvider.create(any(Application.class))).thenReturn(new Application());
        applicationService.create(user, application);
        ArgumentCaptor<Client> clientArgumentCaptor = ArgumentCaptor.forClass(Client.class);
        verify(clientProvider).create(clientArgumentCaptor.capture());
        assertThat(clientArgumentCaptor.getValue())
                .returns("application1", Client::getName)
                .returns(grantTypes, Client::getGrantTypes)
                .returns(redirectUris, Client::getRedirectUris);
        ArgumentCaptor<Application> applicationArgumentCaptor = ArgumentCaptor.forClass(Application.class);
        verify(applicationProvider).create(applicationArgumentCaptor.capture());
        assertThat(applicationArgumentCaptor.getValue())
                .returns(ApplicationType.SIMPLE, Application::getApplicationType)
                .returns("client1", app -> app.getSettings().getApp().getClientId())
                .returns("WEB", app -> app.getSettings().getApp().getType());
        Application updateApplication = TestUtils.createApplication("1", "application1", applicationType,
                TestUtils.createApplicationSettings("client1", "WEB"));
        when(applicationProvider.findById(anyString())).thenReturn(Optional.of(updateApplication));
        Client updateClient = TestUtils.createClient("1", "client1", "application1");
        when(clientProvider.findByClientId(anyString())).thenReturn(Optional.of(updateClient));
        application.setName("application11");
        applicationSettings.getOauth().setGrantTypes(Collections.singletonList("client_credentials"));
        applicationSettings.getOauth().setRedirectUris(Collections.singletonList("https://localhost/redirect2"));
        application.setSettings(applicationSettings);
        applicationService.update(application);
        verify(clientProvider).update(clientArgumentCaptor.capture());
        assertThat(clientArgumentCaptor.getValue())
                .returns("application11", Client::getName)
                .returns(Collections.singletonList("client_credentials"), Client::getGrantTypes)
                .returns(Collections.singletonList("https://localhost/redirect2"), Client::getRedirectUris);
        verify(applicationProvider).update(applicationArgumentCaptor.capture());
        assertThat(applicationArgumentCaptor.getValue())
                .returns("application11", Application::getName)
                .returns("client1", app -> app.getSettings().getApp().getClientId())
                .returns("WEB", app -> app.getSettings().getApp().getType());
        when(clientProvider.getSecret(anyString())).thenReturn("secret1");
        ClientSecret clientSecret = applicationService.getClientSecret(updateApplication.getId());
        verify(clientProvider).getSecret(updateClient.getId());
        assertThat(clientSecret)
                .returns("client1", ClientSecret::getClientId)
                .returns("secret1", ClientSecret::getSecret);
        when(clientProvider.regenerateSecret(anyString())).thenReturn("secret2");
        clientSecret = applicationService.regenerateClientSecret(updateApplication.getId());
        verify(clientProvider).regenerateSecret(updateClient.getId());
        assertThat(clientSecret)
                .returns("client1", ClientSecret::getClientId)
                .returns("secret2", ClientSecret::getSecret);
        applicationService.delete(updateApplication.getId());
        verify(clientProvider).delete(updateClient.getId());
        verify(applicationProvider).delete(updateApplication.getId());
    }

    @Test
    public void testFindAll() {
        User user = TestUtils.createUser("1", "user1");
        when(applicationProvider.findAll(anyInt(), anyInt())).thenReturn(new Page<>());
        applicationService.findAll(user, 1, 10);
        verify(applicationProvider).findAll(1, 10);
    }
}
