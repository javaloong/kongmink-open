package org.javaloong.kongmink.open.apim.gravitee.internal.mapper;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.ApiEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.DataResponse;
import org.javaloong.kongmink.open.apim.model.Api;
import org.javaloong.kongmink.open.apim.model.Plan;
import org.javaloong.kongmink.open.common.model.Page;
import org.modelmapper.TypeToken;

import java.util.Collection;

public class ApiMapper {

    public static Page<Plan> mapToPaginationPlans(DataResponse<?> dataResponse) {
        TypeToken<Collection<Plan>> typeToken = new TypeToken<>() {
        };
        Collection<Plan> data = BeanMapper.map(dataResponse.getData(), typeToken.getType());
        return new Page<>(data, dataResponse.getPaginationTotal());
    }

    public static Api mapToApi(ApiEntity apiEntity) {
        return BeanMapper.map(apiEntity, Api.class);
    }

    public static Page<Api> mapToPaginationApis(DataResponse<?> dataResponse) {
        TypeToken<Collection<Api>> typeToken = new TypeToken<>() {
        };
        Collection<Api> data = BeanMapper.map(dataResponse.getData(), typeToken.getType());
        return new Page<>(data, dataResponse.getPaginationTotal());
    }
}
