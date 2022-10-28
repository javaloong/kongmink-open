package org.javaloong.kongmink.open.core.config.internal;

import org.javaloong.kongmink.open.core.config.ConfigManager;
import org.javaloong.kongmink.open.core.config.ConfigProperties;
import org.javaloong.kongmink.open.data.repository.UserRepository;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.transaction.control.TransactionControl;

import java.util.HashMap;
import java.util.Map;

@Component(service = ConfigManager.class, configurationPid = ConfigManagerImpl.CONFIGURATION_PID)
public class ConfigManagerImpl implements ConfigManager {

    public static final String CONFIGURATION_PID = "org.javaloong.kongmink.open.config";

    private Map<String, Object> configMap;

    @Reference
    TransactionControl transactionControl;
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
        return transactionControl.supports(() -> {
            Map<String, Object> userConfigMap = new HashMap<>(configMap);
            userRepository.findById(userId).ifPresent(user -> {
                if (user.getConfigData() != null) {
                    user.getConfigData().forEach(userConfigMap::put);
                }
            });
            return new ConfigProperties(userConfigMap);
        });
    }
}
