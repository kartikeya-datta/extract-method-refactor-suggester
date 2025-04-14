error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16594.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16594.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16594.java
text:
```scala
public synchronized O@@bject[] toParameterArray(StoreQuery q, Map userParams) {

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import org.apache.openjpa.jdbc.kernel.exps.ExpContext;
import org.apache.openjpa.jdbc.kernel.exps.QueryExpressionsState;
import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.sql.DBDictionary;
import org.apache.openjpa.jdbc.sql.Result;
import org.apache.openjpa.jdbc.sql.ResultSetResult;
import org.apache.openjpa.jdbc.sql.SQLBuffer;
import org.apache.openjpa.jdbc.sql.SQLExceptions;
import org.apache.openjpa.jdbc.sql.SelectImpl;
import org.apache.openjpa.kernel.StoreQuery;
import org.apache.openjpa.kernel.ExpressionStoreQuery.AbstractExpressionExecutor;
import org.apache.openjpa.kernel.exps.QueryExpressions;
import org.apache.openjpa.lib.rop.RangeResultObjectProvider;
import org.apache.openjpa.lib.rop.ResultObjectProvider;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.util.InternalException;

/**
 * A executor for Prepared SQL Query.
 * 
 * @author Pinaki Poddar
 *
 */
@SuppressWarnings("serial")
public class PreparedSQLStoreQuery extends SQLStoreQuery {
    private PreparedQueryImpl _cached;
    public PreparedSQLStoreQuery(JDBCStore store) {
        super(store);
    }
    
    public Executor newDataStoreExecutor(ClassMetaData meta,
        boolean subclasses) {
        return new PreparedSQLExecutor(this, meta);
    }
    
    public boolean setQuery(Object query) {
        if (query instanceof PreparedQueryImpl == false) {
            throw new InternalException(query.getClass() + " not recognized");
        }
        _cached = (PreparedQueryImpl)query;
        return true;
    }
    
    PreparedQueryImpl getPreparedQuery() {
        return _cached;
    }

    /**
     * Executor of a prepared query uses the QueryExpressions of the original query
     * available via the PreparedQuery.
     *
     */
    public static class PreparedSQLExecutor extends AbstractExpressionExecutor {
        private final ClassMetaData _meta;
        private final PreparedSQLStoreQuery _query;
        
        public PreparedSQLExecutor(PreparedSQLStoreQuery q, ClassMetaData candidate) {
            _meta = candidate;
            _query = q;
        }
        
        public QueryExpressions[] getQueryExpressions() {
            return _query.getPreparedQuery().getQueryExpressions();
        }
        
        public Class[] getProjectionTypes(StoreQuery q) {
            return _query.getPreparedQuery().getProjectionTypes();
        }

        public ResultObjectProvider executeQuery(StoreQuery q, Object[] params, Range range) {
            PreparedSQLStoreQuery psq = (PreparedSQLStoreQuery) q;
            PreparedQueryImpl pq = psq.getPreparedQuery();
            JDBCStore store = psq.getStore();
            DBDictionary dict = store.getDBDictionary();

            SQLBuffer buf = new SQLBuffer(dict).append(pq.getTargetQuery());
            Connection conn = store.getConnection();
            JDBCFetchConfiguration fetch = (JDBCFetchConfiguration)q.getContext().getFetchConfiguration();

            ResultObjectProvider rop;
            PreparedStatement stmnt = null;
            try {
                stmnt = !range.lrs ? buf.prepareStatement(conn) : buf.prepareStatement(conn, fetch, -1, -1);

                int index = 0;
                for (int i = 0; i < params.length; i++) {
                    dict.setUnknown(stmnt, ++index, params[i], null);
                }
                dict.setTimeouts(stmnt, fetch, false);

                ResultSet rs = stmnt.executeQuery();
                
                SelectImpl cachedSelect = pq.getSelect();
                Result res = cachedSelect.getEagerResult(conn, stmnt, rs, store, fetch, false, null);
                
                if (getQueryExpressions()[0].projections.length > 0) {
                    ExpContext ctx = new ExpContext(store, params, fetch);
                    QueryExpressionsState state = (QueryExpressionsState)getQueryExpressions()[0].state;
                    rop = new PreparedProjectionResultObjectProvider(cachedSelect, getQueryExpressions(), 
                            new QueryExpressionsState[]{state}, ctx, res);
                } else if (q.getContext().getCandidateType() != null) {
                    rop = new PreparedResultObjectProvider(cachedSelect, 
                        (ClassMapping) _meta, store, fetch, res);
                } else {
                    rop = new SQLProjectionResultObjectProvider(store, fetch,
                        (ResultSetResult)res, q.getContext().getResultType());
                }
            } catch (SQLException se) {
                if (stmnt != null)
                    try { stmnt.close(); } catch (SQLException se2) {}
                try { conn.close(); } catch (SQLException se2) {}
                throw SQLExceptions.getStore(se, dict);
            }

            if (range.start != 0 || range.end != Long.MAX_VALUE)
                rop = new RangeResultObjectProvider(rop, range.start,range.end);
            return rop;
        }
        
        /**
         * Convert given userParams to an array whose ordering matches as 
         * per expected during executeXXX() methods.
         * The given userParams is already re-parameterized, so this method have
         * to merely copy the given Map values.
         * 
         * @see PreparedQueryImpl#reparametrize(Map, org.apache.openjpa.kernel.Broker)
         */
        public Object[] toParameterArray(StoreQuery q, Map userParams) {
            Object[] array = new Object[userParams.size()];

            Set<Map.Entry<Object,Object>> userSet = userParams.entrySet();
            for (Map.Entry<Object,Object> userEntry : userSet) {
                int idx = ((Integer)userEntry.getKey()).intValue();
                array[idx] = userEntry.getValue();
            }
            return array;
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16594.java