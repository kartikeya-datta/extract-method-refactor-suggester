error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9505.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9505.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9505.java
text:
```scala
i@@f (request.waitForStatus() != null && response.status().value() <= request.waitForStatus().value()) {

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

package org.elasticsearch.action.admin.cluster.health;

import com.google.inject.Inject;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.TransportActions;
import org.elasticsearch.action.support.master.TransportMasterNodeOperationAction;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.routing.IndexRoutingTable;
import org.elasticsearch.cluster.routing.IndexShardRoutingTable;
import org.elasticsearch.cluster.routing.RoutingTableValidation;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.timer.TimerService;
import org.elasticsearch.transport.TransportService;
import org.elasticsearch.util.settings.Settings;

import static org.elasticsearch.action.Actions.*;

/**
 * @author kimchy (shay.banon)
 */
public class TransportClusterHealthAction extends TransportMasterNodeOperationAction<ClusterHealthRequest, ClusterHealthResponse> {

    private final ClusterName clusterName;

    private final TimerService timerService;

    @Inject public TransportClusterHealthAction(Settings settings, TransportService transportService, ClusterService clusterService, ThreadPool threadPool,
                                                TimerService timerService, ClusterName clusterName) {
        super(settings, transportService, clusterService, threadPool);
        this.clusterName = clusterName;
        this.timerService = timerService;
    }

    @Override protected String transportAction() {
        return TransportActions.Admin.Cluster.HEALTH;
    }

    @Override protected ClusterHealthRequest newRequest() {
        return new ClusterHealthRequest();
    }

    @Override protected ClusterHealthResponse newResponse() {
        return new ClusterHealthResponse();
    }

    @Override protected ClusterHealthResponse masterOperation(ClusterHealthRequest request) throws ElasticSearchException {
        int waitFor = 2;
        if (request.waitForStatus() == null) {
            waitFor--;
        }
        if (request.waitForRelocatingShards() == -1) {
            waitFor--;
        }
        if (waitFor == 0) {
            // no need to wait for anything
            return clusterHealth(request);
        }
        long endTime = System.currentTimeMillis() + request.timeout().millis();
        while (true) {
            int waitForCounter = 0;
            ClusterHealthResponse response = clusterHealth(request);
            if (request.waitForStatus() != null && response.status() == request.waitForStatus()) {
                waitForCounter++;
            }
            if (request.waitForRelocatingShards() != -1 && response.relocatingShards() <= request.waitForRelocatingShards()) {
                waitForCounter++;
            }
            if (waitForCounter == waitFor) {
                return response;
            }
            if (timerService.estimatedTimeInMillis() > endTime) {
                response.timedOut = true;
                return response;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                response.timedOut = true;
                // we got interrupted, bail
                return response;
            }
        }
    }

    private ClusterHealthResponse clusterHealth(ClusterHealthRequest request) {
        ClusterState clusterState = clusterService.state();
        RoutingTableValidation validation = clusterState.routingTable().validate(clusterState.metaData());
        ClusterHealthResponse response = new ClusterHealthResponse(clusterName.value(), validation.failures());

        String[] indices = processIndices(clusterState, request.indices());
        for (String index : indices) {
            IndexRoutingTable indexRoutingTable = clusterState.routingTable().index(index);
            IndexMetaData indexMetaData = clusterState.metaData().index(index);
            if (indexRoutingTable == null) {
                continue;
            }
            ClusterIndexHealth indexHealth = new ClusterIndexHealth(index, indexMetaData.numberOfShards(), indexMetaData.numberOfReplicas(), validation.indexFailures(indexMetaData.index()));

            for (IndexShardRoutingTable shardRoutingTable : indexRoutingTable) {
                ClusterShardHealth shardHealth = new ClusterShardHealth(shardRoutingTable.shardId().id());
                for (ShardRouting shardRouting : shardRoutingTable) {
                    if (shardRouting.active()) {
                        shardHealth.activeShards++;
                        if (shardRouting.relocating()) {
                            // the shard is relocating, the one he is relocating to will be in initializing state, so we don't count it
                            shardHealth.relocatingShards++;
                        }
                        if (shardRouting.primary()) {
                            shardHealth.primaryActive = true;
                        }
                    }
                }
                if (shardHealth.primaryActive) {
                    if (shardHealth.activeShards == shardRoutingTable.size()) {
                        shardHealth.status = ClusterHealthStatus.GREEN;
                    } else {
                        shardHealth.status = ClusterHealthStatus.YELLOW;
                    }
                } else {
                    shardHealth.status = ClusterHealthStatus.RED;
                }
                indexHealth.shards.put(shardHealth.id(), shardHealth);
            }

            for (ClusterShardHealth shardHealth : indexHealth) {
                if (shardHealth.primaryActive()) {
                    indexHealth.activePrimaryShards++;
                }
                indexHealth.activeShards += shardHealth.activeShards;
                indexHealth.relocatingShards += shardHealth.relocatingShards;
            }
            // update the index status
            indexHealth.status = ClusterHealthStatus.GREEN;
            if (!indexHealth.validationFailures().isEmpty()) {
                indexHealth.status = ClusterHealthStatus.RED;
            } else {
                for (ClusterShardHealth shardHealth : indexHealth) {
                    if (shardHealth.status() == ClusterHealthStatus.RED) {
                        indexHealth.status = ClusterHealthStatus.RED;
                        break;
                    }
                    if (shardHealth.status() == ClusterHealthStatus.YELLOW) {
                        indexHealth.status = ClusterHealthStatus.YELLOW;
                    }
                }
            }

            response.indices.put(indexHealth.index(), indexHealth);
        }

        for (ClusterIndexHealth indexHealth : response) {
            response.activePrimaryShards += indexHealth.activePrimaryShards;
            response.activeShards += indexHealth.activeShards;
            response.relocatingShards += indexHealth.relocatingShards;
        }

        response.status = ClusterHealthStatus.GREEN;
        if (!response.validationFailures().isEmpty()) {
            response.status = ClusterHealthStatus.RED;
        } else {
            for (ClusterIndexHealth indexHealth : response) {
                if (indexHealth.status() == ClusterHealthStatus.RED) {
                    response.status = ClusterHealthStatus.RED;
                    break;
                }
                if (indexHealth.status() == ClusterHealthStatus.YELLOW) {
                    response.status = ClusterHealthStatus.YELLOW;
                }
            }
        }

        return response;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9505.java