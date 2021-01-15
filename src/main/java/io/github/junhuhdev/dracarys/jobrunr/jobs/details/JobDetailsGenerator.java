package io.github.junhuhdev.dracarys.jobrunr.jobs.details;

import io.github.junhuhdev.dracarys.jobrunr.jobs.JobDetails;
import io.github.junhuhdev.dracarys.jobrunr.jobs.lambdas.IocJobLambdaFromStream;
import io.github.junhuhdev.dracarys.jobrunr.jobs.lambdas.JobLambdaFromStream;
import io.github.junhuhdev.dracarys.jobrunr.jobs.lambdas.JobRunrJob;

public interface JobDetailsGenerator {

    <T extends JobRunrJob> JobDetails toJobDetails(T lambda);

    <T> JobDetails toJobDetails(T itemFromStream, JobLambdaFromStream<T> lambda);

    <S, T> JobDetails toJobDetails(T itemFromStream, IocJobLambdaFromStream<S, T> lambda);
}
