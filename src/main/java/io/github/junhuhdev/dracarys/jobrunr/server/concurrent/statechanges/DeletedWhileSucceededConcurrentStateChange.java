package io.github.junhuhdev.dracarys.jobrunr.server.concurrent.statechanges;

import org.jobrunr.server.concurrent.statechanges.AbstractAllowedConcurrentStateChange;

import static org.jobrunr.jobs.states.StateName.DELETED;
import static org.jobrunr.jobs.states.StateName.SUCCEEDED;

public class DeletedWhileSucceededConcurrentStateChange extends AbstractAllowedConcurrentStateChange {

    public DeletedWhileSucceededConcurrentStateChange() {
        super(SUCCEEDED, DELETED);
    }

}
