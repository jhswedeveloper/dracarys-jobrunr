package io.github.junhuhdev.dracarys.jobrunr.examples.api;

import io.github.junhuhdev.dracarys.jobrunr.examples.chain.account.credit.AccountCreditChain;
import io.github.junhuhdev.dracarys.jobrunr.examples.chain.account.debit.AccountDebitChain;
import io.github.junhuhdev.dracarys.jobrunr.examples.component.CommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {

	private final CommandService commandService;

	@PostMapping("/debit")
	public ResponseEntity<?> debit(@RequestBody AccountDebitChain.AccountDebitRequest cmd) {
		var res = commandService.save(cmd);
		return ResponseEntity.ok(res);
	}

	@PostMapping ("/credit")
	public ResponseEntity<?> credit(@RequestBody AccountCreditChain.AccountCreditRequest cmd) {
		var res = commandService.save(cmd);
		return ResponseEntity.ok(res);
	}

}
