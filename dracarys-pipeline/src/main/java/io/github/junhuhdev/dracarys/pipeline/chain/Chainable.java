package io.github.junhuhdev.dracarys.pipeline.chain;

import io.github.junhuhdev.dracarys.pipeline.cmd.Command;

public interface Chainable {

	/** Main entry point to trigger a chain execution */
	ChainContext dispatch(Command.Request event) throws Exception;

}
