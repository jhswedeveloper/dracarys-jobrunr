package io.github.junhuhdev.dracarys.pipeline.cmd;

import java.util.UUID;

public abstract class CommandRequestBase implements Command.Request {

	private final String referenceId;

	protected CommandRequestBase() {
		this.referenceId = getClass().getSimpleName().concat("-").concat(UUID.randomUUID().toString());
	}

	@Override
	public String getReferenceId() {
		return referenceId;
	}

}
