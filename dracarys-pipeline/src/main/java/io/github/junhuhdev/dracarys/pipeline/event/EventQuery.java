package io.github.junhuhdev.dracarys.pipeline.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventQuery {

	Long id;
	String workflow;
	int limit;

}
