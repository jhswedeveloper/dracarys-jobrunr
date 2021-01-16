package io.github.junhuhdev.dracarys.jobrunr.jobs.filters;


import io.github.junhuhdev.dracarys.jobrunr.jobs.AbstractJob;
import io.github.junhuhdev.dracarys.jobrunr.jobs.JobDetails;
import io.github.junhuhdev.dracarys.jobrunr.jobs.annotations.Job;
import io.github.junhuhdev.dracarys.jobrunr.utils.JobUtils;
import io.github.junhuhdev.dracarys.jobrunr.utils.StringUtils;

import java.util.Optional;

public class DisplayNameFilter implements JobClientFilter {

    @Override
    public void onCreating(AbstractJob job) {
        JobDetails jobDetails = job.getJobDetails();
        Optional<String> jobNameFromAnnotation = getJobNameFromAnnotation(jobDetails);
        if (jobNameFromAnnotation.isPresent()) {
            job.setJobName(getNameWithResolvedParameters(jobNameFromAnnotation.get(), jobDetails));
        } else {
            job.setJobName(JobUtils.getReadableNameFromJobDetails(jobDetails));
        }
    }

    @Override
    public void onCreated(AbstractJob job) {
        // nothing to do
    }

    private Optional<String> getJobNameFromAnnotation(JobDetails jobDetails) {
        Optional<Job> jobAnnotation = JobUtils.getJobAnnotation(jobDetails);
        return jobAnnotation
                .map(Job::name)
                .filter(StringUtils::isNotNullOrEmpty);
    }

    private String getNameWithResolvedParameters(String name, JobDetails jobDetails) {
        String finalName = name;
        for (int i = 0; i < jobDetails.getJobParameters().size(); i++) {
            finalName = finalName.replace("%" + i, jobDetails.getJobParameterValues()[i].toString());
        }
        return finalName;
    }
}
