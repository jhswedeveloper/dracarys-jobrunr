package io.github.junhuhdev.dracarys.jobrunr.examples.chain.common;

import io.github.junhuhdev.dracarys.jobrunr.examples.component.email.EmailService;
import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Builder
@AllArgsConstructor
@Data
public class EmailSendCmd implements Command {

	private String from;
	private String to;
	private String content;

	@Slf4j
	@RequiredArgsConstructor
	@Component
	static class Handler implements Command.Handler {

		private final EmailService emailService;

		@Override
		public ChainContext execute(ChainContext ctx, Chain chain) throws Exception {
			var request = ctx.onFirst(EmailSendCmd.class);
			request.ifPresent(emailService::send);
			return chain.proceed(ctx);
		}

	}

}
