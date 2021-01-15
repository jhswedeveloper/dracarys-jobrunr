package io.github.junhuhdev.dracarys.jobrunr.jobs.states;

import org.jobrunr.jobs.states.AbstractJobState;

import java.time.Instant;

import static org.jobrunr.jobs.states.StateName.ENQUEUED;

public class EnqueuedState extends AbstractJobState {

    public EnqueuedState() {
        super(ENQUEUED);
    }

    public Instant getEnqueuedAt() {
        return getCreatedAt();
    }
}
