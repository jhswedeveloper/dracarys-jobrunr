package io.github.junhuhdev.dracarys.jobrunr.examples.component.email;

import io.github.junhuhdev.dracarys.jobrunr.examples.chain.common.EmailSendCmd;

public interface EmailService {

	boolean send(EmailSendCmd body);
}
