package org.javaloong.kongmink.open.service.core.internal;

import org.javaloong.kongmink.open.data.UserRepository;
import org.javaloong.kongmink.open.data.domain.UserEntity;
import org.javaloong.kongmink.open.service.UserConfigService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.transaction.control.TransactionControl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component(service = UserConfigService.class)
public class UserConfigServiceImpl implements UserConfigService {

    @Reference
    TransactionControl transactionControl;
    @Reference
    UserRepository userRepository;

    @Override
    public Map<String, Object> getConfig(String userId) {
        return transactionControl.supports(() ->
                userRepository.findById(userId).map(UserEntity::getConfigData)
                        .orElse(Collections.emptyMap()));
    }

    @Override
    public void updateConfig(String userId, Map<String, Object> configMap) {
        transactionControl.required(() -> {
            userRepository.findById(userId).ifPresent(entity -> {
                if (entity.getConfigData() == null) {
                    entity.setConfigData(new HashMap<>());
                }
                configMap.forEach(entity.getConfigData()::put);
                userRepository.update(entity);
            });
            return null;
        });
    }

    @Override
    public void setConfig(String userId, Map<String, Object> configMap) {
        transactionControl.required(() -> {
            userRepository.findById(userId).ifPresent(entity -> {
                entity.setConfigData(configMap);
                userRepository.update(entity);
            });
            return null;
        });
    }
}
