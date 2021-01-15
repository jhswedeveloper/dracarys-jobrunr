package io.github.junhuhdev.dracarys.jobrunr.dashboard.server.http.url;

public interface UrlPathPart {

    boolean matches(org.jobrunr.dashboard.server.http.url.UrlPathPart pathPart);

    String part();
}
