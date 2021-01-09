package huh.enterprises.dracarysjobrunr.config;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jobrunr.jobs.JobParameter;

import java.lang.reflect.Type;

public class JobParamDeserializer implements JsonDeserializer<JobParameter> {

    @Override
    public JobParameter deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String jobParameterType = jsonObject.get("className").getAsString();
        return new JobParameter(jobParameterType, deserializeToObject(context, jobParameterType, jsonObject.get("object")));
    }

    private Object deserializeToObject(JsonDeserializationContext context, String type, JsonElement jsonElement) {
        return context.deserialize(jsonElement, ReflectionLoader.toClass(type));
    }
}
