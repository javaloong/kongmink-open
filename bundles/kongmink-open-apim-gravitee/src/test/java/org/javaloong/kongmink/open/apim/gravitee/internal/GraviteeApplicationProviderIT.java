package org.javaloong.kongmink.open.apim.gravitee.internal;

import org.javaloong.kongmink.open.apim.ApplicationProvider;
import org.javaloong.kongmink.open.apim.model.Application;
import org.javaloong.kongmink.open.common.client.ClientType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.ws.rs.ForbiddenException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Disabled
public class GraviteeApplicationProviderIT extends GraviteePortalClientTestSupport {

    private ApplicationProvider applicationProvider;

    @BeforeEach
    public void setUp() {
        applicationProvider = new GraviteeApplicationProvider(createPortalClient());
    }

    @Test
    public void findById() {
        String id = "e0e75fa9-3fd7-4584-a75f-a93fd7558400";
        Optional<Application> result = applicationProvider.findById(id);
        assertThat(result).hasValueSatisfying(app -> {
            assertThat(app)
                    .returns("Default application", Application::getName);
        });
    }

    @Test
    public void create() {
        Application application = applicationProvider.create(createApplication());
        assertThat(application)
                .returns("test-resource-server", Application::getClientId)
                .extracting(Application::getId).isNotNull();
    }

    @Test
    public void update() {
        Application application = new Application();
        application.setId("c5e00d43-ba8a-4422-a00d-43ba8a64221b");
        application.setName("test2");
        application.setType(ClientType.WEB.getValue());
        applicationProvider.update(application);
        Optional<Application> result = applicationProvider.findById(application.getId());
        assertThat(result).hasValueSatisfying(app -> {
            assertThat(app)
                    .returns("test2", Application::getName)
                    .returns(ClientType.WEB.getValue(), Application::getType);
        });
    }

    @Test
    public void delete() {
        String id = "c5e00d43-ba8a-4422-a00d-43ba8a64221b";
        applicationProvider.delete(id);
        assertThatThrownBy(() -> applicationProvider.findById(id))
                .isInstanceOf(ForbiddenException.class);
    }

    private Application createApplication() {
        Application application = new Application();
        application.setName("test");
        application.setDescription("test description");
        application.setClientId("test-resource-server");
        application.setType(ClientType.SERVICE.getValue());
        return application;
    }
}
