package io.github.junhuhdev.dracarys.jobrunr.server;

import org.jobrunr.jobs.Job;
import org.jobrunr.jobs.RecurringJob;
import org.jobrunr.jobs.filters.JobDefaultFilters;
import org.jobrunr.jobs.filters.JobFilter;
import org.jobrunr.server.BackgroundJobPerformer;
import org.jobrunr.server.BackgroundJobServerConfiguration;
import org.jobrunr.server.JobActivator;
import org.jobrunr.server.JobZooKeeper;
import org.jobrunr.server.ServerZooKeeper;
import org.jobrunr.server.jmx.BackgroundJobServerMBean;
import org.jobrunr.server.runner.BackgroundJobRunner;
import org.jobrunr.server.runner.BackgroundJobWithIocRunner;
import org.jobrunr.server.runner.BackgroundJobWithoutIocRunner;
import org.jobrunr.server.runner.BackgroundStaticJobWithoutIocRunner;
import org.jobrunr.server.tasks.CheckIfAllJobsExistTask;
import org.jobrunr.server.threadpool.JobRunrExecutor;
import org.jobrunr.server.threadpool.ScheduledThreadPoolJobRunrExecutor;
import org.jobrunr.storage.BackgroundJobServerStatus;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.ThreadSafeStorageProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ServiceLoader;
import java.util.Spliterator;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.compare;
import static java.util.Arrays.asList;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;
import static org.jobrunr.JobRunrException.problematicConfigurationException;
import static org.jobrunr.server.BackgroundJobServerConfiguration.usingStandardBackgroundJobServerConfiguration;

