package io.github.junhuhdev.dracarys.jobrunr.examples.chain.account.credit;

import io.github.junhuhdev.dracarys.jobrunr.examples.component.account.Amount;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainBase;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountCreditChain extends ChainBase<AccountCreditChain.AccountCreditRequest> {

	@Override
	protected List<Class<? extends Command>> getCommands() {
		return List.of(
				AccountCreditValidationCmd.class,
				AccountCreditCmd.class);
	}

	@AllArgsConstructor
	@Data
	public static class AccountCreditRequest implements Command.Request {

		private String email;
		private Amount amount;

	}

}
