package io.github.junhuhdev.dracarys.jobrunr.server.concurrent.statechanges;

import static io.github.junhuhdev.dracarys.jobrunr.jobs.states.StateName.DELETED;
import static io.github.junhuhdev.dracarys.jobrunr.jobs.states.StateName.FAILED;

public class DeletedWhileFailedConcurrentStateChange extends AbstractAllowedConcurrentStateChange {

    public DeletedWhileFailedConcurrentStateChange() {
        super(FAILED, DELETED);
    }

}
