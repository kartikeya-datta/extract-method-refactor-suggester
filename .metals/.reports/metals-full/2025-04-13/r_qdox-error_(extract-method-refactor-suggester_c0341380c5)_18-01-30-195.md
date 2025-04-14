error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7153.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7153.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7153.java
text:
```scala
N@@odeStats nodeStats = nodeService.stats(CommonStatsFlags.NONE, false, true, true, false, false, true, false, false, false);

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
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

package org.elasticsearch.action.admin.cluster.stats;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.action.admin.cluster.health.ClusterIndexHealth;
import org.elasticsearch.action.admin.cluster.node.info.NodeInfo;
import org.elasticsearch.action.admin.cluster.node.stats.NodeStats;
import org.elasticsearch.action.admin.indices.stats.CommonStatsFlags;
import org.elasticsearch.action.admin.indices.stats.ShardStats;
import org.elasticsearch.action.support.nodes.NodeOperationRequest;
import org.elasticsearch.action.support.nodes.TransportNodesOperationAction;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.routing.IndexRoutingTable;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.index.shard.service.IndexShard;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.node.service.NodeService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 *
 */
public class TransportClusterStatsAction extends TransportNodesOperationAction<ClusterStatsRequest, ClusterStatsResponse,
        TransportClusterStatsAction.ClusterStatsNodeRequest, ClusterStatsNodeResponse> {

    private static final CommonStatsFlags SHARD_STATS_FLAGS = new CommonStatsFlags(CommonStatsFlags.Flag.Docs, CommonStatsFlags.Flag.Store,
            CommonStatsFlags.Flag.FieldData, CommonStatsFlags.Flag.FilterCache, CommonStatsFlags.Flag.IdCache,
            CommonStatsFlags.Flag.Completion, CommonStatsFlags.Flag.Segments, CommonStatsFlags.Flag.Percolate);

    private final NodeService nodeService;
    private final IndicesService indicesService;


    @Inject
    public TransportClusterStatsAction(Settings settings, ClusterName clusterName, ThreadPool threadPool,
                                       ClusterService clusterService, TransportService transportService,
                                       NodeService nodeService, IndicesService indicesService) {
        super(settings, clusterName, threadPool, clusterService, transportService);
        this.nodeService = nodeService;
        this.indicesService = indicesService;
    }

    @Override
    protected String executor() {
        return ThreadPool.Names.MANAGEMENT;
    }

    @Override
    protected String transportAction() {
        return ClusterStatsAction.NAME;
    }

    @Override
    protected ClusterStatsResponse newResponse(ClusterStatsRequest clusterStatsRequest, AtomicReferenceArray responses) {
        final List<ClusterStatsNodeResponse> nodeStats = new ArrayList<ClusterStatsNodeResponse>(responses.length());
        for (int i = 0; i < responses.length(); i++) {
            Object resp = responses.get(i);
            if (resp instanceof ClusterStatsNodeResponse) {
                nodeStats.add((ClusterStatsNodeResponse) resp);
            }
        }
        return new ClusterStatsResponse(System.currentTimeMillis(), clusterName,
                clusterService.state().metaData().uuid(), nodeStats.toArray(new ClusterStatsNodeResponse[nodeStats.size()]));
    }

    @Override
    protected ClusterStatsRequest newRequest() {
        return new ClusterStatsRequest();
    }

    @Override
    protected ClusterStatsNodeRequest newNodeRequest() {
        return new ClusterStatsNodeRequest();
    }

    @Override
    protected ClusterStatsNodeRequest newNodeRequest(String nodeId, ClusterStatsRequest request) {
        return new ClusterStatsNodeRequest(nodeId, request);
    }

    @Override
    protected ClusterStatsNodeResponse newNodeResponse() {
        return new ClusterStatsNodeResponse();
    }

    @Override
    protected ClusterStatsNodeResponse nodeOperation(ClusterStatsNodeRequest nodeRequest) throws ElasticSearchException {
        NodeInfo nodeInfo = nodeService.info(false, true, false, true, false, false, true, false, true);
        NodeStats nodeStats = nodeService.stats(CommonStatsFlags.NONE, false, true, true, false, false, true, false, false);
        List<ShardStats> shardsStats = new ArrayList<ShardStats>();
        for (String index : indicesService.indices()) {
            IndexService indexService = indicesService.indexService(index);
            if (indexService == null) {
                continue;
            }
            for (IndexShard indexShard : indexService) {
                if (indexShard.routingEntry().active()) {
                    // only report on fully started shards
                    shardsStats.add(new ShardStats(indexShard, SHARD_STATS_FLAGS));
                }
            }

        }

        ClusterHealthStatus clusterStatus = null;
        if (clusterService.state().nodes().localNodeMaster()) {
            // populate cluster status
            clusterStatus = ClusterHealthStatus.GREEN;
            for (IndexRoutingTable indexRoutingTable : clusterService.state().routingTable()) {
                IndexMetaData indexMetaData = clusterService.state().metaData().index(indexRoutingTable.index());
                if (indexRoutingTable == null) {
                    continue;
                }

                ClusterIndexHealth indexHealth = new ClusterIndexHealth(indexMetaData, indexRoutingTable);
                switch (indexHealth.getStatus()) {
                    case RED:
                        clusterStatus = ClusterHealthStatus.RED;
                        break;
                    case YELLOW:
                        if (clusterStatus != ClusterHealthStatus.RED) {
                            clusterStatus = ClusterHealthStatus.YELLOW;
                        }
                        break;
                }
            }
        }

        return new ClusterStatsNodeResponse(nodeInfo.getNode(), clusterStatus, nodeInfo, nodeStats, shardsStats.toArray(new ShardStats[shardsStats.size()]));

    }

    @Override
    protected boolean accumulateExceptions() {
        return false;
    }

    static class ClusterStatsNodeRequest extends NodeOperationRequest {

        ClusterStatsRequest request;

        ClusterStatsNodeRequest() {
        }

        ClusterStatsNodeRequest(String nodeId, ClusterStatsRequest request) {
            super(request, nodeId);
            this.request = request;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            request = new ClusterStatsRequest();
            request.readFrom(in);
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            request.writeTo(out);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7153.java