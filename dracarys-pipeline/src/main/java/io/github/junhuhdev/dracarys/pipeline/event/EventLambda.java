package io.github.junhuhdev.dracarys.pipeline.event;

@FunctionalInterface
public interface EventLambda {

	void run() throws Exception;

}
