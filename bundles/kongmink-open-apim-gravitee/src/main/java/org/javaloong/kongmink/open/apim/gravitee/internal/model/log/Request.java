package org.javaloong.kongmink.open.apim.gravitee.internal.model.log;

import java.util.List;
import java.util.Map;

public class Request {

    private HttpMethod method = HttpMethod.OTHER;
    private Map<String, List<String>> headers = null;
    private String uri;
    private String body;

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
