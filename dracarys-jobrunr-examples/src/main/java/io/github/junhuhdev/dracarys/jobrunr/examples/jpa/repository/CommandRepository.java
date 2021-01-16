package io.github.junhuhdev.dracarys.jobrunr.examples.jpa.repository;

import io.github.junhuhdev.dracarys.jobrunr.examples.jpa.entity.CommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandRepository extends JpaRepository<CommandEntity, Long> {
}
