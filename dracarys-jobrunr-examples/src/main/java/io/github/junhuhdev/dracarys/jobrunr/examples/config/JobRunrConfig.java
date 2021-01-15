package io.github.junhuhdev.dracarys.jobrunr.examples.config;

import io.github.junhuhdev.dracarys.jobrunr.dashboard.JobRunrDashboardWebServer;
import io.github.junhuhdev.dracarys.jobrunr.jobs.mappers.JobMapper;
import io.github.junhuhdev.dracarys.jobrunr.scheduling.BackgroundJob;
import io.github.junhuhdev.dracarys.jobrunr.scheduling.JobScheduler;
import io.github.junhuhdev.dracarys.jobrunr.server.BackgroundJobServer;
import io.github.junhuhdev.dracarys.jobrunr.server.JobActivator;
import io.github.junhuhdev.dracarys.jobrunr.storage.StorageProvider;
import io.github.junhuhdev.dracarys.jobrunr.storage.sql.common.DefaultSqlStorageProvider;
import io.github.junhuhdev.dracarys.jobrunr.utils.mapper.JsonMapper;
import io.github.junhuhdev.dracarys.jobrunr.utils.mapper.gson.GsonJsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

import static io.github.junhuhdev.dracarys.jobrunr.server.BackgroundJobServerConfiguration.usingStandardBackgroundJobServerConfiguration;

@Configuration
@Slf4j
public class JobRunrConfig {

	@Autowired
	private BackgroundJobServer backgroundJobServer;

	@Bean
	public JobActivator jobActivator(ApplicationContext applicationContext) {
		return applicationContext::getBean;
	}

	@Bean
	public StorageProvider storageProvider(DataSource dataSource, JobMapper jobMapper) {
		var storageProvider = new DefaultSqlStorageProvider(dataSource, DefaultSqlStorageProvider.DatabaseOptions.SKIP_CREATE);
		storageProvider.setJobMapper(jobMapper);
		return storageProvider;
	}

	@Bean
	public JobMapper jobMapper(JsonMapper jsonMapper) {
		return new JobMapper(jsonMapper);
	}

	@Bean
	public JsonMapper jsonMapper() {
		return new GsonJsonMapper();
	}

	@Bean
	public BackgroundJobServer backgroundJobServer(StorageProvider storageProvider, JobActivator jobActivator) {
		var config = usingStandardBackgroundJobServerConfiguration().andWorkerCount(5);
		final BackgroundJobServer backgroundJobServer = new BackgroundJobServer(storageProvider, jobActivator, config);
		backgroundJobServer.start();
		return backgroundJobServer;
	}

	@Bean
	public JobRunrDashboardWebServer dashboardWebServer(StorageProvider storageProvider, JsonMapper jsonMapper, Environment env) {
		int port = Integer.parseInt(env.getProperty("server.port")) + 1;
		log.info("Running dashboard on port {}", port);
		final JobRunrDashboardWebServer jobRunrDashboardWebServer = new JobRunrDashboardWebServer(storageProvider, jsonMapper, port);
		jobRunrDashboardWebServer.start();
		return jobRunrDashboardWebServer;
	}

	@Bean
	public JobScheduler initJobRunr(StorageProvider storageProvider, JobActivator jobActivator) {
		JobScheduler jobScheduler = new JobScheduler(storageProvider);
		BackgroundJob.setJobScheduler(jobScheduler);
		return jobScheduler;
	}

}
