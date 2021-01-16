package io.github.junhuhdev.dracarys.jobrunr.server.concurrent;


import io.github.junhuhdev.dracarys.jobrunr.jobs.Job;
import io.github.junhuhdev.dracarys.jobrunr.server.JobZooKeeper;
import io.github.junhuhdev.dracarys.jobrunr.server.concurrent.statechanges.AllowedConcurrentStateChange;
import io.github.junhuhdev.dracarys.jobrunr.server.concurrent.statechanges.DeletedWhileFailedConcurrentStateChange;
import io.github.junhuhdev.dracarys.jobrunr.server.concurrent.statechanges.DeletedWhileProcessingConcurrentStateChange;
import io.github.junhuhdev.dracarys.jobrunr.server.concurrent.statechanges.DeletedWhileScheduledConcurrentStateChange;
import io.github.junhuhdev.dracarys.jobrunr.server.concurrent.statechanges.DeletedWhileSucceededConcurrentStateChange;
import io.github.junhuhdev.dracarys.jobrunr.storage.ConcurrentJobModificationException;
import io.github.junhuhdev.dracarys.jobrunr.storage.StorageProvider;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ConcurrentJobModificationResolver {

    private final StorageProvider storageProvider;
    private final List<AllowedConcurrentStateChange> allowedConcurrentStateChanges;

    public ConcurrentJobModificationResolver(StorageProvider storageProvider, JobZooKeeper jobZooKeeper) {
        this.storageProvider = storageProvider;
        allowedConcurrentStateChanges = Arrays.asList(
                new DeletedWhileProcessingConcurrentStateChange(jobZooKeeper),
                new DeletedWhileSucceededConcurrentStateChange(),
                new DeletedWhileFailedConcurrentStateChange(),
                new DeletedWhileScheduledConcurrentStateChange()
        );
    }

    public void resolve(ConcurrentJobModificationException e) {
        final List<Job> concurrentUpdatedJobs = e.getConcurrentUpdatedJobs();
        final List<ConcurrentJobModificationResolveResult> failedToResolve = concurrentUpdatedJobs
                .stream()
                .map(this::resolve)
                .filter(ConcurrentJobModificationResolveResult::failed)
                .collect(toList());

        if (!failedToResolve.isEmpty()) {
            throw new ConcurrentJobModificationException(failedToResolve.stream().map(ConcurrentJobModificationResolveResult::getJob).collect(toList()));
        }
    }

    public ConcurrentJobModificationResolveResult resolve(final Job localJob) {
        final Job storageProviderJob = getJobFromStorageProvider(localJob);
        return allowedConcurrentStateChanges
                .stream()
                .filter(allowedConcurrentStateChange -> allowedConcurrentStateChange.matches(localJob.getState(), storageProviderJob.getState()))
                .findFirst()
                .map(allowedConcurrentStateChange -> allowedConcurrentStateChange.resolve(localJob, storageProviderJob))
                .orElse(ConcurrentJobModificationResolveResult.failed(localJob));
    }

    private Job getJobFromStorageProvider(Job localJob) {
        return storageProvider.getJobById(localJob.getId());
    }

}
