package io.github.junhuhdev.dracarys.jobrunr.server.concurrent.statechanges;

import io.github.junhuhdev.dracarys.jobrunr.jobs.Job;
import io.github.junhuhdev.dracarys.jobrunr.server.JobZooKeeper;
import io.github.junhuhdev.dracarys.jobrunr.server.concurrent.ConcurrentJobModificationResolveResult;

import static io.github.junhuhdev.dracarys.jobrunr.jobs.states.StateName.DELETED;
import static io.github.junhuhdev.dracarys.jobrunr.jobs.states.StateName.PROCESSING;

public class DeletedWhileProcessingConcurrentStateChange extends AbstractAllowedConcurrentStateChange {

    private final JobZooKeeper jobZooKeeper;

    public DeletedWhileProcessingConcurrentStateChange(JobZooKeeper jobZooKeeper) {
        super(PROCESSING, DELETED);
        this.jobZooKeeper = jobZooKeeper;
    }

    @Override
    public ConcurrentJobModificationResolveResult resolve(Job localJob, Job storageProviderJob) {
        localJob.delete();
        final Thread threadProcessingJob = jobZooKeeper.getThreadProcessingJob(localJob);
        if (threadProcessingJob != null) {
            threadProcessingJob.interrupt();
        }
        return ConcurrentJobModificationResolveResult.succeeded(localJob);
    }

}
