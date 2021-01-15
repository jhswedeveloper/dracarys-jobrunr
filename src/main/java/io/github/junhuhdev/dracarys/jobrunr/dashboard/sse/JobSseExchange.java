package io.github.junhuhdev.dracarys.jobrunr.dashboard.sse;

import com.sun.net.httpserver.HttpExchange;
import io.github.junhuhdev.dracarys.jobrunr.jobs.JobId;
import io.github.junhuhdev.dracarys.jobrunr.storage.StorageProvider;
import io.github.junhuhdev.dracarys.jobrunr.storage.listeners.JobChangeListener;
import io.github.junhuhdev.dracarys.jobrunr.utils.mapper.JsonMapper;

import java.io.IOException;


public class JobSseExchange extends AbstractObjectSseExchange implements JobChangeListener {

    private final StorageProvider storageProvider;
    private final JobId jobId;

    public JobSseExchange(HttpExchange httpExchange, StorageProvider storageProvider, JsonMapper jsonMapper) throws IOException {
        super(httpExchange, jsonMapper);
        this.storageProvider = storageProvider;
        this.jobId = getJobId(httpExchange);
        storageProvider.addJobStorageOnChangeListener(this);
    }

    @Override
    public JobId getJobId() {
        return jobId;
    }

    @Override
    public void onChange(Job job) {
        sendObject(job);
        if (job.hasState(SUCCEEDED) || job.hasState(FAILED) || job.hasState(DELETED)) {
            close();
        }
    }

    @Override
    public void close() {
        storageProvider.removeJobStorageOnChangeListener(this);
        super.close();
    }

    private static JobId getJobId(HttpExchange httpExchange) {
        final String url = httpExchange.getRequestURI().toString();
        return JobId.parse(url.substring("/sse/jobs/".length()));
    }
}
