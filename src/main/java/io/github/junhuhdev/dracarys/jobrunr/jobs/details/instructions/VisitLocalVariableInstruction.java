package io.github.junhuhdev.dracarys.jobrunr.jobs.details.instructions;

import org.jobrunr.jobs.details.JobDetailsFinderContext;
import org.jobrunr.jobs.details.instructions.AbstractJVMInstruction;

public abstract class VisitLocalVariableInstruction extends AbstractJVMInstruction {

    protected int variable;

    public VisitLocalVariableInstruction(JobDetailsFinderContext jobDetailsBuilder) {
        super(jobDetailsBuilder);
    }

    public void load(int var) {
        this.variable = var;
        jobDetailsBuilder.pushInstructionOnStack(this);
    }

    @Override
    public Object invokeInstruction() {
        return jobDetailsBuilder.getLocalVariable(variable);
    }

}
