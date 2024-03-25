package org.hishmalif.supporthelperbot.data;

import lombok.Data;
import lombok.Builder;
import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.HashMap;

@Data
public class HttpRequest {
    private String url;
    private final HttpMethod method;
    private Map<String, String> params;
    private final Map<String, String> headers;
    private Object body;

    @Builder
    public HttpRequest(String url, HttpMethod method, Map<String, String> params, Map<String, String> headers, Object body) {
        this.url = url;
        this.method = method;
        this.body = body;
        this.params = new HashMap<>();
        this.headers = new HashMap<>();

        if (params != null) {
            this.params.putAll(params);
        }
        if (headers != null) {
            this.headers.putAll(headers);
        }
    }
}