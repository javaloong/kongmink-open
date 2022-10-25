package org.javaloong.kongmink.open.service;

import java.util.Map;

public interface UserService {

    Map<String, Object> getConfig(String userId);

    void updateConfig(String userId, Map<String, Object> configMap);

    void setConfig(String userId, Map<String, Object> configMap);
}
