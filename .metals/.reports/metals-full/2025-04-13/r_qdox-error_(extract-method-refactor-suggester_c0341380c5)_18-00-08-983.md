error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15328.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15328.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15328.java
text:
```scala
protected v@@oid flushBatch() throws SQLException {

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.jdbc.kernel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.sql.Row;
import org.apache.openjpa.jdbc.sql.RowImpl;
import org.apache.openjpa.jdbc.sql.SQLExceptions;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.lib.log.Log;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.util.OptimisticException;

/**
 * Batch prepared statement manager implementation. This prepared statement
 * manager will utilize the JDBC addBatch() and exceuteBatch() to batch the SQL
 * statements together to improve the execution performance.
 * 
 * @author Teresa Kan
 */

public class BatchingPreparedStatementManagerImpl extends
        PreparedStatementManagerImpl {

    private final static Localizer _loc = Localizer
            .forPackage(BatchingPreparedStatementManagerImpl.class);

    private String _batchedSql = null;
    private List _batchedRows = new ArrayList();
    private int _batchLimit;
    private boolean _disableBatch = false;
    private transient Log _log = null;

    /**
     * Constructor. Supply connection.
     */
    public BatchingPreparedStatementManagerImpl(JDBCStore store,
        Connection conn, int batchLimit) {
        super(store, conn);
        _batchLimit = batchLimit;
        _log = store.getConfiguration().getLog(JDBCConfiguration.LOG_JDBC);
        if (_log.isTraceEnabled())
            _log.trace(_loc.get("batch_limit", String.valueOf(_batchLimit)));
    }

    /**
     * Flush the given row immediately or deferred the flush in batch.
     */
    protected void flushAndUpdate(RowImpl row) throws SQLException {
        if (isBatchDisabled(row)) {
            // if there were some statements batched before, then
            // we need to flush them out first before processing the
            // current non batch process.
            flushBatch();

            super.flushAndUpdate(row);
        } else {
            // process the SQL statement, either execute it immediately or
            // batch it for later execution.
            batchOrExecuteRow(row);
        }
    }

    protected void batchOrExecuteRow(RowImpl row) throws SQLException {
        String sql = row.getSQL(_dict);
        if (_batchedSql == null) {
            // brand new SQL
            _batchedSql = sql;
        } else if (!sql.equals(_batchedSql)) {
            // SQL statements changed.
            switch (_batchedRows.size()) {
            case 0:
                break;
            case 1:
                // single entry in cache, direct SQL execution. 
                super.flushAndUpdate((RowImpl) _batchedRows.get(0));
                _batchedRows.clear();
                break;
            default:
                // flush all entries in cache in batch.
                flushBatch();
            }
            _batchedSql = sql;
        }
        _batchedRows.add(row);
    }

    /*
     * Compute if batching is disabled, based on values of batch limit
     * and database characteristics.
     */
    private boolean isBatchDisabled(RowImpl row) {
        boolean rtnVal = true;
        int limit = getBatchLimit();
        if ((limit < 0 || limit > 1) && !isBatchDisabled()) {
            OpenJPAStateManager sm = row.getPrimaryKey();
            ClassMapping cmd = null;
            if (sm != null)
                cmd = (ClassMapping) sm.getMetaData();
            Column[] autoAssign = null;
            if (row.getAction() == Row.ACTION_INSERT)
                autoAssign = row.getTable().getAutoAssignedColumns();
            // validate batch capability
            rtnVal = _dict
                .validateBatchProcess(row, autoAssign, sm, cmd);
            setBatchDisabled(rtnVal);
        }
        return rtnVal;
    }
    
    /**
     * flush all cached up statements to be executed as a single or batched
     * prepared statements.
     */
    protected void flushBatch() {
        List batchedRows = getBatchedRows();
        String batchedSql = getBatchedSql();
        if (batchedRows == null)
            return;

        int batchSize = batchedRows.size();
        if (batchedSql != null &&  batchSize > 0) {
            PreparedStatement ps = null;
            try {
                RowImpl onerow = null;
                ps = prepareStatement(batchedSql);
                if (batchSize == 1) {
                    // execute a single row.
                    onerow = (RowImpl) batchedRows.get(0);
                    flushSingleRow(onerow, ps);
                } else {
                    // cache has more than one rows, execute as batch.
                    int count = 0;
                    int batchedRowsBaseIndex = 0;
                    Iterator itr = batchedRows.iterator();
                    while (itr.hasNext()) {
                        onerow = (RowImpl) itr.next();
                        if (_batchLimit == 1) {
                            flushSingleRow(onerow, ps);
                        } else {
                            if (count < _batchLimit || _batchLimit == -1) {
                                if (ps != null)
                                    onerow.flush(ps, _dict, _store);
                                addBatch(ps, onerow, count);
                                count++;
                            } else {
                                // reach the batchLimit, execute the batch
                                int[] rtn = executeBatch(ps);
                                checkUpdateCount(rtn, batchedRowsBaseIndex, ps);

                                batchedRowsBaseIndex += _batchLimit;

                                if (ps != null)
                                    onerow.flush(ps, _dict, _store);
                                addBatch(ps, onerow, count);
                                // reset the count to 1 for new batch
                                count = 1;
                            }
                        }
                    }
                    // end of the loop, execute the batch
                    int[] rtn = executeBatch(ps);
                    checkUpdateCount(rtn, batchedRowsBaseIndex, ps);
                }
            } catch (SQLException se) {
                SQLException sqex = se.getNextException();
                if (sqex == null)
                    sqex = se;
                throw SQLExceptions.getStore(sqex, ps, _dict);
            } finally {
                _batchedSql = null;
                batchedRows.clear();
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException sqex) {
                        throw SQLExceptions.getStore(sqex, ps, _dict);
                    }
                }
            }
        }
    }

    /*
     * Execute an update of a single row.
     */
    private void flushSingleRow(RowImpl row, PreparedStatement ps)
        throws SQLException {
        if (ps != null)
            row.flush(ps, _dict, _store);
        int count = executeUpdate(ps, row.getSQL(_dict), row);
        if (count != 1) {
            Object failed = row.getFailedObject();
            if (failed != null)
                _exceptions.add(new OptimisticException(failed));
            else if (row.getAction() == Row.ACTION_INSERT)
                throw new SQLException(_loc.get("update-failed-no-failed-obj",
                    String.valueOf(count), row.getSQL(_dict)).getMessage());
        }
    }

    /*
     * Process executeBatch function array of return counts.
     */
    private void checkUpdateCount(int[] count, int batchedRowsBaseIndex,
        PreparedStatement ps)
        throws SQLException {
        // value in int[] count  returned from executeBatch: 
        //               Update          Delete        Insert
        // ===============================================================
        //               OK / Error      OK / Error    OK / Error
        // DB2LUW         1 / 0           1 / 0         1 / SQLException
        // DB2/ZOS        1 / 0           1 / 0        -2 / SQLException
        // Oracle        -2 / -2         -2 / -2       -2 / SQLException
        int cnt = 0;
        int updateSuccessCnt = _dict.getBatchUpdateCount(ps);
        Object failed = null;
        List batchedRows = getBatchedRows();
        for (int i = 0; i < count.length; i++) {
            cnt = count[i];
            RowImpl row = (RowImpl) batchedRows.get(batchedRowsBaseIndex + i);
            failed = row.getFailedObject();
            switch (cnt) {
            case Statement.EXECUTE_FAILED: // -3
                if (failed != null || row.getAction() == Row.ACTION_UPDATE)
                    _exceptions.add(new OptimisticException(failed));
                else if (row.getAction() == Row.ACTION_INSERT)
                    throw new SQLException(_loc.get(
                        "update-failed-no-failed-obj",
                        String.valueOf(count[i]), 
                        row.getSQL(_dict)).getMessage());
                break;
            case Statement.SUCCESS_NO_INFO: // -2
                if (_dict.reportsSuccessNoInfoOnBatchUpdates &&                    
                    updateSuccessCnt != count.length) {
                    // Oracle batching specifics:
                    // treat update/delete of SUCCESS_NO_INFO as failed case
                    // because:
                    // 1. transaction should be rolled back.
                    // 2. if DataCache is enabled, objects in
                    //    cache should be removed.
                    if (failed != null)
                        _exceptions.add(new OptimisticException(failed));
                    else if (row.getAction() == Row.ACTION_INSERT)
                        throw new SQLException(_loc.get(
                            "update-failed-no-failed-obj",
                            String.valueOf(count[i]), 
                            row.getSQL(_dict)).getMessage());
                }
                if (_log.isTraceEnabled())
                    _log.trace(_loc.get("batch_update_info",
                        String.valueOf(cnt), 
                        row.getSQL(_dict)).getMessage());
                break;
            case 0: // no row is inserted, treats it as failed
                // case
                if (failed != null)
                    _exceptions.add(new OptimisticException(failed));
                else if (row.getAction() == Row.ACTION_INSERT)
                    throw new SQLException(_loc.get(
                        "update-failed-no-failed-obj",
                        String.valueOf(count[i]), 
                        row.getSQL(_dict)).getMessage());
            }
        }
    }

    public boolean isBatchDisabled() {
        return _disableBatch;
    }

    public void setBatchDisabled(boolean disableBatch) {
        _disableBatch = disableBatch;
    }

    public int getBatchLimit() {
        return _batchLimit;
    }

    public void setBatchLimit(int batchLimit) {
        _batchLimit = batchLimit;
    }

    public List getBatchedRows() {
        return _batchedRows;
    }

    public String getBatchedSql() {
        return _batchedSql;
    }

    protected void addBatch(PreparedStatement ps, RowImpl row, 
            int count) throws SQLException {
        ps.addBatch();
    }

    protected int[] executeBatch(PreparedStatement ps) 
    throws SQLException {
        return ps.executeBatch();
    }
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15328.java