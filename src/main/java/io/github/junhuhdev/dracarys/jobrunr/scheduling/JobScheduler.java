package io.github.junhuhdev.dracarys.jobrunr.scheduling;


import io.github.junhuhdev.dracarys.jobrunr.configuration.JobRunr;
import io.github.junhuhdev.dracarys.jobrunr.jobs.Job;
import io.github.junhuhdev.dracarys.jobrunr.jobs.JobDetails;
import io.github.junhuhdev.dracarys.jobrunr.jobs.JobId;
import io.github.junhuhdev.dracarys.jobrunr.jobs.RecurringJob;
import io.github.junhuhdev.dracarys.jobrunr.jobs.details.JobDetailsAsmGenerator;
import io.github.junhuhdev.dracarys.jobrunr.jobs.details.JobDetailsGenerator;
import io.github.junhuhdev.dracarys.jobrunr.jobs.filters.JobDefaultFilters;
import io.github.junhuhdev.dracarys.jobrunr.jobs.filters.JobFilter;
import io.github.junhuhdev.dracarys.jobrunr.jobs.filters.JobFilterUtils;
import io.github.junhuhdev.dracarys.jobrunr.jobs.lambdas.IocJobLambda;
import io.github.junhuhdev.dracarys.jobrunr.jobs.lambdas.IocJobLambdaFromStream;
import io.github.junhuhdev.dracarys.jobrunr.jobs.lambdas.JobLambda;
import io.github.junhuhdev.dracarys.jobrunr.jobs.lambdas.JobLambdaFromStream;
import io.github.junhuhdev.dracarys.jobrunr.jobs.states.ScheduledState;
import io.github.junhuhdev.dracarys.jobrunr.scheduling.cron.CronExpression;
import io.github.junhuhdev.dracarys.jobrunr.storage.StorageProvider;

import java.time.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static io.github.junhuhdev.dracarys.jobrunr.utils.streams.StreamUtils.batchCollector;
import static java.time.ZoneId.systemDefault;
import static java.util.Collections.emptyList;

/**
 * Provides methods for creating fire-and-forget, delayed and recurring jobs as well as to delete existing background jobs.
 *
 * @author Ronald Dehuysser
 */
public class JobScheduler {

    private final StorageProvider storageProvider;
    private final JobDetailsGenerator jobDetailsGenerator;
    private final JobFilterUtils jobFilterUtils;
    private static final int BATCH_SIZE = 5000;

    /**
     * Creates a new JobScheduler using the provided storageProvider
     *
     * @param storageProvider the storageProvider to use
     */
    public JobScheduler(StorageProvider storageProvider) {
        this(storageProvider, emptyList());
    }

    /**
     * Creates a new JobScheduler using the provided storageProvider and the list of JobFilters that will be used for every background job
     *
     * @param storageProvider the storageProvider to use
     * @param jobFilters      list of jobFilters that will be used for every job
     */
    public JobScheduler(StorageProvider storageProvider, List<JobFilter> jobFilters) {
        this(storageProvider, new JobDetailsAsmGenerator(), jobFilters);
    }

    JobScheduler(StorageProvider storageProvider, JobDetailsGenerator jobDetailsGenerator, List<JobFilter> jobFilters) {
        if (storageProvider == null) throw new IllegalArgumentException("A JobStorageProvider is required to use the JobScheduler. Please see the documentation on how to setup a JobStorageProvider");
        this.storageProvider = storageProvider;
        this.jobDetailsGenerator = jobDetailsGenerator;
        this.jobFilterUtils = new JobFilterUtils(new JobDefaultFilters(jobFilters));
    }

    /**
     * Creates a new fire-and-forget job based on a given lambda.
     * <h5>An example:</h5>
     * <pre>{@code
     *            MyService service = new MyService();
     *            jobScheduler.enqueue(() -> service.doWork());
     *       }</pre>
     *
     * @param job the lambda which defines the fire-and-forget job
     * @return the id of the job
     */
    public JobId enqueue(JobLambda job) {
        JobDetails jobDetails = jobDetailsGenerator.toJobDetails(job);
        return enqueue(jobDetails);
    }

