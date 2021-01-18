package io.github.junhuhdev.dracarys.jobrunr.examples.jpa.repository;

import io.github.junhuhdev.dracarys.jobrunr.examples.jpa.entity.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, String> {
}
