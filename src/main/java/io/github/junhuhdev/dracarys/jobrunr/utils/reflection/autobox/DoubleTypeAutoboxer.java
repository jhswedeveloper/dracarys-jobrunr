package io.github.junhuhdev.dracarys.jobrunr.utils.reflection.autobox;

import java.math.BigDecimal;

import static io.github.junhuhdev.dracarys.jobrunr.utils.reflection.ReflectionUtils.cast;

public class DoubleTypeAutoboxer implements TypeAutoboxer<Double> {
    @Override
    public boolean supports(Class<?> type) {
        return double.class.equals(type) || Double.class.equals(type);
    }

    @Override
    public Double autobox(Object value, Class<Double> type) {
        if (value instanceof Double) {
            return cast(value);
        } else if (value instanceof BigDecimal) {
            return cast(((BigDecimal) value).doubleValue());
        } else if (value instanceof Integer) {
            return Double.valueOf((Integer) value);
        }
        throw new UnsupportedOperationException(String.format("Cannot autobox %s of type %s to %s", value, value.getClass().getName(), Double.class.getName()));
    }
}
