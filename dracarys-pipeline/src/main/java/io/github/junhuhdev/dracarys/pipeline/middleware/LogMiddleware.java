package io.github.junhuhdev.dracarys.pipeline.middleware;

import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.machinezoo.noexception.Exceptions.sneak;

@RequiredArgsConstructor
@Order(1)
public class LogMiddleware implements Command.Middleware {

	private final CorrelationId correlationId;

	private <R, C extends Command.Handler> Logger logger(C command) {
		return LoggerFactory.getLogger(command.getClass());
	}

	@SuppressWarnings("Unchecked")
	@Override
	public <R, C extends Command.Handler> ChainContext invoke(C command, Next<R> next) throws Exception {
		var logger = logger(command);
		return correlationId.wrap(() -> {
			logger.info("---> Started cmd={}, input={}", command.getClass().getDeclaringClass().getSimpleName(), ((Chain.HandleCommand) next).ctx().getLast());
			ChainContext response = sneak().get(next::invoke);
			logger.info("<--- Completed cmd={}", command.getClass().getDeclaringClass().getSimpleName());
			return response;
		});
	}

}
