package io.github.junhuhdev.dracarys.pipeline.chain;

import io.github.junhuhdev.dracarys.pipeline.cmd.Command;

public interface Chainable<R extends Command.Request> {

	/** Main entry point to trigger a chain execution */
	<C extends Command> ChainContext<C> dispatch(R event) throws Exception;

}
