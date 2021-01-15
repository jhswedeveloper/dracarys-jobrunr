package io.github.junhuhdev.dracarys.jobrunr.jobs.filters;

import io.github.junhuhdev.dracarys.jobrunr.jobs.AbstractJob;

/**
 * A filter that is triggered each time that a Job
 * <ul>
 *     <li>is about to be created (before it is saved to the {@link io.github.junhuhdev.dracarys.jobrunr.storage.StorageProvider})</li>
 *     <li>has been created (after is has been saved in the {@link io.github.junhuhdev.dracarys.jobrunr.storage.StorageProvider}</li>
 * </ul>
 */
public interface JobClientFilter extends JobFilter {

    void onCreating(AbstractJob job);

    void onCreated(AbstractJob job);

}
