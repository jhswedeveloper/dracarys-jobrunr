package io.github.junhuhdev.dracarys.jobrunr.dashboard;

import io.github.junhuhdev.dracarys.jobrunr.dashboard.server.http.StaticFileHttpHandler;

public class JobRunrStaticFileHandler extends StaticFileHttpHandler {

    public JobRunrStaticFileHandler() {
        super("/dashboard", "org/jobrunr/dashboard/frontend/build/", true);
    }

}
