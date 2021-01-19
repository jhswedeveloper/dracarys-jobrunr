package io.github.junhuhdev.dracarys.pipeline.cmd;

import io.github.junhuhdev.dracarys.pipeline.api.CommandStorageApi;
import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;
import io.github.junhuhdev.dracarys.pipeline.exception.RetryCmdException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Data
public class ExceptionHandlerCmd implements Command {

	@Slf4j
	@RequiredArgsConstructor
	@Component
	static class Handler implements Command.Handler {

		private final CommandStorageApi commandStorageApi;

		@Override
		public ChainContext execute(ChainContext ctx, Chain chain) throws Exception {
			try {
				return chain.proceed(ctx);
			} catch (RetryCmdException e) {
				log.error("Retrying fault {}", ctx.getId(), e);
				ctx.store(FaultRetryCmd.of("Retrying %s", e.getMessage()));
				commandStorageApi.update(ctx.getCmdCtx());
				throw e;
			} catch (Exception e) {
				log.error("Exception was thrown while processing {}", ctx.getId(), e);
				ctx.store(new FaultCmd(e));
			}
			return ctx;
		}

	}

}
