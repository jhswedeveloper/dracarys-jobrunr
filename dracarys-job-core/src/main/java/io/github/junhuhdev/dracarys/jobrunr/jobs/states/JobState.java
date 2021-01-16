package io.github.junhuhdev.dracarys.jobrunr.jobs.states;

import java.time.Instant;

public interface JobState {

    StateName getName();

    Instant getCreatedAt();

    Instant getUpdatedAt();

}
