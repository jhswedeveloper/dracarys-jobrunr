package io.github.junhuhdev.dracarys.jobrunr.dashboard.server.http.handlers;

import io.github.junhuhdev.dracarys.jobrunr.dashboard.server.http.HttpRequest;
import io.github.junhuhdev.dracarys.jobrunr.dashboard.server.http.HttpResponse;
import io.github.junhuhdev.dracarys.jobrunr.utils.exceptions.Exceptions;

public interface HttpRequestHandler extends Exceptions.ThrowingBiConsumer<HttpRequest, HttpResponse> {

}
