package org.javaloong.kongmink.open.apim.gravitee.internal.mapper;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.ApplicationEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.NewApplicationEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.application.ApplicationSettings;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.application.SimpleApplicationSettings;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.DataResponse;
import org.javaloong.kongmink.open.apim.model.Application;
import org.javaloong.kongmink.open.common.model.Page;
import org.modelmapper.TypeToken;

import java.util.Collection;

public class ApplicationMapper {

    public static NewApplicationEntity mapToNewApplicationEntity(Application application) {
        NewApplicationEntity newApplicationEntity = BeanMapper.map(application, NewApplicationEntity.class);
        newApplicationEntity.setSettings(getApplicationSettings(application));
        return newApplicationEntity;
    }

    public static Application mapToApplication(ApplicationEntity applicationEntity) {
        return BeanMapper.map(applicationEntity, Application.class);
    }

    public static void map(Application application, ApplicationEntity applicationEntity) {
        BeanMapper.map(application, applicationEntity);
        applicationEntity.setSettings(getApplicationSettings(application));
    }

    public static Page<Application> mapToPaginationApplications(DataResponse<?> dataResponse) {
        TypeToken<Collection<Application>> typeToken = new TypeToken<>() {
        };
        Collection<Application> data = BeanMapper.map(dataResponse.getData(), typeToken.getType());
        return new Page<>(data, dataResponse.getPaginationTotal());
    }

    private static ApplicationSettings getApplicationSettings(Application application) {
        ApplicationSettings settings = new ApplicationSettings();
        SimpleApplicationSettings simpleSettings = new SimpleApplicationSettings();
        simpleSettings.setClientId(application.getClientId());
        simpleSettings.setType(application.getClientType());
        settings.setApp(simpleSettings);
        return settings;
    }
}
