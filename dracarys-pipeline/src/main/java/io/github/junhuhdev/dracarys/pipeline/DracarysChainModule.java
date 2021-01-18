package io.github.junhuhdev.dracarys.pipeline;

import io.github.junhuhdev.dracarys.pipeline.middleware.CorrelationId;
import io.github.junhuhdev.dracarys.pipeline.middleware.LogMiddleware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan(basePackages = "io.github.junhuhdev.dracarys.pipeline")
public class DracarysChainModule {

	@ConditionalOnMissingBean(LogMiddleware.class)
	@Bean
	public LogMiddleware logMiddleware(CorrelationId correlationId) {
		log.info("Initializing logging middleware...");
		return new LogMiddleware(correlationId);
	}

}
