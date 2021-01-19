package io.github.junhuhdev.dracarys.pipeline.cmd;

import java.util.Optional;

public class FaultRetryCmd implements Command {

	private final String message;

	public FaultRetryCmd(String message) {
		this.message = message;
	}

	public static FaultRetryCmd of(String format, Object... args) {
		return new FaultRetryCmd(String.format(format, args));
	}

	@Override
	public Optional<CommandStatus> nextState() {
		return Optional.of(CommandStatus.RETRY);
	}

	public String getMessage() {
		return message;
	}

}
