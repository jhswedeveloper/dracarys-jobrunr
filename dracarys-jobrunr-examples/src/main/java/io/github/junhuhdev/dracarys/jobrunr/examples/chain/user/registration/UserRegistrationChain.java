package io.github.junhuhdev.dracarys.jobrunr.examples.chain.user.registration;

import io.github.junhuhdev.dracarys.jobrunr.examples.chain.common.EmailSendCmd;
import io.github.junhuhdev.dracarys.jobrunr.examples.chain.common.SlackNotifyCmd;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainBase;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRegistrationChain extends ChainBase {

	@Override
	protected List<Class<? extends Command>> getCommands() {
		return List.of(
				UserValidateCmd.class,
				UserCreateCmd.class,
				EmailSendCmd.class,
				SlackNotifyCmd.class);
	}

}
