package io.github.junhuhdev.dracarys.jobrunr.examples.chain.user.registration;

import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserCreateCmd implements Command {

	@Override
	public ChainContext execute(ChainContext ctx, Chain chain) throws Exception {
		return null;
	}

}
