package io.github.junhuhdev.dracarys.jobrunr.examples.chain.user.registration;

import io.github.junhuhdev.dracarys.jobrunr.examples.jpa.repository.UserRepository;
import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
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

		@Override
		public ChainContext execute(ChainContext ctx, Chain chain) throws Exception {
			log.info("---> Running {}", getClass().getSimpleName());
			var request = ctx.getFirst(UserRegistrationChain.UserRegistrationRequest.class);
			var userExists = userRepository.existsById(request.getEmail());
			if (userExists) {

			}
			return chain.proceed(ctx);
		}

	}

}
