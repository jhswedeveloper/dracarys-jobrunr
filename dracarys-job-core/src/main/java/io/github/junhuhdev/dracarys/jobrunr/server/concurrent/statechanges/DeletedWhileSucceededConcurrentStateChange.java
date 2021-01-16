package io.github.junhuhdev.dracarys.jobrunr.server.concurrent.statechanges;

import static io.github.junhuhdev.dracarys.jobrunr.jobs.states.StateName.DELETED;
import static io.github.junhuhdev.dracarys.jobrunr.jobs.states.StateName.SUCCEEDED;

public class DeletedWhileSucceededConcurrentStateChange extends AbstractAllowedConcurrentStateChange {

    public DeletedWhileSucceededConcurrentStateChange() {
        super(SUCCEEDED, DELETED);
    }

}
