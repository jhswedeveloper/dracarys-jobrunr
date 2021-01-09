package io.github.junhuhdev.config;

import org.jobrunr.jobs.Job;
import org.jobrunr.jobs.RecurringJob;
import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.utils.mapper.JsonMapper;

public class JobRunrJobMapper extends JobMapper {

	public JobRunrJobMapper() {
		super(new JobRunrGsonMapper());
	}

	public JobRunrJobMapper(JsonMapper jsonMapper) {
		super(new JobRunrGsonMapper());
	}

	@Override
	public String serializeJob(Job job) {
		return super.serializeJob(job);
	}

	@Override
	public Job deserializeJob(String serializedJobAsString) {
		return super.deserializeJob(serializedJobAsString);
	}

	@Override
	public String serializeRecurringJob(RecurringJob job) {
		return super.serializeRecurringJob(job);
	}

	@Override
	public RecurringJob deserializeRecurringJob(String serializedJobAsString) {
		return super.deserializeRecurringJob(serializedJobAsString);
	}

}
