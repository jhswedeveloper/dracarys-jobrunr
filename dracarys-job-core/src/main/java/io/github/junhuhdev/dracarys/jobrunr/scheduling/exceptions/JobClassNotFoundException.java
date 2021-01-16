package io.github.junhuhdev.dracarys.jobrunr.scheduling.exceptions;

import io.github.junhuhdev.dracarys.jobrunr.jobs.JobDetails;

public class JobClassNotFoundException extends JobNotFoundException {

    public JobClassNotFoundException(JobDetails jobDetails) {
        super(jobDetails);
    }

    public JobClassNotFoundException(String message) {
        super(message);
    }
}
