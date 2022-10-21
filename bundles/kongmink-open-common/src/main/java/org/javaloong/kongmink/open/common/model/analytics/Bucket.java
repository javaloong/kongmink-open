package org.javaloong.kongmink.open.common.model.analytics;

import java.util.List;
import java.util.Map;

public class Bucket {

    private List<Bucket> buckets = null;
    private String field;
    private String name;
    private List<Number> data = null;
    private Map<String, Map<String, Object>> metadata = null;

    public List<Bucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Bucket> buckets) {
        this.buckets = buckets;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Number> getData() {
        return data;
    }

    public void setData(List<Number> data) {
        this.data = data;
    }

    public Map<String, Map<String, Object>> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Map<String, Object>> metadata) {
        this.metadata = metadata;
    }
}
