package io.github.junhuhdev.dracarys.jobrunr.examples.infra.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "io.github.junhuhdev.dracarys.jobrunr.examples.jpa.entity")
@EnableJpaRepositories(basePackages = "io.github.junhuhdev.dracarys.jobrunr.examples.jpa.repository")
@Configuration
public class JpaConfiguration {
}
