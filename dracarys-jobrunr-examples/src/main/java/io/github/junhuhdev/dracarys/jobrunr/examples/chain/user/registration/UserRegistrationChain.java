package io.github.junhuhdev.dracarys.jobrunr.examples.chain.user.registration;

import io.github.junhuhdev.dracarys.jobrunr.examples.chain.common.EmailSendCmd;
import io.github.junhuhdev.dracarys.jobrunr.examples.chain.common.SlackNotifyCmd;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainBase;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.cmd.CommandRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRegistrationChain extends ChainBase<UserRegistrationChain.UserRegistrationRequest> {

	@Override
	protected List<Class<? extends Command>> getCommands() {
		return List.of(
				UserValidateCmd.class,
				UserCreateCmd.class,
				EmailSendCmd.class,
				SlackNotifyCmd.class);
	}

	@EqualsAndHashCode(callSuper = true)
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class UserRegistrationRequest extends CommandRequest {

		private String email;
		private String name;
		private String password;

	}

}
