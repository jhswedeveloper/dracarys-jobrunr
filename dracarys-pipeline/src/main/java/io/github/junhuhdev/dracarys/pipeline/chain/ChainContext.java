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

    private final List<C> command;

    public ChainContext(C command) {
        this.command = Lists.newArrayList(command);
    }

    public C getEventTransaction() {
        return command.stream().findFirst().get();
    }


    public String getId() {
        return command.stream().findFirst().get().getId();
    }

    /**
     * Command can store command
     * @param command
     */
    public void store(C command) {

    }
}
