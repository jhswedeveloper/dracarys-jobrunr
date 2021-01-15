CREATE TABLE jobrunr_migrations
(
    id          nchar(36) PRIMARY KEY,
    script      varchar(64) NOT NULL,
    installedOn varchar(29) NOT NULL
);

CREATE TABLE jobrunr_jobs
(
    id           NCHAR(36) PRIMARY KEY,
    version      int          NOT NULL,
    jobAsJson    text         NOT NULL,
    jobSignature VARCHAR(512) NOT NULL,
    state        VARCHAR(36)  NOT NULL,
    createdAt    TIMESTAMP    NOT NULL,
    updatedAt    TIMESTAMP    NOT NULL,
    scheduledAt  TIMESTAMP
);
CREATE INDEX jobrunr_state_idx ON jobrunr_jobs (state);
CREATE INDEX jobrunr_job_signature_idx ON jobrunr_jobs (jobSignature);
CREATE INDEX jobrunr_job_created_at_idx ON jobrunr_jobs (createdAt);
CREATE INDEX jobrunr_job_updated_at_idx ON jobrunr_jobs (updatedAt);
CREATE INDEX jobrunr_job_scheduled_at_idx ON jobrunr_jobs (scheduledAt);


CREATE TABLE jobrunr_recurring_jobs
(
    id        NCHAR(128) PRIMARY KEY,
    version   int  NOT NULL,
    jobAsJson text NOT NULL
);



CREATE TABLE jobrunr_backgroundjobservers
(
    id                     NCHAR(36) PRIMARY KEY,
    workerPoolSize         int           NOT NULL,
    pollIntervalInSeconds  int           NOT NULL,
    firstHeartbeat         TIMESTAMP(6)  NOT NULL,
    lastHeartbeat          TIMESTAMP(6)  NOT NULL,
    running                int           NOT NULL,
    systemTotalMemory      BIGINT        NOT NULL,
    systemFreeMemory       BIGINT        NOT NULL,
    systemCpuLoad          NUMERIC(3, 2) NOT NULL,
    processMaxMemory       BIGINT        NOT NULL,
    processFreeMemory      BIGINT        NOT NULL,
    processAllocatedMemory BIGINT        NOT NULL,
    processCpuLoad         NUMERIC(3, 2) NOT NULL
);
CREATE INDEX jobrunr_bgjobsrvrs_fsthb_idx ON jobrunr_backgroundjobservers (firstHeartbeat);
CREATE INDEX jobrunr_bgjobsrvrs_lsthb_idx ON jobrunr_backgroundjobservers (lastHeartbeat);


create table jobrunr_job_counters
(
    name   NCHAR(36) PRIMARY KEY,
    amount int NOT NULL
);

INSERT INTO jobrunr_job_counters (name, amount)
VALUES ('AWAITING', 0);
INSERT INTO jobrunr_job_counters (name, amount)
VALUES ('SCHEDULED', 0);
INSERT INTO jobrunr_job_counters (name, amount)
VALUES ('ENQUEUED', 0);
INSERT INTO jobrunr_job_counters (name, amount)
VALUES ('PROCESSING', 0);
INSERT INTO jobrunr_job_counters (name, amount)
VALUES ('FAILED', 0);
INSERT INTO jobrunr_job_counters (name, amount)
VALUES ('SUCCEEDED', 0);

create view jobrunr_jobs_stats
as
select count(*)                                                                           as total,
       (select count(*) from jobrunr_jobs jobs where jobs.state = 'AWAITING')             as awaiting,
       (select count(*) from jobrunr_jobs jobs where jobs.state = 'SCHEDULED')            as scheduled,
       (select count(*) from jobrunr_jobs jobs where jobs.state = 'ENQUEUED')             as enqueued,
       (select count(*) from jobrunr_jobs jobs where jobs.state = 'PROCESSING')           as processing,
       (select count(*) from jobrunr_jobs jobs where jobs.state = 'FAILED')               as failed,
       (select((select count(*) from jobrunr_jobs jobs where jobs.state = 'SUCCEEDED') +
               (select amount from jobrunr_job_counters jc where jc.name = 'SUCCEEDED'))) as succeeded,
       (select count(*) from jobrunr_backgroundjobservers)                                as nbrOfBackgroundJobServers,
       (select count(*) from jobrunr_recurring_jobs)                                      as nbrOfRecurringJobs
from jobrunr_jobs j;



drop view jobrunr_jobs_stats;

create view jobrunr_jobs_stats
as
select count(*)                                                                           as total,
       (select count(*) from jobrunr_jobs jobs where jobs.state = 'AWAITING')             as awaiting,
       (select count(*) from jobrunr_jobs jobs where jobs.state = 'SCHEDULED')            as scheduled,
       (select count(*) from jobrunr_jobs jobs where jobs.state = 'ENQUEUED')             as enqueued,
       (select count(*) from jobrunr_jobs jobs where jobs.state = 'PROCESSING')           as processing,
       (select count(*) from jobrunr_jobs jobs where jobs.state = 'FAILED')               as failed,
       (select((select count(*) from jobrunr_jobs jobs where jobs.state = 'SUCCEEDED') +
               (select amount from jobrunr_job_counters jc where jc.name = 'SUCCEEDED'))) as succeeded,
       (select count(*) from jobrunr_jobs jobs where jobs.state = 'DELETED')              as deleted,
       (select count(*) from jobrunr_backgroundjobservers)                                as nbrOfBackgroundJobServers,
       (select count(*) from jobrunr_recurring_jobs)                                      as nbrOfRecurringJobs
from jobrunr_jobs j;



ALTER TABLE jobrunr_jobs
    ADD recurringJobId VARCHAR(128);
CREATE INDEX jobrunr_job_rci_idx ON jobrunr_jobs (recurringJobId);


ALTER TABLE jobrunr_backgroundjobservers
    ADD deleteSucceededJobsAfter VARCHAR(32);
ALTER TABLE jobrunr_backgroundjobservers
    ADD permanentlyDeleteJobsAfter VARCHAR(32);
