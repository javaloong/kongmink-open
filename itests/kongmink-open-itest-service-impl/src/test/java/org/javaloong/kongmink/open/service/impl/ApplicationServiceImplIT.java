package org.javaloong.kongmink.open.service.impl;

import org.javaloong.kongmink.open.apim.ApplicationProvider;
import org.javaloong.kongmink.open.apim.model.Application;
import org.javaloong.kongmink.open.apim.model.application.ApplicationSettings;
import org.javaloong.kongmink.open.common.application.ApplicationType;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.service.ApplicationService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ApplicationServiceImplIT extends AbstractServiceTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext context = FrameworkUtil.getBundle(ApplicationServiceImplIT.class).getBundleContext();
        context.registerService(ApplicationProvider.class, Mockito.mock(ApplicationProvider.class), null);
    }

    @Inject
    ApplicationProvider applicationProvider;
    @Inject
    ApplicationService applicationService;

    @Test
    public void testCRUD() {
        ApplicationType applicationType = ApplicationType.WEB;
        List<String> grantTypes = Collections.singletonList("authorization_code");
        List<String> redirectUris = Collections.singletonList("https://localhost/redirect");
        ApplicationSettings applicationSettings = TestUtils.createOAuthApplicationSettings(applicationType, grantTypes, redirectUris);
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
        applicationService.delete(application.getId());
        verify(applicationProvider).delete(application.getId());
        when(applicationProvider.findAll(anyInt(), anyInt())).thenReturn(new Page<>());
        applicationService.findAll(user, 1, 10);
        verify(applicationProvider).findAll(1, 10);
    }
}
