package org.javaloong.kongmink.open.apim.gravitee.internal.mapper;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.ApplicationEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.LogEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.NewApplicationEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.DataResponse;
import org.javaloong.kongmink.open.common.model.Application;
import org.javaloong.kongmink.open.common.model.ApplicationLog;
import org.javaloong.kongmink.open.common.model.Page;
import org.modelmapper.TypeToken;

import java.util.Collection;

public class ApplicationMapper {

    public static NewApplicationEntity mapToNewApplicationEntity(Application application) {
        return BeanMapper.map(application, NewApplicationEntity.class);
    }

    public static Application mapToApplication(ApplicationEntity applicationEntity) {
        return BeanMapper.map(applicationEntity, Application.class);
    }

    public static void map(Application application, ApplicationEntity applicationEntity) {
        BeanMapper.map(application, applicationEntity);
    }

    public static Page<Application> mapToPaginationApplications(DataResponse<?> dataResponse) {
        TypeToken<Collection<Application>> typeToken = new TypeToken<>() {
        };
        Collection<Application> data = BeanMapper.map(dataResponse.getData(), typeToken.getType());
        return new Page<>(data, dataResponse.getPaginationTotal());
    }

    public static ApplicationLog mapToLog(LogEntity logEntity) {
        return BeanMapper.map(logEntity, ApplicationLog.class);
    }

    public static Page<ApplicationLog> mapToPaginationLogs(DataResponse<?> dataResponse) {
        TypeToken<Collection<ApplicationLog>> typeToken = new TypeToken<>() {
        };
        Collection<ApplicationLog> data = BeanMapper.map(dataResponse.getData(), typeToken.getType());
        return new Page<>(data, dataResponse.getDataTotal());
    }
}
