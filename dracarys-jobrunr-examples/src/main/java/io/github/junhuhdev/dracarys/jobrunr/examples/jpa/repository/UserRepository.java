package io.github.junhuhdev.dracarys.jobrunr.examples.jpa.repository;

import io.github.junhuhdev.dracarys.jobrunr.examples.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}
