package io.github.junhuhdev.dracarys.jobrunr.dashboard.server.http;

import com.sun.net.httpserver.HttpExchange;
import io.github.junhuhdev.dracarys.jobrunr.dashboard.server.AbstractTeenyHttpHandler;

import java.io.IOException;

public class RedirectHttpHandler extends AbstractTeenyHttpHandler {

    private final String contextPath;
    private final String to;

    public RedirectHttpHandler(String contextPath, String to) {
        this.contextPath = contextPath;
        this.to = to;
    }

    @Override
    public String getContextPath() {
        return contextPath;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().add("Location", to);
        httpExchange.sendResponseHeaders(302, -1);
    }
}
