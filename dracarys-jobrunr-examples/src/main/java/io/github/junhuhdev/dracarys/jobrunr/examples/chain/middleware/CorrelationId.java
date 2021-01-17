package io.github.junhuhdev.dracarys.jobrunr.examples.chain.middleware;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

@Component
public class CorrelationId {
    private static final String MDC_KEY = "ccid";
    private final AtomicLong counter = new AtomicLong();

    <T> T wrap(Supplier<T> action) throws Exception  {
        var closeable = MDC.putCloseable(MDC_KEY, next());
        try (closeable) {
            return action.get();
        }
    }

    private String next() {
        return String.valueOf(counter.incrementAndGet() % 1000);
    }
}
