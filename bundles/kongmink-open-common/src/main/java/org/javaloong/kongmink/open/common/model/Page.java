package org.javaloong.kongmink.open.common.model;

import java.util.Collection;

public class Page<T> {

    private final Collection<T> data;
    private final long totalCount;

    public Page(Collection<T> data, long totalCount) {
        this.data = data;
        this.totalCount = totalCount;
    }

    public Collection<T> getData() {
        return data;
    }

    public long getTotalCount() {
        return totalCount;
    }
}
