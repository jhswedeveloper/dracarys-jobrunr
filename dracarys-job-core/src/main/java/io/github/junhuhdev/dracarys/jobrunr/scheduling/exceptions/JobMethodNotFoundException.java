package io.github.junhuhdev.dracarys.jobrunr.scheduling.exceptions;

import io.github.junhuhdev.dracarys.jobrunr.jobs.JobDetails;

public class JobMethodNotFoundException extends JobNotFoundException {

    public JobMethodNotFoundException(JobDetails jobDetails) {
        super(jobDetails);
    }

    public JobMethodNotFoundException(String message) {
        super(message);
    }
}
