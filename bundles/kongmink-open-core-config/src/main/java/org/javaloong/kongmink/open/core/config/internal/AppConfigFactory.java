package org.javaloong.kongmink.open.core.config.internal;

import org.javaloong.kongmink.open.core.config.ConfigProperties;
import org.javaloong.kongmink.open.core.config.ConfigFactory;
import org.javaloong.kongmink.open.data.UserRepository;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import java.util.HashMap;
import java.util.Map;

import static org.javaloong.kongmink.open.core.config.internal.AppConfigFactory.APP_CONFIG_FACTORY_PID;

@Component(service = ConfigFactory.class, configurationPid = APP_CONFIG_FACTORY_PID)
public class AppConfigFactory implements ConfigFactory {

    public static final String APP_CONFIG_FACTORY_PID = "org.javaloong.kongmink.open.core.config";

    private Map<String, Object> configMap;

    @Reference
    UserRepository userRepository;

    @Activate
    void activate(Map<String, Object> properties) {
        configMap = new HashMap<>(properties);
    }

    @Modified
    void modified(Map<String, Object> properties) {
        configMap = new HashMap<>(properties);
    }

    @Override
    public ConfigProperties getConfig() {
        return new ConfigProperties(configMap);
    }

    @Override
    public ConfigProperties getConfig(String userId) {
        Map<String, Object> userConfigMap = new HashMap<>(configMap);
        userRepository.findById(userId).ifPresent(user -> {
            if (user.getConfigData() != null) {
                user.getConfigData().forEach(userConfigMap::put);
            }
        });
        return new ConfigProperties(userConfigMap);
    }
}
