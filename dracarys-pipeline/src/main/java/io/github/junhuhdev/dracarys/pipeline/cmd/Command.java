package io.github.junhuhdev.dracarys.pipeline.cmd;

import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;
import io.github.junhuhdev.dracarys.pipeline.common.Conditional;

/**
 * Default interface for all commands to implement.
 */
public interface Command extends Conditional {
//	/**
//	 * (1) Proceed to next command "chain.proceed(ctx);"
//	 * (2) Do not proceed to next command "return ctx"
//	 */
//	ChainContext execute(ChainContext ctx, Chain chain) throws Exception;

	interface Request extends Command {
	}

	interface Handler extends Command {

		ChainContext execute(ChainContext ctx, Chain chain) throws Exception;

	}

}
