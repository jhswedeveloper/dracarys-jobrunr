package io.github.junhuhdev.dracarys.jobrunr.jobs.details.instructions;

import io.github.junhuhdev.dracarys.jobrunr.jobs.details.JobDetailsFinderContext;

public abstract class ZeroOperandInstruction extends AbstractJVMInstruction {

    public ZeroOperandInstruction(JobDetailsFinderContext jobDetailsBuilder) {
        super(jobDetailsBuilder);
    }

    public void load() {
        jobDetailsBuilder.pushInstructionOnStack(this);
    }
}