public class BackgroundJobServer implements BackgroundJobServerMBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(org.jobrunr.server.BackgroundJobServer.class);

    private final BackgroundJobServerStatus serverStatus;
    private final StorageProvider storageProvider;
    private final List<BackgroundJobRunner> backgroundJobRunners;
    private final ServerZooKeeper serverZooKeeper;
    private final JobZooKeeper jobZooKeeper;
    private volatile Boolean isMaster;

    private ScheduledThreadPoolExecutor zookeeperThreadPool;
    private JobRunrExecutor jobExecutor;
    private JobDefaultFilters jobDefaultFilters;

    public BackgroundJobServer(StorageProvider storageProvider) {
        this(storageProvider, null);
    }

    public BackgroundJobServer(StorageProvider storageProvider, JobActivator jobActivator) {
        this(storageProvider, jobActivator, usingStandardBackgroundJobServerConfiguration());
    }

    public BackgroundJobServer(StorageProvider storageProvider, JobActivator jobActivator, BackgroundJobServerConfiguration configuration) {
        if (storageProvider == null)
            throw new IllegalArgumentException("A JobStorageProvider is required to use the JobScheduler. Please see the documentation on how to setup a JobStorageProvider");

        this.serverStatus = new BackgroundJobServerStatus(configuration.backgroundJobServerWorkerPolicy.getWorkerCount(), configuration.pollIntervalInSeconds, configuration.deleteSucceededJobsAfter, configuration.permanentlyDeleteDeletedJobsAfter);
        this.storageProvider = new ThreadSafeStorageProvider(storageProvider);
        this.backgroundJobRunners = initializeBackgroundJobRunners(jobActivator);
        this.jobDefaultFilters = new JobDefaultFilters();
        this.serverZooKeeper = createServerZooKeeper();
        this.jobZooKeeper = createJobZooKeeper();
    }

    public UUID getId() {
        return serverStatus.getId();
    }

    public void start() {
        if (isStarted()) return;
        serverStatus.start();
        startZooKeepers();
        startWorkers();
        runStartupTasks();
    }

    public void pauseProcessing() {
        if (isStopped()) throw new IllegalStateException("First start the BackgroundJobServer before pausing");
        if (isPaused()) return;
        serverStatus.pause();
        stopWorkers();
        LOGGER.info("Paused job processing");
    }

    public void resumeProcessing() {
        if (isStopped()) throw new IllegalStateException("First start the BackgroundJobServer before resuming");
        if (isProcessing()) return;
        startWorkers();
        serverStatus.resume();
        LOGGER.info("Resumed job processing");
    }

    public void stop() {
        if (isStopped()) return;
        isMaster = null;
        stopWorkers();
        stopZooKeepers();
        serverStatus.stop();
        LOGGER.info("BackgroundJobServer and BackgroundJobPerformers stopped");
    }

    public boolean isAnnounced() {
        return isMaster != null;
    }

    public boolean isUnAnnounced() {
        return !isAnnounced();
    }

    public boolean isMaster() {
        return isAnnounced() && isMaster;
    }

    synchronized void setIsMaster(Boolean isMaster) {
        this.isMaster = isMaster;
        if (isMaster != null) {
            LOGGER.info("JobRunr BackgroundJobServer ({}) and {} BackgroundJobPerformers started successfully", getId(), serverStatus.getWorkerPoolSize());
        } else {
            LOGGER.error("JobRunr BackgroundJobServer failed to start");
        }
    }

    public boolean isRunning() {
        return serverStatus.isRunning();
    }

    public BackgroundJobServerStatus getServerStatus() {
        return serverStatus;
    }

    public JobZooKeeper getJobZooKeeper() {
        return jobZooKeeper;
    }

    public StorageProvider getStorageProvider() {
        return storageProvider;
    }

    public void setJobFilters(List<JobFilter> jobFilters) {
        this.jobDefaultFilters = new JobDefaultFilters(jobFilters);
    }

    JobDefaultFilters getJobFilters() {
        return jobDefaultFilters;
    }

    BackgroundJobRunner getBackgroundJobRunner(Job job) {
        return backgroundJobRunners.stream()
                .filter(jobRunner -> jobRunner.supports(job))
                .findFirst()
                .orElseThrow(() -> problematicConfigurationException("Could not find a BackgroundJobRunner: either no JobActivator is registered, your Background Job Class is not registered within the IoC container or your Job does not have a default no-arg constructor."));
    }

    void processJob(Job job) {
        BackgroundJobPerformer backgroundJobPerformer = new BackgroundJobPerformer(this, job);
        jobExecutor.execute(backgroundJobPerformer);
        LOGGER.debug("Submitted BackgroundJobPerformer for job {} to executor service", job.getId());
    }

    void scheduleJob(RecurringJob recurringJob) {
        Job job = recurringJob.toScheduledJob();
        this.storageProvider.save(job);
    }

    boolean isStarted() {
        return !isStopped();
    }

    boolean isStopped() {
        return zookeeperThreadPool == null;
    }

    boolean isPaused() {
        return !isProcessing();
    }

    boolean isProcessing() {
        return serverStatus.isRunning();
    }

    private void startZooKeepers() {
        zookeeperThreadPool = new ScheduledThreadPoolJobRunrExecutor(2, "backgroundjob-zookeeper-pool");
        zookeeperThreadPool.scheduleAtFixedRate(serverZooKeeper, 0, serverStatus.getPollIntervalInSeconds(), TimeUnit.SECONDS);
        zookeeperThreadPool.scheduleAtFixedRate(jobZooKeeper, 1, serverStatus.getPollIntervalInSeconds(), TimeUnit.SECONDS);
    }

    private void stopZooKeepers() {
        serverZooKeeper.stop();
        stop(zookeeperThreadPool);
        this.zookeeperThreadPool = null;
    }

    private void startWorkers() {
        jobExecutor = loadJobRunrExecutor();
        jobExecutor.start();
    }

    private void stopWorkers() {
        if (jobExecutor == null) return;
        jobExecutor.stop();
        this.jobExecutor = null;
    }

    private void runStartupTasks() {
        try {
            List<Runnable> startupTasks = asList(new CheckIfAllJobsExistTask(this));
            startupTasks.forEach(jobExecutor::execute);
        } catch (Exception notImportant) {
            // server is shut down immediately
        }
    }

    private List<BackgroundJobRunner> initializeBackgroundJobRunners(JobActivator jobActivator) {
        return asList(
                new BackgroundStaticJobWithoutIocRunner(),
                new BackgroundJobWithIocRunner(jobActivator),
                new BackgroundJobWithoutIocRunner()
        );
    }

    private void stop(ScheduledExecutorService executorService) {
        if (executorService == null) return;
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private ServerZooKeeper createServerZooKeeper() {
        return new ServerZooKeeper(this);
    }

    private JobZooKeeper createJobZooKeeper() {
        return new JobZooKeeper(this);
    }

    private JobRunrExecutor loadJobRunrExecutor() {
        ServiceLoader<JobRunrExecutor> serviceLoader = ServiceLoader.load(JobRunrExecutor.class);
        return stream(spliteratorUnknownSize(serviceLoader.iterator(), Spliterator.ORDERED), false)
                .sorted((a, b) -> compare(b.getPriority(), a.getPriority()))
                .findFirst()
                .orElse(new ScheduledThreadPoolJobRunrExecutor(serverStatus.getWorkerPoolSize(), "backgroundjob-worker-pool"));
    }

}
