package io.github.junhuhdev.dracarys.jobrunr.dashboard.ui.model.problems;

import org.jobrunr.dashboard.ui.model.problems.Problem;

import java.util.ArrayList;

public class Problems extends ArrayList<Problem> {

    public void addProblem(Problem problem) {
        this.add(problem);
    }

    public void removeProblemsOfType(String type) {
        removeIf(problem -> type.equals(problem.type));
    }
}
