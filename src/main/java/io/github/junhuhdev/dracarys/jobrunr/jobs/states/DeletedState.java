package io.github.junhuhdev.dracarys.jobrunr.jobs.states;

import org.jobrunr.jobs.states.AbstractJobState;
import org.jobrunr.jobs.states.StateName;

import java.time.Instant;

public class DeletedState extends AbstractJobState {

    public DeletedState() {
        super(StateName.DELETED);
    }

    public Instant getDeletedAt() {
        return getCreatedAt();
    }
}
