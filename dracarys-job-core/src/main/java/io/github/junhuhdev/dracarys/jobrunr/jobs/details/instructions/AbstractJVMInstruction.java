package io.github.junhuhdev.dracarys.jobrunr.jobs.details.instructions;

import io.github.junhuhdev.dracarys.jobrunr.jobs.details.JobDetailsFinderContext;

public abstract class AbstractJVMInstruction {

    protected final JobDetailsFinderContext jobDetailsBuilder;

    public AbstractJVMInstruction(JobDetailsFinderContext jobDetailsBuilder) {
        this.jobDetailsBuilder = jobDetailsBuilder;
    }

    public abstract Object invokeInstruction();

    public void invokeInstructionAndPushOnStack() {
        Object result = invokeInstruction();
        jobDetailsBuilder.getStack().add(result);
    }
}
