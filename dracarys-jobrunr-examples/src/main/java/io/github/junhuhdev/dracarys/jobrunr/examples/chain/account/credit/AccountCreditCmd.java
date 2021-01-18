package io.github.junhuhdev.dracarys.jobrunr.examples.chain.account.credit;

import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import org.springframework.stereotype.Component;

public class AccountCreditCmd implements Command {


	@Component
	static class Handler implements Command.Handler {

		@Override
		public ChainContext execute(ChainContext ctx, Chain chain) throws Exception {
			return null;
		}

	}
}
