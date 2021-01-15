package io.github.junhuhdev.dracarys.jobrunr.dashboard.ui.model.problems;


import io.github.junhuhdev.dracarys.jobrunr.jobs.states.StateName;
import io.github.junhuhdev.dracarys.jobrunr.storage.StorageProvider;
import io.github.junhuhdev.dracarys.jobrunr.storage.listeners.JobStatsChangeListener;

import java.util.Set;
import java.util.stream.Collectors;


public class ProblemsManager implements JobStatsChangeListener {

    private final StorageProvider storageProvider;
    private Problems problems;
    private JobStats jobStats;

    public ProblemsManager(StorageProvider storageProvider) {
        this.storageProvider = storageProvider;
    }

    public Problems getProblems() {
        if (problems == null) {
            initProblems();
        }
        return problems;
    }

    private void initProblems() {
        this.problems = new Problems();
        initScheduledJobNotFoundProblems();
    }

    private void initScheduledJobNotFoundProblems() {
        problems.removeProblemsOfType(JobsNotFoundProblem.TYPE);
        Set<String> jobsThatCannotBeFoundAnymore = storageProvider.getDistinctJobSignatures(StateName.SCHEDULED).stream().filter(jobSignature -> !jobExists(jobSignature)).collect(Collectors.toSet());
        if (!jobsThatCannotBeFoundAnymore.isEmpty()) {
            storageProvider.addJobStorageOnChangeListener(this);
            jobStats = storageProvider.getJobStats();
            problems.addProblem(new JobsNotFoundProblem(jobsThatCannotBeFoundAnymore));
        } else {
            storageProvider.removeJobStorageOnChangeListener(this);
        }
    }

    @Override
    public void onChange(JobStats jobStats) {
        if (jobStats.getScheduled() < this.jobStats.getScheduled()) {
            initScheduledJobNotFoundProblems();
        }
    }
}
