package org.javaloong.kongmink.open.apim.gravitee.internal.resource.param;

import javax.ws.rs.QueryParam;

public class AnalyticsParam {

    @QueryParam("from")
    private long from;

    @QueryParam("to")
    private long to;

    @QueryParam("interval")
    private long interval;

    @QueryParam("query")
    private String query;

    @QueryParam("field")
    private String field;

    @QueryParam("size")
    private int size;

    @QueryParam("type")
    private String type;

    @QueryParam("ranges")
    private String ranges;

    @QueryParam("aggs")
    private String aggs;

    @QueryParam("order")
    private String order;

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRanges() {
        return ranges;
    }

    public void setRanges(String ranges) {
        this.ranges = ranges;
    }

    public String getAggs() {
        return aggs;
    }

    public void setAggs(String aggs) {
        this.aggs = aggs;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
