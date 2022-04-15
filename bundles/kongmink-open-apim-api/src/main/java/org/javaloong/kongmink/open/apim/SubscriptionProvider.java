package org.javaloong.kongmink.open.apim;

import org.javaloong.kongmink.open.apim.model.ApiKey;
import org.javaloong.kongmink.open.apim.model.Subscription;
import org.javaloong.kongmink.open.apim.model.SubscriptionStatus;
import org.javaloong.kongmink.open.common.model.Page;

import java.util.List;
import java.util.Optional;

public interface SubscriptionProvider {

    Optional<Subscription> findById(String id);

    Page<Subscription> findAllByApi(String apiId, List<SubscriptionStatus> statuses,
                               int page, int size);

    Page<Subscription> findAllByApplication(String applicationId, List<SubscriptionStatus> statuses,
                               int page, int size);

    Page<Subscription> findAll(List<SubscriptionStatus> statuses, int page, int size);

    Subscription create(Subscription subscription);

    ApiKey renewKey(String subscriptionId);

    void revokeKey(String subscriptionId, String apiKey);
}
