package io.github.junhuhdev.dracarys.jobrunr.storage;

import org.jobrunr.storage.BackgroundJobServerStatus;
import org.jobrunr.storage.StorageException;

public class ServerTimedOutException extends StorageException {

    public ServerTimedOutException(BackgroundJobServerStatus serverStatus, StorageException e) {
        super("Server " + serverStatus.getId() + " has timed out and must reannounce itself", e);
    }

}
