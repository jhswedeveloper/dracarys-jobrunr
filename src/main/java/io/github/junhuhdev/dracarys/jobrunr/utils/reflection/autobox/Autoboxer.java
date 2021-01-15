package io.github.junhuhdev.dracarys.jobrunr.utils.reflection.autobox;

import org.jobrunr.utils.reflection.autobox.BooleanTypeAutoboxer;
import org.jobrunr.utils.reflection.autobox.DoubleTypeAutoboxer;
import org.jobrunr.utils.reflection.autobox.DurationTypeAutoboxer;
import org.jobrunr.utils.reflection.autobox.EnumAutoboxer;
import org.jobrunr.utils.reflection.autobox.FloatTypeAutoboxer;
import org.jobrunr.utils.reflection.autobox.InstantTypeAutoboxer;
import org.jobrunr.utils.reflection.autobox.IntegerTypeAutoboxer;
import org.jobrunr.utils.reflection.autobox.LongTypeAutoboxer;
import org.jobrunr.utils.reflection.autobox.StringTypeAutoboxer;
import org.jobrunr.utils.reflection.autobox.TypeAutoboxer;
import org.jobrunr.utils.reflection.autobox.UUIDTypeAutoboxer;

import java.util.Arrays;
import java.util.List;

import static org.jobrunr.utils.reflection.ReflectionUtils.cast;

public class Autoboxer {

    private static final List<TypeAutoboxer> autoboxers = Arrays.asList(
            new BooleanTypeAutoboxer(),
            new InstantTypeAutoboxer(),
            new IntegerTypeAutoboxer(),
            new LongTypeAutoboxer(),
            new DoubleTypeAutoboxer(),
            new FloatTypeAutoboxer(),
            new StringTypeAutoboxer(),
            new UUIDTypeAutoboxer(),
            new EnumAutoboxer(),
            new DurationTypeAutoboxer()
    );

    private Autoboxer() {

    }

    @SuppressWarnings("unchecked")
    public static <T> T autobox(Object value, Class<T> type) {
        if (type.equals(value.getClass())) {
            return cast(value);
        }

        return cast(autoboxers.stream()
                .filter(autoboxer -> autoboxer.supports(type))
                .findFirst()
                .map(autoboxer -> autoboxer.autobox(value, type))
                .orElseThrow(() -> new UnsupportedOperationException(String.format("Cannot autobox %s of type %s to %s", value, value.getClass().getName(), type.getName()))));

    }
}
