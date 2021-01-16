package io.github.junhuhdev.dracarys.pipeline.cmd;

import com.google.common.collect.Lists;
import org.checkerframework.checker.units.qual.C;

import java.util.List;
import java.util.UUID;

public class CommandContext {

	private final String id;
	private final List<Command.Request> requests;

	public CommandContext(Command.Request request) {
		this.requests = Lists.newArrayList(request);
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return this.id;
	}

	public List<Command.Request> getRequests() {
		return requests;
	}

	@SuppressWarnings("unchecked")
	public <R extends Command.Request> R getFirst(Class<R> type) {
		for (var req : requests) {
			if (type.isInstance(req)) {
				return (R) req;
			}
		}
		throw new IllegalArgumentException("Request " + type.getSimpleName() + " not found");
	}

	public void store(Command.Request request) {
		this.requests.add(request);
	}

}
