package io.github.junhuhdev.dracarys.jobrunr.jobs.states;

import java.time.Duration;

@SuppressWarnings("FieldMayBeFinal") // because of JSON-B
public class SucceededState extends AbstractJobState {

    private Duration latencyDuration;
    private Duration processDuration;

    protected SucceededState() { // for json deserialization
        this(null, null);
    }

    public SucceededState(Duration latencyDuration, Duration processDuration) {
        super(StateName.SUCCEEDED);
        this.latencyDuration = latencyDuration;
        this.processDuration = processDuration;
    }

    public Duration getLatencyDuration() {
        return latencyDuration;
    }

    public Duration getProcessDuration() {
        return processDuration;
    }
}
