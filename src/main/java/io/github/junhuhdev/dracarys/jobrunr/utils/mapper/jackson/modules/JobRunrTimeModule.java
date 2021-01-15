package io.github.junhuhdev.dracarys.jobrunr.utils.mapper.jackson.modules;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.jobrunr.jobs.JobParameter;
import org.jobrunr.utils.mapper.jackson.modules.DurationDeserializer;
import org.jobrunr.utils.mapper.jackson.modules.DurationSerializer;
import org.jobrunr.utils.mapper.jackson.modules.InstantDeserializer;
import org.jobrunr.utils.mapper.jackson.modules.InstantSerializer;
import org.jobrunr.utils.mapper.jackson.modules.JobParameterDeserializer;
import org.jobrunr.utils.mapper.jackson.modules.JobParameterSerializer;

import java.time.Duration;
import java.time.Instant;

public class JobRunrTimeModule extends SimpleModule {

    public JobRunrTimeModule() {
        addSerializer(JobParameter.class, new JobParameterSerializer());
        addDeserializer(JobParameter.class, new JobParameterDeserializer());
        addSerializer(Instant.class, new InstantSerializer());
        addDeserializer(Instant.class, new InstantDeserializer());
        addSerializer(Duration.class, new DurationSerializer());
        addDeserializer(Duration.class, new DurationDeserializer());
    }
}
