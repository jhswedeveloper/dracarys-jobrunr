package io.github.junhuhdev.dracarys.pipeline.event;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
public enum EventState {
	/** REGISTERED state -> waiting to be processed */
	REGISTERED,
	/** PROCESSING state -> currently being processed by a thread  */
	PROCESSING,
	/** WAITING_CALLBACK state -> waiting for callback from Kafka */
	WAITING_CALLBACK,
	SUCCESSFUL,
	FAILED;
	private final static Map<EventState, List<EventState>> stateTransitions = new HashMap<>();

	static {
		stateTransitions.put(REGISTERED, Lists.newArrayList(PROCESSING, FAILED));
		stateTransitions.put(PROCESSING, Lists.newArrayList(SUCCESSFUL, FAILED, WAITING_CALLBACK));
		stateTransitions.put(WAITING_CALLBACK, Lists.newArrayList(PROCESSING, FAILED));
	}

	public boolean canStateTransitionTo(EventState newState) {
		var possibleNewStates = stateTransitions.get(this);
		if (isEmpty(possibleNewStates)) {
			return false;
		}
		for (var possibleNewState : possibleNewStates) {
			if (possibleNewState.equals(newState)) {
				return true;
			}
		}
		return false;
	}

	public boolean isFinalized() {
		switch (this) {
			case SUCCESSFUL:
			case FAILED:
				return true;
			default:
				return false;
		}
	}

	public boolean isProcessing() {
		return this == PROCESSING;
	}

	public boolean isSuccessful() {
		return this == SUCCESSFUL;
	}

	public boolean isFailed() {
		switch (this) {
			case FAILED:
				return true;
			default:
				return false;
		}
	}
}
