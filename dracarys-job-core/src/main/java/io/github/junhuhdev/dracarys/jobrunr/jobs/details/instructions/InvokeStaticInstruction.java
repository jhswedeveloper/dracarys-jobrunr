package io.github.junhuhdev.dracarys.jobrunr.jobs.details.instructions;


import io.github.junhuhdev.dracarys.jobrunr.jobs.details.JobDetailsFinderContext;

import java.util.List;

import static io.github.junhuhdev.dracarys.jobrunr.jobs.details.JobDetailsGeneratorUtils.createObjectViaStaticMethod;
import static io.github.junhuhdev.dracarys.jobrunr.jobs.details.JobDetailsGeneratorUtils.findParamTypesFromDescriptorAsArray;
import static io.github.junhuhdev.dracarys.jobrunr.jobs.details.JobDetailsGeneratorUtils.toFQClassName;

public class InvokeStaticInstruction extends VisitMethodInstruction {

    public InvokeStaticInstruction(JobDetailsFinderContext jobDetailsBuilder) {
        super(jobDetailsBuilder);
    }

    @Override
    public Object invokeInstruction() {
        String className = toFQClassName(owner);
        String methodName = name;
        Class<?>[] paramTypes = findParamTypesFromDescriptorAsArray(descriptor);
        List<Object> parameters = getParametersUsingParamTypes(paramTypes);

        return createObjectViaStaticMethod(className, methodName, paramTypes, parameters.toArray());
    }

}
