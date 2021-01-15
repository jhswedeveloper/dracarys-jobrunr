package io.github.junhuhdev.dracarys.jobrunr.server.concurrent;

import org.jobrunr.jobs.Job;

public class ConcurrentJobModificationResolveResult {

    private final boolean succeeded;
    private final Job job;

    private ConcurrentJobModificationResolveResult(boolean succeeded, Job job) {
        this.succeeded = succeeded;
        this.job = job;
    }

    public static org.jobrunr.server.concurrent.ConcurrentJobModificationResolveResult succeeded(Job job) {
        return new org.jobrunr.server.concurrent.ConcurrentJobModificationResolveResult(true, job);
    }

    public static org.jobrunr.server.concurrent.ConcurrentJobModificationResolveResult failed(Job job) {
        return new org.jobrunr.server.concurrent.ConcurrentJobModificationResolveResult(false, job);
    }

    public boolean failed() {
        return !succeeded;
    }

    public Job getJob() {
        return job;
    }
}
