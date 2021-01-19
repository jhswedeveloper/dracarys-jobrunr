package io.github.junhuhdev.dracarys.pipeline.cmd;

import java.util.UUID;

public abstract class CommandRequest implements Command.Request {

	private final String referenceId;

	protected CommandRequest() {
		this.referenceId = getClass().getSimpleName().concat("-").concat(UUID.randomUUID().toString());
	}

	@Override
	public String getReferenceId() {
		return referenceId;
	}

}
