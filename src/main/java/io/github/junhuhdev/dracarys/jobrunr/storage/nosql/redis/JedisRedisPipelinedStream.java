package io.github.junhuhdev.dracarys.jobrunr.storage.nosql.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class JedisRedisPipelinedStream<T> implements Stream<T> {

    private final Stream<T> initialStream;
    private final Jedis jedis;

    public JedisRedisPipelinedStream(Collection<T> initial, Jedis jedis) {
        this(initial.stream(), jedis);
    }

    public JedisRedisPipelinedStream(Stream<T> initialStream, Jedis jedis) {
        this.initialStream = initialStream;
        this.jedis = jedis;
    }

    public <R> org.jobrunr.storage.nosql.redis.JedisRedisPipelinedStream<R> mapUsingPipeline(BiFunction<Pipeline, T, R> biFunction) {
        List<R> collect;
        try (final Pipeline pipeline = jedis.pipelined()) {
            collect = initialStream
                    .map(item -> biFunction.apply(pipeline, item))
                    .collect(toList()); // must collect otherwise map is not executed
            pipeline.sync();
        }
        return new org.jobrunr.storage.nosql.redis.JedisRedisPipelinedStream<>(collect, jedis);
    }

    public <R> org.jobrunr.storage.nosql.redis.JedisRedisPipelinedStream<R> mapAfterSync(Function<? super T, ? extends R> function) {
        return new org.jobrunr.storage.nosql.redis.JedisRedisPipelinedStream<>(initialStream.map(function), jedis);
    }

    @Override
    public Stream<T> filter(Predicate<? super T> predicate) {
        return initialStream.filter(predicate);
    }

    @Override
    public <R> Stream<R> map(Function<? super T, ? extends R> function) {
        return initialStream.map(function);
    }

    @Override
    public IntStream mapToInt(ToIntFunction<? super T> toIntFunction) {
        return initialStream.mapToInt(toIntFunction);
    }

    @Override
    public LongStream mapToLong(ToLongFunction<? super T> toLongFunction) {
        return initialStream.mapToLong(toLongFunction);
    }

    @Override
    public DoubleStream mapToDouble(ToDoubleFunction<? super T> toDoubleFunction) {
        return initialStream.mapToDouble(toDoubleFunction);
    }

    @Override
    public <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> function) {
        return initialStream.flatMap(function);
    }

    @Override
    public IntStream flatMapToInt(Function<? super T, ? extends IntStream> function) {
        return initialStream.flatMapToInt(function);
    }

    @Override
    public LongStream flatMapToLong(Function<? super T, ? extends LongStream> function) {
        return initialStream.flatMapToLong(function);
    }

    @Override
    public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> function) {
        return initialStream.flatMapToDouble(function);
    }

    @Override
    public Stream<T> distinct() {
        return initialStream.distinct();
    }

    @Override
    public Stream<T> sorted() {
        return initialStream.sorted();
    }

    @Override
    public Stream<T> sorted(Comparator<? super T> comparator) {
        return initialStream.sorted(comparator);
    }

    @Override
    public Stream<T> peek(Consumer<? super T> consumer) {
        return initialStream.peek(consumer);
    }

    @Override
    public org.jobrunr.storage.nosql.redis.JedisRedisPipelinedStream<T> limit(long l) {
        return new org.jobrunr.storage.nosql.redis.JedisRedisPipelinedStream<>(initialStream.limit(l), jedis);
    }

    @Override
    public org.jobrunr.storage.nosql.redis.JedisRedisPipelinedStream<T> skip(long l) {
        return new org.jobrunr.storage.nosql.redis.JedisRedisPipelinedStream<>(initialStream.skip(l), jedis);
    }

    @Override
    public void forEach(Consumer<? super T> consumer) {
        initialStream.forEach(consumer);
    }

    @Override
    public void forEachOrdered(Consumer<? super T> consumer) {
        initialStream.forEachOrdered(consumer);
    }

    @Override
    public Object[] toArray() {
        return initialStream.toArray();
    }

    @Override
    public <A> A[] toArray(IntFunction<A[]> intFunction) {
        return initialStream.toArray(intFunction);
    }

    @Override
    public T reduce(T t, BinaryOperator<T> binaryOperator) {
        return initialStream.reduce(t, binaryOperator);
    }

    @Override
    public Optional<T> reduce(BinaryOperator<T> binaryOperator) {
        return initialStream.reduce(binaryOperator);
    }

    @Override
    public <U> U reduce(U u, BiFunction<U, ? super T, U> biFunction, BinaryOperator<U> binaryOperator) {
        return initialStream.reduce(u, biFunction, binaryOperator);
    }

    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> biConsumer, BiConsumer<R, R> biConsumer1) {
        return initialStream.collect(supplier, biConsumer, biConsumer1);
    }

    @Override
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        return initialStream.collect(collector);
    }

    @Override
    public Optional<T> min(Comparator<? super T> comparator) {
        return initialStream.min(comparator);
    }

    @Override
    public Optional<T> max(Comparator<? super T> comparator) {
        return initialStream.max(comparator);
    }

    @Override
    public long count() {
        return initialStream.count();
    }

    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        return initialStream.anyMatch(predicate);
    }

    @Override
    public boolean allMatch(Predicate<? super T> predicate) {
        return initialStream.allMatch(predicate);
    }

    @Override
    public boolean noneMatch(Predicate<? super T> predicate) {
        return initialStream.noneMatch(predicate);
    }

    @Override
    public Optional<T> findFirst() {
        return initialStream.findFirst();
    }

    @Override
    public Optional<T> findAny() {
        return initialStream.findAny();
    }

    public static <T1> Builder<T1> builder() {
        return Stream.builder();
    }

    public static <T1> Stream<T1> empty() {
        return Stream.empty();
    }

    public static <T1> Stream<T1> of(T1 t1) {
        return Stream.of(t1);
    }

    @SafeVarargs
    public static <T1> Stream<T1> of(T1... values) {
        return Stream.of(values);
    }

    public static <T1> Stream<T1> iterate(T1 seed, UnaryOperator<T1> f) {
        return Stream.iterate(seed, f);
    }

    public static <T1> Stream<T1> generate(Supplier<? extends T1> s) {
        return Stream.generate(s);
    }

    public static <T1> Stream<T1> concat(Stream<? extends T1> a, Stream<? extends T1> b) {
        return Stream.concat(a, b);
    }

    @Override
    public Iterator<T> iterator() {
        return initialStream.iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return initialStream.spliterator();
    }

    @Override
    public boolean isParallel() {
        return initialStream.isParallel();
    }

    @Override
    public Stream<T> sequential() {
        return initialStream.sequential();
    }

    @Override
    public Stream<T> parallel() {
        return initialStream.parallel();
    }

    @Override
    public Stream<T> unordered() {
        return initialStream.unordered();
    }

    @Override
    public Stream<T> onClose(Runnable runnable) {
        return initialStream.onClose(runnable);
    }

    @Override
    public void close() {
        initialStream.close();
    }
}