    /**
     * Creates new fire-and-forget jobs for each item in the input stream using the lambda passed as {@code jobFromStream}.
     * <h5>An example:</h5>
     * <pre>{@code
     *      MyService service = new MyService();
     *      Stream<UUID> workStream = getWorkStream();
     *      jobScheduler.enqueue(workStream, (uuid) -> service.doWork(uuid));
     * }</pre>
     *
     * @param input         the stream of items for which to create fire-and-forget jobs
     * @param jobFromStream the lambda which defines the fire-and-forget job to create for each item in the {@code input}
     */
    public <T> void enqueue(Stream<T> input, JobLambdaFromStream<T> jobFromStream) {
        input
                .map(x -> jobDetailsGenerator.toJobDetails(x, jobFromStream))
                .map(Job::new)
                .collect(batchCollector(BATCH_SIZE, this::saveJobs));
    }

    /**
     * Creates a new fire-and-forget job based on a given lambda. The IoC container will be used to resolve {@code MyService}.
     * <h5>An example:</h5>
     * <pre>{@code
     *            jobScheduler.<MyService>enqueue(x -> x.doWork());
     *       }</pre>
     *
     * @param iocJob the lambda which defines the fire-and-forget job
     * @return the id of the job
     */
    public <S> JobId enqueue(IocJobLambda<S> iocJob) {
        JobDetails jobDetails = jobDetailsGenerator.toJobDetails(iocJob);
        return enqueue(jobDetails);
    }

    /**
     * Creates new fire-and-forget jobs for each item in the input stream using the lambda passed as {@code jobFromStream}. The IoC container will be used to resolve {@code MyService}.
     * <h5>An example:</h5>
     * <pre>{@code
     *      Stream<UUID> workStream = getWorkStream();
     *      jobScheduler.<MyService, UUID>enqueue(workStream, (x, uuid) -> x.doWork(uuid));
     * }</pre>
     *
     * @param input            the stream of items for which to create fire-and-forget jobs
     * @param iocJobFromStream the lambda which defines the fire-and-forget job to create for each item in the {@code input}
     */
    public <S, T> void enqueue(Stream<T> input, IocJobLambdaFromStream<S, T> iocJobFromStream) {
        input
                .map(x -> jobDetailsGenerator.toJobDetails(x, iocJobFromStream))
                .map(Job::new)
                .collect(batchCollector(BATCH_SIZE, this::saveJobs));
    }

    /**
     * Creates a new fire-and-forget job based on the given lambda and schedules it to be enqueued at the given moment of time.
     * <h5>An example:</h5>
     * <pre>{@code
     *      MyService service = new MyService();
     *      jobScheduler.schedule(() -> service.doWork(), ZonedDateTime.now().plusHours(5));
     * }</pre>
     *
     * @param job           the lambda which defines the fire-and-forget job
     * @param zonedDateTime The moment in time at which the job will be enqueued.
     * @return the id of the Job
     */
    public JobId schedule(JobLambda job, ZonedDateTime zonedDateTime) {
        return schedule(job, zonedDateTime.toInstant());
    }

    /**
     * Creates a new fire-and-forget job based on the given lambda and schedules it to be enqueued at the given moment of time. The IoC container will be used to resolve {@code MyService}.
     * <h5>An example:</h5>
     * <pre>{@code
     *      jobScheduler.<MyService>schedule(x -> x.doWork(), ZonedDateTime.now().plusHours(5));
     * }</pre>
     *
     * @param iocJob        the lambda which defines the fire-and-forget job
     * @param zonedDateTime The moment in time at which the job will be enqueued.
     * @return the id of the Job
     */
    public <S> JobId schedule(IocJobLambda<S> iocJob, ZonedDateTime zonedDateTime) {
        return schedule(iocJob, zonedDateTime.toInstant());
    }

