package io.github.junhuhdev.dracarys.jobrunr.jobs.details.instructions;

import io.github.junhuhdev.dracarys.jobrunr.jobs.details.JobDetailsFinderContext;

public abstract class VisitFieldInstruction extends AbstractJVMInstruction {

    protected String owner;
    protected String name;
    protected String descriptor;

    public VisitFieldInstruction(JobDetailsFinderContext jobDetailsBuilder) {
        super(jobDetailsBuilder);
    }

    public void load(String owner, String name, String descriptor) {
        this.owner = owner;
        this.name = name;
        this.descriptor = descriptor;
        jobDetailsBuilder.pushInstructionOnStack(this);
    }
}
