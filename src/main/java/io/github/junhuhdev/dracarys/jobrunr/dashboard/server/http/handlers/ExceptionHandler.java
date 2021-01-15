package io.github.junhuhdev.dracarys.jobrunr.dashboard.server.http.handlers;


import java.util.function.BiConsumer;

public interface ExceptionHandler<T extends Exception> extends BiConsumer<T, HttpResponse> {
}
