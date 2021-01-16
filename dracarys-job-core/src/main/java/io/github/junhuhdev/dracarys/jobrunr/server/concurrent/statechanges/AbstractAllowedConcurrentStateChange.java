package io.github.junhuhdev.dracarys.jobrunr.server.concurrent.statechanges;

import io.github.junhuhdev.dracarys.jobrunr.jobs.Job;
import io.github.junhuhdev.dracarys.jobrunr.jobs.states.StateName;
import io.github.junhuhdev.dracarys.jobrunr.server.concurrent.ConcurrentJobModificationResolveResult;

public abstract class AbstractAllowedConcurrentStateChange implements AllowedConcurrentStateChange {

    private final StateName localState;
    private final StateName storageProviderState;

    protected AbstractAllowedConcurrentStateChange(StateName localState, StateName storageProviderState) {
        this.localState = localState;
        this.storageProviderState = storageProviderState;
    }

    public boolean matches(StateName localState, StateName storageProviderState) {
        return this.localState == localState && this.storageProviderState == storageProviderState;
    }

    @Override
    public ConcurrentJobModificationResolveResult resolve(Job localJob, Job storageProviderJob) {
        //nothing more we can do
        return ConcurrentJobModificationResolveResult.succeeded(localJob);
    }

}
