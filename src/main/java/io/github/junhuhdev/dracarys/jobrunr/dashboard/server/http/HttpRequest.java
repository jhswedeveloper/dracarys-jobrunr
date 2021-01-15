package io.github.junhuhdev.dracarys.jobrunr.dashboard.server.http;

import io.github.junhuhdev.dracarys.jobrunr.dashboard.server.http.url.TeenyRequestUrl;

public class HttpRequest {

    private final TeenyRequestUrl requestUrl;

    public HttpRequest(TeenyRequestUrl requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String param(String paramName) {
        return requestUrl.param(paramName);
    }

    public <T> T param(String paramName, Class<T> clazz) {
        return requestUrl.param(paramName, clazz);

    }

    public <T> T fromQueryParams(Class<T> clazz) {
        return requestUrl.fromQueryParams(clazz);
    }

    public <T> T queryParam(String queryParamName, Class<T> clazz, T defaultValue) {
        return requestUrl.queryParam(queryParamName, clazz, defaultValue);
    }
}
