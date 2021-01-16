package io.github.junhuhdev.dracarys.pipeline.chain;

import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.cmd.CommandContext;

import java.util.List;

public class ChainContext {

	private final CommandContext commandContext;

	public ChainContext(Command.Request command) {
		this.commandContext = new CommandContext(command);
	}

	public String getId() {
		return this.commandContext.getId();
	}

	public List<Command.Request> getAll() {
		return this.commandContext.getRequests();
	}

	public <R extends Command.Request> R getFirst(Class<R> type) {
		return this.commandContext.getFirst(type);
	}

	/**
	 * Command can store command
	 * @param command
	 */
	public void store(Command.Request command) {
		this.commandContext.store(command);
	}

}
