package io.github.junhuhdev.dracarys.jobrunr.examples.chain.common;

import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Data
@Component
public class EmailSendCmd implements Command {

	private String from;
	private String to;
	private String content;

	@Override
	public ChainContext execute(ChainContext ctx, Chain chain) throws Exception {
		return null;
	}

}
