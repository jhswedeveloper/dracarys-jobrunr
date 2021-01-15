package io.github.junhuhdev.dracarys.pipeline.cmd;

import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;
import io.github.junhuhdev.dracarys.pipeline.event.Event;
import io.github.junhuhdev.dracarys.pipeline.event.EventState;
import io.github.junhuhdev.dracarys.pipeline.jdbc.EventJdbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class TransactionLockCmd implements Command {

	private final EventJdbcRepository eventJdbcRepository;

	@Override
	public ChainContext execute(ChainContext ctx, Chain chain) throws Exception {
		try {
			ctx.store(new LockEvent());
			if (eventJdbcRepository.lock(ctx.getEventTransaction())) {
				return chain.proceed(ctx);
			}
		} catch (Exception e) {
			log.error("Could not set to processing event={}", ctx.getEventTransaction(), e);
			return ctx;
		}
		log.warn("Event is already being processed by another thread... {}", ctx.getEventTransaction());
		return ctx;
	}

	@Value
	private static class LockEvent implements Event {

		@Override
		public Optional<EventState> nextState() {
			return Optional.of(EventState.PROCESSING);
		}

	}

}
