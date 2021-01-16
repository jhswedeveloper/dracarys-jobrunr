package io.github.junhuhdev.dracarys.jobrunr.utils.mapper.jsonb;


import io.github.junhuhdev.dracarys.jobrunr.utils.mapper.JsonMapper;
import io.github.junhuhdev.dracarys.jobrunr.utils.mapper.jsonb.serializer.DurationTypeDeserializer;
import io.github.junhuhdev.dracarys.jobrunr.utils.mapper.jsonb.serializer.DurationTypeSerializer;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import java.io.OutputStream;
import java.io.Writer;

public class JsonbJsonMapper implements JsonMapper {

    private final Jsonb jsonb;

    public JsonbJsonMapper() {
        jsonb = JsonbBuilder.create(new JsonbConfig()
                .withNullValues(true)
                .withSerializers(new DurationTypeSerializer())
                .withDeserializers(new DurationTypeDeserializer())
                .withPropertyVisibilityStrategy(new FieldAccessStrategy())
                .withAdapters(new JobAdapter(), new RecurringJobAdapter())
        );

    }

    @Override
    public String serialize(Object object) {
        return jsonb.toJson(object);
    }

    @Override
    public void serialize(Writer writer, Object object) {
        jsonb.toJson(object, writer);
    }

    @Override
    public void serialize(OutputStream outputStream, Object object) {
        jsonb.toJson(object, outputStream);
    }

    @Override
    public <T> T deserialize(String serializedObjectAsString, Class<T> clazz) {
        return jsonb.fromJson(serializedObjectAsString, clazz);
    }
}
