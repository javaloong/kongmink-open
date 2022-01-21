package org.javaloong.kongmink.open.common.model;

import java.util.Collection;

public class Page<T> {

    private Collection<T> data;
    private long totalCount;

    public Page() {
    }

    public Page(Collection<T> data, long totalCount) {
        this.data = data;
        this.totalCount = totalCount;
    }

    public Collection<T> getData() {
        return data;
    }

    public void setData(Collection<T> data) {
        this.data = data;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
