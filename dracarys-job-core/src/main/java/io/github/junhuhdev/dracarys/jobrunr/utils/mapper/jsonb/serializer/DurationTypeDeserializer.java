package io.github.junhuhdev.dracarys.jobrunr.utils.mapper.jsonb.serializer;

import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.stream.JsonParser;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.Duration;

public class DurationTypeDeserializer implements JsonbDeserializer<Duration> {

    @Override
    public Duration deserialize(JsonParser jsonParser, DeserializationContext deserializationContext, Type type) {
        final BigDecimal durationAsSecAndNanoSec = jsonParser.getBigDecimal();
        return Duration.ofSeconds(
                durationAsSecAndNanoSec.longValue(),
                durationAsSecAndNanoSec.remainder(BigDecimal.ONE).movePointRight(durationAsSecAndNanoSec.scale()).abs().longValue()
        );
    }
}
