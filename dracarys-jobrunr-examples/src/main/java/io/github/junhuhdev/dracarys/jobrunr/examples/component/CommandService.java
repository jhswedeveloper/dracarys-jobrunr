package io.github.junhuhdev.dracarys.jobrunr.examples.component;

import io.github.junhuhdev.dracarys.jobrunr.examples.jpa.entity.CommandEntity;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;

public interface CommandService {

	CommandEntity save(Command.Request request);
}