    /**
     * Creates a new fire-and-forget job based on the given lambda and schedules it to be enqueued at the given moment of time.
     * <h5>An example:</h5>
     * <pre>{@code
     *      MyService service = new MyService();
     *      jobScheduler.schedule(() -> service.doWork(), OffsetDateTime.now().plusHours(5));
     * }</pre>
     *
     * @param job            the lambda which defines the fire-and-forget job
     * @param offsetDateTime The moment in time at which the job will be enqueued.
     * @return the id of the Job
     */
    public JobId schedule(JobLambda job, OffsetDateTime offsetDateTime) {
        return schedule(job, offsetDateTime.toInstant());
    }

    /**
     * Creates a new fire-and-forget job based on the given lambda and schedules it to be enqueued at the given moment of time. The IoC container will be used to resolve {@code MyService}.
     * <h5>An example:</h5>
     * <pre>{@code
     *      jobScheduler.<MyService>schedule(x -> x.doWork(), OffsetDateTime.now().plusHours(5));
     * }</pre>
     *
     * @param iocJob         the lambda which defines the fire-and-forget job
     * @param offsetDateTime The moment in time at which the job will be enqueued.
     * @return the id of the Job
     */
    public <S> JobId schedule(IocJobLambda<S> iocJob, OffsetDateTime offsetDateTime) {
        return schedule(iocJob, offsetDateTime.toInstant());
    }

    /**
     * Creates a new fire-and-forget job based on the given lambda and schedules it to be enqueued at the given moment of time.
     * <h5>An example:</h5>
     * <pre>{@code
     *      MyService service = new MyService();
     *      jobScheduler.schedule(() -> service.doWork(), LocalDateTime.now().plusHours(5));
     * }</pre>
     *
     * @param job           the lambda which defines the fire-and-forget job
     * @param localDateTime The moment in time at which the job will be enqueued. It will use the systemDefault ZoneId to transform it to an UTC Instant
     * @return the id of the Job
     */
    public JobId schedule(JobLambda job, LocalDateTime localDateTime) {
        return schedule(job, localDateTime.atZone(systemDefault()).toInstant());
    }

    /**
     * Creates a new fire-and-forget job based on the given lambda and schedules it to be enqueued at the given moment of time. The IoC container will be used to resolve {@code MyService}.
     * <h5>An example:</h5>
     * <pre>{@code
     *      jobScheduler.<MyService>schedule(x -> x.doWork(), LocalDateTime.now().plusHours(5));
     * }</pre>
     *
     * @param iocJob        the lambda which defines the fire-and-forget job
     * @param localDateTime The moment in time at which the job will be enqueued. It will use the systemDefault ZoneId to transform it to an UTC Instant
     * @return the id of the Job
     */
    public <S> JobId schedule(IocJobLambda<S> iocJob, LocalDateTime localDateTime) {
        return schedule(iocJob, localDateTime.atZone(systemDefault()).toInstant());
    }

    /**
     * Creates a new fire-and-forget job based on the given lambda and schedules it to be enqueued at the given moment of time.
     * <h5>An example:</h5>
     * <pre>{@code
     *      MyService service = new MyService();
     *      jobScheduler.schedule(() -> service.doWork(), Instant.now().plusHours(5));
     * }</pre>
     *
     * @param job     the lambda which defines the fire-and-forget job
     * @param instant The moment in time at which the job will be enqueued.
     * @return the id of the Job
     */
    public JobId schedule(JobLambda job, Instant instant) {
        JobDetails jobDetails = jobDetailsGenerator.toJobDetails(job);
        return schedule(jobDetails, instant);
    }

