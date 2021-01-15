package io.github.junhuhdev.dracarys.pipeline.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import javax.sql.DataSource;
import java.util.Optional;

public abstract class BaseJdbcRepository<K, V> extends NamedParameterJdbcDaoSupport {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	private final int fetchSize;

	protected BaseJdbcRepository(DataSource dataSource, int fetchSize) {
		setDataSource(dataSource);
		this.fetchSize = fetchSize;
	}

	@Override
	protected void initTemplateConfig() {
		super.initTemplateConfig();
		Optional.ofNullable(getJdbcTemplate())
				.ifPresent(jdbcTemplate -> {
					jdbcTemplate.setFetchSize(this.fetchSize);
				});
	}

}
