package org.javaloong.kongmink.open.service.impl;

import org.javaloong.kongmink.open.apim.ApplicationProvider;
import org.javaloong.kongmink.open.apim.model.ApiKey;
import org.javaloong.kongmink.open.apim.model.Application;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.service.ApplicationService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Optional;

@Component(service = ApplicationService.class)
public class ApplicationServiceImpl implements ApplicationService {

    @Reference
    ApplicationProvider applicationProvider;

    @Override
    public Optional<Application> findById(String id) {
        return applicationProvider.findById(id);
    }

    @Override
    public Application create(User user, Application application) {
        return applicationProvider.create(application);
    }

    @Override
    public void update(Application application) {
        applicationProvider.findById(application.getId()).ifPresent(updateApplication -> {
            updateApplication.setName(application.getName());
            updateApplication.setDescription(application.getDescription());
            updateApplication.setPicture(application.getPicture());
            updateApplication.setSettings(application.getSettings());
            applicationProvider.update(updateApplication);
        });
    }

    @Override
    public void delete(String id) {
        applicationProvider.delete(id);
    }

    @Override
    public Page<Application> findAll(User user, int page, int size) {
        return applicationProvider.findAll(page, size);
    }

    @Override
    public ApiKey renewSharedKey(String applicationId) {
        return applicationProvider.renewSharedKey(applicationId);
    }

    @Override
    public void revokeKey(String applicationId, String apiKey) {
        applicationProvider.revokeKey(applicationId, apiKey);
    }
}
