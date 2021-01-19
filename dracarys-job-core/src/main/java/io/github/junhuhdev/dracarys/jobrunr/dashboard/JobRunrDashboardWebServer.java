package io.github.junhuhdev.dracarys.jobrunr.dashboard;

import com.sun.net.httpserver.HttpContext;
import io.github.junhuhdev.dracarys.jobrunr.api.DracarysJobStorageApi;
import io.github.junhuhdev.dracarys.jobrunr.dashboard.server.TeenyHttpHandler;
import io.github.junhuhdev.dracarys.jobrunr.dashboard.server.TeenyWebServer;
import io.github.junhuhdev.dracarys.jobrunr.dashboard.server.http.RedirectHttpHandler;
import io.github.junhuhdev.dracarys.jobrunr.storage.StorageProvider;
import io.github.junhuhdev.dracarys.jobrunr.storage.ThreadSafeStorageProvider;
import io.github.junhuhdev.dracarys.jobrunr.utils.annotations.VisibleFor;
import io.github.junhuhdev.dracarys.jobrunr.utils.mapper.JsonMapper;
import io.github.junhuhdev.dracarys.jobrunr.utils.mapper.jackson.JacksonJsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.junhuhdev.dracarys.jobrunr.dashboard.JobRunrDashboardWebServerConfiguration.usingStandardDashboardConfiguration;

/**
 * Provides a dashboard which gives insights in your jobs and servers.
 * The dashboard server starts by default on port 8000.
 *
 * @author Ronald Dehuysser
 */
public class JobRunrDashboardWebServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobRunrDashboardWebServer.class);

    private final StorageProvider storageProvider;
    private final JsonMapper jsonMapper;
    private final DracarysJobStorageApi dracarysJobStorageApi;
    private final int port;

    private TeenyWebServer teenyWebServer;

    public static void main(String[] args) {
        new JobRunrDashboardWebServer(null, new JacksonJsonMapper(), null);
    }

    public JobRunrDashboardWebServer(StorageProvider storageProvider, JsonMapper jsonMapper, DracarysJobStorageApi dracarysJobStorageApi) {
        this(storageProvider, jsonMapper, 8000, dracarysJobStorageApi);
    }

    public JobRunrDashboardWebServer(StorageProvider storageProvider, JsonMapper jsonMapper, int port, DracarysJobStorageApi dracarysJobStorageApi) {
        this(storageProvider, jsonMapper, usingStandardDashboardConfiguration().andPort(port), dracarysJobStorageApi);
    }

    public JobRunrDashboardWebServer(StorageProvider storageProvider, JsonMapper jsonMapper, JobRunrDashboardWebServerConfiguration configuration, DracarysJobStorageApi dracarysJobStorageApi) {
        this.storageProvider = new ThreadSafeStorageProvider(storageProvider);
        this.jsonMapper = jsonMapper;
        this.port = configuration.port;
        this.dracarysJobStorageApi = dracarysJobStorageApi;
    }

    public void start() {
        RedirectHttpHandler redirectHttpHandler = new RedirectHttpHandler("/", "/dashboard");
        JobRunrStaticFileHandler staticFileHandler = createStaticFileHandler();
        JobRunrApiHandler dashboardHandler = createApiHandler(storageProvider, jsonMapper, dracarysJobStorageApi);
        JobRunrSseHandler sseHandler = createSSeHandler(storageProvider, jsonMapper);

        teenyWebServer = new TeenyWebServer(port);
        registerContext(redirectHttpHandler);
        registerContext(staticFileHandler);
        registerContext(dashboardHandler);
        registerContext(sseHandler);
        teenyWebServer.start();

        LOGGER.info("JobRunr Dashboard started at http://{}:{}",
                teenyWebServer.getWebServerHostAddress(),
                teenyWebServer.getWebServerHostPort());
    }

    public void stop() {
        if (teenyWebServer == null) return;
        teenyWebServer.stop();
        LOGGER.info("JobRunr dashboard stopped");
        teenyWebServer = null;
    }

    HttpContext registerContext(TeenyHttpHandler httpHandler) {
        return teenyWebServer.createContext(httpHandler);
    }

    @VisibleFor("github issue 18")
    JobRunrStaticFileHandler createStaticFileHandler() {
        return new JobRunrStaticFileHandler();
    }

    @VisibleFor("github issue 18")
    JobRunrApiHandler createApiHandler(StorageProvider storageProvider, JsonMapper jsonMapper, DracarysJobStorageApi dracarysJobStorageApi) {
        return new JobRunrApiHandler(storageProvider, jsonMapper, dracarysJobStorageApi);
    }

    @VisibleFor("github issue 18")
    JobRunrSseHandler createSSeHandler(StorageProvider storageProvider, JsonMapper jsonMapper) {
        return new JobRunrSseHandler(storageProvider, jsonMapper);
    }

}
