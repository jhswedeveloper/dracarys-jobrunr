package io.github.junhuhdev.dracarys.pipeline.common;

import io.github.junhuhdev.dracarys.pipeline.cmd.Command;

public class Voidy implements Command.Request {

	@Override
	public String toString() {
		return Voidy.class.getSimpleName();
	}

	@Override
	public int hashCode() {
		return Voidy.class.getName().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Voidy;
	}

}
