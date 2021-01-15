package io.github.junhuhdev.dracarys.jobrunr.dashboard.ui.model.problems;

import org.jobrunr.dashboard.ui.model.problems.Problem;

import java.util.HashSet;
import java.util.Set;

public class JobsNotFoundProblem extends Problem {

    public static final String TYPE = "jobs-not-found";

    private HashSet<String> jobsNotFound;

    public JobsNotFoundProblem(Set<String> jobsNotFound) {
        super(TYPE);
        this.jobsNotFound = new HashSet<>(jobsNotFound);
    }

    public Set<String> getJobsNotFound() {
        return jobsNotFound;
    }
}
