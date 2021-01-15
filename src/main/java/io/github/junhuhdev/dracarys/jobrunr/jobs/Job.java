package io.github.junhuhdev.dracarys.jobrunr.jobs;

import io.github.junhuhdev.dracarys.jobrunr.jobs.states.DeletedState;
import io.github.junhuhdev.dracarys.jobrunr.jobs.states.EnqueuedState;
import io.github.junhuhdev.dracarys.jobrunr.jobs.states.FailedState;
import io.github.junhuhdev.dracarys.jobrunr.jobs.states.ProcessingState;
import io.github.junhuhdev.dracarys.jobrunr.jobs.states.ScheduledState;
import io.github.junhuhdev.dracarys.jobrunr.jobs.states.StateName;
import io.github.junhuhdev.dracarys.jobrunr.jobs.states.SucceededState;
import io.github.junhuhdev.dracarys.jobrunr.server.BackgroundJobServer;
import io.github.junhuhdev.dracarys.jobrunr.storage.ConcurrentJobModificationException;
import io.github.junhuhdev.dracarys.jobrunr.utils.streams.StreamUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;

/**
 * Defines the job with it's JobDetails, History and Job Metadata
 */
public class Job extends AbstractJob {

    private UUID id;
    private ArrayList<JobState> jobHistory;
    private final ConcurrentMap<String, Object> metadata;

    private Job() {
        // used for deserialization
        this.metadata = new ConcurrentHashMap<>();
    }

    public Job(JobDetails jobDetails) {
        this(jobDetails, new EnqueuedState());
    }

    public Job(JobDetails jobDetails, JobState jobState) {
        this(jobDetails, singletonList(jobState));
    }

    public Job(JobDetails jobDetails, List<JobState> jobHistory) {
        this(null, 0, jobDetails, jobHistory, new ConcurrentHashMap<>());
    }

    public Job(UUID id, int version, JobDetails jobDetails, List<JobState> jobHistory, ConcurrentMap<String, Object> metadata) {
        super(jobDetails, version);
        if (jobHistory.isEmpty()) throw new IllegalStateException("A job should have at least one initial state");
        this.id = id;
        this.jobHistory = new ArrayList<>(jobHistory);
        this.metadata = metadata;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public List<JobState> getJobStates() {
        return unmodifiableList(jobHistory);
    }

    public <T extends JobState> Stream<T> getJobStatesOfType(Class<T> clazz) {
        return StreamUtils.ofType(getJobStates(), clazz);
    }

    public <T extends JobState> Optional<T> getLastJobStateOfType(Class<T> clazz) {
        return getJobStatesOfType(clazz).reduce((first, second) -> second);
    }

    public <T extends JobState> T getJobState() {
        return cast(getJobState(-1));
    }

    public JobState getJobState(int element) {
        if (element >= 0) {
            return jobHistory.get(element);
        } else {
            if (Math.abs(element) > jobHistory.size()) return null;
            return jobHistory.get(jobHistory.size() + element);
        }
    }

    public StateName getState() {
        return getJobState().getName();
    }

    protected void addJobState(JobState jobState) {
        this.jobHistory.add(jobState);
    }

    public boolean hasState(StateName state) {
        return getState().equals(state);
    }

    public void enqueue() {
        addJobState(new EnqueuedState());
    }

    public void scheduleAt(Instant instant, String reason) {
        addJobState(new ScheduledState(instant, reason));
    }

    public void startProcessingOn(BackgroundJobServer backgroundJobServer) {
        if (getState() == StateName.PROCESSING) throw new ConcurrentJobModificationException(this);
        addJobState(new ProcessingState(backgroundJobServer.getId()));
    }

    public void updateProcessing() {
        ProcessingState jobState = getJobState();
        jobState.setUpdatedAt(Instant.now());
    }

    public void succeeded() {
        Optional<EnqueuedState> lastEnqueuedState = getLastJobStateOfType(EnqueuedState.class);
        if (!lastEnqueuedState.isPresent())
            throw new IllegalStateException("Job cannot succeed if it was not enqueued before.");

        Duration latencyDuration = Duration.between(lastEnqueuedState.get().getEnqueuedAt(), getJobState().getCreatedAt());
        Duration processDuration = Duration.between(getJobState().getCreatedAt(), Instant.now());
        addJobState(new SucceededState(latencyDuration, processDuration));
    }

    public void failed(String message, Exception exception) {
        addJobState(new FailedState(message, exception));
    }

    public void delete() {
        addJobState(new DeletedState());
    }

    public Instant getCreatedAt() {
        return getJobState(0).getCreatedAt();
    }

    public Instant getUpdatedAt() {
        return getJobState().getUpdatedAt();
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", jobSignature='" + getJobSignature() + '\'' +
                ", jobName='" + getJobName() + '\'' +
                ", updatedAt='" + getUpdatedAt() + '\'' +
                '}';
    }
}
