package io.github.junhuhdev.dracarys.jobrunr.server.strategy;

import org.jobrunr.storage.PageRequest;

public interface WorkDistributionStrategy {

    boolean canOnboardNewWork();

    PageRequest getWorkPageRequest();
}
