package io.github.junhuhdev.dracarys.jobrunr.configuration;

public class JobRunr {

    private static JobRunrConfiguration jobRunrConfiguration;

    private JobRunr() {
    }

    public static JobRunrConfiguration configure() {
        jobRunrConfiguration = new JobRunrConfiguration();
        Runtime.getRuntime().addShutdownHook(new Thread(JobRunr::destroy, "extShutdownHook"));
        return jobRunrConfiguration;
    }

    public static JobRunrConfiguration destroy() {
        if (jobRunrConfiguration != null) {
            if (jobRunrConfiguration.backgroundJobServer != null) jobRunrConfiguration.backgroundJobServer.stop();
            if (jobRunrConfiguration.dashboardWebServer != null) jobRunrConfiguration.dashboardWebServer.stop();
            if (jobRunrConfiguration.storageProvider != null) jobRunrConfiguration.storageProvider.close();
        }
        return jobRunrConfiguration;
    }
}
