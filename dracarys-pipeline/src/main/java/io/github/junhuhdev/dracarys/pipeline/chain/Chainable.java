package io.github.junhuhdev.dracarys.pipeline.chain;

import io.github.junhuhdev.dracarys.pipeline.event.EventTransaction;

public interface Chainable {

	/** Main entry point to trigger a chain execution */
	ChainContext dispatch(EventTransaction event) throws Exception;

	/** Resume processing of callback event */
	default ChainContext resume(String referenceId) throws Exception {
		throw new IllegalStateException(String.format("Chain %s does not support resume", this.getClass().getSimpleName()));
	}

	/** Flag whether this chain can be mixed with others */
	default boolean isMixable() {
		return true;
	}

}
