package org.javaloong.kongmink.open.common.model.analytics;

import java.util.Map;

public class TopHitsAnalytics implements Analytics {

    private Map<String, Long> values;

    private Map<String, Map<String, String>> metadata;

    public Map<String, Long> getValues() {
        return values;
    }

    public void setValues(Map<String, Long> values) {
        this.values = values;
    }

    public Map<String, Map<String, String>> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Map<String, String>> metadata) {
        this.metadata = metadata;
    }
}
