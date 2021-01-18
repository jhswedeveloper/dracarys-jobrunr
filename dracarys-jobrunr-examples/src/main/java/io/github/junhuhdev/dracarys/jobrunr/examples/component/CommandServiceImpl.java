package io.github.junhuhdev.dracarys.jobrunr.examples.component;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import io.github.junhuhdev.dracarys.jobrunr.examples.jpa.entity.CommandEntity;
import io.github.junhuhdev.dracarys.jobrunr.examples.jpa.repository.CommandRepository;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.xstream.XStreamFactory;
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
	public CommandEntity save(Command.Request request) {
		var json = gson.toJson(request);
		var history = XStreamFactory.xstream().toXML(Lists.newArrayList(request));
		var cmd = CommandEntity.builder()
				.referenceId(request.getReferenceId())
				.commandClass(request.getClass().getName())
				.command(json)
				.history(history)
				.build();
		return commandRepository.save(cmd);
	}

}