    /**
     * Creates a new fire-and-forget job based on the given lambda and schedules it to be enqueued at the given moment of time. The IoC container will be used to resolve {@code MyService}.
     * <h5>An example:</h5>
     * <pre>{@code
     *      jobScheduler.<MyService>schedule(x -> x.doWork(), Instant.now().plusHours(5));
     * }</pre>
     *
     * @param iocJob  the lambda which defines the fire-and-forget job
     * @param instant The moment in time at which the job will be enqueued.
     * @return the id of the Job
     */
    public <S> JobId schedule(IocJobLambda<S> iocJob, Instant instant) {
        JobDetails jobDetails = jobDetailsGenerator.toJobDetails(iocJob);
        return schedule(jobDetails, instant);
    }

    /**
     * Deletes a job and sets it's state to DELETED. If the job is being processed, it will be interrupted.
     *
     * @param id the id of the job
     */
    public void delete(UUID id) {
        storageProvider.delete(id);
    }

    /**
     * @see #delete(UUID)
     */
    public void delete(JobId jobId) {
        storageProvider.delete(jobId.asUUID());
    }

    /**
     * Creates a new recurring job based on the given lambda and the given cron expression. The jobs will be scheduled using the systemDefault timezone.
     * <h5>An example:</h5>
     * <pre>{@code
     *      MyService service = new MyService();
     *      jobScheduler.scheduleRecurrently(() -> service.doWork(), Cron.daily());
     * }</pre>
     *
     * @param job  the lambda which defines the fire-and-forget job
     * @param cron The cron expression defining when to run this recurring job
     * @return the id of this recurring job which can be used to alter or delete it
     */
    public String scheduleRecurrently(JobLambda job, String cron) {
        return scheduleRecurrently(null, job, cron);
    }

    /**
     * Creates a new recurring job based on the given lambda and the given cron expression. The IoC container will be used to resolve {@code MyService}. The jobs will be scheduled using the systemDefault timezone.
     * <h5>An example:</h5>
     * <pre>{@code
     *      jobScheduler.<MyService>scheduleRecurrently(x -> x.doWork(), Cron.daily());
     * }</pre>
     *
     * @param iocJob the lambda which defines the fire-and-forget job
     * @param cron   The cron expression defining when to run this recurring job
     * @return the id of this recurring job which can be used to alter or delete it
     */
    public <S> String scheduleRecurrently(IocJobLambda<S> iocJob, String cron) {
        return scheduleRecurrently(null, iocJob, cron);
    }

    /**
     * Creates a new recurring job based on the given id, lambda and cron expression. The jobs will be scheduled using the systemDefault timezone
     * <h5>An example:</h5>
     * <pre>{@code
     *      MyService service = new MyService();
     *      jobScheduler.scheduleRecurrently("my-recurring-job", () -> service.doWork(), Cron.daily());
     * }</pre>
     *
     * @param id   the id of this recurring job which can be used to alter or delete it
     * @param job  the lambda which defines the fire-and-forget job
     * @param cron The cron expression defining when to run this recurring job
     * @return the id of this recurring job which can be used to alter or delete it
     */
    public String scheduleRecurrently(String id, JobLambda job, String cron) {
        return scheduleRecurrently(id, job, cron, systemDefault());
    }

    /**
     * Creates a new recurring job based on the given id, lambda and cron expression. The IoC container will be used to resolve {@code MyService}. The jobs will be scheduled using the systemDefault timezone
     * <h5>An example:</h5>
     * <pre>{@code
     *      jobScheduler.<MyService>scheduleRecurrently("my-recurring-job", x -> x.doWork(), Cron.daily());
     * }</pre>
     *
     * @param id     the id of this recurring job which can be used to alter or delete it
     * @param iocJob the lambda which defines the fire-and-forget job
     * @param cron   The cron expression defining when to run this recurring job
     * @return the id of this recurring job which can be used to alter or delete it
     */
    public <S> String scheduleRecurrently(String id, IocJobLambda<S> iocJob, String cron) {
        return scheduleRecurrently(id, iocJob, cron, systemDefault());
    }

