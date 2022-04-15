package org.javaloong.kongmink.open.apim;

import org.javaloong.kongmink.open.apim.model.Application;
import org.javaloong.kongmink.open.common.model.Page;

import java.util.Optional;

public interface ApplicationProvider {

    Optional<Application> findById(String id);

    Application create(Application application);

    void update(Application application);

    void delete(String id);

    Page<Application> findAll(int page, int size);
}
