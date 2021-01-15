package io.github.junhuhdev.dracarys.jobrunr.dashboard;

/**
 * This class allows to configure the JobRunrDashboard
 */
public class JobRunrDashboardWebServerConfiguration {
    int port = 8000;

    private JobRunrDashboardWebServerConfiguration() {

    }

    /**
     * This returns the default configuration with the JobRunrDashboard running on port 8000
     *
     * @return the default JobRunrDashboard configuration
     */
    public static org.jobrunr.dashboard.JobRunrDashboardWebServerConfiguration usingStandardDashboardConfiguration() {
        return new org.jobrunr.dashboard.JobRunrDashboardWebServerConfiguration();
    }

    /**
     * Specifies the port on which the JobRunrDashboard will run
     *
     * @param port the port on which the JobRunrDashboard will run
     * @return the same configuration instance which provides a fluent api
     */
    public org.jobrunr.dashboard.JobRunrDashboardWebServerConfiguration andPort(int port) {
        this.port = port;
        return this;
    }
}
