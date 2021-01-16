package io.github.junhuhdev.dracarys.debezium.config;

import io.debezium.engine.RecordChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.function.Consumer;

@Slf4j
public class DebeziumDefaultConsumer implements DebeziumConsumer{

	@Override
	public Consumer<RecordChangeEvent<SourceRecord>> handle() {
		return null;
	}

	private void handleCommand(RecordChangeEvent<SourceRecord> sourceRecord) {
		log.info("Incoming {}", sourceRecord);
	}
}
