package io.github.junhuhdev.dracarys.pipeline.common;

import java.util.List;

import static java.util.Collections.emptyList;

public interface Conditional {

	default List<String> canProcessWorkflows() {
		return emptyList();
	}

}
