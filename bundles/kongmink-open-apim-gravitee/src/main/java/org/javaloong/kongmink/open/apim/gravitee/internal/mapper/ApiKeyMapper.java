package org.javaloong.kongmink.open.apim.gravitee.internal.mapper;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.Key;
import org.javaloong.kongmink.open.apim.model.ApiKey;

public class ApiKeyMapper {

    public static ApiKey mapToApiKey(Key key) {
        return BeanMapper.map(key, ApiKey.class);
    }
}
