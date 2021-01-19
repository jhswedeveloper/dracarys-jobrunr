package io.github.junhuhdev.dracarys.pipeline.common;

import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.cmd.CommandRequest;

public class Voidy extends CommandRequest {

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
