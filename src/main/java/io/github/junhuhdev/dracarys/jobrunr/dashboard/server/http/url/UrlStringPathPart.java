package io.github.junhuhdev.dracarys.jobrunr.dashboard.server.http.url;

import org.jobrunr.dashboard.server.http.url.UrlPathPart;

public class UrlStringPathPart implements UrlPathPart {

    private final String part;

    public UrlStringPathPart(String part) {
        this.part = part;
    }

    @Override
    public boolean matches(UrlPathPart pathPart) {
        if (pathPart instanceof org.jobrunr.dashboard.server.http.url.UrlStringPathPart) {
            return part.equals(((org.jobrunr.dashboard.server.http.url.UrlStringPathPart) pathPart).part);
        }
        return false;
    }

    @Override
    public String part() {
        return part;
    }
}
