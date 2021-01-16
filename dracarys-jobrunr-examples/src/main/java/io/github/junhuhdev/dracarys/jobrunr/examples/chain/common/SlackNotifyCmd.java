package io.github.junhuhdev.dracarys.jobrunr.examples.chain.common;

import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Data
public class SlackNotifyCmd implements Command {

	@Slf4j
	@RequiredArgsConstructor
	@Component
	static class Handler implements Command.Handler {

		@Override
		public ChainContext execute(ChainContext ctx, Chain chain) throws Exception {
			return null;
		}

	}

}
