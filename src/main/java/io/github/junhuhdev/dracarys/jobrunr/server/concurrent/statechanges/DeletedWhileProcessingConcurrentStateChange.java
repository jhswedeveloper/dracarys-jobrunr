package io.github.junhuhdev.dracarys.jobrunr.server.concurrent.statechanges;

import org.jobrunr.jobs.Job;
import org.jobrunr.server.JobZooKeeper;
import org.jobrunr.server.concurrent.ConcurrentJobModificationResolveResult;
import org.jobrunr.server.concurrent.statechanges.AbstractAllowedConcurrentStateChange;

import static org.jobrunr.jobs.states.StateName.DELETED;
import static org.jobrunr.jobs.states.StateName.PROCESSING;

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
