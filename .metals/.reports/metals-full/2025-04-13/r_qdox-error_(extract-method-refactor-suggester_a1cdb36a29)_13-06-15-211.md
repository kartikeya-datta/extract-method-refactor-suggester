error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7755.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7755.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7755.java
text:
```scala
public C@@learableScheduler getClearableScheduler();

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
package org.apache.openjpa.datacache;

import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.lib.conf.Configurable;
import org.apache.openjpa.lib.conf.ObjectValue;

/**
 * Manages the system's data and query caches. You can retrieve the data cache manager from the 
 * {@link OpenJPAConfiguration#getDataCacheManagerInstance()}.
 * <br>
 * Manages zero or more individual {@link DataCache caches} or partitions. Each individual partition
 * is identified by a string-based identifier.
 *  
 * Decides eligibility to cache for managed types.
 * 
 *
 * @author Abe White
 * @author Patrick Linskey
 * @author Pinaki Poddar
 */
public interface DataCacheManager {

    /**
     * Initialize the manager, supplying the cache configuration.
     */
    public void initialize(OpenJPAConfiguration conf, ObjectValue dataCache,
        ObjectValue queryCache);

    /**
     * Return the system-wide data cache, or null if caching is not enabled.
     */
    public DataCache getSystemDataCache();

    /**
     * Return the named data cache, or null if it does not exist.
     */
    public DataCache getDataCache(String name);

    /**
     * Return the named data cache. If the given name is null, the default
     * data cache is returned.
     *
     * @param create if true, the cache will be created if it does
     * not already exist
     */
    public DataCache getDataCache(String name, boolean create);

    /**
     * Return the system query cache, or null if not configured.
     */
    public QueryCache getSystemQueryCache();

    /**
     * Return the PCData generator if configured.
     */
    public DataCachePCDataGenerator getPCDataGenerator();

    /**
     * Return the runnable which schedules evictions.
     */
    public DataCacheScheduler getDataCacheScheduler();
    
    /**
     * Select the cache where the given managed proxy instance should be cached.
     * This decision <em>may</em> override the cache returned by 
     * {@link CacheDistributionPolicy#selectCache(OpenJPAStateManager, Object) policy}
     * as specified by the user.  
     *  
     * @param sm the managed proxy instance
     * @return the cache that will store the state of the given managed instance.
     * 
     * @since 2.0.0
     */
    public DataCache selectCache(final OpenJPAStateManager sm);
    
    /**
     * Return the user-specific policy that <em>suggests</em> the cache where a managed entity state is stored.  
     * 
     * @since 2.0.0
     */
    public CacheDistributionPolicy getDistributionPolicy();
    
    /**
     * Close all caches.
     */
    public void close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7755.java