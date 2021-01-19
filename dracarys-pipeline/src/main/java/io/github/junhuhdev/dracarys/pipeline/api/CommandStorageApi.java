package io.github.junhuhdev.dracarys.pipeline.api;

import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.cmd.CommandContext;

import java.util.List;

public interface CommandStorageApi {

	void update(CommandContext cmd);

	List<Command> findByReferenceId(String refId);
}
