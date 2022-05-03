package org.javaloong.kongmink.open.apim.gravitee.internal.mapper;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.KeyEntity;
import org.javaloong.kongmink.open.apim.model.ApiKey;

public class ApiKeyMapper {

    public static ApiKey mapToApiKey(KeyEntity keyEntity) {
        return BeanMapper.map(keyEntity, ApiKey.class);
    }
}
