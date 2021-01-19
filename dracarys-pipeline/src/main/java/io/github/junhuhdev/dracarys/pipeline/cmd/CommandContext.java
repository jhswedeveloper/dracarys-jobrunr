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
		this.id = ((Command.Request) request).getReferenceId();
	}

	public CommandContext(List<Command> cmds) {
		this.requests = Lists.newArrayList(cmds);
		this.id = cmds.stream()
				.filter(r -> r instanceof Command.Request)
				.map(r -> ((Command.Request) r).getReferenceId())
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Should not throw"));
	}

	public String getId() {
		return this.id;
	}

	public List<Command> getCommands() {
		return requests;
	}

	@SuppressWarnings("unchecked")
	public <CMD extends Command> CMD getFirst(Class<CMD> type) {
		for (Command req : requests) {
			if (type.isInstance(req)) {
				return (CMD) req;
			}
		}
		throw new IllegalArgumentException("Request " + type.getSimpleName() + " not found");
	}

	@SuppressWarnings("unchecked")
	public <CMD extends Command> Optional<CMD> onFirst(Class<CMD> type) {
		for (Command req : requests) {
			if (type.isInstance(req)) {
				return Optional.of((CMD) req);
			}
		}
		return Optional.empty();
	}

	public int countFaults() {
		int count = 0;
		for (Command req : requests) {
			if (req instanceof FaultCmd) {
				count++;
			}
		}
		return count;
	}

	public Command getLast() {
		return requests.get(requests.size() - 1);
	}

	public void store(Command request) {
		this.requests.add(request);
	}

}
