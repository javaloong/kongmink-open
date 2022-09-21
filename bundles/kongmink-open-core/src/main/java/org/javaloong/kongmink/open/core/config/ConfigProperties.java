package org.javaloong.kongmink.open.core.config;

import java.util.HashMap;
import java.util.Map;

public class ConfigProperties extends HashMap<String, Object> {

    public ConfigProperties(Map<String, Object> m) {
        super(m);
    }

    public <V> V get(String key, Class<V> type) {
        return type.cast(get(key));
    }

    public <V> V getOrDefault(String key, V defaultValue, Class<V> type) {
        return type.cast(getOrDefault(key, defaultValue));
    }
}
