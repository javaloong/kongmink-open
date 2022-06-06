package org.javaloong.kongmink.open.service;

import org.javaloong.kongmink.open.apim.model.ApiKey;
import org.javaloong.kongmink.open.apim.model.Application;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.user.User;

import java.util.Optional;

public interface ApplicationService {

    Optional<Application> findById(String id);

    Application create(User user, Application application);

    void update(Application application);

    void delete(String id);

    Page<Application> findAll(User user, int page, int size);

    ApiKey renewSharedKey(String applicationId);

    void revokeKey(String applicationId, String apiKey);
}
