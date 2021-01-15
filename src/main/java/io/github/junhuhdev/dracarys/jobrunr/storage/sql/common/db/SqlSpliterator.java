package io.github.junhuhdev.dracarys.jobrunr.storage.sql.common.db;

import io.github.junhuhdev.dracarys.jobrunr.storage.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Spliterator;
import java.util.function.Consumer;

public class SqlSpliterator implements Spliterator<SqlResultSet>, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlSpliterator.class);

    private final DataSource dataSource;
    private final String sqlStatement;
    private final Consumer<PreparedStatement> paramsSetter;
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean hasMore;

    public SqlSpliterator(DataSource dataSource, String sqlStatement, Consumer<PreparedStatement> paramsSetter) {
        this.dataSource = dataSource;
        this.sqlStatement = sqlStatement;
        this.paramsSetter = paramsSetter;
    }

    @Override
    public boolean tryAdvance(Consumer<? super SqlResultSet> consumer) {
        try {
            if (ps == null) {
                init();
                hasMore = rs.next();
                if (!hasMore) {
                    close();
                }
            }
            if (!hasMore) return false;
            consumer.accept(new SqlResultSet(rs));
            hasMore = rs.next();
            if (!hasMore) {
                close();
            }
            return hasMore;
        } catch (SQLException e) {
            close();
            throw new StorageException(e);
        }
    }

    private void init() {
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sqlStatement, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(100);
            paramsSetter.accept(ps);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            close();
            throw new StorageException(e);
        }
    }

    @Override
    public void close() {
        close(rs);
        close(ps);
        close(conn);
    }

    private void close(AutoCloseable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
            //nothing we can do here
            if(closeable instanceof Connection) {
                LOGGER.error("Could not close connection", e);
            }
        }
    }

    @Override
    public Spliterator<SqlResultSet> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
        return 0;
    }
}
