package io.github.junhuhdev.dracarys.pipeline.cmd;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class FaultCmd implements Command {

	private static final int MAX_STACK_FRAMES = 10;
	private final String message;
	private final String stackTrace;

	public FaultCmd(final String message) {
		this.message = message;
		this.stackTrace = null;
	}

	public FaultCmd(final Throwable cause) {
		this.message = null;
		this.stackTrace = getStackTrace(cause);
	}

	public FaultCmd(final String message, final Throwable cause) {
		this.message = message;
		this.stackTrace = getStackTrace(cause);
	}

	public String getMessage() {
		return message;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	private String getStackTrace(Throwable t) {
		if (null == t) {
			return "Exception is null";
		}
		StringBuilder errMsg = new StringBuilder();
		String[] stackTrace = ExceptionUtils.getRootCauseStackTrace(t);
		for (int i = 0; i < MAX_STACK_FRAMES && i < stackTrace.length; i++) {
			errMsg.append(stackTrace[i]).append("\n");
		}
		return errMsg.toString();
	}

}
