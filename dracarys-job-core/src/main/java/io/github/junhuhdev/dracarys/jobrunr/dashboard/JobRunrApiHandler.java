package io.github.junhuhdev.dracarys.jobrunr.dashboard;


import io.github.junhuhdev.dracarys.jobrunr.api.DracarysJobStorageApi;
import io.github.junhuhdev.dracarys.jobrunr.dashboard.server.http.RestHttpHandler;
import io.github.junhuhdev.dracarys.jobrunr.dashboard.server.http.handlers.HttpRequestHandler;
import io.github.junhuhdev.dracarys.jobrunr.dashboard.ui.model.RecurringJobUIModel;
import io.github.junhuhdev.dracarys.jobrunr.dashboard.ui.model.problems.ProblemsManager;
import io.github.junhuhdev.dracarys.jobrunr.jobs.Job;
import io.github.junhuhdev.dracarys.jobrunr.jobs.RecurringJob;
import io.github.junhuhdev.dracarys.jobrunr.jobs.states.StateName;
import io.github.junhuhdev.dracarys.jobrunr.storage.JobNotFoundException;
import io.github.junhuhdev.dracarys.jobrunr.storage.PageRequest;
import io.github.junhuhdev.dracarys.jobrunr.storage.StorageProvider;
import io.github.junhuhdev.dracarys.jobrunr.utils.mapper.JsonMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JobRunrApiHandler extends RestHttpHandler {

    private ProblemsManager problemsManager;

    public JobRunrApiHandler(StorageProvider storageProvider, JsonMapper jsonMapper, DracarysJobStorageApi dracarysJobStorageApi) {
        super("/api", jsonMapper);
        this.problemsManager = new ProblemsManager(storageProvider);

        get("/jobs", findJobByState(storageProvider));
        get("/jobs/history/:id", getCmdHistory(dracarysJobStorageApi));

        get("/jobs/:id", getJobById(storageProvider));
        delete("/jobs/:id", deleteJobById(storageProvider));
        post("/jobs/:id/requeue", requeueJobById(storageProvider));

        get("/problems", getProblems(storageProvider));

        get("/recurring-jobs", getRecurringJobs(storageProvider));
        delete("/recurring-jobs/:id", deleteRecurringJob(storageProvider));
        post("/recurring-jobs/:id/trigger", triggerRecurringJob(storageProvider));

        get("/servers", getBackgroundJobServers(storageProvider));

        withExceptionMapping(JobNotFoundException.class, (exc, resp) -> resp.statusCode(404));
    }

    private HttpRequestHandler getCmdHistory(DracarysJobStorageApi dracarysJobStorageApi) {
        return (request, response) -> response.asJson(dracarysJobStorageApi.findByJobId(request.param(":id")));
    }

    private HttpRequestHandler getJobById(StorageProvider storageProvider) {
        return (request, response) -> response.asJson(storageProvider.getJobById(request.param(":id", UUID.class)));
    }

    private HttpRequestHandler deleteJobById(StorageProvider storageProvider) {
        return (request, response) -> {
            storageProvider.delete(request.param(":id", UUID.class));
            response.statusCode(204);
        };
    }

    private HttpRequestHandler requeueJobById(StorageProvider storageProvider) {
        return (request, response) -> {
            final Job job = storageProvider.getJobById(request.param(":id", UUID.class));
            job.enqueue();
            storageProvider.save(job);
            response.statusCode(204);
        };
    }

    private HttpRequestHandler findJobByState(StorageProvider storageProvider) {
        return (request, response) ->
                response.asJson(
                        storageProvider.getJobPage(
                                request.queryParam("state", StateName.class, StateName.ENQUEUED),
                                request.fromQueryParams(PageRequest.class)
                        ));
    }

    private HttpRequestHandler getProblems(StorageProvider storageProvider) {
        return (request, response) -> {
            response.asJson(problemsManager.getProblems());
        };
    }

    private HttpRequestHandler getRecurringJobs(StorageProvider storageProvider) {
        return (request, response) -> {
            final List<RecurringJobUIModel> recurringJobUIModels = storageProvider
                    .getRecurringJobs()
                    .stream()
                    .map(RecurringJobUIModel::new)
                    .collect(Collectors.toList());
            response.asJson(recurringJobUIModels);
        };
    }

    private HttpRequestHandler deleteRecurringJob(StorageProvider storageProvider) {
        return (request, response) -> {
            storageProvider.deleteRecurringJob(request.param(":id"));
            response.statusCode(204);
        };
    }

    private HttpRequestHandler triggerRecurringJob(StorageProvider storageProvider) {
        return (request, response) -> {
            final RecurringJob recurringJob = storageProvider.getRecurringJobs()
                    .stream()
                    .filter(rj -> request.param(":id").equals(rj.getId()))
                    .findFirst()
                    .orElseThrow(() -> new JobNotFoundException(request.param(":id")));

            final Job job = recurringJob.toEnqueuedJob();
            storageProvider.save(job);
            response.statusCode(204);
        };
    }

    private HttpRequestHandler getBackgroundJobServers(StorageProvider storageProvider) {
        return (request, response) -> response.asJson(storageProvider.getBackgroundJobServers());
    }
}
