package huh.enterprises.dracarysjobrunr.config;

import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.server.JobActivator;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.sql.common.DefaultSqlStorageProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class DracarysJobRunrConfiguration {

	@Bean
	public JobActivator jobActivator(ApplicationContext applicationContext) {
		return applicationContext::getBean;
	}

	@Bean
	public StorageProvider storageProvider(DataSource dataSource) {
		var storageProvider = new DefaultSqlStorageProvider(dataSource, DefaultSqlStorageProvider.DatabaseOptions.SKIP_CREATE);
		storageProvider.setJobMapper(new JobRunrJobMapper());
		return storageProvider;
	}

	@Bean
	public JobScheduler initJobRunr(StorageProvider storageProvider, JobActivator jobActivator, Environment environment) {
		return new JobRunrConfig()
				.useStorageProvider(storageProvider)
				.useJobActivator(jobActivator)
				.useDefaultBackgroundJobServerIf(true, 10)
				.useDashboard(Integer.parseInt(environment.getProperty("server.port")) + 1)
				.initialize();
	}

}
