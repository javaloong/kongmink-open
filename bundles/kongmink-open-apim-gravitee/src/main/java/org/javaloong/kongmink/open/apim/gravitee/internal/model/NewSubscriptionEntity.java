package org.javaloong.kongmink.open.apim.gravitee.internal.model;

public class NewSubscriptionEntity {

    private String application;
    private String plan;
    private String request;

    public NewSubscriptionEntity() {}

    public NewSubscriptionEntity(String plan, String application) {
        this.application = application;
        this.plan = plan;
    }

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

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
