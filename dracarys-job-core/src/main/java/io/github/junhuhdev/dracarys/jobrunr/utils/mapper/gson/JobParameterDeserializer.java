package io.github.junhuhdev.dracarys.jobrunr.utils.mapper.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.junhuhdev.dracarys.jobrunr.jobs.JobParameter;
import io.github.junhuhdev.dracarys.jobrunr.utils.reflection.ReflectionUtils;

import java.lang.reflect.Type;

import static io.github.junhuhdev.dracarys.jobrunr.utils.mapper.JsonMapperUtils.getActualClassName;

public class JobParameterDeserializer implements JsonDeserializer<JobParameter> {

    @Override
    public JobParameter deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String jobParameterMethodType = jsonObject.get("className").getAsString();
        String jobParameterActualType = jsonObject.has("actualClassName") ? jsonObject.get("actualClassName").getAsString() : null;
        return new JobParameter(jobParameterMethodType, deserializeToObject(context, getActualClassName(jobParameterMethodType, jobParameterActualType), jsonObject.get("object")));
    }

    private Object deserializeToObject(JsonDeserializationContext context, String type, JsonElement jsonElement) {
        return context.deserialize(jsonElement, ReflectionUtils.toClass(type));
    }
}
