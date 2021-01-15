package io.github.junhuhdev.dracarys.jobrunr.jobs.details.instructions;

import org.jobrunr.jobs.details.JobDetailsFinderContext;
import org.jobrunr.jobs.details.instructions.AbstractJVMInstruction;

public class SingleIntOperandInstruction extends AbstractJVMInstruction {

    private int intValue;

    public SingleIntOperandInstruction(JobDetailsFinderContext jobDetailsBuilder) {
        super(jobDetailsBuilder);
    }

    public void load(int intValue) {
        this.intValue = intValue;
        jobDetailsBuilder.pushInstructionOnStack(this);
    }

    @Override
    public Object invokeInstruction() {
        return intValue;
    }
}
