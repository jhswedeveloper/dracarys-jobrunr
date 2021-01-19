package io.github.junhuhdev.dracarys.pipeline.api;

import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.cmd.CommandContext;
import io.github.junhuhdev.dracarys.pipeline.cmd.CommandStatus;

import java.util.List;

public interface CommandStorageApi {

	void update(CommandContext cmd);

	boolean lock(String referenceId);

	List<Command> findByReferenceId(String referenceId);

	CommandStatus findStatus(String referenceId);
}
