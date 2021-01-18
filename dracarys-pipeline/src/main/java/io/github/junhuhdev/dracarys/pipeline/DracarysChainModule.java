package io.github.junhuhdev.dracarys.pipeline;

import io.github.junhuhdev.dracarys.pipeline.middleware.log.CorrelationId;
import io.github.junhuhdev.dracarys.pipeline.middleware.log.LogMiddleware;
import io.github.junhuhdev.dracarys.pipeline.middleware.transactional.TxMiddleware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@ComponentScan(basePackages = "io.github.junhuhdev.dracarys.pipeline")
public class DracarysChainModule {

	@Order(1)
	@ConditionalOnMissingBean(LogMiddleware.class)
	@Bean
	public LogMiddleware logMiddleware(CorrelationId correlationId) {
		log.info("Initializing logging middleware...");
		return new LogMiddleware(correlationId);
	}

	@Order(2)
	@ConditionalOnMissingBean(TxMiddleware.class)
	@Bean
	public TxMiddleware txMiddleware(PlatformTransactionManager transactionManager) {
		log.info("Initializing transactional middleware...");
		return new TxMiddleware(transactionManager);
	}

}
