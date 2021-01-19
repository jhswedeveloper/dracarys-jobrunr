package io.github.junhuhdev.dracarys.jobrunr.examples.jpa.repository;

import io.github.junhuhdev.dracarys.jobrunr.examples.jpa.entity.CommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommandRepository extends JpaRepository<CommandEntity, Long> {

	CommandEntity findByReferenceId(String referenceId);

	CommandEntity findByJobId(String jobId);

	@Modifying
	@Query(value = "UPDATE COMMAND SET STATUS = 'PROCESSING' WHERE REFERENCE_ID = :referenceId AND STATUS <> 'PROCESSING'", nativeQuery = true)
	int lock(@Param("referenceId") String referenceId);

}
