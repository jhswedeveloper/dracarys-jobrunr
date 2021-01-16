package io.github.junhuhdev.dracarys.debezium.config;

import io.debezium.connector.postgresql.PostgresConnectorConfig;
import io.debezium.embedded.EmbeddedEngine;
import io.debezium.relational.history.MemoryDatabaseHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DebeziumConnectorConfiguration {
    private final static Logger log = LoggerFactory.getLogger(DebeziumConnectorConfiguration.class);
    public static final String APP_NAME = "familydb-postgres-connector";

    @Bean
    public io.debezium.config.Configuration debeziumConfiguration() {
        log.info("Starting debezium config...");
        return io.debezium.config.Configuration.create()
                .with(EmbeddedEngine.CONNECTOR_CLASS, "io.debezium.connector.postgresql.PostgresConnector")
                .with(EmbeddedEngine.ENGINE_NAME, APP_NAME)
                .with(EmbeddedEngine.OFFSET_STORAGE, "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", "/tmp/offsets.dat")
//                .with(PostgresConnectorConfig.DATABASE_NAME, MemoryDatabaseHistory.class.getName())
                .with("database.server.name", APP_NAME)
                .with("database.hostname", "localhost")
                .with("database.port", "5433")
                .with("database.user", "user")
                .with("database.password", "test")
                .with("database.dbname", "familydb")
                .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
                .with("database.history.file.filename", "/temp/dbhistory.dat")
                // Comma-separated list of tables to capture
                .with("schema.include.list", "public")
                .with("table.include.list", "public.family")
                // Ignore delete statements
                // .with("tombstones.on.delete", "false")
                // Send JSON without schema
                .with("schemas.enable", false)
                .build();
    }
}
