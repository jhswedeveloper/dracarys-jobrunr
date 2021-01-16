package io.github.junhuhdev.dracarys.jobrunr.jobs.details.instructions;

import io.github.junhuhdev.dracarys.jobrunr.jobs.details.JobDetailsFinderContext;

import static io.github.junhuhdev.dracarys.jobrunr.jobs.details.JobDetailsGeneratorUtils.getObjectViaField;

public class GetFieldInstruction extends VisitFieldInstruction {

    public GetFieldInstruction(JobDetailsFinderContext jobDetailsBuilder) {
        super(jobDetailsBuilder);
    }

    @Override
    public Object invokeInstruction() {
        return getObjectViaField(jobDetailsBuilder.getStack().pollLast(), name);
    }
}
