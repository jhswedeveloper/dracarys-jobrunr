package io.github.junhuhdev.dracarys.jobrunr.examples.component;

import com.google.gson.Gson;
import io.github.junhuhdev.dracarys.jobrunr.examples.jpa.entity.CommandEntity;
import io.github.junhuhdev.dracarys.jobrunr.examples.jpa.repository.CommandRepository;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommandServiceImpl implements CommandService {

	private final CommandRepository commandRepository;
	private final Gson gson;

	@Override
	public void save(Command.Request request) {
		var cmd = CommandEntity.builder()
				.commandClass()
				.command()
				.build();
		commandRepository.save(cmd);
	}

}
