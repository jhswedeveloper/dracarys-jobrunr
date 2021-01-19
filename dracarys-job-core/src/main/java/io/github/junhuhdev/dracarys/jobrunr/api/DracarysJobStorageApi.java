package io.github.junhuhdev.dracarys.jobrunr.api;

public interface DracarysJobStorageApi {

	TxCommand findByJobId(String jobId);
}
