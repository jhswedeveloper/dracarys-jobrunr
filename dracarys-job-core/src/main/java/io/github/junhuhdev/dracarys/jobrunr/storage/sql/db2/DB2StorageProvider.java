package io.github.junhuhdev.dracarys.jobrunr.storage.sql.db2;


import io.github.junhuhdev.dracarys.jobrunr.storage.sql.common.DefaultSqlStorageProvider;

import javax.sql.DataSource;

public class DB2StorageProvider extends DefaultSqlStorageProvider {

    public DB2StorageProvider(DataSource dataSource) {
        super(dataSource);
    }

}
