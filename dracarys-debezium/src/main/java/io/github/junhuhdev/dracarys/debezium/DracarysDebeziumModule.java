package io.github.junhuhdev.dracarys.debezium;

import io.github.junhuhdev.dracarys.debezium.config.DebeziumConsumer;
import io.github.junhuhdev.dracarys.debezium.config.DebeziumDefaultConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@Configuration
@ComponentScan(basePackages = "io.github.junhuhdev.dracarys.debezium")
public class DracarysDebeziumModule {

	private final static Logger log = LoggerFactory.getLogger(DracarysDebeziumModule.class);

	@ConditionalOnMissingBean
	@ConditionalOnBean(DebeziumConsumer.class)
	@Bean
	public DebeziumConsumer debeziumConsumer() {
		log.info("Staring default debezium consumer...");
		return new DebeziumDefaultConsumer();
	}

}
