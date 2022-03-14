package org.javaloong.kongmink.open.apim.gravitee.internal;

import org.javaloong.kongmink.open.apim.ApplicationProvider;
import org.javaloong.kongmink.open.apim.gravitee.internal.mapper.ApplicationMapper;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.ApplicationEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.NewApplicationEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.ApplicationResource;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.ApplicationsResource;
import org.javaloong.kongmink.open.apim.model.Application;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.ws.rs.NotFoundException;
import java.util.Optional;

@Component(service = ApplicationProvider.class)
public class GraviteeApplicationProvider implements ApplicationProvider {

    private final GraviteePortalClient client;

    @Activate
    public GraviteeApplicationProvider(@Reference GraviteePortalClient client) {
        this.client = client;
    }

    @Override
    public Optional<Application> findById(String id) {
        ApplicationResource applicationResource = getApplicationResource(id);
        return getApplicationEntity(applicationResource).map(ApplicationMapper::mapToApplication);
    }

    @Override
    public Application create(Application application) {
        ApplicationsResource applicationsResource = client.getApplicationsResource();
        NewApplicationEntity newApplicationEntity = ApplicationMapper.mapToNewApplicationEntity(application);
        ApplicationEntity applicationEntity = applicationsResource.createApplication(newApplicationEntity);
        return ApplicationMapper.mapToApplication(applicationEntity);
    }

    @Override
    public void update(Application application) {
        ApplicationResource applicationResource = getApplicationResource(application.getId());
        ApplicationEntity applicationEntity = applicationResource.getApplicationByApplicationId();
        ApplicationMapper.map(application, applicationEntity);
        applicationResource.updateApplicationByApplicationId(applicationEntity);
    }

    @Override
    public void delete(String id) {
        ApplicationResource applicationResource = getApplicationResource(id);
        applicationResource.deleteApplicationByApplicationId();
    }

    private ApplicationResource getApplicationResource(String id) {
        return client.getApplicationsResource().getApplicationResource(id);
    }

    private Optional<ApplicationEntity> getApplicationEntity(ApplicationResource resource) {
        try {
            return Optional.ofNullable(resource.getApplicationByApplicationId());
        } catch (NotFoundException ex) {
            return Optional.empty();
        }
    }
}
