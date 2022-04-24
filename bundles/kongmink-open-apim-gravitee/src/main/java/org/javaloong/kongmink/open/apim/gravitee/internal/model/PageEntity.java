package org.javaloong.kongmink.open.apim.gravitee.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PageEntity {

    private String id;
    private String name;
    private String type;
    private Integer order;
    private String parent;
    @JsonProperty("updated_at")
    private Date updatedAt;
    private PageConfiguration configuration;
    private List<PageMedia> media;
    private List<Metadata> metadata;
    @JsonProperty("_links")
    private PageLinks links;
    private String content;
    private PageRevisionId contentRevisionId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PageConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(PageConfiguration configuration) {
        this.configuration = configuration;
    }

    public List<PageMedia> getMedia() {
        return media;
    }

    public void setMedia(List<PageMedia> media) {
        this.media = media;
    }

    public List<Metadata> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<Metadata> metadata) {
        this.metadata = metadata;
    }

    public PageLinks getLinks() {
        return links;
    }

    public void setLinks(PageLinks links) {
        this.links = links;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PageRevisionId getContentRevisionId() {
        return contentRevisionId;
    }

    public void setContentRevisionId(PageRevisionId contentRevisionId) {
        this.contentRevisionId = contentRevisionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageEntity that = (PageEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class PageRevisionId {

        private String pageId;
        private Integer revision;

        public String getPageId() {
            return pageId;
        }

        public void setPageId(String pageId) {
            this.pageId = pageId;
        }

        public Integer getRevision() {
            return revision;
        }

        public void setRevision(Integer revision) {
            this.revision = revision;
        }
    }
}
