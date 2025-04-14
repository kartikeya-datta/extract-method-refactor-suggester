error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1743.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1743.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1743.java
text:
```scala
t@@hreadPool.cached().execute(new RoutingTableUpdater());

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.cluster.routing;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.cluster.*;
import org.elasticsearch.cluster.routing.allocation.RoutingAllocation;
import org.elasticsearch.cluster.routing.allocation.ShardsAllocation;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.threadpool.ThreadPool;

import java.util.concurrent.Future;

import static org.elasticsearch.cluster.ClusterState.*;
import static org.elasticsearch.common.unit.TimeValue.*;

/**
 * @author kimchy (shay.banon)
 */
public class RoutingService extends AbstractLifecycleComponent<RoutingService> implements ClusterStateListener {

    private final ThreadPool threadPool;

    private final ClusterService clusterService;

    private final ShardsAllocation shardsAllocation;

    private final TimeValue schedule;

    private volatile boolean routingTableDirty = false;

    private volatile Future scheduledRoutingTableFuture;

    @Inject public RoutingService(Settings settings, ThreadPool threadPool, ClusterService clusterService, ShardsAllocation shardsAllocation) {
        super(settings);
        this.threadPool = threadPool;
        this.clusterService = clusterService;
        this.shardsAllocation = shardsAllocation;
        this.schedule = componentSettings.getAsTime("schedule", timeValueSeconds(10));
    }

    @Override protected void doStart() throws ElasticSearchException {
        clusterService.add(this);
    }

    @Override protected void doStop() throws ElasticSearchException {
        if (scheduledRoutingTableFuture != null) {
            scheduledRoutingTableFuture.cancel(true);
            scheduledRoutingTableFuture = null;
        }
        clusterService.remove(this);
    }

    @Override protected void doClose() throws ElasticSearchException {
    }

    @Override public void clusterChanged(ClusterChangedEvent event) {
        if (event.source().equals(RoutingTableUpdater.CLUSTER_UPDATE_TASK_SOURCE)) {
            // that's us, ignore this event
            return;
        }
        if (event.state().nodes().localNodeMaster()) {
            // we are master, schedule the routing table updater
            if (scheduledRoutingTableFuture == null) {
                // a new master (us), make sure we reroute shards
                routingTableDirty = true;
                scheduledRoutingTableFuture = threadPool.scheduleWithFixedDelay(new RoutingTableUpdater(), schedule);
            }
            if (event.nodesRemoved() || event.routingTableChanged()) {
                // if nodes were removed, we don't want to wait for the scheduled task
                // since we want to get primary election as fast as possible

                // also, if the routing table changed, it means that we have new indices, or shard have started
                // or failed, we want to apply this as fast as possible
                routingTableDirty = true;
                threadPool.execute(new RoutingTableUpdater());
            } else {
                if (event.nodesAdded()) {
                    routingTableDirty = true;
                }
            }
        } else {
            if (scheduledRoutingTableFuture != null) {
                scheduledRoutingTableFuture.cancel(true);
                scheduledRoutingTableFuture = null;
            }
        }
    }

    private class RoutingTableUpdater implements Runnable {

        private static final String CLUSTER_UPDATE_TASK_SOURCE = "routing-table-updater";

        @Override public void run() {
            try {
                if (!routingTableDirty) {
                    return;
                }
                if (lifecycle.stopped()) {
                    return;
                }
                clusterService.submitStateUpdateTask(CLUSTER_UPDATE_TASK_SOURCE, new ClusterStateUpdateTask() {
                    @Override public ClusterState execute(ClusterState currentState) {
                        RoutingAllocation.Result routingResult = shardsAllocation.reroute(currentState);
                        if (!routingResult.changed()) {
                            // no state changed
                            return currentState;
                        }
                        return newClusterStateBuilder().state(currentState).routingResult(routingResult).build();
                    }
                });
                routingTableDirty = false;
            } catch (Exception e) {
                logger.warn("Failed to reroute routing table", e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1743.java