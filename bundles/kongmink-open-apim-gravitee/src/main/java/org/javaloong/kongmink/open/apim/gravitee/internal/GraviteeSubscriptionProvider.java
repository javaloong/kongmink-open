package org.javaloong.kongmink.open.apim.gravitee.internal;

import org.javaloong.kongmink.open.apim.SubscriptionProvider;
import org.javaloong.kongmink.open.apim.gravitee.internal.mapper.ApiKeyMapper;
import org.javaloong.kongmink.open.apim.gravitee.internal.mapper.SubscriptionMapper;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.NewSubscriptionEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.SubscriptionEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.DataResponse;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.SubscriptionKeysResource;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.SubscriptionResource;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.param.PaginationParam;
import org.javaloong.kongmink.open.apim.model.ApiKey;
import org.javaloong.kongmink.open.apim.model.Subscription;
import org.javaloong.kongmink.open.apim.model.SubscriptionStatus;
import org.javaloong.kongmink.open.common.model.Page;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.ws.rs.NotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component(service = SubscriptionProvider.class)
public class GraviteeSubscriptionProvider implements SubscriptionProvider {

    private static final String INCLUDE_KEYS = "keys";

    private final GraviteePortalClient client;

    @Activate
    public GraviteeSubscriptionProvider(@Reference GraviteePortalClient client) {
        this.client = client;
    }

    @Override
    public Optional<Subscription> findById(String id) {
        SubscriptionResource subscriptionResource = getSubscriptionResource(id);
        List<String> include = Collections.singletonList(INCLUDE_KEYS);
        return getSubscriptionEntity(subscriptionResource, include).map(SubscriptionMapper::mapToSubscription);
    }

    @Override
    public Page<Subscription> findAll(String apiId, String applicationId,
                                      List<SubscriptionStatus> statuses, int page, int size) {
        PaginationParam paginationParam = createPaginationParam(page, size);
        DataResponse<SubscriptionEntity> dataResponse = client.getSubscriptionsResource()
                .getSubscriptions(apiId, applicationId, statuses, paginationParam);
        return SubscriptionMapper.mapToPaginationSubscriptions(dataResponse);
    }

    @Override
    public Subscription create(Subscription subscription) {
        NewSubscriptionEntity newSubscriptionEntity = SubscriptionMapper.mapToNewSubscriptionEntity(subscription);
        SubscriptionEntity subscriptionEntity = client.getSubscriptionsResource()
                .createSubscription(newSubscriptionEntity);
        return SubscriptionMapper.mapToSubscription(subscriptionEntity);
    }

    @Override
    public void close(String id) {
        SubscriptionResource subscriptionResource = getSubscriptionResource(id);
        subscriptionResource.closeSubscription();
    }

    @Override
    public ApiKey renewKey(String subscriptionId) {
        SubscriptionKeysResource subscriptionKeysResource = getSubscriptionKeysResource(subscriptionId);
        return ApiKeyMapper.mapToApiKey(subscriptionKeysResource.renewKeySubscription());
    }

    @Override
    public void revokeKey(String subscriptionId, String apiKey) {
        SubscriptionKeysResource subscriptionKeysResource = getSubscriptionKeysResource(subscriptionId);
        subscriptionKeysResource.revokeKeySubscription(apiKey);
    }

    private SubscriptionResource getSubscriptionResource(String id) {
        return client.getSubscriptionsResource().getSubscriptionResource(id);
    }

    private SubscriptionKeysResource getSubscriptionKeysResource(String subscriptionId) {
        return getSubscriptionResource(subscriptionId).getSubscriptionKeysResource();
    }

    private Optional<SubscriptionEntity> getSubscriptionEntity(SubscriptionResource resource, List<String> include) {
        try {
            return Optional.ofNullable(resource.getSubscriptionBySubscriptionId(include));
        } catch (NotFoundException ex) {
            return Optional.empty();
        }
    }

    private PaginationParam createPaginationParam(int page, int size) {
        PaginationParam paginationParam = new PaginationParam();
        paginationParam.setPage(page);
        paginationParam.setSize(size);
        return paginationParam;
    }
}
