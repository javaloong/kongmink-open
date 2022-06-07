package org.javaloong.kongmink.open.apim.gravitee.internal;

import org.javaloong.kongmink.open.apim.SubscriptionProvider;
import org.javaloong.kongmink.open.apim.model.ApiKey;
import org.javaloong.kongmink.open.apim.model.Subscription;
import org.javaloong.kongmink.open.apim.model.SubscriptionStatus;
import org.javaloong.kongmink.open.common.model.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
public class GraviteeSubscriptionProviderIT extends GraviteePortalClientTestSupport {

    private SubscriptionProvider subscriptionProvider;

    @BeforeEach
    public void setUp() {
        subscriptionProvider = new GraviteeSubscriptionProvider(createPortalClient());
    }

    @Test
    public void findById() {
        String id = "846157c5-d6fc-440d-a157-c5d6fc740ddb";
        Optional<Subscription> result = subscriptionProvider.findById(id);
        assertThat(result).hasValueSatisfying(subscription -> {
            assertThat(subscription.getApi()).isEqualTo("167f6b66-da50-4c39-bf6b-66da505c391a");
        });
    }

    @Test
    public void findAllByApi() {
        String apiId = "167f6b66-da50-4c39-bf6b-66da505c391a";
        List<SubscriptionStatus> statuses = Arrays.asList(SubscriptionStatus.ACCEPTED,
                SubscriptionStatus.PENDING, SubscriptionStatus.PAUSED);
        Page<Subscription> result = subscriptionProvider.findAll(apiId, null, statuses, 1, 10);
        assertThat(result.getTotalCount()).isEqualTo(1);
        assertThat(result.getData()).hasSize(1)
                .extracting(Subscription::getApi).isNotEmpty();
    }

    @Test
    public void findAllByApplication() {
        String applicationId = "e0e75fa9-3fd7-4584-a75f-a93fd7558400";
        List<SubscriptionStatus> statuses = Arrays.asList(SubscriptionStatus.ACCEPTED,
                SubscriptionStatus.PENDING, SubscriptionStatus.PAUSED);
        Page<Subscription> result = subscriptionProvider.findAll(null, applicationId, statuses, 1, 10);
        assertThat(result.getTotalCount()).isEqualTo(1);
        assertThat(result.getData()).hasSize(1)
                .extracting(Subscription::getApi).isNotEmpty();
    }

    @Test
    public void findAll() {
        List<SubscriptionStatus> statuses = Arrays.asList(SubscriptionStatus.ACCEPTED,
                SubscriptionStatus.PENDING, SubscriptionStatus.PAUSED);
        Page<Subscription> result = subscriptionProvider.findAll(null, null, statuses, 1, 10);
        assertThat(result.getTotalCount()).isEqualTo(1);
        assertThat(result.getData()).hasSize(1)
                .extracting(Subscription::getApi).isNotEmpty();
    }

    @Test
    public void create() {
        Subscription newSubscription = subscriptionProvider.create(createSubscription());
        assertThat(newSubscription)
                .extracting(Subscription::getId).isNotNull();
    }

    @Test
    public void close() {
        String id = "846157c5-d6fc-440d-a157-c5d6fc740ddb";
        subscriptionProvider.close(id);
    }

    @Test
    public void renewKey() {
        String subscriptionId = "846157c5-d6fc-440d-a157-c5d6fc740ddb";
        ApiKey apiKey = subscriptionProvider.renewKey(subscriptionId);
        assertThat(apiKey).extracting(ApiKey::getKey).isNotNull();
    }

    @Test
    public void revokeKey() {
        String subscriptionId = "846157c5-d6fc-440d-a157-c5d6fc740ddb";
        String apiKey = "4189db48-0602-410d-a058-ae7bc62f5301";
        subscriptionProvider.revokeKey(subscriptionId, apiKey);
    }

    private Subscription createSubscription() {
        Subscription subscription = new Subscription();
        subscription.setApplication("e0e75fa9-3fd7-4584-a75f-a93fd7558400");
        subscription.setPlan("6927cdd9-3da6-453a-a7cd-d93da6653a8b");
        return subscription;
    }
}
