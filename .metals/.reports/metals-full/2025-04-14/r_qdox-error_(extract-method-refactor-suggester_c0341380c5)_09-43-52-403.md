error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5289.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5289.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5289.java
text:
```scala
l@@ogger.debug("applying started shard {}, reason [{}]", shardRouting, reason);

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

package org.elasticsearch.cluster.action.shard;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.ClusterStateUpdateTask;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.cluster.routing.IndexRoutingTable;
import org.elasticsearch.cluster.routing.IndexShardRoutingTable;
import org.elasticsearch.cluster.routing.RoutingTable;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.cluster.routing.allocation.ShardsAllocation;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.io.stream.VoidStreamable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.*;

import java.io.IOException;

import static org.elasticsearch.cluster.ClusterState.*;
import static org.elasticsearch.cluster.routing.ImmutableShardRouting.*;
import static org.elasticsearch.common.collect.Lists.*;

/**
 * @author kimchy (Shay Banon)
 */
public class ShardStateAction extends AbstractComponent {

    private final TransportService transportService;

    private final ClusterService clusterService;

    private final ShardsAllocation shardsAllocation;

    private final ThreadPool threadPool;

    @Inject public ShardStateAction(Settings settings, ClusterService clusterService, TransportService transportService,
                                    ShardsAllocation shardsAllocation, ThreadPool threadPool) {
        super(settings);
        this.clusterService = clusterService;
        this.transportService = transportService;
        this.shardsAllocation = shardsAllocation;
        this.threadPool = threadPool;

        transportService.registerHandler(ShardStartedTransportHandler.ACTION, new ShardStartedTransportHandler());
        transportService.registerHandler(ShardFailedTransportHandler.ACTION, new ShardFailedTransportHandler());
    }

    public void shardFailed(final ShardRouting shardRouting, final String reason) throws ElasticSearchException {
        logger.warn("sending failed shard for {}, reason [{}]", shardRouting, reason);
        DiscoveryNodes nodes = clusterService.state().nodes();
        if (nodes.localNodeMaster()) {
            threadPool.execute(new Runnable() {
                @Override public void run() {
                    innerShardFailed(shardRouting, reason);
                }
            });
        } else {
            transportService.sendRequest(clusterService.state().nodes().masterNode(),
                    ShardFailedTransportHandler.ACTION, new ShardRoutingEntry(shardRouting, reason), new VoidTransportResponseHandler() {
                        @Override public void handleException(RemoteTransportException exp) {
                            logger.warn("failed to send failed shard to [{}]", exp, clusterService.state().nodes().masterNode());
                        }
                    });
        }
    }

    public void shardStarted(final ShardRouting shardRouting, final String reason) throws ElasticSearchException {
        if (logger.isDebugEnabled()) {
            logger.debug("sending shard started for {}, reason [{}]", shardRouting, reason);
        }
        DiscoveryNodes nodes = clusterService.state().nodes();
        if (nodes.localNodeMaster()) {
            threadPool.execute(new Runnable() {
                @Override public void run() {
                    innerShardStarted(shardRouting, reason);
                }
            });
        } else {
            transportService.sendRequest(clusterService.state().nodes().masterNode(),
                    ShardStartedTransportHandler.ACTION, new ShardRoutingEntry(shardRouting, reason), new VoidTransportResponseHandler() {
                        @Override public void handleException(RemoteTransportException exp) {
                            logger.warn("failed to send shard started to [{}]", exp, clusterService.state().nodes().masterNode());
                        }
                    });
        }
    }

    private void innerShardFailed(final ShardRouting shardRouting, final String reason) {
        logger.warn("received shard failed for {}, reason [{}]", shardRouting, reason);
        clusterService.submitStateUpdateTask("shard-failed (" + shardRouting + "), reason [" + reason + "]", new ClusterStateUpdateTask() {
            @Override public ClusterState execute(ClusterState currentState) {
                RoutingTable routingTable = currentState.routingTable();
                IndexRoutingTable indexRoutingTable = routingTable.index(shardRouting.index());
                // if there is no routing table, the index has been deleted while it was being allocated
                // which is fine, we should just ignore this
                if (indexRoutingTable == null) {
                    return currentState;
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Applying failed shard {}, reason [{}]", shardRouting, reason);
                }
                RoutingTable prevRoutingTable = currentState.routingTable();
                RoutingTable newRoutingTable = shardsAllocation.applyFailedShards(currentState, newArrayList(shardRouting));
                if (prevRoutingTable == newRoutingTable) {
                    return currentState;
                }
                return newClusterStateBuilder().state(currentState).routingTable(newRoutingTable).build();
            }
        });
    }

    private void innerShardStarted(final ShardRouting shardRouting, final String reason) {
        if (logger.isDebugEnabled()) {
            logger.debug("received shard started for {}, reason [{}]", shardRouting, reason);
        }
        clusterService.submitStateUpdateTask("shard-started (" + shardRouting + "), reason [" + reason + "]", new ClusterStateUpdateTask() {
            @Override public ClusterState execute(ClusterState currentState) {
                RoutingTable routingTable = currentState.routingTable();
                IndexRoutingTable indexRoutingTable = routingTable.index(shardRouting.index());
                // if there is no routing table, the index has been deleted while it was being allocated
                // which is fine, we should just ignore this
                if (indexRoutingTable == null) {
                    return currentState;
                }
                // find the one that maps to us, if its already started, no need to do anything...
                // the shard might already be started since the nodes that is starting the shards might get cluster events
                // with the shard still initializing, and it will try and start it again (until the verification comes)
                IndexShardRoutingTable indexShardRoutingTable = indexRoutingTable.shard(shardRouting.id());
                for (ShardRouting entry : indexShardRoutingTable) {
                    if (shardRouting.currentNodeId().equals(entry.currentNodeId())) {
                        // we found the same shard that exists on the same node id
                        if (entry.started()) {
                            // already started, do nothing here...
                            return currentState;
                        }
                    }
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Applying started shard {}, reason [{}]", shardRouting, reason);
                }
                RoutingTable newRoutingTable = shardsAllocation.applyStartedShards(currentState, newArrayList(shardRouting));
                if (routingTable == newRoutingTable) {
                    return currentState;
                }
                return newClusterStateBuilder().state(currentState).routingTable(newRoutingTable).build();
            }
        });
    }

    private class ShardFailedTransportHandler extends BaseTransportRequestHandler<ShardRoutingEntry> {

        static final String ACTION = "cluster/shardFailure";

        @Override public ShardRoutingEntry newInstance() {
            return new ShardRoutingEntry();
        }

        @Override public void messageReceived(ShardRoutingEntry request, TransportChannel channel) throws Exception {
            innerShardFailed(request.shardRouting, request.reason);
            channel.sendResponse(VoidStreamable.INSTANCE);
        }
    }

    private class ShardStartedTransportHandler extends BaseTransportRequestHandler<ShardRoutingEntry> {

        static final String ACTION = "cluster/shardStarted";

        @Override public ShardRoutingEntry newInstance() {
            return new ShardRoutingEntry();
        }

        @Override public void messageReceived(ShardRoutingEntry request, TransportChannel channel) throws Exception {
            innerShardStarted(request.shardRouting, request.reason);
            channel.sendResponse(VoidStreamable.INSTANCE);
        }
    }

    private static class ShardRoutingEntry implements Streamable {

        private ShardRouting shardRouting;

        private String reason;

        private ShardRoutingEntry() {
        }

        private ShardRoutingEntry(ShardRouting shardRouting, String reason) {
            this.shardRouting = shardRouting;
            this.reason = reason;
        }

        @Override public void readFrom(StreamInput in) throws IOException {
            shardRouting = readShardRoutingEntry(in);
            reason = in.readUTF();
        }

        @Override public void writeTo(StreamOutput out) throws IOException {
            shardRouting.writeTo(out);
            out.writeUTF(reason);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5289.java