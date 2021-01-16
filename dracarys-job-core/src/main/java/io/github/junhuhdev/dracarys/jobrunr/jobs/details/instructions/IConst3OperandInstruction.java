package io.github.junhuhdev.dracarys.jobrunr.jobs.details.instructions;

import io.github.junhuhdev.dracarys.jobrunr.jobs.details.JobDetailsFinderContext;

public class IConst3OperandInstruction extends ZeroOperandInstruction {

    public IConst3OperandInstruction(JobDetailsFinderContext jobDetailsBuilder) {
        super(jobDetailsBuilder);
    }

    @Override
    public Object invokeInstruction() {
        return 3;
    }
}
