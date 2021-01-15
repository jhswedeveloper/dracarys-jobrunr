package io.github.junhuhdev.dracarys.jobrunr.scheduling.exceptions;

import io.github.junhuhdev.dracarys.jobrunr.common.JobRunrException;

public class FieldNotFoundException extends JobRunrException {

    public FieldNotFoundException(Class<?> clazz, String fieldName) {
        super(clazz + "." + fieldName);
    }
}
