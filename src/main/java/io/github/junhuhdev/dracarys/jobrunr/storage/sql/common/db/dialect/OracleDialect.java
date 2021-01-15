package io.github.junhuhdev.dracarys.jobrunr.storage.sql.common.db.dialect;

import org.jobrunr.storage.sql.common.db.dialect.Dialect;

public class OracleDialect implements Dialect {

    @Override
    public String limitAndOffset(String order) {
        return " ORDER BY " + order + " OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY";
    }
}
