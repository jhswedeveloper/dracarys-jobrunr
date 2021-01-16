package io.github.junhuhdev.dracarys.debezium.config;

import com.google.gson.Gson;
import io.debezium.config.Configuration;
import io.debezium.connector.postgresql.PostgresConnector;
import io.debezium.data.Envelope;
import io.debezium.embedded.EmbeddedEngine;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.storage.MemoryOffsetBackingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static io.debezium.data.Envelope.FieldName.*;
import static java.util.stream.Collectors.toMap;

@org.springframework.context.annotation.Configuration
public class CDC {
    private final static Logger log = LoggerFactory.getLogger(CDC.class);

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final Gson gson;
    private final DebeziumConsumer debeziumConsumer;

    private EmbeddedEngine engine;

    public CDC(Gson gson, DebeziumConsumer debeziumConsumer) {
        this.gson = gson;
        this.debeziumConsumer = debeziumConsumer;
    }

    @PostConstruct
    public void startEmbeddedEngine() {
        log.info("Starting CDC.....");
        Configuration config = Configuration.empty()
                .withSystemProperties(Function.identity()).edit()
                .with(EmbeddedEngine.CONNECTOR_CLASS, PostgresConnector.class)
                .with(EmbeddedEngine.ENGINE_NAME, "cache-invalidation-engine")
                .with(EmbeddedEngine.OFFSET_STORAGE, MemoryOffsetBackingStore.class)
                .with("name", "cache-invalidation-connector")
                .with("database.hostname", "localhost")
                .with("database.port", 5433)
                .with("database.user", "user")
                .with("database.password", "test")
                .with("database.server.name", "dbserver1")
                .with("database.dbname", "familydb")
                .with("table.include.list", "public.command")
//                .with(PostgresConnectorConfig.SNAPSHOT_MODE, PostgresConnectorConfig.SnapshotMode.ALWAYS)
                .build();
        try {
            engine = EmbeddedEngine.create()
                    .using(config)
                    .notifying(debeziumConsumer.handle())
//                    .notifying(this::handleCommand)
                    .build();
            executorService.execute(engine);
        } catch (Throwable e) {
            log.error("Error", e);
        }
    }

    @PreDestroy
    public void destroy() {
        log.info("Stopping debezium....");
        try {
            engine.stop();
            executorService.shutdown();
            while (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                log.info("Waiting another 5 seconds for the embedded engine to shut down");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void handleCommand(SourceRecord sourceRecord) {
        try {
            Struct sourceRecordValue = (Struct) sourceRecord.value();
            if (sourceRecordValue != null) {
                Envelope.Operation operation = Envelope.Operation.forCode((String) sourceRecordValue.get(OPERATION));
                // Only if this is a transactional operation.
                if (operation != Envelope.Operation.READ
                        && sourceRecord
                        .topic()
                        .equalsIgnoreCase("familydb-postgres-connector.public.integration")) {
                    // update elasticsearch
                    Map<String, Object> message = parseMessage(sourceRecordValue, operation);
                }
                if (operation == Envelope.Operation.CREATE
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
//                                                chainRouter.dispatch((Command.Request) cmd);
                                            } catch (Exception e) {
                                                log.error("Failed to handle cmd", e);
                                            }
                                        }
//										if (cmd instanceof Command) {
//											pipeline.send((Command) cmd);
//										}
                                    });
                        } catch (ClassNotFoundException e) {
                            log.error("Command class has been moved or renamed {}", message, e);
                        } catch (Exception e) {
                            log.error("Failed to handle cmd", e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to handle event {}", sourceRecord, e);
        }
    }

    private Map<String, Object> parseMessage(Struct sourceRecordValue, Envelope.Operation operation) {
        String record = parseFieldName(operation);
        Struct struct = (Struct) sourceRecordValue.get(record);
        return struct.schema().fields().stream()
                .map(Field::name)
                .filter(fieldName -> struct.get(fieldName) != null)
                .map(fieldName -> Pair.of(fieldName, struct.get(fieldName)))
                .collect(toMap(Pair::getKey, Pair::getValue));
    }

    private String parseFieldName(Envelope.Operation operation) {
        // For Delete operations fetch state of a record before an operation.
        if (operation == Envelope.Operation.DELETE) {
            return BEFORE;
        }
        // For Update & Insert operations fetch state of a record after an operation.
        return AFTER;
    }
}
