package io.github.junhuhdev.dracarys.jobrunr.storage.listeners;

import io.github.junhuhdev.dracarys.jobrunr.jobs.Job;
import io.github.junhuhdev.dracarys.jobrunr.jobs.JobId;

public interface JobChangeListener extends StorageProviderChangeListener, AutoCloseable {

    JobId getJobId();

    void onChange(Job job);
}
