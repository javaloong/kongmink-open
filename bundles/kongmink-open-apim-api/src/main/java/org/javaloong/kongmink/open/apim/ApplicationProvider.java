package org.javaloong.kongmink.open.apim;

import org.javaloong.kongmink.open.common.model.ApiKey;
import org.javaloong.kongmink.open.common.model.Application;
import org.javaloong.kongmink.open.common.model.ApplicationLog;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.model.analytics.Analytics;
import org.javaloong.kongmink.open.common.model.analytics.query.AnalyticsQuery;
import org.javaloong.kongmink.open.common.model.log.query.LogQuery;

import java.util.Optional;

public interface ApplicationProvider {

    Optional<Application> findById(String id);

    Application create(Application application);

    void update(Application application);

    void delete(String id);

    Page<Application> findAll(int page, int size);

    ApplicationLog getLog(String applicationId, String logId, Long timestamp);

    Page<ApplicationLog> getLogs(String applicationId, LogQuery logQuery, int page, int size);

    <T extends Analytics> T getAnalytics(
            String applicationId, AnalyticsQuery analyticsQuery, Class<T> analyticsClass);

    ApiKey renewSharedKey(String applicationId);

    void revokeKey(String applicationId, String apiKey);
}
