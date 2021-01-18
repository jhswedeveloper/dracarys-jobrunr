package io.github.junhuhdev.dracarys.pipeline.cmd;

import io.github.junhuhdev.dracarys.pipeline.api.CommandStorageApi;
import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Data
public class SaveAsLastCmd implements Command {

	@Slf4j
	@RequiredArgsConstructor
	@Component
	static class Handler implements Command.Handler {

		private final CommandStorageApi commandStorageApi;

		@Override
		public ChainContext execute(ChainContext ctx, Chain chain) throws Exception {
			ctx = chain.proceed(ctx);
			commandStorageApi.update(ctx.getCmdCtx());
			return ctx;
		}

	}

}
