package io.github.junhuhdev.dracarys.jobrunr.storage.sql.h2;


import io.github.junhuhdev.dracarys.jobrunr.storage.sql.common.DefaultSqlStorageProvider;

import javax.sql.DataSource;

public class H2StorageProvider extends DefaultSqlStorageProvider {

    public H2StorageProvider(DataSource dataSource) {
        super(dataSource);
    }

}
