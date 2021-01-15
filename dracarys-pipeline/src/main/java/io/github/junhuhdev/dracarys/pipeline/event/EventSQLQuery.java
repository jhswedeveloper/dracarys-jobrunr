package io.github.junhuhdev.dracarys.pipeline.event;

import lombok.Value;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

@Value
public class EventSQLQuery {

	String sql;
	SqlParameterSource params;

}
