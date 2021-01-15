package io.github.junhuhdev.dracarys.jobrunr.utils.reflection.autobox;

import oracle.sql.TIMESTAMP;
import org.jobrunr.JobRunrException;
import org.jobrunr.utils.reflection.autobox.InstantTypeAutoboxer;

import java.sql.SQLException;
import java.time.Instant;

public class InstantForOracleTypeAutoboxer extends InstantTypeAutoboxer {

    @Override
    public Instant autobox(Object value, Class<Instant> type) {
        try {
            return ((TIMESTAMP) value).timestampValue().toInstant();
        } catch (SQLException e) {
            throw JobRunrException.shouldNotHappenException(e);
        }
    }
}