    /**
     * Creates a new recurring job based on the given id, lambda, cron expression and {@code ZoneId}.
     * <h5>An example:</h5>
     * <pre>{@code
     *      MyService service = new MyService();
     *      jobScheduler.scheduleRecurrently("my-recurring-job", () -> service.doWork(), Cron.daily(), ZoneId.of("Europe/Brussels"));
     * }</pre>
     *
     * @param id     the id of this recurring job which can be used to alter or delete it
     * @param job    the lambda which defines the fire-and-forget job
     * @param cron   The cron expression defining when to run this recurring job
     * @param zoneId The zoneId (timezone) of when to run this recurring job
     * @return the id of this recurring job which can be used to alter or delete it
     */
    public String scheduleRecurrently(String id, JobLambda job, String cron, ZoneId zoneId) {
        JobDetails jobDetails = jobDetailsGenerator.toJobDetails(job);
        return scheduleRecurrently(id, jobDetails, CronExpression.create(cron), zoneId);
    }

    /**
     * Creates a new recurring job based on the given id, lambda, cron expression and {@code ZoneId}. The IoC container will be used to resolve {@code MyService}.
     * <h5>An example:</h5>
     * <pre>{@code
     *      jobScheduler.<MyService>scheduleRecurrently("my-recurring-job", x -> x.doWork(), Cron.daily(), ZoneId.of("Europe/Brussels"));
     * }</pre>
     *
     * @param id     the id of this recurring job which can be used to alter or delete it
     * @param iocJob the lambda which defines the fire-and-forget job
     * @param cron   The cron expression defining when to run this recurring job
     * @param zoneId The zoneId (timezone) of when to run this recurring job
     * @return the id of this recurring job which can be used to alter or delete it
     */
    public <S> String scheduleRecurrently(String id, IocJobLambda<S> iocJob, String cron, ZoneId zoneId) {
        JobDetails jobDetails = jobDetailsGenerator.toJobDetails(iocJob);
        return scheduleRecurrently(id, jobDetails, CronExpression.create(cron), zoneId);
    }

    /**
     * Deletes the recurring job based on the given id.
     * <h5>An example:</h5>
     * <pre>{@code
     *      jobScheduler.delete("my-recurring-job"));
     * }</pre>
     *
     * @param id the id of the recurring job to delete
     */
    public void delete(String id) {
        this.storageProvider.deleteRecurringJob(id);
    }

    /**
     * Utility method to register the shutdown of JobRunr in various containers - it is even automatically called by Spring Framework.
     * Note that this will stop the BackgroundJobServer, the Dashboard and the StorageProvider. JobProcessing will stop and enqueueing new jobs will fail.
     */
    public void shutdown() {
        JobRunr.destroy();
    }

    JobId enqueue(JobDetails jobDetails) {
        return saveJob(new Job(jobDetails));
    }

    JobId schedule(JobDetails jobDetails, Instant scheduleAt) {
        return saveJob(new Job(jobDetails, new ScheduledState(scheduleAt)));
    }

    JobId saveJob(Job job) {
        jobFilterUtils.runOnCreatingFilter(job);
        Job savedJob = this.storageProvider.save(job);
        jobFilterUtils.runOnCreatedFilter(savedJob);
        return new JobId(savedJob.getId());
    }

    List<Job> saveJobs(List<Job> jobs) {
        jobFilterUtils.runOnCreatingFilter(jobs);
        final List<Job> savedJobs = this.storageProvider.save(jobs);
        jobFilterUtils.runOnCreatedFilter(savedJobs);
        return savedJobs;
    }

    String scheduleRecurrently(String id, JobDetails jobDetails, CronExpression cronExpression, ZoneId zoneId) {
        final RecurringJob recurringJob = new RecurringJob(id, jobDetails, cronExpression, zoneId);
        jobFilterUtils.runOnCreatingFilter(recurringJob);
        RecurringJob savedRecurringJob = this.storageProvider.saveRecurringJob(recurringJob);
        jobFilterUtils.runOnCreatedFilter(recurringJob);
        return savedRecurringJob.getId();
    }
}
