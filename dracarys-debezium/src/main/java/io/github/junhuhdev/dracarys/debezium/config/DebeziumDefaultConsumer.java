package io.github.junhuhdev.dracarys.debezium.config;

import io.debezium.engine.RecordChangeEvent;
import org.apache.kafka.connect.source.SourceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class DebeziumDefaultConsumer implements DebeziumConsumer{
	private final static Logger log = LoggerFactory.getLogger(DebeziumDefaultConsumer.class);


	private void handleCommand(SourceRecord sourceRecord) {
		log.info("Incoming {}", sourceRecord);
	}

	@Override
	public Consumer<SourceRecord> handle() {
		return this::handleCommand;
	}
}
