package org.javaloong.kongmink.open.service;

import org.javaloong.kongmink.open.apim.model.ApiKey;
import org.javaloong.kongmink.open.apim.model.Subscription;
import org.javaloong.kongmink.open.apim.model.SubscriptionStatus;
import org.javaloong.kongmink.open.common.model.Page;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {

    Optional<Subscription> findById(String id);

    Page<Subscription> findAll(String apiId, String applicationId,
                               List<SubscriptionStatus> statuses, int page, int size);

    Subscription create(Subscription subscription);

    void close(String id);

    ApiKey renewKey(String subscriptionId);

    void revokeKey(String subscriptionId, String apiKey);
}
