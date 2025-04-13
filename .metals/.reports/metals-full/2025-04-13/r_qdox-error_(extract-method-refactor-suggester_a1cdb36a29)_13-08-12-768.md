error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10067.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10067.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10067.java
text:
```scala
r@@eturn stats.getReadCount(c);

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

import org.apache.openjpa.datacache.CacheStatistics;
import org.apache.openjpa.datacache.DataCache;
import org.apache.openjpa.lib.instrumentation.AbstractInstrument;
import org.apache.openjpa.lib.instrumentation.InstrumentationLevel;

/**
 * Provides a basic instrument implementation wrapper for the data cache.  This
 * class can be extended to create a provider specific instrument for the
 * data cache.
 */
public abstract class AbstractDataCacheInstrument extends AbstractInstrument 
    implements DataCacheInstrument {

    /**
     * Value indicating that cache statistics are not available.
     */
    public static final long NO_STATS = -1;

    private DataCache _dc = null;
    private String _configID = null;
    private String _configRef = null;
        
    public void setDataCache(DataCache dc) {
        _dc = dc;
    }
    
    public void setConfigId(String cid) {
        _configID = cid;
    }
    
    public void setContextRef(String cref) {
        _configRef = cref;
    }
        
    public long getHitCount() {
        CacheStatistics stats = getStatistics();
        if (stats != null)
            return stats.getHitCount();
        return NO_STATS;
    }
    
    public long getHitCount(String className) 
        throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        return getHitCount(clazz);
    }
    
    public long getReadCount() {
        CacheStatistics stats = getStatistics();
        if (stats != null)
            return stats.getReadCount();
        return NO_STATS;
    }
    
    public long getReadCount(String className)
        throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        return getReadCount(clazz);        
    }
    
    public long getTotalHitCount() {
        CacheStatistics stats = getStatistics();
        if (stats != null)
            return stats.getTotalHitCount();
        return NO_STATS;
    }

    public long getTotalHitCount(String className)
        throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        return getTotalHitCount(clazz);      
    }
    
    public long getTotalReadCount() {
        CacheStatistics stats = getStatistics();
        if (stats != null)
            return stats.getTotalReadCount();
        return NO_STATS;
    }

    public long getTotalReadCount(String className)
        throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        return getTotalReadCount(clazz);      
    }
    
    public long getTotalWriteCount() {
        CacheStatistics stats = getStatistics();
        if (stats != null)
            return stats.getTotalWriteCount();
        return NO_STATS;
    }

    public long getTotalWriteCount(String className)
        throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        return getTotalWriteCount(clazz);      
    }
    
    public long getWriteCount() {
        CacheStatistics stats = getStatistics();
        if (stats != null)
            return stats.getWriteCount();
        return NO_STATS;
    }

    public long getWriteCount(String className)
        throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        return getWriteCount(clazz);      
    }

    public void reset() {
        CacheStatistics stats = getStatistics();
        if (stats != null)
            stats.reset();        
    }
    
    public Date sinceDate() {
        CacheStatistics stats = getStatistics();
        if (stats != null)
            return stats.since();
        return null;
    }
    
    public Date startDate() {
        CacheStatistics stats = getStatistics();
        if (stats != null)
            return stats.start();        
        return null;
    }
    
    public long getEvictionCount() {
        CacheStatistics stats = getStatistics();
// TODO : Implement eviction count in data cache stats
//        if (stats != null)
//            return stats.getEvictionCount();
        return NO_STATS;
    }
    
    public long getTotalEvictionCount() {
        CacheStatistics stats = getStatistics();
     // TODO : Implement eviction count in data cache stats
//        if (stats != null)
//            return stats.getTotalEvictionCount();
        return NO_STATS;
    }

    public String getConfigId() {
        return _configID;
    }

    public String getContextRef() {
        return _configRef;
    }

    public String getCacheName() {
        if (_dc != null)
            return _dc.getName();
        return null;
    }
    
    private CacheStatistics getStatistics() {
        if (_dc != null) {
            return _dc.getStatistics();
        }
        return null;
    }

    private long getWriteCount(Class<?> c) {
        CacheStatistics stats = getStatistics();
        if (stats != null)
            return stats.getWriteCount(c);
        return NO_STATS;
    }
    
    private long getTotalWriteCount(Class<?> c) {
        CacheStatistics stats = getStatistics();
        if (stats != null)
            return stats.getTotalWriteCount(c);
        return NO_STATS;
    }
    
    private long getTotalReadCount(Class<?> c) {
        CacheStatistics stats = getStatistics();
        if (stats != null)
            return stats.getTotalReadCount(c);
        return NO_STATS;
    }
    
    private long getTotalHitCount(Class<?> c) {
        CacheStatistics stats = getStatistics();
        if (stats != null)
            return stats.getTotalHitCount(c);
        return NO_STATS;
    }

    private long getReadCount(Class<?> c) {
        CacheStatistics stats = getStatistics();
        if (stats != null)
            stats.getReadCount(c);
        return NO_STATS;
    }

    private long getHitCount(Class<?> c) {
        CacheStatistics stats = getStatistics();
        if (stats != null)
            return stats.getHitCount(c);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10067.java