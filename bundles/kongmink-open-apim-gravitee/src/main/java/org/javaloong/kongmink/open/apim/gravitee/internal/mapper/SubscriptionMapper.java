package org.javaloong.kongmink.open.apim.gravitee.internal.mapper;

import org.javaloong.kongmink.open.apim.model.Subscription;
import org.javaloong.kongmink.open.apim.gravitee.internal.mapper.BeanMapper;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.NewSubscriptionEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.SubscriptionEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.DataResponse;
import org.javaloong.kongmink.open.common.model.Page;
import org.modelmapper.TypeToken;

import java.util.Collection;

public class SubscriptionMapper {

    public static NewSubscriptionEntity mapToNewSubscriptionEntity(Subscription subscription) {
        return BeanMapper.map(subscription, NewSubscriptionEntity.class);
    }

    public static Subscription mapToSubscription(SubscriptionEntity subscriptionEntity) {
        return BeanMapper.map(subscriptionEntity, Subscription.class);
    }

    public static Page<Subscription> mapToPaginationSubscriptions(DataResponse<?> dataResponse) {
        TypeToken<Collection<Subscription>> typeToken = new TypeToken<>() {
        };
        Collection<Subscription> data = BeanMapper.map(dataResponse.getData(), typeToken.getType());
        return new Page<>(data, dataResponse.getPaginationTotal());
    }
}
