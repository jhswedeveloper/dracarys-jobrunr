package io.github.junhuhdev.dracarys.debezium.config;

import io.debezium.connector.postgresql.PostgresConnectorConfig;
import io.debezium.embedded.EmbeddedEngine;
import io.debezium.relational.history.MemoryDatabaseHistory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DebeziumConnectorConfiguration {

	public static final String APP_NAME = "familydb-postgres-connector";

	@Bean
	public io.debezium.config.Configuration debeziumConfiguration() {
		return io.debezium.config.Configuration.create()
				.with(EmbeddedEngine.CONNECTOR_CLASS, "io.debezium.connector.postgresql.PostgresConnector")
				.with(EmbeddedEngine.ENGINE_NAME, APP_NAME)
				.with(
						EmbeddedEngine.OFFSET_STORAGE,
						"org.apache.kafka.connect.storage.MemoryOffsetBackingStore")
				.with(PostgresConnectorConfig.DATABASE_NAME, MemoryDatabaseHistory.class.getName())
				.with("database.server.name", APP_NAME)
				.with("database.hostname", "localhost")
				.with("database.port", "5433")
				.with("database.user", "user")
				.with("database.password", "test")
				.with("database.dbname", "familydb")
				// Comma-separated list of tables to capture
				.with("table.include.list", "public.command")
				// Ignore delete statements
				// .with("tombstones.on.delete", "false")
				// Send JSON without schema
				.with("schemas.enable", false)
				.build();
	}

}
