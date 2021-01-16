package io.github.junhuhdev.dracarys.debezium.config;

import io.debezium.engine.RecordChangeEvent;
import org.apache.kafka.connect.source.SourceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class DebeziumDefaultConsumer implements DebeziumConsumer{
	private final static Logger log = LoggerFactory.getLogger(DebeziumDefaultConsumer.class);

	@Override
	public Consumer<RecordChangeEvent<SourceRecord>> handle() {
		return null;
	}

	private void handleCommand(RecordChangeEvent<SourceRecord> sourceRecord) {
		log.info("Incoming {}", sourceRecord);
	}
}
