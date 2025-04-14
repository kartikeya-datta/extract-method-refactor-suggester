error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1810.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1810.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1810.java
text:
```scala
i@@f (count.equals(previousCount))

package org.apache.cassandra.service;
/*
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.utils.StatusLogger;

public class GCInspector
{
    private static final Logger logger = LoggerFactory.getLogger(GCInspector.class);
    final static long INTERVAL_IN_MS = 1000;
    final static long MIN_DURATION = 200;
    final static long MIN_DURATION_TPSTATS = 1000;
    
    public static final GCInspector instance = new GCInspector();

    private HashMap<String, Long> gctimes = new HashMap<String, Long>();
    private HashMap<String, Long> gccounts = new HashMap<String, Long>();

    List<GarbageCollectorMXBean> beans = new ArrayList<GarbageCollectorMXBean>();
    MemoryMXBean membean = ManagementFactory.getMemoryMXBean();

    private volatile boolean cacheSizesReduced;

    public GCInspector()
    {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        try
        {
            ObjectName gcName = new ObjectName(ManagementFactory.GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE + ",*");
            for (ObjectName name : server.queryNames(gcName, null))
            {
                GarbageCollectorMXBean gc = ManagementFactory.newPlatformMXBeanProxy(server, name.getCanonicalName(), GarbageCollectorMXBean.class);
                beans.add(gc);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public void start()
    {
        // don't bother starting a thread that will do nothing.
        if (beans.size() == 0)
            return;         
        Runnable t = new Runnable()
        {
            public void run()
            {
                logGCResults();
            }
        };
        StorageService.scheduledTasks.scheduleWithFixedDelay(t, INTERVAL_IN_MS, INTERVAL_IN_MS, TimeUnit.MILLISECONDS);
    }

    private void logGCResults()
    {
        for (GarbageCollectorMXBean gc : beans)
        {
            Long previousTotal = gctimes.get(gc.getName());
            Long total = gc.getCollectionTime();
            if (previousTotal == null)
                previousTotal = 0L;
            if (previousTotal.equals(total))            
                continue;
            gctimes.put(gc.getName(), total);
            Long duration = total - previousTotal;
            assert duration > 0;

            Long previousCount = gccounts.get(gc.getName());
            Long count = gc.getCollectionCount();
            
            if (previousCount == null)
                previousCount = 0L;           
            if (count == previousCount)
                continue;
            
            gccounts.put(gc.getName(), count);

            MemoryUsage mu = membean.getHeapMemoryUsage();
            long memoryUsed = mu.getUsed();
            long memoryMax = mu.getMax();

            String st = String.format("GC for %s: %s ms for %s collections, %s used; max is %s",
                                      gc.getName(), duration, count - previousCount, memoryUsed, memoryMax);
            long durationPerCollection = duration / (count - previousCount);
            if (durationPerCollection > MIN_DURATION)                          
                logger.info(st);
            else if (logger.isDebugEnabled())
                logger.debug(st);

            if (durationPerCollection > MIN_DURATION_TPSTATS)
                StatusLogger.log();

            // if we just finished a full collection and we're still using a lot of memory, try to reduce the pressure
            if (gc.getName().equals("ConcurrentMarkSweep"))
            {
                double usage = (double) memoryUsed / memoryMax;

                if (memoryUsed > DatabaseDescriptor.getReduceCacheSizesAt() * memoryMax && !cacheSizesReduced)
                {
                    cacheSizesReduced = true;
                    logger.warn("Heap is " + usage + " full.  You may need to reduce memtable and/or cache sizes.  Cassandra is now reducing cache sizes to free up memory.  Adjust reduce_cache_sizes_at threshold in cassandra.yaml if you don't want Cassandra to do this automatically");
                    StorageService.instance.reduceCacheSizes();
                }

                if (memoryUsed > DatabaseDescriptor.getFlushLargestMemtablesAt() * memoryMax)
                {
                    logger.warn("Heap is " + usage + " full.  You may need to reduce memtable and/or cache sizes.  Cassandra will now flush up to the two largest memtables to free up memory.  Adjust flush_largest_memtables_at threshold in cassandra.yaml if you don't want Cassandra to do this automatically");
                    StorageService.instance.flushLargestMemtables();
                }
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1810.java