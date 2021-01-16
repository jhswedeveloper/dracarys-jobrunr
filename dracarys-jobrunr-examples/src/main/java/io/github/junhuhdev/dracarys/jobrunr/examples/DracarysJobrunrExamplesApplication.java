package io.github.junhuhdev.dracarys.jobrunr.examples;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DracarysJobrunrExamplesApplication {

	static {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}

	public static void main(String[] args) {
		SpringApplication.run(DracarysJobrunrExamplesApplication.class, args);
	}

}
