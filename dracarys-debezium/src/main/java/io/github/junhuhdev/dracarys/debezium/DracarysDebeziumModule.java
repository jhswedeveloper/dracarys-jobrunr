package io.github.junhuhdev.dracarys.debezium;

import io.github.junhuhdev.dracarys.debezium.config.DebeziumConsumer;
import io.github.junhuhdev.dracarys.debezium.config.DebeziumDefaultConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan(basePackages = "io.github.junhuhdev.dracarys.debezium")
public class DracarysDebeziumModule {

	@ConditionalOnBean
	@Bean
	public DebeziumConsumer debeziumConsumer() {
		return new DebeziumDefaultConsumer();
	}

}
