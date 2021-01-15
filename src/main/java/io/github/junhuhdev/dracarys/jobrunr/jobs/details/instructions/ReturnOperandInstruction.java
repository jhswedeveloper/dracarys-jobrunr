package io.github.junhuhdev.dracarys.jobrunr.jobs.details.instructions;

import org.jobrunr.jobs.details.JobDetailsFinderContext;

public class ReturnOperandInstruction extends ZeroOperandInstruction {

    public ReturnOperandInstruction(JobDetailsFinderContext jobDetailsBuilder) {
        super(jobDetailsBuilder);
    }

    @Override
    public void load() {
        // not needed
    }

    @Override
    public Object invokeInstruction() {
        return null;
    }
}
