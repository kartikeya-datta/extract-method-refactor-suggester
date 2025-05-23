error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15697.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15697.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15697.java
text:
```scala
i@@f (qk.toString().equals(key)) {

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
package org.apache.openjpa.instrumentation;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.openjpa.datacache.AbstractQueryCache;
import org.apache.openjpa.datacache.QueryCache;
import org.apache.openjpa.datacache.QueryKey;
import org.apache.openjpa.kernel.QueryStatistics;
import org.apache.openjpa.lib.instrumentation.AbstractInstrument;
import org.apache.openjpa.lib.instrumentation.InstrumentationLevel;

/**
 * Provides a basic instrument implementation wrapper for the query cache.  This
 * class can be extended to create a provider specific instrument for the
 * query cache.
 */
public abstract class AbstractQueryCacheInstrument extends AbstractInstrument
    implements QueryCacheInstrument {

    /**
     * Value indicating that cache statistics are not available.
     */
    public static final long NO_STATS = -1;
    
    private QueryCache _qc;
    private String _configId = null;
    private String _configRef = null;
        
    public void setQueryCache(QueryCache qc) {
        _qc = qc;
    }
    
    public void setConfigId(String cid) {
        _configId = cid;
    }
    
    public void setContextRef(String cref) {
        _configRef = cref;
    }
    
    public String getConfigId() {
        return _configId;
    }

    public String getContextRef() {
        return _configRef;
    }
    
    public void setPreparedQueryCache(QueryCache qc) {
        _qc = qc;
    }
    
    private QueryStatistics<QueryKey> getStatistics() {
        if (_qc == null)
            return null;
        return _qc.getStatistics();
    }

    public long getExecutionCount() {
        QueryStatistics<QueryKey> stats = getStatistics();
        if (stats != null)
            return stats.getExecutionCount();
        return NO_STATS;
    }

    public long getExecutionCount(String queryKey) {
        QueryStatistics<QueryKey> stats = getStatistics();
        if (stats != null) {
            QueryKey qk = findKey(queryKey);
            return stats.getExecutionCount(qk);
        }
        return NO_STATS;
    }

    public long getTotalExecutionCount() {
        QueryStatistics<QueryKey> stats = getStatistics();
        if (stats != null)
            return stats.getTotalExecutionCount();
        return NO_STATS;
    }

    public long getTotalExecutionCount(String queryKey) {
        QueryStatistics<QueryKey> stats = getStatistics();
        if (stats != null) {
            QueryKey qk = findKey(queryKey);
            return stats.getTotalExecutionCount(qk);
        }
        return NO_STATS;
    }

    public long getHitCount() {
        QueryStatistics<QueryKey> stats = getStatistics();
        if (stats != null)
            return stats.getHitCount();
        return NO_STATS;
    }

    public long getHitCount(String queryKey) {
        QueryStatistics<QueryKey> stats = getStatistics();
        if (stats != null) {
            QueryKey qk = findKey(queryKey);
            return stats.getHitCount(qk);
        }
        return NO_STATS;
    }

    public long getTotalHitCount() {
        QueryStatistics<QueryKey> stats = getStatistics();
        if (stats != null)
            return stats.getTotalHitCount();
        return NO_STATS;
    }

    public long getTotalHitCount(String queryKey) {
        QueryStatistics<QueryKey> stats = getStatistics();
        if (stats != null) {
            QueryKey qk = findKey(queryKey);
            return stats.getTotalHitCount(qk);
        }
        return NO_STATS;
    }

    public void reset() {
        QueryStatistics<QueryKey> stats = getStatistics();
        if (stats != null)
            stats.reset();        
    }
    
    public Date sinceDate() {
        QueryStatistics<QueryKey> stats = getStatistics();
        if (stats != null)
            return stats.since();
        return null;
    }
    
    public Date startDate() {
        QueryStatistics<QueryKey> stats = getStatistics();
        if (stats != null)
            return stats.start();        
        return null;
    }
    
    /**
     * Returns number of total evictions since last reset
     */
    public long getEvictionCount() {
        QueryStatistics<QueryKey> stats = getStatistics();
        if (stats != null)
            return stats.getEvictionCount();
        return NO_STATS;
    }
    
    /**
     * Returns number of total eviction requests since start.
     */
    public long getTotalEvictionCount() {
        QueryStatistics<QueryKey> stats = getStatistics();
        if (stats != null)
            return stats.getTotalEvictionCount();
        return NO_STATS;
    }

    /**
     * Returns all query keys currently tracked in the cache.
     * @return
     */
    public Set<String> queryKeys() {
        QueryStatistics<QueryKey> stats = getStatistics();
        if (stats != null) {
            Set<String> keys = new HashSet<String>();
            for (QueryKey qk : stats.keys()) {
                keys.add(qk.toString());
            }
            return keys;
        }
        return null;
    }

    private QueryKey findKey(String key) {
        QueryStatistics<QueryKey> stats = getStatistics();
        for (QueryKey qk : stats.keys()) {
            if (qk.toString().equals(key.toString())) {
                return qk;
            }
        }
        return null;
    }
    
    public long count() {
        if (_qc == null) {
            return NO_STATS;
        }
        if (_qc instanceof AbstractQueryCache) {
            AbstractQueryCache aqc = (AbstractQueryCache)_qc;
            return aqc.count();
        }
        return NO_STATS;
    }

    public InstrumentationLevel getLevel() {
        return InstrumentationLevel.FACTORY;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15697.java