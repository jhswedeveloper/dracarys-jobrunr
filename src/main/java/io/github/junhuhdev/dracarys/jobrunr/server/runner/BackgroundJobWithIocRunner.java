package io.github.junhuhdev.dracarys.jobrunr.server.runner;

import org.jobrunr.jobs.Job;
import org.jobrunr.server.JobActivator;

import static org.jobrunr.utils.JobUtils.assertJobExists;
import static org.jobrunr.utils.reflection.ReflectionUtils.toClass;

public class BackgroundJobWithIocRunner extends AbstractBackgroundJobRunner {

    private final JobActivator jobActivator;

    public BackgroundJobWithIocRunner(JobActivator jobActivator) {
        this.jobActivator = jobActivator;
    }

    @Override
    public boolean supports(Job job) {
        assertJobExists(job.getJobDetails());
        if (jobActivator == null) return false;
        return jobActivator.activateJob(toClass(job.getJobDetails().getClassName())) != null;
    }

    @Override
    protected BackgroundJobWorker getBackgroundJobWorker(Job job) {
        return new ConsumerBackgroundJobWorker(jobActivator, job);
    }

    protected static class ConsumerBackgroundJobWorker extends BackgroundJobWorker {

        private final JobActivator jobActivator;

        public ConsumerBackgroundJobWorker(JobActivator jobActivator, Job job) {
            super(job);
            this.jobActivator = jobActivator;
        }

        @Override
        protected Object getJobToPerform(Class<?> jobToPerformClass) {
            return jobActivator.activateJob(jobToPerformClass);
        }
    }
}
