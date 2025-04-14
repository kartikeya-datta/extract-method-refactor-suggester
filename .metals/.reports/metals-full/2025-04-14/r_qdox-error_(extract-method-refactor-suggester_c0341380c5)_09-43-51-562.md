error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1438.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1438.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1438.java
text:
```scala
s@@tages.put(GOSSIP_STAGE, new JMXEnabledThreadPoolExecutor("GOSSIP_STAGE"));

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.cassandra.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.cassandra.config.DatabaseDescriptor;

import static org.apache.cassandra.config.DatabaseDescriptor.getConcurrentReaders;
import static org.apache.cassandra.config.DatabaseDescriptor.getConcurrentWriters;


/**
 * This class manages executor services for Messages recieved: each Message requests
 * running on a specific "stage" for concurrency control; hence the Map approach,
 * even though stages (executors) are not created dynamically.
 */
public class StageManager
{
    private static Map<String, ThreadPoolExecutor> stages = new HashMap<String, ThreadPoolExecutor>();

    public final static String READ_STAGE = "ROW-READ-STAGE";
    public final static String MUTATION_STAGE = "ROW-MUTATION-STAGE";
    public final static String STREAM_STAGE = "STREAM-STAGE";
    public final static String GOSSIP_STAGE = "GS";
    public static final String RESPONSE_STAGE = "RESPONSE-STAGE";
    public final static String AE_SERVICE_STAGE = "AE-SERVICE-STAGE";
    private static final String LOADBALANCE_STAGE = "LOAD-BALANCER-STAGE";
    public static final String MIGRATION_STAGE = "MIGRATION-STAGE";

    public static final long KEEPALIVE = 60; // seconds to keep "extra" threads alive for when idle

    static
    {
        stages.put(MUTATION_STAGE, multiThreadedConfigurableStage(MUTATION_STAGE, getConcurrentWriters()));
        stages.put(READ_STAGE, multiThreadedConfigurableStage(READ_STAGE, getConcurrentReaders()));        
        stages.put(RESPONSE_STAGE, multiThreadedStage(RESPONSE_STAGE, Math.max(2, Runtime.getRuntime().availableProcessors())));
        // the rest are all single-threaded
        stages.put(STREAM_STAGE, new JMXEnabledThreadPoolExecutor(STREAM_STAGE));
        stages.put(GOSSIP_STAGE, new JMXEnabledThreadPoolExecutor("GMFD"));
        stages.put(AE_SERVICE_STAGE, new JMXEnabledThreadPoolExecutor(AE_SERVICE_STAGE));
        stages.put(LOADBALANCE_STAGE, new JMXEnabledThreadPoolExecutor(LOADBALANCE_STAGE));
        stages.put(MIGRATION_STAGE, new JMXEnabledThreadPoolExecutor(MIGRATION_STAGE));
    }

    private static ThreadPoolExecutor multiThreadedStage(String name, int numThreads)
    {
        // avoid running afoul of requirement in DebuggableThreadPoolExecutor that single-threaded executors
        // must have unbounded queues
        assert numThreads > 1 : "multi-threaded stages must have at least 2 threads";

        return new JMXEnabledThreadPoolExecutor(numThreads,
                                                numThreads,
                                                KEEPALIVE,
                                                TimeUnit.SECONDS,
                                                new LinkedBlockingQueue<Runnable>(DatabaseDescriptor.getStageQueueSize()),
                                                new NamedThreadFactory(name));
    }
    
    private static ThreadPoolExecutor multiThreadedConfigurableStage(String name, int numThreads)
    {
        assert numThreads > 1 : "multi-threaded stages must have at least 2 threads";
        
        return new JMXConfigurableThreadPoolExecutor(numThreads,
                                                     numThreads,
                                                     KEEPALIVE,
                                                     TimeUnit.SECONDS,
                                                     new LinkedBlockingQueue<Runnable>(DatabaseDescriptor.getStageQueueSize()),
                                                     new NamedThreadFactory(name));
    }

    /**
     * Retrieve a stage from the StageManager
     * @param stageName name of the stage to be retrieved.
    */
    public static ThreadPoolExecutor getStage(String stageName)
    {
        return stages.get(stageName);
    }
    
    /**
     * This method shuts down all registered stages.
     */
    public static void shutdownNow()
    {
        Set<String> stages = StageManager.stages.keySet();
        for (String stage : stages)
        {
            StageManager.stages.get(stage).shutdownNow();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1438.java