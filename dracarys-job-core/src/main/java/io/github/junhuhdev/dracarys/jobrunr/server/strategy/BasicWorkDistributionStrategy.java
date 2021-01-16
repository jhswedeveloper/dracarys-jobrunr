package io.github.junhuhdev.dracarys.jobrunr.server.strategy;

import io.github.junhuhdev.dracarys.jobrunr.server.BackgroundJobServer;
import io.github.junhuhdev.dracarys.jobrunr.server.JobZooKeeper;
import io.github.junhuhdev.dracarys.jobrunr.storage.BackgroundJobServerStatus;
import io.github.junhuhdev.dracarys.jobrunr.storage.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.junhuhdev.dracarys.jobrunr.storage.PageRequest.ascOnUpdatedAt;

public class BasicWorkDistributionStrategy implements WorkDistributionStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicWorkDistributionStrategy.class);

    private final BackgroundJobServerStatus backgroundJobServerStatus;
    private final JobZooKeeper jobZooKeeper;

    public BasicWorkDistributionStrategy(BackgroundJobServer backgroundJobServer, JobZooKeeper jobZooKeeper) {
        this.backgroundJobServerStatus = backgroundJobServer.getServerStatus();
        this.jobZooKeeper = jobZooKeeper;
    }

    @Override
    public boolean canOnboardNewWork() {
        final double occupiedWorkerCount = jobZooKeeper.getOccupiedWorkerCount();
        final double workerPoolSize = backgroundJobServerStatus.getWorkerPoolSize();
        final boolean canOnboardWork = (occupiedWorkerCount / workerPoolSize) < 0.7;
        return canOnboardWork;
    }

    @Override
    public PageRequest getWorkPageRequest() {
        final int occupiedWorkerCount = jobZooKeeper.getOccupiedWorkerCount();
        final int workerPoolSize = backgroundJobServerStatus.getWorkerPoolSize();

        final int limit = workerPoolSize - occupiedWorkerCount;
        LOGGER.debug("Can onboard {} new work (occupiedWorkerCount = {}; workerPoolSize = {}).", limit, occupiedWorkerCount, workerPoolSize);
        return ascOnUpdatedAt(limit);
    }
}
