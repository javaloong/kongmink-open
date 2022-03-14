package org.javaloong.kongmink.open.apim.gravitee.internal.resource;

import javax.ws.rs.Path;

@Path("environments/{envId}")
public interface EnvironmentsResource {

    @Path("apis")
    ApisResource getApisResource();

    @Path("applications")
    ApplicationsResource getApplicationsResource();

    @Path("auth")
    AuthResource getAuthResource();

    @Path("categories")
    CategoriesResource getCategoriesResource();

    @Path("subscriptions")
    SubscriptionsResource getSubscriptionsResource();

    @Path("user")
    UserResource getUserResource();
}
