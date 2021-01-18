package io.github.junhuhdev.dracarys.jobrunr.examples.infra.config;

import io.github.junhuhdev.dracarys.jobrunr.examples.jpa.repository.CommandRepository;
import io.github.junhuhdev.dracarys.pipeline.EnableDracarysChains;
import io.github.junhuhdev.dracarys.pipeline.api.CommandStorageApi;
import io.github.junhuhdev.dracarys.pipeline.cmd.CommandContext;
import io.github.junhuhdev.dracarys.pipeline.xstream.XStreamFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableDracarysChains
@Configuration
public class DracarysChainConfiguration {

	@Bean
	public CommandStorageApi commandStorageApi(CommandRepository commandRepository) {
		return new CommandStorageApi() {
			@Override
			public void update(CommandContext cmd) {
				var cmdEntity = commandRepository.findByReferenceId(cmd.getId());
				var xml = XStreamFactory.xstream().toXML(cmd.getCommands());
				cmdEntity.setHistory(xml);
				commandRepository.save(cmdEntity);
			}
		};
	}
}
