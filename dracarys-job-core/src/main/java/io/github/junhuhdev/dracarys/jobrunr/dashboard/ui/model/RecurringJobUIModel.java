package io.github.junhuhdev.dracarys.jobrunr.dashboard.ui.model;


import io.github.junhuhdev.dracarys.jobrunr.jobs.RecurringJob;

import java.time.Instant;

public class RecurringJobUIModel extends RecurringJob {

    private Instant nextRun;

    public RecurringJobUIModel(RecurringJob recurringJob) {
        super(recurringJob.getId(), recurringJob.getJobDetails(), recurringJob.getCronExpression(), recurringJob.getZoneId());
        setJobName(recurringJob.getJobName());
        nextRun = super.getNextRun();
    }

    @Override
    public Instant getNextRun() {
        return nextRun;
    }
}
