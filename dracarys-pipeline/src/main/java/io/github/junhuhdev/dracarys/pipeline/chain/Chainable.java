package io.github.junhuhdev.dracarys.pipeline.chain;

import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.event.EventTransaction;
import org.checkerframework.checker.units.qual.C;

public interface Chainable<R extends Command.Request, C extends Command> {

	/** Main entry point to trigger a chain execution */
	ChainContext<C> dispatch(R event) throws Exception;

}
