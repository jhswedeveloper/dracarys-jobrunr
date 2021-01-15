package io.github.junhuhdev.dracarys.pipeline.event;

import java.util.Optional;

public interface Event {

	/** Optionally override this method when nextState transition is required */
	default Optional<EventState> nextState() {
		return Optional.empty();
	}

	default void nextUpdate(EventTransaction xEventTransaction) {
	}

}
