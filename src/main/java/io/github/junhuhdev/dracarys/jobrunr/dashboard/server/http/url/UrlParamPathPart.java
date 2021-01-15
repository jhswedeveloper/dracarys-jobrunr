package io.github.junhuhdev.dracarys.jobrunr.dashboard.server.http.url;

import org.jobrunr.dashboard.server.http.url.UrlPathPart;

public class UrlParamPathPart implements UrlPathPart {
    private final String paramName;

    public UrlParamPathPart(String part) {
        this.paramName = part;
    }

    @Override
    public boolean matches(UrlPathPart pathPart) {
        return true;
    }

    @Override
    public String part() {
        return paramName;
    }
}
