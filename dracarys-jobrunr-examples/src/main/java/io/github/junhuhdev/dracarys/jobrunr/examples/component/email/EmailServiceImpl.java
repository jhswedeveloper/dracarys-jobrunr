package io.github.junhuhdev.dracarys.jobrunr.examples.component.email;

import io.github.junhuhdev.dracarys.jobrunr.examples.chain.common.EmailSendCmd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService{

	@Override
	public boolean send(EmailSendCmd body) {
		return true;
	}

}
