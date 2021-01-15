package io.github.junhuhdev.dracarys.jobrunr.jobs.states;

import org.jobrunr.jobs.states.StateName;

import java.time.Instant;

public interface JobState {

    StateName getName();

    Instant getCreatedAt();

    Instant getUpdatedAt();

}
