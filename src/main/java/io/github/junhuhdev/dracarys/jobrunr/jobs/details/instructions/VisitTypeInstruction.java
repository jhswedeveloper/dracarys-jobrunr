package io.github.junhuhdev.dracarys.jobrunr.jobs.details.instructions;

import org.jobrunr.jobs.details.JobDetailsFinderContext;
import org.jobrunr.jobs.details.instructions.AbstractJVMInstruction;

public abstract class VisitTypeInstruction extends AbstractJVMInstruction {

    protected String type;

    public VisitTypeInstruction(JobDetailsFinderContext jobDetailsBuilder) {
        super(jobDetailsBuilder);
    }

    public void load(String type) {
        this.type = type;
        jobDetailsBuilder.pushInstructionOnStack(this);
    }

}
