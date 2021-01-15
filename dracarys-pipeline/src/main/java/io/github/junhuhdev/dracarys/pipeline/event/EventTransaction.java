package io.github.junhuhdev.dracarys.pipeline.event;

import com.google.common.collect.Lists;
import io.github.junhuhdev.dracarys.pipeline.xstream.XStreamFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventTransaction implements Event {

	private Long id;
	private Long parentId;
	private String referenceId;
	@Builder.Default
	private EventState state = EventState.REGISTERED;
	private String workflow;
	private List<Event> events = new ArrayList<>();
	@Builder.Default
	private LocalDateTime created = LocalDateTime.now();

	public static EventTransaction newTransaction(Event event, String workflow) {
		return EventTransaction.builder()
				.workflow(workflow)
				.events(Lists.newArrayList(event))
				.build();
	}

	@SuppressWarnings("unchecked")
	public static List<Event> fromXml(String xml) {
		return (List<Event>) XStreamFactory.xstream().fromXML(xml);
	}

	public boolean isProcessing() {
		return getState().isProcessing();
	}

	public void triggerStateTransition(EventState newState) {
		if (!state.canStateTransitionTo(newState)) {
			throw new IllegalStateException("Cannot trigger state transition from " + this.state + " to " + newState);
		}
		this.state = newState;
	}

	@SuppressWarnings("unchecked")
	public <T extends Event> T getFirstEvent(Class<T> event) {
		for (Event xEvent : events) {
			if (event.isInstance(xEvent)) {
				return (T) xEvent;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T extends Event> T getLatestEvent(Class<T> event) {
		for (int i = events.size() - 1; i >= 0; i--) {
			if (event.isInstance(events.get(i))) {
				return (T) events.get(i);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T extends Event> T getLatestEvent() {
		return (T) events.get(events.size() - 1);
	}

	public void store(Event event) {
		if (Objects.nonNull(event)) {
			this.events.add(event);
		}
	}

	public String toXml() {
		return XStreamFactory.xstream().toXML(getEvents());
	}

	@Override
	public String toString() {
		return "XEventTransaction{" +
				"id=" + id +
				", parentId=" + parentId +
				", state=" + state +
				", workflow='" + workflow + '\'' +
				'}';
	}

}
