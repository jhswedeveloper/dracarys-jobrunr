package io.github.junhuhdev.dracarys.jobrunr.dashboard.server;

import org.jobrunr.dashboard.server.TeenyHttpHandler;

public abstract class AbstractTeenyHttpHandler implements TeenyHttpHandler {

    public void close() {
        // defalt to no-op
    }

}
