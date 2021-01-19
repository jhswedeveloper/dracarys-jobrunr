package io.github.junhuhdev.dracarys.pipeline.cmd;

public class FaultRetryCmd extends FaultCmd {

	public FaultRetryCmd(String message) {
		super(message);
	}

	public static FaultRetryCmd of(String format, Object... args) {
		return new FaultRetryCmd(String.format(format, args));
	}

}
