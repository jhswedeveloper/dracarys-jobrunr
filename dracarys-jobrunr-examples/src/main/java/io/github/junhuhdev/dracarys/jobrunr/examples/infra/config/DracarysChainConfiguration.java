package io.github.junhuhdev.dracarys.jobrunr.examples.infra.config;

import io.github.junhuhdev.dracarys.jobrunr.examples.jpa.repository.CommandRepository;
import io.github.junhuhdev.dracarys.jobrunr.jobs.context.JobRunrDashboardLogger;
import io.github.junhuhdev.dracarys.pipeline.EnableDracarysChains;
import io.github.junhuhdev.dracarys.pipeline.api.CommandStorageApi;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.cmd.CommandContext;
import io.github.junhuhdev.dracarys.pipeline.cmd.CommandStatus;
import io.github.junhuhdev.dracarys.pipeline.xstream.XStreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.transaction.Transactional;
import java.util.List;

@EnableDracarysChains
@Configuration
public class DracarysChainConfiguration {

	private final static Logger log = new JobRunrDashboardLogger(LoggerFactory.getLogger(CommandStorageApi.class));

	@Bean
	public CommandStorageApi commandStorageApi(CommandRepository commandRepository) {
		return new CommandStorageApi() {
			@Transactional(Transactional.TxType.REQUIRES_NEW)
			@Override
			public void update(CommandContext cmd) {
				log.info("Updating cmd id={}, cmds={}", cmd.getId(), cmd.getCommands().size());
				var cmdEntity = commandRepository.findByReferenceId(cmd.getId());
				var xml = XStreamFactory.xstream().toXML(cmd.getCommands());
				cmdEntity.setHistory(xml);
				cmdEntity.setRetryCount(cmd.countFaults());
				cmdEntity.setStatus(cmd.getStatus());
				commandRepository.saveAndFlush(cmdEntity);
			}

			@Transactional(Transactional.TxType.REQUIRES_NEW)
			@Override
			public boolean lock(String referenceId) {
				return commandRepository.lock(referenceId) == 1;
			}

			@Override
			public List<Command> findByReferenceId(String refId) {
				return commandRepository.findByReferenceId(refId).getTxHistory();
			}

			@Override
			public CommandStatus findStatus(String referenceId) {
				return commandRepository.findByReferenceId(referenceId).getStatus();
			}
		};
	}

}
