package io.github.junhuhdev.dracarys.jobrunr.examples.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/family")
public class UserController {

	@GetMapping
	public ResponseEntity<?> find() {
		return ResponseEntity.ok().build();
	}

}
