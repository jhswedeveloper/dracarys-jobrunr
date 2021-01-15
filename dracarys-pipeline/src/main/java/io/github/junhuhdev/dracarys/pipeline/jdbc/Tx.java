package io.github.junhuhdev.dracarys.pipeline.jdbc;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

/** Wrapper to execute db transactions programatically */
public class Tx {

	private final TransactionTemplate tx;

	public Tx(TransactionTemplate tx) {
		this.tx = tx;
		tx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
	}

	<T> T wrap(Supplier<T> action) {
		return tx.execute(status -> action.get());
	}

}
