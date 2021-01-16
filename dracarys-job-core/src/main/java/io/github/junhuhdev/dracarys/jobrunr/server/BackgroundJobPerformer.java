package io.github.junhuhdev.dracarys.jobrunr.server;

import io.github.junhuhdev.dracarys.jobrunr.jobs.Job;
import io.github.junhuhdev.dracarys.jobrunr.jobs.context.JobRunrDashboardLogger;
import io.github.junhuhdev.dracarys.jobrunr.jobs.filters.JobPerformingFilters;
import io.github.junhuhdev.dracarys.jobrunr.jobs.states.StateName;
import io.github.junhuhdev.dracarys.jobrunr.server.runner.BackgroundJobRunner;
import io.github.junhuhdev.dracarys.jobrunr.storage.ConcurrentJobModificationException;
import io.github.junhuhdev.dracarys.jobrunr.storage.JobNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

import static io.github.junhuhdev.dracarys.jobrunr.jobs.states.StateName.PROCESSING;
import static io.github.junhuhdev.dracarys.jobrunr.utils.exceptions.Exceptions.hasCause;

public class BackgroundJobPerformer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackgroundJobPerformer.class);

    private static final AtomicInteger concurrentModificationExceptionCounter = new AtomicInteger();
    private final BackgroundJobServer backgroundJobServer;
    private final JobPerformingFilters jobPerformingFilters;
    private final Job job;

    public BackgroundJobPerformer(BackgroundJobServer backgroundJobServer, Job job) {
        this.backgroundJobServer = backgroundJobServer;
        this.jobPerformingFilters = new JobPerformingFilters(job, backgroundJobServer.getJobFilters());
        this.job = job;
    }

    public void run() {
        try {
            backgroundJobServer.getJobZooKeeper().notifyThreadOccupied();
            boolean canProcess = updateJobStateToProcessingRunJobFiltersAndReturnIfProcessingCanStart();
            if (canProcess) {
                runActualJob();
                updateJobStateToSucceededAndRunJobFilters();
            }
        } catch (Exception e) {
            if (isJobDeletedWhileProcessing(e)) {
                // nothing to do anymore as Job is deleted
                return;
            } else if (isJobServerStopped(e)) {
                updateJobStateToFailedAndRunJobFilters("Job processing was stopped as background job server has stopped", e);
                Thread.currentThread().interrupt();
            } else if (isJobNotFoundException(e)) {
                updateJobStateToFailedAndRunJobFilters("Job method not found", e);
            } else {
                updateJobStateToFailedAndRunJobFilters("An exception occurred during the performance of the job", e);
            }
        } finally {
            backgroundJobServer.getJobZooKeeper().notifyThreadIdle();
        }
    }

    private boolean updateJobStateToProcessingRunJobFiltersAndReturnIfProcessingCanStart() {
        try {
            job.startProcessingOn(backgroundJobServer);
            saveAndRunStateRelatedJobFilters(job);
            LOGGER.debug("Job(id={}, jobName='{}') processing started", job.getId(), job.getJobName());
            return job.hasState(PROCESSING);
        } catch (ConcurrentJobModificationException e) {
            // processing already started on other server
            LOGGER.trace("Could not start processing job {} - it is already in a newer state (collision {})", job.getId(), concurrentModificationExceptionCounter.incrementAndGet());
            return false;
        }
    }

    private void runActualJob() throws Exception {
        try {
            JobRunrDashboardLogger.setJob(job);
            backgroundJobServer.getJobZooKeeper().startProcessing(job, Thread.currentThread());
            LOGGER.trace("Job(id={}, jobName='{}') is running", job.getId(), job.getJobName());
            jobPerformingFilters.runOnJobProcessingFilters();
            BackgroundJobRunner backgroundJobRunner = backgroundJobServer.getBackgroundJobRunner(job);
            backgroundJobRunner.run(job);
            jobPerformingFilters.runOnJobProcessedFilters();
        } finally {
            backgroundJobServer.getJobZooKeeper().stopProcessing(job);
            JobRunrDashboardLogger.clearJob();
        }
    }

    private void updateJobStateToSucceededAndRunJobFilters() {
        try {
            LOGGER.debug("Job(id={}, jobName='{}') processing succeeded", job.getId(), job.getJobName());
            job.succeeded();
            saveAndRunStateRelatedJobFilters(job);
        } catch (Exception badException) {
            LOGGER.error("ERROR - could not update job(id={}, jobName='{}') to SUCCEEDED state", job.getId(), job.getJobName(), badException);
        }
    }

    private void updateJobStateToFailedAndRunJobFilters(String message, Exception e) {
        try {
            LOGGER.warn("Job(id={}, jobName='{}') processing failed: {}", job.getId(), job.getJobName(), message, e);
            job.failed(message, e);
            saveAndRunStateRelatedJobFilters(job);
        } catch (Exception badException) {
            LOGGER.error("ERROR - could not update job(id={}, jobName='{}') to FAILED state", job.getId(), job.getJobName(), badException);
        }
    }

    protected void saveAndRunStateRelatedJobFilters(Job job) {
        jobPerformingFilters.runOnStateAppliedFilters();
        StateName beforeStateElection = job.getState();
        jobPerformingFilters.runOnStateElectionFilter();
        StateName afterStateElection = job.getState();
        this.backgroundJobServer.getStorageProvider().save(job);
        if (beforeStateElection != afterStateElection) {
            jobPerformingFilters.runOnStateAppliedFilters();
        }
    }

    private boolean isJobDeletedWhileProcessing(Exception e) {
        return hasCause(e, InterruptedException.class) && job.hasState(StateName.DELETED);
    }

    private boolean isJobServerStopped(Exception e) {
        return hasCause(e, InterruptedException.class) && !job.hasState(StateName.DELETED);
    }

    private boolean isJobNotFoundException(Exception e) {
        return e instanceof JobNotFoundException;
    }
}
