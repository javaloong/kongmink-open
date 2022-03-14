package org.javaloong.kongmink.open.apim.gravitee.internal.resource.param;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.FilterApiQuery;

import javax.ws.rs.QueryParam;

public class ApisParam {

    @QueryParam("context-path")
    private String contextPath;

    @QueryParam("label")
    private String label;

    @QueryParam("version")
    private String version;

    @QueryParam("name")
    private String name;

    @QueryParam("tag")
    private String tag;

    @QueryParam("category")
    private String category;

    @QueryParam("filter")
    private FilterApiQuery filter;

    @QueryParam("-filter")
    private FilterApiQuery excludedFilter;

    @QueryParam("promoted")
    private Boolean promoted;

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public FilterApiQuery getFilter() {
        return filter;
    }

    public void setFilter(FilterApiQuery filter) {
        this.filter = filter;
    }

    public FilterApiQuery getExcludedFilter() {
        return excludedFilter;
    }

    public void setExcludedFilter(FilterApiQuery excludedFilter) {
        this.excludedFilter = excludedFilter;
    }

    public Boolean getPromoted() {
        return promoted;
    }

    public void setPromoted(Boolean promoted) {
        this.promoted = promoted;
    }

    public boolean isCategoryMode() {
        return this.getCategory() != null && this.getFilter() == null;
    }
}
