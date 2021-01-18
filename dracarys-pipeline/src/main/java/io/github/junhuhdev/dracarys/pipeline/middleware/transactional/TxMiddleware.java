package io.github.junhuhdev.dracarys.pipeline.middleware.transactional;

import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import static com.machinezoo.noexception.Exceptions.sneak;

@RequiredArgsConstructor
@Order(2)
public class TxMiddleware implements Command.Middleware {

	private final PlatformTransactionManager txManager;

	@Override
	public <C extends Command.Handler> ChainContext invoke(C command, ChainContext ctx, Next next) throws Exception {
		var tx = new TransactionTemplate(txManager);
		tx.setName("Tx for " + command.getClass().getSimpleName());
		tx.setReadOnly(command instanceof ReadOnly);
		return tx.execute(status -> sneak().get(next::invoke));
	}

}
