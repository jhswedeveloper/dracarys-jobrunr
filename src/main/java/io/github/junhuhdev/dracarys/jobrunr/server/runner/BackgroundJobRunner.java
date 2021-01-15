package io.github.junhuhdev.dracarys.jobrunr.server.runner;

import org.jobrunr.jobs.Job;

public interface BackgroundJobRunner {

    boolean supports(Job job);

    void run(Job job) throws Exception;

}
