package io.github.junhuhdev.dracarys.jobrunr.jobs.details.instructions;

import io.github.junhuhdev.dracarys.jobrunr.jobs.details.JobDetailsFinderContext;

public class DupOperandInstruction extends ZeroOperandInstruction {

    public DupOperandInstruction(JobDetailsFinderContext jobDetailsBuilder) {
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
