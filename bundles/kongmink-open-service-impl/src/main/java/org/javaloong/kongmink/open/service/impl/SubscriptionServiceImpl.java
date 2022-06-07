package org.javaloong.kongmink.open.service.impl;

import org.javaloong.kongmink.open.apim.SubscriptionProvider;
import org.javaloong.kongmink.open.apim.model.ApiKey;
import org.javaloong.kongmink.open.apim.model.Subscription;
import org.javaloong.kongmink.open.apim.model.SubscriptionStatus;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.service.SubscriptionService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;
import java.util.Optional;

@Component(service = SubscriptionService.class)
public class SubscriptionServiceImpl implements SubscriptionService {

    @Reference
    SubscriptionProvider subscriptionProvider;

    @Override
    public Optional<Subscription> findById(String id) {
        return subscriptionProvider.findById(id);
    }

    @Override
    public Page<Subscription> findAll(String apiId, String applicationId,
                                      List<SubscriptionStatus> statuses, int page, int size) {
        return subscriptionProvider.findAll(apiId, applicationId, statuses, page, size);
    }

    @Override
    public Subscription create(Subscription subscription) {
        return subscriptionProvider.create(subscription);
    }

    @Override
    public void close(String id) {
        subscriptionProvider.close(id);
    }

    @Override
    public ApiKey renewKey(String subscriptionId) {
        return subscriptionProvider.renewKey(subscriptionId);
    }

    @Override
    public void revokeKey(String subscriptionId, String apiKey) {
        subscriptionProvider.revokeKey(subscriptionId, apiKey);
    }
}
