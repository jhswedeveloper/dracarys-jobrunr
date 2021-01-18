package io.github.junhuhdev.dracarys.pipeline.cmd;

import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;

import java.util.UUID;

/**
 * Default interface for all commands to implement.
 */
public interface Command {

	interface Request extends Command {

		default String getReferenceId() {
			return getClass().getSimpleName().concat("-").concat(UUID.randomUUID().toString());
		}
	}

	interface Handler extends Command {

		ChainContext execute(ChainContext ctx, Chain chain) throws Exception;

	}

	@FunctionalInterface
	interface Middleware {

		<R, C extends Command.Handler> ChainContext invoke(C command, Next<R> next) throws Exception;

		interface Next<T> {

			ChainContext invoke() throws Exception;

		}

	}

}
