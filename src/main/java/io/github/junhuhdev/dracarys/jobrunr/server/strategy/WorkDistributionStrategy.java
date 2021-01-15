package io.github.junhuhdev.dracarys.jobrunr.server.strategy;

import io.github.junhuhdev.dracarys.jobrunr.storage.PageRequest;

public interface WorkDistributionStrategy {

    boolean canOnboardNewWork();

    PageRequest getWorkPageRequest();
}
