package io.github.junhuhdev.dracarys.jobrunr.examples.component;

import io.github.junhuhdev.dracarys.pipeline.cmd.Command;

public interface CommandService {

	void save(Command.Request request);
}
