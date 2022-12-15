package org.javaloong.kongmink.open.apim.gravitee.internal;

import org.javaloong.kongmink.open.apim.SubscriptionProvider;
import org.javaloong.kongmink.open.common.model.ApiKey;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.model.Subscription;
import org.javaloong.kongmink.open.common.model.SubscriptionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("gravitee")
public class GraviteeSubscriptionProviderIT extends GraviteePortalClientTestSupport {

    private SubscriptionProvider subscriptionProvider;

    @BeforeEach
    public void setUp() {
        subscriptionProvider = new GraviteeSubscriptionProvider(createPortalClient());
    }

    @Test
    public void testSubscriptionCRUD() {
        // create subscription
        Subscription subscription = new Subscription();
        subscription.setApplication("4bba913d-96d9-4471-ba91-3d96d9a47169");
        subscription.setPlan("2d31f91c-75f1-4496-b1f9-1c75f1349634");
        Subscription newSubscription = subscriptionProvider.create(subscription);
        assertThat(newSubscription)
                .extracting(Subscription::getId).isNotNull();
        Optional<Subscription> result = subscriptionProvider.findById(newSubscription.getId());
        assertThat(result).hasValueSatisfying(value ->
                assertThat(value.getApi()).isEqualTo("ebbb4e21-9bbc-400d-bb4e-219bbc400d97"));
        // renew key
        ApiKey apiKey = subscriptionProvider.renewKey(newSubscription.getId());
        assertThat(apiKey).extracting(ApiKey::getKey).isNotNull();
        result = subscriptionProvider.findById(newSubscription.getId());
        assertThat(result).hasValueSatisfying(value -> assertThat(value.getKeys()).hasSize(2));
        // revoke key
        subscriptionProvider.revokeKey(newSubscription.getId(), apiKey.getKey());
        result = subscriptionProvider.findById(newSubscription.getId());
        assertThat(result).hasValueSatisfying(value -> assertThat(value.getKeys()).hasSize(2)
                .extracting(ApiKey::getRevoked).contains(Boolean.TRUE, Boolean.FALSE));
        // close subscription
        subscriptionProvider.close(newSubscription.getId());
        result = subscriptionProvider.findById(newSubscription.getId());
        assertThat(result).hasValueSatisfying(value -> assertThat(value.getKeys()).hasSize(2)
                .extracting(ApiKey::getRevoked).containsExactly(Boolean.TRUE, Boolean.TRUE));
    }

    @Test
    public void findAllByApi() {
        String apiId = "ebbb4e21-9bbc-400d-bb4e-219bbc400d97";
        List<SubscriptionStatus> statuses = Arrays.asList(SubscriptionStatus.ACCEPTED,
                SubscriptionStatus.PENDING, SubscriptionStatus.PAUSED);
        Page<Subscription> result = subscriptionProvider.findAll(apiId, null, statuses, 1, 10);
        assertThat(result.getTotalCount()).isEqualTo(1);
        assertThat(result.getData()).hasSize(1)
                .extracting(Subscription::getApi).isNotEmpty();
    }

    @Test
    public void findAllByApplication() {
        String applicationId = "bfe2c4c2-dfc7-43c3-a2c4-c2dfc703c365";
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
}
