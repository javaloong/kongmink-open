package org.javaloong.kongmink.open.common.model.analytics;

import java.util.List;

public class HistogramAnalytics implements Analytics {

    private Timestamp timestamp;
    private List<Bucket> values;

    public List<Bucket> getValues() {
        return values;
    }

    public void setValues(List<Bucket> values) {
        this.values = values;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
