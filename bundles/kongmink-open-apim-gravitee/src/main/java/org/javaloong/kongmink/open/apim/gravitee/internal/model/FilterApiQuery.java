package org.javaloong.kongmink.open.apim.gravitee.internal.model;

public enum FilterApiQuery {
    ALL("ALL"),
    FEATURED("FEATURED"),
    MINE("MINE"),
    STARRED("STARRED"),
    TRENDINGS("TRENDINGS");

    private String value;

    FilterApiQuery(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static FilterApiQuery fromValue(String value) {
        for (FilterApiQuery b : FilterApiQuery.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
