//package io.github.junhuhdev.dracarys.debezium.config;
//
//import com.google.gson.Gson;
//import io.debezium.config.Configuration;
//import io.debezium.data.Envelope.Operation;
//import io.debezium.embedded.Connect;
//import io.debezium.engine.DebeziumEngine;
//import io.debezium.engine.RecordChangeEvent;
//import io.debezium.engine.format.ChangeEventFormat;
//import org.apache.commons.lang3.tuple.Pair;
//import org.apache.kafka.connect.data.Field;
//import org.apache.kafka.connect.data.Struct;
//import org.apache.kafka.connect.source.SourceRecord;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.io.IOException;
//import java.util.Map;
//import java.util.Optional;
//import java.util.Properties;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.function.Consumer;
//
//import static io.debezium.data.Envelope.FieldName.AFTER;
//import static io.debezium.data.Envelope.FieldName.BEFORE;
//import static io.debezium.data.Envelope.FieldName.OPERATION;
//import static java.util.stream.Collectors.toMap;
//
////@Component
//public class DebeziumEmbeddedEngine {
//    private final static Logger log = LoggerFactory.getLogger(DebeziumEmbeddedEngine.class);
//    private final Configuration configuration;
//    private final Gson gson;
//    private final DebeziumConsumer consumer;
//
//    public DebeziumEmbeddedEngine(Configuration configuration, Gson gson, DebeziumConsumer consumer) {
//        this.configuration = configuration;
//        this.gson = gson;
//        this.consumer = consumer;
//    }
//
//    @PostConstruct
//    public void start() {
//        final Properties props = new Properties();
//        props.setProperty("name", "engine");
//        props.setProperty("connector.class", "io.debezium.connector.postgresql.PostgresConnector");
//        props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
//        props.setProperty("offset.storage.file.filename", "/tmp/offsets.dat");
//        props.setProperty("offset.flush.interval.ms", "60000");
//        /* begin connector properties */
//        props.setProperty("database.hostname", "localhost");
//        props.setProperty("database.dbname", "familydb");
//        props.setProperty("database.port", "5433");
//        props.setProperty("database.user", "user");
//        props.setProperty("database.password", "test");
//        props.setProperty("database.server.id", "85744");
//        props.setProperty("database.server.name", "my-app-connector");
//        props.setProperty("table.include.list", "public.family");
//        props.setProperty("database.history",
//                "io.debezium.relational.history.FileDatabaseHistory");
//        props.setProperty("database.history.file.filename", "/tmp/dbhistory.dat");
//        log.info("Starting Dracarys Debezium Embedded Engine...");
//        try (DebeziumEngine<RecordChangeEvent<SourceRecord>> engine =
//                     DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
//                             .using(props)
////						     .notifying(consumer.handle())
//                             .notifying(this::handleCommand)
//                             .build()) {
//            // Run the engine asynchronously...
//            ExecutorService executor = Executors.newSingleThreadExecutor();
//            executor.execute(engine);
//        } catch (IOException e) {
//            log.error("Unexpected error", e);
//            Thread.currentThread().interrupt();
//            throw new RuntimeException(e);
//        }
//        log.info("STOPPED");
//    }
//
//    /**
//     * Note that your application’s handler function should not throw any exceptions; if it does, the
//     * engine will log any exception thrown by the method and will continue to operate on the next
//     * source record, but your application will not have another chance to handle the particular
//     * source record that caused the exception, meaning your application might become inconsistent
//     * with the database.
//     *
//     * @param sourceRecord
//     */
//    @SuppressWarnings("unchecked")
//    private void handleCommand(RecordChangeEvent<SourceRecord> sourceRecord) {
//        try {
//            Struct sourceRecordValue = (Struct) sourceRecord.record().value();
//            if (sourceRecordValue != null) {
//                Operation operation = Operation.forCode((String) sourceRecordValue.get(OPERATION));
//                // Only if this is a transactional operation.
//                if (operation != Operation.READ
//                        && sourceRecord
//                        .record()
//                        .topic()
//                        .equalsIgnoreCase("familydb-postgres-connector.public.integration")) {
//                    // update elasticsearch
//                    Map<String, Object> message = parseMessage(sourceRecordValue, operation);
//                }
//                if (operation == Operation.CREATE
//                        && sourceRecord
//                        .record()
//                        .topic()
//                        .equalsIgnoreCase("familydb-postgres-connector.public.command")) {
//                    // Build a map with all row data received.
//                    Map<String, Object> message = parseMessage(sourceRecordValue, operation);
//                    // Call the service to handle the data change.
//                    log.info("Captured operation={}, event={}", operation.name(), message);
//                    if (message.containsKey("command")) {
//                        try {
//                            var onCmd =
//                                    Optional.ofNullable(
//                                            gson.fromJson(
//                                                    (String) message.get("command"),
//                                                    Class.forName((String) message.get("command_class"))));
//                            onCmd.ifPresent(
//                                    cmd -> {
////										if (cmd instanceof Command) {
////											pipeline.send((Command) cmd);
////										}
//                                    });
//                        } catch (ClassNotFoundException e) {
//                            log.error("Command class has been moved or renamed {}", message, e);
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            log.error("Failed to handle event {}", sourceRecord, e);
//        }
//    }
//
//    private Map<String, Object> parseMessage(Struct sourceRecordValue, Operation operation) {
//        String record = parseFieldName(operation);
//        Struct struct = (Struct) sourceRecordValue.get(record);
//        return struct.schema().fields().stream()
//                .map(Field::name)
//                .filter(fieldName -> struct.get(fieldName) != null)
//                .map(fieldName -> Pair.of(fieldName, struct.get(fieldName)))
//                .collect(toMap(Pair::getKey, Pair::getValue));
//    }
//
//    private String parseFieldName(Operation operation) {
//        // For Delete operations fetch state of a record before an operation.
//        if (operation == Operation.DELETE) {
//            return BEFORE;
//        }
//        // For Update & Insert operations fetch state of a record after an operation.
//        return AFTER;
//    }
//}
