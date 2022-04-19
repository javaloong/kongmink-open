package org.javaloong.kongmink.open.apim;

import org.javaloong.kongmink.open.apim.model.Application;
import org.javaloong.kongmink.open.apim.model.analytics.Analytics;
import org.javaloong.kongmink.open.apim.model.analytics.query.AnalyticsQuery;
import org.javaloong.kongmink.open.common.model.Page;

import java.util.Optional;

public interface ApplicationProvider {

    <T extends Analytics> T getAnalytics(String applicationId, AnalyticsQuery analyticsQuery,
                                         Class<T> analyticsClass);

    Optional<Application> findById(String id);

    Application create(Application application);

    void update(Application application);

    void delete(String id);

    Page<Application> findAll(int page, int size);
}
