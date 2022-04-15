package org.javaloong.kongmink.open.apim.model;

import java.util.Objects;

public class ApiMetrics {

    private String id;
    private Number subscribers;
    private Number hits;
    private Number health;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Number getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Number subscribers) {
        this.subscribers = subscribers;
    }

    public Number getHits() {
        return hits;
    }

    public void setHits(Number hits) {
        this.hits = hits;
    }

    public Number getHealth() {
        return health;
    }

    public void setHealth(Number health) {
        this.health = health;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiMetrics that = (ApiMetrics) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
