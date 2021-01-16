package io.github.junhuhdev.dracarys.jobrunr.examples.infra;

import com.google.gson.Gson;
import io.debezium.data.Envelope;
import io.debezium.engine.RecordChangeEvent;
import io.github.junhuhdev.dracarys.debezium.config.DebeziumConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static io.debezium.data.Envelope.*;
import static io.debezium.data.Envelope.FieldName.AFTER;
import static io.debezium.data.Envelope.FieldName.BEFORE;
import static io.debezium.data.Envelope.FieldName.OPERATION;
import static java.util.stream.Collectors.toMap;

@Primary
@Log4j2
@RequiredArgsConstructor
@Component
public class DracarysDebeziumConsumer implements DebeziumConsumer {

	private final Gson gson;

	@Override
	public Consumer<RecordChangeEvent<SourceRecord>> handle() {
		return this::handleCommand;
	}

	/**
	 * Note that your application’s handler function should not throw any exceptions; if it does, the
	 * engine will log any exception thrown by the method and will continue to operate on the next
	 * source record, but your application will not have another chance to handle the particular
	 * source record that caused the exception, meaning your application might become inconsistent
	 * with the database.
	 *
	 * @param sourceRecord
	 */
	@SuppressWarnings("unchecked")
	private void handleCommand(RecordChangeEvent<SourceRecord> sourceRecord) {
		try {
			Struct sourceRecordValue = (Struct) sourceRecord.record().value();
			if (sourceRecordValue != null) {
				Operation operation = Operation.forCode((String) sourceRecordValue.get(OPERATION));
				// Only if this is a transactional operation.
				if (operation != Operation.READ
						&& sourceRecord
						.record()
						.topic()
						.equalsIgnoreCase("familydb-postgres-connector.public.integration")) {
					// update elasticsearch
					Map<String, Object> message = parseMessage(sourceRecordValue, operation);
				}
				if (operation == Operation.CREATE
						&& sourceRecord
						.record()
						.topic()
						.equalsIgnoreCase("familydb-postgres-connector.public.command")) {
					// Build a map with all row data received.
					Map<String, Object> message = parseMessage(sourceRecordValue, operation);
					// Call the service to handle the data change.
					log.info("Captured operation={}, event={}", operation.name(), message);
					if (message.containsKey("command")) {
						try {
							var onCmd =
									Optional.ofNullable(
											gson.fromJson(
													(String) message.get("command"),
													Class.forName((String) message.get("command_class"))));
							onCmd.ifPresent(
									cmd -> {
//										if (cmd instanceof Command) {
//											pipeline.send((Command) cmd);
//										}
									});
						} catch (ClassNotFoundException e) {
							log.error("Command class has been moved or renamed {}", message, e);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Failed to handle event {}", sourceRecord, e);
		}
	}

	private Map<String, Object> parseMessage(Struct sourceRecordValue, Operation operation) {
		String record = parseFieldName(operation);
		Struct struct = (Struct) sourceRecordValue.get(record);
		return struct.schema().fields().stream()
				.map(Field::name)
				.filter(fieldName -> struct.get(fieldName) != null)
				.map(fieldName -> Pair.of(fieldName, struct.get(fieldName)))
				.collect(toMap(Pair::getKey, Pair::getValue));
	}

	private String parseFieldName(Operation operation) {
		// For Delete operations fetch state of a record before an operation.
		if (operation == Operation.DELETE) {
			return BEFORE;
		}
		// For Update & Insert operations fetch state of a record after an operation.
		return AFTER;
	}

}
