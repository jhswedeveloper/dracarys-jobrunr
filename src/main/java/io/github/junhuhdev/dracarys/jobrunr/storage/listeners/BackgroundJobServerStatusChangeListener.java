package io.github.junhuhdev.dracarys.jobrunr.storage.listeners;


import io.github.junhuhdev.dracarys.jobrunr.storage.BackgroundJobServerStatus;

import java.util.List;

public interface BackgroundJobServerStatusChangeListener extends StorageProviderChangeListener {

    void onChange(List<BackgroundJobServerStatus> changedServerStates);

}
