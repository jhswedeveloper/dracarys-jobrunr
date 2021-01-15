package io.github.junhuhdev.dracarys.jobrunr.jobs.mappers;

import io.github.junhuhdev.dracarys.jobrunr.jobs.Job;
import io.github.junhuhdev.dracarys.jobrunr.jobs.RecurringJob;
import io.github.junhuhdev.dracarys.jobrunr.utils.mapper.JsonMapper;

public class JobMapper {

	private final JsonMapper jsonMapper;

	public JobMapper(JsonMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
	}

	public String serializeJob(Job job) {
		return jsonMapper.serialize(job);
	}

	public Job deserializeJob(String serializedJobAsString) {
		return jsonMapper.deserialize(serializedJobAsString, Job.class);
	}

	public String serializeRecurringJob(RecurringJob job) {
		return jsonMapper.serialize(job);
	}

	public RecurringJob deserializeRecurringJob(String serializedJobAsString) {
		return jsonMapper.deserialize(serializedJobAsString, RecurringJob.class);
	}

}
