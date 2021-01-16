package io.github.junhuhdev.dracarys.jobrunr.storage.listeners;

import io.github.junhuhdev.dracarys.jobrunr.storage.JobStats;

public interface JobStatsChangeListener extends StorageProviderChangeListener {

    void onChange(JobStats jobStats);

}
