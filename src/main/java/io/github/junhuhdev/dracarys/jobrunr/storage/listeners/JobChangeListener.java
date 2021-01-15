package io.github.junhuhdev.dracarys.jobrunr.storage.listeners;

import org.jobrunr.jobs.Job;
import org.jobrunr.jobs.JobId;
import org.jobrunr.storage.listeners.StorageProviderChangeListener;

public interface JobChangeListener extends StorageProviderChangeListener, AutoCloseable {

    JobId getJobId();

    void onChange(Job job);
}
