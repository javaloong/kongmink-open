package org.javaloong.kongmink.open.rest.core.dto;

import org.javaloong.kongmink.open.common.model.Subscription;

import javax.validation.constraints.NotEmpty;

public class SubscriptionDTO {

    @NotEmpty
    private String application;
    @NotEmpty
    private String plan;

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Subscription toSubscription() {
        Subscription subscription = new Subscription();
        subscription.setApplication(application);
        subscription.setPlan(plan);
        return subscription;
    }
}
