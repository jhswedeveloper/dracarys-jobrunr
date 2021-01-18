package io.github.junhuhdev.dracarys.jobrunr.examples.chain.account.debit;

import io.github.junhuhdev.dracarys.pipeline.chain.ChainBase;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountDebitChain extends ChainBase<AccountDebitChain.AccountDebitRequest> {

	@Override
	protected List<Class<? extends Command>> getCommands() {
		return List.of(

		              );
	}

	public static class AccountDebitRequest implements Command.Request {

	}
}
