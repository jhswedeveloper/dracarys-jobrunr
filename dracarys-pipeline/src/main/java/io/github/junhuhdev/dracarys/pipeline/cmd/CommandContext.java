package io.github.junhuhdev.dracarys.pipeline.cmd;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CommandContext {

	private final String id;
	private final List<Command> requests;

	public CommandContext(Command request) {
		this.requests = Lists.newArrayList(request);
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return this.id;
	}

	public List<Command> getRequests() {
		return requests;
	}

	@SuppressWarnings("unchecked")
	public <R extends Command> R getFirst(Class<R> type) {
		for (Command req : requests) {
			if (type.isInstance(req)) {
				return (R) req;
			}
		}
		throw new IllegalArgumentException("Request " + type.getSimpleName() + " not found");
	}

	@SuppressWarnings("unchecked")
	public <R extends Command> Optional<R> onFirst(Class<R> type) {
		for (Command req : requests) {
			if (type.isInstance(req)) {
				return Optional.of((R) req);
			}
		}
		return Optional.empty();
	}

	public void store(Command request) {
		this.requests.add(request);
	}

}
