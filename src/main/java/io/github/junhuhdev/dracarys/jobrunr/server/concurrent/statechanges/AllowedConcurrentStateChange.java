package io.github.junhuhdev.dracarys.jobrunr.server.concurrent.statechanges;

import io.github.junhuhdev.dracarys.jobrunr.jobs.Job;
import io.github.junhuhdev.dracarys.jobrunr.jobs.states.StateName;
import io.github.junhuhdev.dracarys.jobrunr.server.concurrent.ConcurrentJobModificationResolveResult;

public interface AllowedConcurrentStateChange {

    boolean matches(StateName localState, StateName storageProviderState);

    ConcurrentJobModificationResolveResult resolve(Job localJob, Job storageProviderJob);
}
