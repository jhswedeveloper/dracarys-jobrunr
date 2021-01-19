package io.github.junhuhdev.dracarys.pipeline.cmd;

import io.github.junhuhdev.dracarys.pipeline.api.CommandStorageApi;
import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;
import io.github.junhuhdev.dracarys.pipeline.exception.AlreadyLockedCmdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

public class LockHandlerCmd implements Command {

	@Component
	@RequiredArgsConstructor
	@Slf4j
	static class Handler implements Command.Handler {

		private final CommandStorageApi commandStorageApi;

		@Override
		public ChainContext execute(ChainContext ctx, Chain chain) throws Exception {
			try {
				ctx.store(new LockCmd());
				if (commandStorageApi.lock(ctx.getId())) {
					return chain.proceed(ctx);
				}
			} catch (Exception e) {
				log.error("Failed to lock cmd id={}", ctx.getId(), e);
			}
			throw new AlreadyLockedCmdException(String.format("Cmd %s is already locked", ctx.getId()));
		}

	}

	private static class LockCmd implements Command {

		private final LocalDateTime created = LocalDateTime.now();

		@Override
		public Optional<CommandStatus> nextState() {
			return Optional.of(CommandStatus.PROCESSING);
		}

	}

}
