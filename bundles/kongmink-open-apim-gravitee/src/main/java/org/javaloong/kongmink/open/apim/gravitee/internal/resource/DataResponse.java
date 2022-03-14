package org.javaloong.kongmink.open.apim.gravitee.internal.resource;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class DataResponse<T> {

    public static final String METADATA_DATA_KEY = "data";
    public static final String METADATA_DATA_TOTAL_KEY = "total";
    public static final String METADATA_PAGINATION_KEY = "pagination";
    public static final String METADATA_PAGINATION_TOTAL_KEY = "total";
    public static final String METADATA_PAGINATION_SIZE_KEY = "size";
    public static final String METADATA_PAGINATION_CURRENT_PAGE_KEY = "current_page";
    public static final String METADATA_PAGINATION_TOTAL_PAGE_KEY = "total_pages";
    public static final String METADATA_PAGINATION_FIRST_ITEM_INDEX_KEY = "first";
    public static final String METADATA_PAGINATION_LAST_ITEM_INDEX_KEY = "last";

    private Collection<T> data = Collections.emptyList();
    private Map<String, Map<String, Object>> metadata = Collections.emptyMap();

    public DataResponse<T> data(Collection<T> data) {
        this.data = data;
        return this;
    }

    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Collection<?> getData() {
        return data;
    }

    public DataResponse<T> metadata(Map<String, Map<String, Object>> metadata) {
        this.metadata = metadata;
        return this;
    }

    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Map<String, Map<String, Object>> getMetadata() {
        return metadata;
    }

    public Integer getDataTotal() {
        return getMetaValue(METADATA_DATA_KEY, METADATA_DATA_TOTAL_KEY);
    }

    public Integer getPaginationTotal() {
        return getMetaValue(METADATA_PAGINATION_KEY, METADATA_PAGINATION_TOTAL_KEY);
    }

    public Integer getPaginationSize() {
        return getMetaValue(METADATA_PAGINATION_KEY, METADATA_PAGINATION_SIZE_KEY);
    }

    public Integer getCurrentPage() {
        return getMetaValue(METADATA_PAGINATION_KEY, METADATA_PAGINATION_CURRENT_PAGE_KEY);
    }

    public Integer getTotalPage() {
        return getMetaValue(METADATA_PAGINATION_KEY, METADATA_PAGINATION_TOTAL_PAGE_KEY);
    }

    public Integer getPaginationFirst() {
        return getMetaValue(METADATA_PAGINATION_KEY, METADATA_PAGINATION_FIRST_ITEM_INDEX_KEY);
    }

    public Integer getPaginationLast() {
        return getMetaValue(METADATA_PAGINATION_KEY, METADATA_PAGINATION_LAST_ITEM_INDEX_KEY);
    }

    private Integer getMetaValue(String metaKey, String key) {
        Integer value = 0;
        if (metadata.containsKey(metaKey)) {
            value = (Integer) metadata.get(metaKey).get(key);
        }
        return value;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataResponse<?> dataResponse = (DataResponse<?>) o;
        return (
                Objects.equals(this.data, dataResponse.data) &&
                        Objects.equals(this.metadata, dataResponse.metadata)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, metadata);
    }
}
