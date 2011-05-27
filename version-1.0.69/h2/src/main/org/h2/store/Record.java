/*
 * Copyright 2004-2008 H2 Group. Licensed under the H2 License, Version 1.0
 * (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.store;

import java.sql.SQLException;

import org.h2.log.LogSystem;
import org.h2.util.CacheObject;

/**
 * A record represents a persisted row in a table, or a index page. When a
 * record is persisted to disk, it is first written into a {@link DataPage}
 * buffer.
 */
public abstract class Record extends CacheObject {
    private boolean deleted;
    private int sessionId;
    private int storageId;
    private int lastLog = LogSystem.LOG_WRITTEN;
    private int lastPos = LogSystem.LOG_WRITTEN;

    /**
     * Get the number of bytes required for the data if the given data page
     * would be used.
     * 
     * @param dummy the template data page
     * @return the number of bytes
     */
    public abstract int getByteCount(DataPage dummy) throws SQLException;

    /**
     * Write the record to the data page.
     *
     * @param buff the data page
     */
    public abstract void write(DataPage buff) throws SQLException;

    /**
     * This method is called just before the page is written.
     * If a read operation is required before writing, this needs to be done here.
     * Because the data page buffer is shared for read and write operations.
     * The method may read data and change the file pointer.
     */
    public void prepareWrite() throws SQLException {
    }

    public boolean isEmpty() {
        return false;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void commit() {
        this.sessionId = 0;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setStorageId(int storageId) {
        this.storageId = storageId;
    }

    public int getStorageId() {
        return storageId;
    }

    public void setLastLog(int log, int pos) {
        lastLog = log;
        lastPos = pos;
    }

    public void setLogWritten(int log, int pos) {
        if (log < lastLog) {
            return;
        }
        if (log > lastLog || pos >= lastPos) {
            lastLog = LogSystem.LOG_WRITTEN;
            lastPos = LogSystem.LOG_WRITTEN;
        }
    }

    public boolean canRemove() {
        if ((isChanged() && !isLogWritten()) || isPinned()) {
            return false;
        }
        // TODO not required if we write the log only when committed
        if (sessionId != 0) {
            return false;
        }
        return true;
    }

    public boolean isLogWritten() {
        return lastLog == LogSystem.LOG_WRITTEN;
    }

}