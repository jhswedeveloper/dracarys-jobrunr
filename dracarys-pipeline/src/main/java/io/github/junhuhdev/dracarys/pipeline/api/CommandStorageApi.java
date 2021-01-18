package io.github.junhuhdev.dracarys.pipeline.api;

import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.cmd.CommandContext;

public interface CommandStorageApi {

	void update(CommandContext cmd);
}
