package io.github.junhuhdev.dracarys.jobrunr.examples.chain.account.credit;

import io.github.junhuhdev.dracarys.jobrunr.examples.component.account.Amount;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainBase;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.cmd.CommandRequestBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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

	@EqualsAndHashCode(callSuper = true)
	@AllArgsConstructor
	@Data
	public static class AccountCreditRequest extends CommandRequestBase {

		private String email;
		private Amount amount;

	}

}
