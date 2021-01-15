package io.github.junhuhdev.dracarys.jobrunr.jobs.states;

import org.jobrunr.jobs.states.AbstractJobState;
import org.jobrunr.jobs.states.StateName;

import java.time.Instant;
import java.util.UUID;

@SuppressWarnings("FieldMayBeFinal") // because of JSON-B
public class ProcessingState extends AbstractJobState {

    private UUID serverId;
    private Instant updatedAt;

    protected ProcessingState() { // for json deserialization
        this(null);
    }

    public ProcessingState(UUID serverId) {
        super(StateName.PROCESSING);
        this.serverId = serverId;
        this.updatedAt = getCreatedAt();
    }

    public UUID getServerId() {
        return serverId;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
