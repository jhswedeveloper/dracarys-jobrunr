package io.github.junhuhdev.dracarys.jobrunr.examples.chain.user.registration;

import io.github.junhuhdev.dracarys.jobrunr.examples.jpa.repository.CommandRepository;
import io.github.junhuhdev.dracarys.jobrunr.examples.jpa.repository.UserRepository;
import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.cmd.FaultCmd;
import io.github.junhuhdev.dracarys.pipeline.exception.RetryCmdException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Data
public class UserValidateCmd implements Command {

	@Slf4j
	@RequiredArgsConstructor
	@Component
	static class Handler implements Command.Handler {

		private final UserRepository userRepository;
		private final CommandRepository commandRepository;

		@Override
		public ChainContext execute(ChainContext ctx, Chain chain) throws Exception {
			var request = ctx.getFirst(UserRegistrationChain.UserRegistrationRequest.class);
			var userExists = userRepository.existsById(request.getEmail());
			var cmd = commandRepository.findByReferenceId(request.getReferenceId());
			if (userExists) {
				throw new RetryCmdException(String.format("User with email %s already exists.", request.getEmail()));
//				ctx.store(FaultCmd.of("User with email %s already exists.", request.getEmail()));
//				return ctx;
			}
			return chain.proceed(ctx);
		}

	}

}
