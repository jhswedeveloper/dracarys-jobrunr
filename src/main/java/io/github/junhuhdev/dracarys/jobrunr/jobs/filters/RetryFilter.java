package io.github.junhuhdev.dracarys.jobrunr.jobs.filters;

import org.jobrunr.jobs.Job;
import org.jobrunr.jobs.filters.ElectStateFilter;
import org.jobrunr.jobs.states.FailedState;
import org.jobrunr.jobs.states.JobState;
import org.jobrunr.utils.JobUtils;

import static java.time.Instant.now;
import static org.jobrunr.jobs.states.StateName.FAILED_STATES;

/**
 * A JobFilter of type {@link ElectStateFilter} that will retry the job if it fails for up to 10 times.
 * This JobFilter is added by default in JobRunr.
 * <p>
 * If you want to configure the amount of retries, create a new instance and pass it to the JobRunrConfiguration, e.g.:
 *
 * <pre>
 *     JobRunr.configure()
 *                 ...
 *                 .withJobFilter(new RetryFilter(20)) // this will result in 20 retries
 *                 ...
 *                 .initialize();
 * </pre>
 */
public class RetryFilter implements ElectStateFilter {

    public static final int DEFAULT_NBR_OF_RETRIES = 10;

    private final int numberOfRetries;

    public RetryFilter() {
        this(DEFAULT_NBR_OF_RETRIES);
    }

    public RetryFilter(int numberOfRetries) {
        this.numberOfRetries = numberOfRetries;
    }

    @Override
    public void onStateElection(Job job, JobState newState) {
        if (isNotFailed(newState) || isProblematicExceptionAndMustNotRetry(newState) || maxAmountOfRetriesReached(job)) return;

        job.scheduleAt(now().plusSeconds(getSecondsToAdd(job)), String.format("Retry %d of %d", getFailureCount(job), numberOfRetries));
    }

    protected long getSecondsToAdd(Job job) {
        return getExponentialBackoffPolicy(job, 3);
    }

    protected long getExponentialBackoffPolicy(Job job, int seed) {
        return (long) Math.pow(seed, getFailureCount(job));
    }

    private boolean maxAmountOfRetriesReached(Job job) {
        int maxNumberOfRetries = JobUtils.getJobAnnotation(job.getJobDetails()).map(org.jobrunr.jobs.annotations.Job::retries).orElse(this.numberOfRetries);
        return getFailureCount(job) >= maxNumberOfRetries;
    }

    private long getFailureCount(Job job) {
        return job.getJobStates().stream().filter(FAILED_STATES).count();
    }

    private boolean isProblematicExceptionAndMustNotRetry(JobState newState) {
        if (newState instanceof FailedState) {
            return ((FailedState) newState).mustNotRetry();
        }
        return false;
    }

    private boolean isNotFailed(JobState newState) {
        return !(newState instanceof FailedState);
    }

}
