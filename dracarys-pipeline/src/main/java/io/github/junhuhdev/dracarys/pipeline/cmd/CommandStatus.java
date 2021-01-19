package io.github.junhuhdev.dracarys.pipeline.cmd;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.CollectionUtils.isEmpty;

public enum CommandStatus {
	/** REGISTERED state -> waiting to be processed */
	REGISTERED,
	/** PROCESSING state -> currently being processed by a thread  */
	PROCESSING,
	/** WAITING_CALLBACK state -> waiting for callback from Kafka */
	WAITING_CALLBACK,
	SUCCESSFUL,
	FAILED;
	private final static Map<CommandStatus, List<CommandStatus>> stateTransitions = new HashMap<>();

	static {
		stateTransitions.put(REGISTERED, Lists.newArrayList(PROCESSING, FAILED));
		stateTransitions.put(PROCESSING, Lists.newArrayList(SUCCESSFUL, FAILED, WAITING_CALLBACK));
		stateTransitions.put(WAITING_CALLBACK, Lists.newArrayList(PROCESSING, FAILED));
	}

	public boolean canStateTransitionTo(CommandStatus newState) {
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
}
