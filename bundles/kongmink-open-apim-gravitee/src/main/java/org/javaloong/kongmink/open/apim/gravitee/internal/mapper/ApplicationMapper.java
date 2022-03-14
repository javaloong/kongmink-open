package org.javaloong.kongmink.open.apim.gravitee.internal.mapper;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.ApplicationEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.NewApplicationEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.application.ApplicationSettings;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.application.SimpleApplicationSettings;
import org.javaloong.kongmink.open.apim.model.Application;

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

    private static ApplicationSettings getApplicationSettings(Application application) {
        ApplicationSettings settings = new ApplicationSettings();
        SimpleApplicationSettings simpleSettings = new SimpleApplicationSettings();
        simpleSettings.setType(application.getType());
        simpleSettings.setClientId(application.getClientId());
        settings.setApp(simpleSettings);
        return settings;
    }
}
