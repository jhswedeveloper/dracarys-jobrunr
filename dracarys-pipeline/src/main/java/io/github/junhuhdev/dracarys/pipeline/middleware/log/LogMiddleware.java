package io.github.junhuhdev.dracarys.pipeline.middleware.log;

import com.machinezoo.noexception.throwing.ThrowingSupplier;
import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import java.time.Duration;
import java.time.Instant;

import static com.machinezoo.noexception.Exceptions.sneak;

@RequiredArgsConstructor
@Order(1)
public class LogMiddleware implements Command.Middleware {

	private final CorrelationId correlationId;

	private <C extends Command.Handler> Logger logger(C command) {
		return LoggerFactory.getLogger(command.getClass());
	}

	@SuppressWarnings("Unchecked")
	@Override
	public <C extends Command.Handler> ChainContext invoke(C command, ChainContext ctx, Next next) throws Exception {
		var logger = logger(command);
		return correlationId.wrap(() -> {
			Instant start = Instant.now();
			logger.info("---> Started cmd={}, input={}", command.getClass().getDeclaringClass().getSimpleName(), ctx.getLast());
			ChainContext response = sneak().get(next::invoke);
			logger.info("<--- Completed cmd={} took {} ms", command.getClass().getDeclaringClass().getSimpleName(), Duration.between(start, Instant.now()).toMillis());
			return response;
		});
	}

}
