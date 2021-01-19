package io.github.junhuhdev.dracarys.pipeline.cmd;

import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Default interface for all commands to implement.
 */
public interface Command {

	default Optional<CommandStatus> nextState() {
		return Optional.empty();
	}

	interface Request extends Command {

		String getReferenceId();

	}

	interface Handler extends Command {

		ChainContext execute(ChainContext ctx, Chain chain) throws Exception;

	}

	@FunctionalInterface
	interface Middlewares {

		Stream<Middleware> supply();

	}

	@FunctionalInterface
	interface Middleware {

		<CMD extends Command.Handler> ChainContext invoke(CMD command, ChainContext ctx, Next next) throws Exception;

		interface Next {

			ChainContext invoke() throws Exception;

		}

	}

}
