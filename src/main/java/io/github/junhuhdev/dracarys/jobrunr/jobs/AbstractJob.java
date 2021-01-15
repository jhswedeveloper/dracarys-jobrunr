package io.github.junhuhdev.dracarys.jobrunr.jobs;

import io.github.junhuhdev.dracarys.jobrunr.utils.JobUtils;
import io.github.junhuhdev.dracarys.jobrunr.utils.resilience.Lock;
import io.github.junhuhdev.dracarys.jobrunr.utils.resilience.Lockable;

public abstract class AbstractJob implements Lockable {

	private final transient Lock locker;
	private int version;
	private String jobSignature;
	private String jobName;
	private JobDetails jobDetails;

	protected AbstractJob() {
		// used for deserialization
		this.locker = new Lock();
	}

	public AbstractJob(JobDetails jobDetails) {
		this(jobDetails, 0);
	}

	public AbstractJob(JobDetails jobDetails, int version) {
		this();
		this.jobDetails = jobDetails;
		this.version = version;
		this.jobSignature = JobUtils.getJobSignature(jobDetails);
	}

	public abstract Object getId();

	public int getVersion() {
		return version;
	}

	/**
	 * Increases the version of this Job instance
	 *
	 * @return the version before it was increased
	 */
	public int increaseVersion() {
		return version++;
	}

	public String getJobSignature() {
		return jobSignature;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public JobDetails getJobDetails() {
		return jobDetails;
	}

	@Override
	public Lock lock() {
		return locker.lock();
	}

}
