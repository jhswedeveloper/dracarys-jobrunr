package io.github.junhuhdev.dracarys.jobrunr.storage.listeners;

import org.jobrunr.storage.BackgroundJobServerStatus;
import org.jobrunr.storage.listeners.StorageProviderChangeListener;

import java.util.List;

public interface BackgroundJobServerStatusChangeListener extends StorageProviderChangeListener {

    void onChange(List<BackgroundJobServerStatus> changedServerStates);

}
