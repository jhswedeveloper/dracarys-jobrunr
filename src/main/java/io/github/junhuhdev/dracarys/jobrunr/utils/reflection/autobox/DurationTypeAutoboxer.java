package io.github.junhuhdev.dracarys.jobrunr.utils.reflection.autobox;

import org.jobrunr.utils.reflection.autobox.TypeAutoboxer;

import java.time.Duration;

public class DurationTypeAutoboxer implements TypeAutoboxer<Duration> {
    @Override
    public boolean supports(Class<?> type) {
        return Duration.class.equals(type);
    }

    @Override
    public Duration autobox(Object value, Class<Duration> type) {
        if (value instanceof Duration) {
            return (Duration) value;
        } else if (value instanceof CharSequence) {
            return Duration.parse((CharSequence) value);
        }
        throw new UnsupportedOperationException(String.format("Cannot autobox %s of type %s to %s", value, value.getClass().getName(), Duration.class.getName()));
    }
}
