package org.javaloong.kongmink.open.apim.gravitee.internal.resource;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.NewSubscriptionEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.model.SubscriptionEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.param.PaginationParam;
import org.javaloong.kongmink.open.common.model.SubscriptionStatus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

public interface SubscriptionsResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    SubscriptionEntity createSubscription(NewSubscriptionEntity newSubscriptionEntity);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    DataResponse<SubscriptionEntity> getSubscriptions(
            @QueryParam("apiId") String apiId,
            @QueryParam("applicationId") String applicationId,
            @QueryParam("statuses") List<SubscriptionStatus> statuses,
            @BeanParam PaginationParam paginationParam);

    @Path("{subscriptionId}")
    SubscriptionResource getSubscriptionResource(@PathParam("subscriptionId") String subscriptionId);
}
