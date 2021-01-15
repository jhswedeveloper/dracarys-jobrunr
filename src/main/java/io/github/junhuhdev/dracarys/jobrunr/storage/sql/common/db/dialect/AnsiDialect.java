package io.github.junhuhdev.dracarys.jobrunr.storage.sql.common.db.dialect;

import org.jobrunr.storage.sql.common.db.dialect.Dialect;

public class AnsiDialect implements Dialect {

    @Override
    public String limitAndOffset(String order) {
        return " ORDER BY " + order + " LIMIT :limit OFFSET :offset";
    }
}
