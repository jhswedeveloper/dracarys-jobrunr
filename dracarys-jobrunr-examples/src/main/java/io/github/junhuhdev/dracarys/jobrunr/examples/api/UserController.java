package io.github.junhuhdev.dracarys.jobrunr.examples.api;

import io.github.junhuhdev.dracarys.jobrunr.examples.chain.user.registration.UserRegistrationChain;
import io.github.junhuhdev.dracarys.jobrunr.examples.component.CommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

	private final CommandService commandService;

	@GetMapping
	public ResponseEntity<?> find() {
		return ResponseEntity.ok().build();
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody UserRegistrationChain.UserRegistrationRequest request) {
		commandService.save(request);
		return ResponseEntity.ok().build();
	}

}
