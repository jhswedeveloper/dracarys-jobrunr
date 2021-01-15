package io.github.junhuhdev.dracarys.jobrunr.scheduling.exceptions;

import org.jobrunr.jobs.JobDetails;
import org.jobrunr.scheduling.exceptions.JobNotFoundException;

public class JobClassNotFoundException extends JobNotFoundException {

    public JobClassNotFoundException(JobDetails jobDetails) {
        super(jobDetails);
    }

    public JobClassNotFoundException(String message) {
        super(message);
    }
}
