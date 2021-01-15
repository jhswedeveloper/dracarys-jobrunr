package io.github.junhuhdev.dracarys.jobrunr.jobs.states;

import java.time.Instant;

import static io.github.junhuhdev.dracarys.jobrunr.jobs.states.StateName.ENQUEUED;

public class EnqueuedState extends AbstractJobState {

    public EnqueuedState() {
        super(ENQUEUED);
    }

    public Instant getEnqueuedAt() {
        return getCreatedAt();
    }
}
