package io.github.junhuhdev.dracarys.pipeline.common;

import java.util.stream.Stream;

@FunctionalInterface
public interface StreamSupplier<T> {

    Stream<T> supply();

    default StreamEx<T> supplyEx() {
        return new StreamEx<>(supply());
    }
}
