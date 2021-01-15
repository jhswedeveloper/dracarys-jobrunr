package io.github.junhuhdev.dracarys.jobrunr.server.tasks;

import io.github.junhuhdev.dracarys.jobrunr.jobs.states.StateName;
import io.github.junhuhdev.dracarys.jobrunr.server.BackgroundJobServer;
import io.github.junhuhdev.dracarys.jobrunr.storage.StorageProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

import static io.github.junhuhdev.dracarys.jobrunr.common.JobRunrException.shouldNotHappenException;
import static io.github.junhuhdev.dracarys.jobrunr.utils.JobUtils.jobExists;
import static java.util.stream.Collectors.toSet;

public class CheckIfAllJobsExistTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackgroundJobServer.class);

    private final StorageProvider storageProvider;

    public CheckIfAllJobsExistTask(BackgroundJobServer backgroundJobServer) {
        storageProvider = backgroundJobServer.getStorageProvider();
    }

    @Override
    public void run() {
        try {
            Set<String> distinctJobSignatures = storageProvider.getDistinctJobSignatures(StateName.SCHEDULED);
            Set<String> jobsThatCannotBeFound = distinctJobSignatures.stream().filter(job -> !jobExists(job)).collect(toSet());
            if (!jobsThatCannotBeFound.isEmpty()) {
                LOGGER.warn("JobRunr found SCHEDULED jobs that do not exist anymore in your code. These jobs will fail with a JobNotFoundException (due to a ClassNotFoundException or a MethodNotFoundException)." +
                        "\n\tBelow you can find the method signatures of the jobs that cannot be found anymore: " +
                        jobsThatCannotBeFound.stream().map(sign -> "\n\t" + sign + ",").collect(Collectors.joining())
                );
            }
        } catch (Exception e) {
            LOGGER.error("Unexpected exception running `CheckIfAllJobsExistTask`", shouldNotHappenException(e));
        }
    }
}
