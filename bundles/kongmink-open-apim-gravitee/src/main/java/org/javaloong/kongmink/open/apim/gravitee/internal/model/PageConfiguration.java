package org.javaloong.kongmink.open.apim.gravitee.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PageConfiguration {

    @JsonProperty("try_it")
    private Boolean tryIt;
    @JsonProperty("try_it_anonymous")
    private Boolean tryItAnonymous;
    @JsonProperty("try_it_url")
    private String tryItUrl;
    @JsonProperty("show_url")
    private Boolean showUrl;
    @JsonProperty("display_operation_id")
    private Boolean displayOperationId;
    @JsonProperty("doc_expansion")
    private String docExpansion;
    @JsonProperty("enable_filtering")
    private Boolean enableFiltering;
    @JsonProperty("show_extensions")
    private Boolean showExtensions;
    @JsonProperty("show_common_extensions")
    private Boolean showCommonExtensions;
    @JsonProperty("max_displayed_tags")
    private Number maxDisplayedTags;
    private String viewer;

    public Boolean getTryIt() {
        return tryIt;
    }

    public void setTryIt(Boolean tryIt) {
        this.tryIt = tryIt;
    }

    public Boolean getTryItAnonymous() {
        return tryItAnonymous;
    }

    public void setTryItAnonymous(Boolean tryItAnonymous) {
        this.tryItAnonymous = tryItAnonymous;
    }

    public String getTryItUrl() {
        return tryItUrl;
    }

    public void setTryItUrl(String tryItUrl) {
        this.tryItUrl = tryItUrl;
    }

    public Boolean getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(Boolean showUrl) {
        this.showUrl = showUrl;
    }

    public Boolean getDisplayOperationId() {
        return displayOperationId;
    }

    public void setDisplayOperationId(Boolean displayOperationId) {
        this.displayOperationId = displayOperationId;
    }

    public String getDocExpansion() {
        return docExpansion;
    }

    public void setDocExpansion(String docExpansion) {
        this.docExpansion = docExpansion;
    }

    public Boolean getEnableFiltering() {
        return enableFiltering;
    }

    public void setEnableFiltering(Boolean enableFiltering) {
        this.enableFiltering = enableFiltering;
    }

    public Boolean getShowExtensions() {
        return showExtensions;
    }

    public void setShowExtensions(Boolean showExtensions) {
        this.showExtensions = showExtensions;
    }

    public Boolean getShowCommonExtensions() {
        return showCommonExtensions;
    }

    public void setShowCommonExtensions(Boolean showCommonExtensions) {
        this.showCommonExtensions = showCommonExtensions;
    }

    public Number getMaxDisplayedTags() {
        return maxDisplayedTags;
    }

    public void setMaxDisplayedTags(Number maxDisplayedTags) {
        this.maxDisplayedTags = maxDisplayedTags;
    }

    public String getViewer() {
        return viewer;
    }

    public void setViewer(String viewer) {
        this.viewer = viewer;
    }
}
