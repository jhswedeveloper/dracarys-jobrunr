package io.github.junhuhdev.dracarys.jobrunr.utils.mapper.jsonb.adapters;


import io.github.junhuhdev.dracarys.jobrunr.jobs.states.JobState;
import io.github.junhuhdev.dracarys.jobrunr.utils.mapper.jsonb.JobRunrJsonb;

import javax.json.*;
import javax.json.bind.adapter.JsonbAdapter;
import java.util.ArrayList;
import java.util.List;

import static io.github.junhuhdev.dracarys.jobrunr.utils.mapper.jsonb.NullSafeJsonBuilder.nullSafeJsonObjectBuilder;
import static io.github.junhuhdev.dracarys.jobrunr.utils.reflection.ReflectionUtils.toClass;

public class JobHistoryAdapter implements JsonbAdapter<List<JobState>, JsonArray> {

    private final JobRunrJsonb jsonb;

    public JobHistoryAdapter(JobRunrJsonb jsonb) {
        this.jsonb = jsonb;
    }

    @Override
    public JsonArray adaptToJson(List<JobState> jobStates) {
        final JsonArrayBuilder historyJsonObject = Json.createArrayBuilder();
        for (JobState jobState : jobStates) {
            final JsonObject jsonObject = nullSafeJsonObjectBuilder(jsonb, jobState)
                    .add("@class", jobState.getClass().getName())
                    .build();
            historyJsonObject.add(jsonObject);
        }
        return historyJsonObject.build();
    }

    @Override
    public List<JobState> adaptFromJson(JsonArray jsonArray) {
        List<JobState> result = new ArrayList<>();
        for (JsonValue jsonValue : jsonArray) {
            final JsonObject jsonObject = jsonValue.asJsonObject();
            String className = jsonObject.getString("@class");
            result.add(jsonb.fromJsonValue(jsonObject, toClass(className)));
        }
        return result;
    }
}
