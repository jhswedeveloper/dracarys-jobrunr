package io.github.junhuhdev.dracarys.jobrunr.jobs.filters;

import org.jobrunr.jobs.AbstractJob;
import org.jobrunr.jobs.JobDetails;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.utils.JobUtils;
import org.jobrunr.utils.StringUtils;

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
