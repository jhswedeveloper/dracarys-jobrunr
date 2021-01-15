package io.github.junhuhdev.dracarys.jobrunr.server.concurrent;

import io.github.junhuhdev.dracarys.jobrunr.jobs.Job;

public class ConcurrentJobModificationResolveResult {

    private final boolean succeeded;
    private final Job job;

    private ConcurrentJobModificationResolveResult(boolean succeeded, Job job) {
        this.succeeded = succeeded;
        this.job = job;
    }

    public static ConcurrentJobModificationResolveResult succeeded(Job job) {
        return new ConcurrentJobModificationResolveResult(true, job);
    }

    public static ConcurrentJobModificationResolveResult failed(Job job) {
        return new ConcurrentJobModificationResolveResult(false, job);
    }

    public boolean failed() {
        return !succeeded;
    }

    public Job getJob() {
        return job;
    }
}
