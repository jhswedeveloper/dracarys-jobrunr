package io.github.junhuhdev.dracarys.jobrunr.server.runner;

import io.github.junhuhdev.dracarys.jobrunr.jobs.Job;
import io.github.junhuhdev.dracarys.jobrunr.jobs.JobDetails;

import static io.github.junhuhdev.dracarys.jobrunr.utils.JobUtils.assertJobExists;
import static io.github.junhuhdev.dracarys.jobrunr.utils.reflection.ReflectionUtils.hasDefaultNoArgConstructor;

public class BackgroundJobWithoutIocRunner extends AbstractBackgroundJobRunner {

    @Override
    public boolean supports(Job job) {
        JobDetails jobDetails = job.getJobDetails();
        assertJobExists(jobDetails);
        return !jobDetails.getStaticFieldName().isPresent() && hasDefaultNoArgConstructor(jobDetails.getClassName());
    }

    @Override
    protected BackgroundJobWorker getBackgroundJobWorker(Job job) {
        return new BackgroundJobWorker(job);
    }
}
