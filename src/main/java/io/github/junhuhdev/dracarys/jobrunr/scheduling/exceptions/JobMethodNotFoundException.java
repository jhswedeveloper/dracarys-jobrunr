package io.github.junhuhdev.dracarys.jobrunr.scheduling.exceptions;

import org.jobrunr.jobs.JobDetails;
import org.jobrunr.scheduling.exceptions.JobNotFoundException;

public class JobMethodNotFoundException extends JobNotFoundException {

    public JobMethodNotFoundException(JobDetails jobDetails) {
        super(jobDetails);
    }

    public JobMethodNotFoundException(String message) {
        super(message);
    }
}
