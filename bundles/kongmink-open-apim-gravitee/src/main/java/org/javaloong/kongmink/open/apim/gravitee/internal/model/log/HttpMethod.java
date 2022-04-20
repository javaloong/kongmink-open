package org.javaloong.kongmink.open.apim.gravitee.internal.model.log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets HttpMethod
 */
public enum HttpMethod {

    CONNECT("CONNECT"),

    DELETE("DELETE"),

    GET("GET"),

    HEAD("HEAD"),

    OPTIONS("OPTIONS"),

    PATCH("PATCH"),

    POST("POST"),

    PUT("PUT"),

    TRACE("TRACE"),

    OTHER("OTHER");

    private String value;

    HttpMethod(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static HttpMethod fromValue(String value) {
        for (HttpMethod b : HttpMethod.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
