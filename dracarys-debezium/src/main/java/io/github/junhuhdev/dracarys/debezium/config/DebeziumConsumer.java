package io.github.junhuhdev.dracarys.debezium.config;

import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.function.Consumer;

public interface DebeziumConsumer {

	Consumer<RecordChangeEvent<SourceRecord>> handle();

}
