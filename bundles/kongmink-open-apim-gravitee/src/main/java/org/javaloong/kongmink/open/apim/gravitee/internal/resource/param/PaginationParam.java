package org.javaloong.kongmink.open.apim.gravitee.internal.resource.param;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public class PaginationParam {

    public static final String PAGE_QUERY_PARAM_NAME = "page";
    public static final String SIZE_QUERY_PARAM_NAME = "size";

    private static final String PAGE_QUERY_PARAM_DEFAULT = "1";
    private static final String SIZE_QUERY_PARAM_DEFAULT = "10";

    @DefaultValue(PAGE_QUERY_PARAM_DEFAULT)
    @QueryParam(PAGE_QUERY_PARAM_NAME)
    Integer page;

    @DefaultValue(SIZE_QUERY_PARAM_DEFAULT)
    @QueryParam(SIZE_QUERY_PARAM_NAME)
    Integer size;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public boolean hasPagination() {
        return this.getSize() != null && this.getSize().equals(-1);
    }
}
