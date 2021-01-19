package io.github.junhuhdev.dracarys.jobrunr.examples.infra;

import com.google.gson.Gson;
import io.github.junhuhdev.dracarys.debezium.config.DebeziumConsumer;
import io.github.junhuhdev.dracarys.jobrunr.examples.jpa.repository.CommandRepository;
import io.github.junhuhdev.dracarys.jobrunr.scheduling.JobScheduler;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainRouter;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static io.debezium.data.Envelope.*;
import static io.debezium.data.Envelope.FieldName.AFTER;
import static io.debezium.data.Envelope.FieldName.BEFORE;
import static io.debezium.data.Envelope.FieldName.OPERATION;
import static java.util.stream.Collectors.toMap;

@Slf4j
@SuppressWarnings("Unchecked")
public class DracarysDebeziumConsumer implements DebeziumConsumer {

	private final Gson gson;
	private final ChainRouter router;
	private final JobScheduler jobScheduler;
	private final CommandRepository commandRepository;

	public DracarysDebeziumConsumer(Gson gson, ChainRouter router, JobScheduler jobScheduler, CommandRepository commandRepository) {
		this.gson = gson;
		this.router = router;
		this.jobScheduler = jobScheduler;
		this.commandRepository = commandRepository;
	}

	@Transactional
	@Override
	public Consumer<SourceRecord> handle() {
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
	private void handleCommand(SourceRecord sourceRecord) {
		try {
//            log.info("Incoming ... {}", sourceRecord);
			Struct sourceRecordValue = (Struct) sourceRecord.value();
			if (sourceRecordValue != null) {
				Operation operation = Operation.forCode((String) sourceRecordValue.get(OPERATION));
				// Only if this is a transactional operation.
				if (operation != Operation.READ
						&& sourceRecord
						.topic()
						.equalsIgnoreCase("familydb-postgres-connector.public.integration")) {
					// update elasticsearch
					Map<String, Object> message = parseMessage(sourceRecordValue, operation);
				}
				if (operation == Operation.CREATE
						&& sourceRecord
						.topic()
						.equalsIgnoreCase("dbserver1.public.command")) {
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
										if (cmd instanceof Command.Request) {
											try {
												var res = jobScheduler.enqueue(() -> router.dispatch((Command.Request) cmd));
												var cmdRecord = commandRepository.findByReferenceId(((Command.Request) cmd).getReferenceId());
												cmdRecord.setJobId(res.toString());
												commandRepository.save(cmdRecord);
											} catch (Exception e) {
												log.error("Failed to dispatch", e);
												e.printStackTrace();
											}
										}
									});
						} catch (ClassNotFoundException | NoClassDefFoundError e) {
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
