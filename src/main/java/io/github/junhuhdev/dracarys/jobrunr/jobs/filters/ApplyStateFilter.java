package io.github.junhuhdev.dracarys.jobrunr.jobs.filters;

import io.github.junhuhdev.dracarys.jobrunr.jobs.Job;
import io.github.junhuhdev.dracarys.jobrunr.jobs.states.JobState;

/**
 * A filter that is triggered each time that the state of a Job has changed.
 * Can be useful for adding extra logging, ... .
 * This filter will be called after that the job has been saved to a {@link org.jobrunr.storage.StorageProvider}.
 * Altering the job will not have any influence as it is not saved.
 */
public interface ApplyStateFilter extends JobFilter {

    /**
     * @param job      the job in which to apply the filter
     * @param oldState the previous state - can be null
     * @param newState the new state
     */
    void onStateApplied(Job job, JobState oldState, JobState newState);
}
