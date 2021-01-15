package io.github.junhuhdev.dracarys.jobrunr.server.runner;

import io.github.junhuhdev.dracarys.jobrunr.jobs.Job;
import io.github.junhuhdev.dracarys.jobrunr.jobs.context.JobContext;

public class RunnerJobContext extends JobContext {

    public RunnerJobContext(Job job) {
        super(job);
    }

}
