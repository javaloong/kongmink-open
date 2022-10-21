package org.javaloong.kongmink.open.service.core.internal;

import org.javaloong.kongmink.open.apim.SubscriptionProvider;
import org.javaloong.kongmink.open.common.model.ApiKey;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.model.Subscription;
import org.javaloong.kongmink.open.service.SubscriptionService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SubscriptionServiceImplIT extends AbstractServiceTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext context = FrameworkUtil.getBundle(SubscriptionServiceImplIT.class).getBundleContext();
        context.registerService(SubscriptionProvider.class, Mockito.mock(SubscriptionProvider.class), null);
    }

    @Inject
    SubscriptionProvider subscriptionProvider;
    @Inject
    SubscriptionService subscriptionService;

    @Test
    public void testCRUD() {
        Subscription subscription = TestUtils.createSubscription("1", "application1", "plan1");
        when(subscriptionProvider.create(any(Subscription.class))).thenReturn(new Subscription());
        subscriptionService.create(subscription);
        ArgumentCaptor<Subscription> subscriptionArgumentCaptor = ArgumentCaptor.forClass(Subscription.class);
        verify(subscriptionProvider).create(subscriptionArgumentCaptor.capture());
        assertThat(subscriptionArgumentCaptor.getValue())
                .returns(subscription.getApplication(), Subscription::getApplication)
                .returns(subscription.getPlan(), Subscription::getPlan);
        doNothing().when(subscriptionProvider).close(anyString());
        subscriptionService.close("1");
        verify(subscriptionProvider).close("1");
        when(subscriptionProvider.findById(anyString())).thenReturn(Optional.of(subscription));
        subscriptionService.findById("1");
        verify(subscriptionProvider).findById("1");
        when(subscriptionProvider.findAll(anyString(), anyString(), anyList(), anyInt(), anyInt())).thenReturn(new Page<>());
        subscriptionService.findAll("api1", "application1", Collections.emptyList(), 1, 2);
        verify(subscriptionProvider).findAll("api1", "application1", Collections.emptyList(), 1, 2);
        when(subscriptionProvider.renewKey(anyString())).thenReturn(new ApiKey());
        subscriptionService.renewKey("1");
        verify(subscriptionProvider).renewKey("1");
        doNothing().when(subscriptionProvider).revokeKey(anyString(), anyString());
        subscriptionService.revokeKey("1", "key1");
        verify(subscriptionProvider).revokeKey("1", "key1");
    }
}
