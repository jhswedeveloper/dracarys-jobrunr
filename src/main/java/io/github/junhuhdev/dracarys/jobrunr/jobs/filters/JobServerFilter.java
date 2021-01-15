package io.github.junhuhdev.dracarys.jobrunr.jobs.filters;

import io.github.junhuhdev.dracarys.jobrunr.jobs.Job;

/**
 * A filter that is triggered each time that a Job starts processing or has been processed.
 * Can be useful for adding extra logging, ... .
 */
public interface JobServerFilter extends JobFilter {

    void onProcessing(Job job);

    void onProcessed(Job job);

}
