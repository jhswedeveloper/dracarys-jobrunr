package io.github.junhuhdev.dracarys.jobrunr.storage;

import org.jobrunr.storage.JobStats;
import org.jobrunr.storage.JobStatsExtended;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Math.ceil;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;

public class JobStatsEnricher {

    private final ReentrantLock lock = new ReentrantLock();
    private JobStats firstRelevantJobStats;
    private JobStats previousJobStats;
    private JobStatsExtended jobStatsExtended;

    public JobStatsExtended enrich(JobStats jobStats) {
        JobStats latestJobStats = getLatestJobStats(jobStats, previousJobStats);
        if (lock.tryLock()) {
            setFirstRelevantJobStats(latestJobStats);
            setJobStatsExtended(latestJobStats);
            setPreviousJobStats(latestJobStats);
            lock.unlock();
        }
        if (jobStatsExtended == null) return new JobStatsExtended(latestJobStats);
        return jobStatsExtended;
    }

    private static JobStats getLatestJobStats(JobStats jobStats, JobStats previousJobStats) {
        if (previousJobStats == null) return jobStats;
        if (jobStats.getTimeStamp().isAfter(previousJobStats.getTimeStamp())) return jobStats;
        return previousJobStats;
    }

    private void setFirstRelevantJobStats(JobStats jobStats) {
        if (firstRelevantJobStats == null) {
            firstRelevantJobStats = jobStats;
        } else if (jobStats.getEnqueued() < 1 && jobStats.getProcessing() < 1) {
            firstRelevantJobStats = jobStats;
        } else if (jobStats.getEnqueued() > firstRelevantJobStats.getEnqueued()) {
            firstRelevantJobStats = jobStats;
        }
    }

    private void setJobStatsExtended(JobStats jobStats) {
        JobStats previousJobStats = this.previousJobStats != null ? this.previousJobStats : this.firstRelevantJobStats;
        Long amountSucceeded = jobStats.getSucceeded() - previousJobStats.getSucceeded();
        Long amountFailed = jobStats.getFailed() - previousJobStats.getFailed();
        Instant estimatedProcessingFinishedInstant = estimatedProcessingFinishedInstant(firstRelevantJobStats, jobStats);
        if (estimatedProcessingFinishedInstant != null) {
            jobStatsExtended = new JobStatsExtended(jobStats, amountSucceeded, amountFailed, estimatedProcessingFinishedInstant);
        } else if (jobStatsExtended != null && jobStatsExtended.getEstimation().isEstimatedProcessingFinishedInstantAvailable()) {
            jobStatsExtended = new JobStatsExtended(jobStats, amountSucceeded, amountFailed, jobStatsExtended.getEstimation().getEstimatedProcessingFinishedAt());
        }
    }

    private Instant estimatedProcessingFinishedInstant(JobStats firstRelevantJobStats, JobStats jobStats) {
        if (jobStats.getSucceeded() - firstRelevantJobStats.getSucceeded() < 1) return null;
        BigDecimal durationForAmountSucceededInSeconds = valueOf(Duration.between(firstRelevantJobStats.getTimeStamp(), jobStats.getTimeStamp()).getSeconds());
        if (ZERO.equals(durationForAmountSucceededInSeconds)) return null;
        BigDecimal amountSucceededPerSecond = valueOf(ceil(jobStats.getSucceeded() - firstRelevantJobStats.getSucceeded())).divide(durationForAmountSucceededInSeconds, RoundingMode.CEILING);
        if (ZERO.equals(amountSucceededPerSecond)) return null;
        BigDecimal processingTimeInSeconds = BigDecimal.valueOf(jobStats.getEnqueued() + jobStats.getProcessing()).divide(amountSucceededPerSecond, RoundingMode.HALF_UP);
        return Instant.now().plusSeconds(processingTimeInSeconds.longValue());
    }

    private void setPreviousJobStats(JobStats jobStats) {
        previousJobStats = jobStats;
    }

}
