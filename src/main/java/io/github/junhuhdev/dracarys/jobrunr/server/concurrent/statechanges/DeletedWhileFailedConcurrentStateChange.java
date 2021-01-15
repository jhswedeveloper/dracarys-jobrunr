package io.github.junhuhdev.dracarys.jobrunr.server.concurrent.statechanges;

import org.jobrunr.server.concurrent.statechanges.AbstractAllowedConcurrentStateChange;

import static org.jobrunr.jobs.states.StateName.DELETED;
import static org.jobrunr.jobs.states.StateName.FAILED;

public class DeletedWhileFailedConcurrentStateChange extends AbstractAllowedConcurrentStateChange {

    public DeletedWhileFailedConcurrentStateChange() {
        super(FAILED, DELETED);
    }

}
