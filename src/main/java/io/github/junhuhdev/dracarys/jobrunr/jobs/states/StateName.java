package io.github.junhuhdev.dracarys.jobrunr.jobs.states;

import org.jobrunr.jobs.states.FailedState;
import org.jobrunr.jobs.states.JobState;

import java.util.function.Predicate;

public enum StateName {

    AWAITING,
    SCHEDULED,
    ENQUEUED,
    PROCESSING,
    FAILED,
    SUCCEEDED,
    DELETED;

    public static final Predicate<JobState> FAILED_STATES = state -> state instanceof FailedState;
}
