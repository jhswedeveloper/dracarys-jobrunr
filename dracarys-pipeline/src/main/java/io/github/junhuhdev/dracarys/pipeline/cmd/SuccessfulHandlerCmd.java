package io.github.junhuhdev.dracarys.pipeline.cmd;

import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

public class SuccessfulHandlerCmd implements Command {

	@Component
	@RequiredArgsConstructor
	@Slf4j
	static class Handler implements Command.Handler {

		@Override
		public ChainContext execute(ChainContext ctx, Chain chain) throws Exception {
			if (ctx.getStatus().canStateTransitionTo(CommandStatus.SUCCESSFUL)) {
				ctx.store(new SuccessfulCmd());
			}
			return chain.proceed(ctx);
		}

	}

	private static class SuccessfulCmd implements Command {

		private final LocalDateTime created = LocalDateTime.now();

		@Override
		public Optional<CommandStatus> nextState() {
			return Optional.of(CommandStatus.SUCCESSFUL);
		}

	}

}
