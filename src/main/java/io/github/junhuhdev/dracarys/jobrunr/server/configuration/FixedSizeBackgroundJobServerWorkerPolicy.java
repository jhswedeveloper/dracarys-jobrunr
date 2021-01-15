package io.github.junhuhdev.dracarys.jobrunr.server.configuration;

import org.jobrunr.server.configuration.BackgroundJobServerWorkerPolicy;

public class FixedSizeBackgroundJobServerWorkerPolicy implements BackgroundJobServerWorkerPolicy {

    private final int workerCount;

    public FixedSizeBackgroundJobServerWorkerPolicy(int workerCount) {
        this.workerCount = workerCount;
    }

    @Override
    public int getWorkerCount() {
        return workerCount;
    }
}
