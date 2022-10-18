package org.javaloong.kongmink.open.core.config;

public interface ConfigManager {

    ConfigProperties getConfig();

    ConfigProperties getConfig(String userId);
}
