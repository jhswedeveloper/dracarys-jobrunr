package io.github.junhuhdev.dracarys.jobrunr.storage;


import io.github.junhuhdev.dracarys.jobrunr.jobs.Job;
import io.github.junhuhdev.dracarys.jobrunr.jobs.JobDetails;
import io.github.junhuhdev.dracarys.jobrunr.jobs.JobId;
import io.github.junhuhdev.dracarys.jobrunr.jobs.RecurringJob;
import io.github.junhuhdev.dracarys.jobrunr.jobs.mappers.JobMapper;
import io.github.junhuhdev.dracarys.jobrunr.jobs.states.StateName;
import io.github.junhuhdev.dracarys.jobrunr.storage.listeners.StorageProviderChangeListener;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * The StorageProvider allows to store, retrieve and delete background jobs.
 */
public interface StorageProvider extends AutoCloseable {

    void addJobStorageOnChangeListener(StorageProviderChangeListener listener);

    void removeJobStorageOnChangeListener(StorageProviderChangeListener listener);

    void setJobMapper(JobMapper jobMapper);

    void announceBackgroundJobServer(BackgroundJobServerStatus serverStatus);

    boolean signalBackgroundJobServerAlive(BackgroundJobServerStatus serverStatus);

    void signalBackgroundJobServerStopped(BackgroundJobServerStatus serverStatus);

    List<BackgroundJobServerStatus> getBackgroundJobServers();

    UUID getLongestRunningBackgroundJobServerId();

    int removeTimedOutBackgroundJobServers(Instant heartbeatOlderThan);

    Job save(Job job);

    /**
     * This method changes the state of the job to the DeletedState - it does not permanently delete it yet
     *
     * @param id the id of the job
     */
    int delete(UUID id);

    int deletePermanently(UUID id);

    Job getJobById(UUID id);

    List<Job> save(List<Job> jobs);

    List<Job> getJobs(StateName state, Instant updatedBefore, PageRequest pageRequest);

    List<Job> getScheduledJobs(Instant scheduledBefore, PageRequest pageRequest);

    Long countJobs(StateName state);

    List<Job> getJobs(StateName state, PageRequest pageRequest);

    Page<Job> getJobPage(StateName state, PageRequest pageRequest);

    int deleteJobsPermanently(StateName state, Instant updatedBefore);

    Set<String> getDistinctJobSignatures(StateName... states);

    boolean exists(JobDetails jobDetails, StateName... states);

    boolean recurringJobExists(String recurringJobId, StateName... states);

    RecurringJob saveRecurringJob(RecurringJob recurringJob);

    List<RecurringJob> getRecurringJobs();

    int deleteRecurringJob(String id);

    JobStats getJobStats();

    void publishJobStatCounter(StateName state, int amount);

    default Job getJobById(JobId jobId) {
        return getJobById(jobId.asUUID());
    }

    void close();
}
