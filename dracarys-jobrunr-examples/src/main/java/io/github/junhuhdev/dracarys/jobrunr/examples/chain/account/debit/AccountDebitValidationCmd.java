package io.github.junhuhdev.dracarys.jobrunr.examples.chain.account.debit;

import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.cmd.FaultCmd;
import org.springframework.stereotype.Component;

public class AccountDebitValidationCmd implements Command {

	@Component
	static class Handler implements Command.Handler {

		@Override
		public ChainContext execute(ChainContext ctx, Chain chain) throws Exception {
			var req = ctx.getFirst(AccountDebitChain.AccountDebitRequest.class);
			if (req.getAmount().isPositive()) {
				ctx.store(FaultCmd.of("Cannot debit positive amount {}", req.getAmount()));
				return ctx;
			}
			return chain.proceed(ctx);
		}

	}

}
