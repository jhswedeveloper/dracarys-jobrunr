package io.github.junhuhdev.dracarys.pipeline.chain;

import com.google.common.collect.Lists;
import io.github.junhuhdev.dracarys.pipeline.event.Event;
import io.github.junhuhdev.dracarys.pipeline.event.EventState;
import io.github.junhuhdev.dracarys.pipeline.event.EventTransaction;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class ChainContext {

    private final EventTransaction xEventTransaction;

    public ChainContext(EventTransaction xEventTransaction) {
        this.xEventTransaction = xEventTransaction;
    }

    public EventTransaction getEventTransaction() {
        return xEventTransaction;
    }

    public void setNextWorkflow(String workflow) {
        xEventTransaction.setWorkflow(workflow);
    }

    public String getWorkflow() {
        return xEventTransaction.getWorkflow();
    }

    public Long getId() {
        return xEventTransaction.getId();
    }

    public Long getParentId() {
        return xEventTransaction.getParentId();
    }

    public Long getOriginId() {
        return isNull(xEventTransaction.getParentId()) ? xEventTransaction.getId() : xEventTransaction.getParentId();
    }

    public List<Event> getEvents() {
        return xEventTransaction.getEvents();
    }

    public EventState getState() {
        return xEventTransaction.getState();
    }

    public <T extends Event> T getFirstEvent(Class<T> event) {
        return this.xEventTransaction.getFirstEvent(event);
    }

    public <T extends Event> T getLastEvent(Class<T> event) {
        return this.xEventTransaction.getLatestEvent(event);
    }

    public <T extends Event> T getLastEvent() {
        return this.xEventTransaction.getLatestEvent();
    }

    public EventTransaction newChild(Event nextEvent, String nextWorkflow) {
        return EventTransaction.builder()
                .parentId(getOriginId())
                .workflow(nextWorkflow)
                .events(Lists.newArrayList(nextEvent))
                .build();
    }

    public void store(Event event) {
        requireNonNull(event, "Event cannot be null");
        this.xEventTransaction.store(event);
        event.nextUpdate(this.xEventTransaction);
        var onNextState = event.nextState();
        onNextState.ifPresent(this.xEventTransaction::triggerStateTransition);
    }
}
