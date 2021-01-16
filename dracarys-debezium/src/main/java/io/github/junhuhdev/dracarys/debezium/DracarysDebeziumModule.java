package io.github.junhuhdev.dracarys.debezium;

import io.github.junhuhdev.dracarys.debezium.config.DebeziumConsumer;
import io.github.junhuhdev.dracarys.debezium.config.DebeziumDefaultConsumer;
import io.github.junhuhdev.dracarys.debezium.config.DebeziumEmbeddedEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "io.github.junhuhdev.dracarys.debezium")
public class DracarysDebeziumModule {

	private final static Logger log = LoggerFactory.getLogger(DracarysDebeziumModule.class);

	@ConditionalOnBean
	@Bean
	public DebeziumConsumer debeziumConsumer() {
		log.info("Staring default debezium consumer...");
		return new DebeziumDefaultConsumer();
	}

}
