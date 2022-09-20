package org.javaloong.kongmink.open.core.service;

import java.util.Map;

public interface UserConfigService {

    Map<String, Object> getConfig(String userId);

    void updateConfig(String userId, Map<String, Object> configMap);

    void setConfig(String userId, Map<String, Object> configMap);
}
