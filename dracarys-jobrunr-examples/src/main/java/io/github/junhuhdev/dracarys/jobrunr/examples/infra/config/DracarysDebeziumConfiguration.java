package io.github.junhuhdev.dracarys.jobrunr.examples.infra.config;

import com.google.gson.Gson;
import io.github.junhuhdev.dracarys.debezium.EnableDracarysDebezium;
import io.github.junhuhdev.dracarys.debezium.config.DebeziumConsumer;
import io.github.junhuhdev.dracarys.jobrunr.examples.infra.DracarysDebeziumConsumer;
import io.github.junhuhdev.dracarys.jobrunr.scheduling.JobScheduler;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainRouter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@EnableDracarysDebezium
@Configuration
public class DracarysDebeziumConfiguration {

	@Primary
    @Bean
    public DebeziumConsumer debeziumConsumer(Gson gson, ChainRouter router, JobScheduler jobScheduler) {
        log.info("Starting DRACARYS DEBEZIUM....");
		return new DracarysDebeziumConsumer(gson, router, jobScheduler);
    }
}
