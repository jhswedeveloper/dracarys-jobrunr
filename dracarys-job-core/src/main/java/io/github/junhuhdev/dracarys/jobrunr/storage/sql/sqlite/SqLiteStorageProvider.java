package io.github.junhuhdev.dracarys.jobrunr.storage.sql.sqlite;


import io.github.junhuhdev.dracarys.jobrunr.storage.sql.common.DefaultSqlStorageProvider;

import javax.sql.DataSource;

public class SqLiteStorageProvider extends DefaultSqlStorageProvider {

    public SqLiteStorageProvider(DataSource dataSource) {
        super(dataSource);
    }

}
