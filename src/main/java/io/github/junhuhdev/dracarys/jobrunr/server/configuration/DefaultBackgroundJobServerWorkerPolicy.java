package io.github.junhuhdev.dracarys.jobrunr.server.configuration;

import org.jobrunr.server.configuration.BackgroundJobServerWorkerPolicy;

public class DefaultBackgroundJobServerWorkerPolicy implements BackgroundJobServerWorkerPolicy {

    private final int workerCount;

    public DefaultBackgroundJobServerWorkerPolicy() {
        // see https://jobs.zalando.com/en/tech/blog/how-to-set-an-ideal-thread-pool-size
        workerCount = (Runtime.getRuntime().availableProcessors() * 8);
    }

    @Override
    public int getWorkerCount() {
        return workerCount;
    }

}
