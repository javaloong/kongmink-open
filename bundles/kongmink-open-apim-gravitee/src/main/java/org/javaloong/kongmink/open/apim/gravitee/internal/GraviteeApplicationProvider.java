package org.javaloong.kongmink.open.apim.gravitee.internal;

import org.javaloong.kongmink.open.apim.ApplicationProvider;
import org.javaloong.kongmink.open.apim.gravitee.internal.mapper.ApiKeyMapper;
import org.javaloong.kongmink.open.apim.gravitee.internal.mapper.ApplicationMapper;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.ApplicationEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.KeyEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.LogEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.NewApplicationEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.*;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.param.AnalyticsParam;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.param.LogsParam;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.param.PaginationParam;
import org.javaloong.kongmink.open.common.model.ApiKey;
import org.javaloong.kongmink.open.common.model.Application;
import org.javaloong.kongmink.open.common.model.ApplicationLog;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.model.analytics.Analytics;
import org.javaloong.kongmink.open.common.model.analytics.query.AnalyticsQuery;
import org.javaloong.kongmink.open.common.model.log.query.LogQuery;
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

    @Override
    public Page<Application> findAll(int page, int size) {
        PaginationParam paginationParam = createPaginationParam(page, size);
        DataResponse<ApplicationEntity> dataResponse = client.getApplicationsResource().getApplications(
                paginationParam, false, null);
        return ApplicationMapper.mapToPaginationApplications(dataResponse);
    }

    @Override
    public ApplicationLog getLog(String applicationId, String logId, Long timestamp) {
        ApplicationLogsResource applicationLogsResource = getApplicationResource(applicationId)
                .getApplicationLogsResource();
        LogEntity logEntity = applicationLogsResource.applicationLog(logId, timestamp);
        return ApplicationMapper.mapToLog(logEntity);
    }

    @Override
    public Page<ApplicationLog> getLogs(String applicationId, LogQuery logQuery, int page, int size) {
        ApplicationLogsResource applicationLogsResource = getApplicationResource(applicationId)
                .getApplicationLogsResource();
        PaginationParam paginationParam = createPaginationParam(page, size);
        LogsParam logsParam = createLogsParam(logQuery);
        DataResponse<LogEntity> dataResponse = applicationLogsResource.applicationLogs(
                paginationParam, logsParam);
        return ApplicationMapper.mapToPaginationLogs(dataResponse);
    }

    @Override
    public <T extends Analytics> T getAnalytics(String applicationId, AnalyticsQuery analyticsQuery,
                                                Class<T> analyticsClass) {
        ApplicationAnalyticsResource applicationAnalyticsResource = getApplicationResource(applicationId)
                .getApplicationAnalyticsResource();
        AnalyticsParam analyticsParam = createAnalyticsParam(analyticsQuery);
        return applicationAnalyticsResource.hits(analyticsParam)
                .readEntity(analyticsClass);
    }

    @Override
    public ApiKey renewSharedKey(String applicationId) {
        ApplicationKeysResource applicationKeysResource = getApplicationResource(applicationId)
                .getApplicationKeysResource();
        KeyEntity keyEntity = applicationKeysResource.renewSharedKey();
        return ApiKeyMapper.mapToApiKey(keyEntity);
    }

    @Override
    public void revokeKey(String applicationId, String apiKey) {
        ApplicationKeysResource applicationKeysResource = getApplicationResource(applicationId)
                .getApplicationKeysResource();
        applicationKeysResource.revokeKeySubscription(apiKey);
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

    private LogsParam createLogsParam(LogQuery logQuery) {
        LogsParam logsParam = new LogsParam();
        logsParam.setFrom(logQuery.getFrom());
        logsParam.setTo(logQuery.getTo());
        logsParam.setQuery(logQuery.getQuery());
        logsParam.setField(logQuery.getField());
        logsParam.setOrder(logQuery.getOrder());
        return logsParam;
    }

    private AnalyticsParam createAnalyticsParam(AnalyticsQuery analyticsQuery) {
        AnalyticsParam analyticsParam = new AnalyticsParam();
        analyticsParam.setFrom(analyticsQuery.getFrom());
        analyticsParam.setTo(analyticsQuery.getTo());
        analyticsParam.setInterval(analyticsQuery.getInterval());
        analyticsParam.setQuery(analyticsQuery.getQuery());
        analyticsParam.setField(analyticsParam.getField());
        analyticsParam.setType(analyticsQuery.getType().name());
        analyticsParam.setRanges(analyticsQuery.getRanges());
        analyticsParam.setAggs(analyticsQuery.getAggregations());
        analyticsParam.setOrder(analyticsQuery.getOrder());
        return analyticsParam;
    }

    private PaginationParam createPaginationParam(int page, int size) {
        PaginationParam paginationParam = new PaginationParam();
        paginationParam.setPage(page);
        paginationParam.setSize(size);
        return paginationParam;
    }
}
