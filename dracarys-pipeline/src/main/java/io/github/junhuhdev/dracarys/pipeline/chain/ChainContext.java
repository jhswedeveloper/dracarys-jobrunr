package io.github.junhuhdev.dracarys.pipeline.chain;

import com.google.common.collect.Lists;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.event.Event;
import io.github.junhuhdev.dracarys.pipeline.event.EventState;
import io.github.junhuhdev.dracarys.pipeline.event.EventTransaction;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class ChainContext<C extends Command> {

    private final C command;

    public ChainContext(C command) {
        this.command = command;
    }

    public C getEventTransaction() {
        return command;
    }


    public Long getId() {
        return command.getId();
    }

    public Long getParentId() {
        return command.getParentId();
    }

    public Long getOriginId() {
        return isNull(command.getParentId()) ? command.getId() : command.getParentId();
    }

    public List<Event> getEvents() {
        return command.getEvents();
    }

    public EventState getState() {
        return command.getState();
    }

    public <T extends Event> T getFirstEvent(Class<T> event) {
        return this.command.getFirstEvent(event);
    }

    public <T extends Event> T getLastEvent(Class<T> event) {
        return this.command.getLatestEvent(event);
    }

    public <T extends Event> T getLastEvent() {
        return this.command.getLatestEvent();
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
        this.command.store(event);
        event.nextUpdate(this.command);
        var onNextState = event.nextState();
        onNextState.ifPresent(this.command::triggerStateTransition);
    }
}
