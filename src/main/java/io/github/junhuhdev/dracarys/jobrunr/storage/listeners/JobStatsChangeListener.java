package io.github.junhuhdev.dracarys.jobrunr.storage.listeners;

import org.jobrunr.storage.JobStats;
import org.jobrunr.storage.listeners.StorageProviderChangeListener;

public interface JobStatsChangeListener extends StorageProviderChangeListener {

    void onChange(JobStats jobStats);

}
