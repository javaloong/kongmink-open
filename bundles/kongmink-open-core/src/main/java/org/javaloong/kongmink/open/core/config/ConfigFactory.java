package org.javaloong.kongmink.open.core.config;

public interface ConfigFactory {

    ConfigProperties getConfig();

    ConfigProperties getConfig(String userId);
}
