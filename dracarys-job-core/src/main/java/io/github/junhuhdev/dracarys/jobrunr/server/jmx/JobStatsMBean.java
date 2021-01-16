package io.github.junhuhdev.dracarys.jobrunr.server.jmx;

import java.time.Instant;

public interface JobStatsMBean {
    Instant getTimeStamp();

    Long getTotal();

    Long getAwaiting();

    Long getScheduled();

    Long getEnqueued();

    Long getProcessing();

    Long getFailed();

    Long getSucceeded();

    int getRecurringJobs();

    int getBackgroundJobServers();
}
