error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5311.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5311.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5311.java
text:
```scala
R@@outingTable.Builder routingTableBuilder = RoutingTable.builder(updatedState.routingTable());

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

package org.elasticsearch.gateway;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.cluster.*;
import org.elasticsearch.cluster.block.ClusterBlock;
import org.elasticsearch.cluster.block.ClusterBlockLevel;
import org.elasticsearch.cluster.block.ClusterBlocks;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.cluster.routing.RoutingTable;
import org.elasticsearch.cluster.routing.allocation.AllocationService;
import org.elasticsearch.cluster.routing.allocation.RoutingAllocation;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.discovery.Discovery;
import org.elasticsearch.discovery.DiscoveryService;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.threadpool.ThreadPool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.elasticsearch.cluster.ClusterState.newClusterStateBuilder;

/**
 *
 */
public class GatewayService extends AbstractLifecycleComponent<GatewayService> implements ClusterStateListener {

    public static final ClusterBlock STATE_NOT_RECOVERED_BLOCK = new ClusterBlock(1, "state not recovered / initialized", true, true, RestStatus.SERVICE_UNAVAILABLE, ClusterBlockLevel.ALL);

    private final Gateway gateway;

    private final ThreadPool threadPool;

    private final AllocationService allocationService;

    private final ClusterService clusterService;

    private final DiscoveryService discoveryService;

    private final TimeValue recoverAfterTime;
    private final int recoverAfterNodes;
    private final int expectedNodes;
    private final int recoverAfterDataNodes;
    private final int expectedDataNodes;
    private final int recoverAfterMasterNodes;
    private final int expectedMasterNodes;


    private final AtomicBoolean recovered = new AtomicBoolean();
    private final AtomicBoolean scheduledRecovery = new AtomicBoolean();

    @Inject
    public GatewayService(Settings settings, Gateway gateway, AllocationService allocationService, ClusterService clusterService, DiscoveryService discoveryService, ThreadPool threadPool) {
        super(settings);
        this.gateway = gateway;
        this.allocationService = allocationService;
        this.clusterService = clusterService;
        this.discoveryService = discoveryService;
        this.threadPool = threadPool;
        // allow to control a delay of when indices will get created
        this.recoverAfterTime = componentSettings.getAsTime("recover_after_time", null);
        this.recoverAfterNodes = componentSettings.getAsInt("recover_after_nodes", -1);
        this.expectedNodes = componentSettings.getAsInt("expected_nodes", -1);
        this.recoverAfterDataNodes = componentSettings.getAsInt("recover_after_data_nodes", -1);
        this.expectedDataNodes = componentSettings.getAsInt("expected_data_nodes", -1);
        // default the recover after master nodes to the minimum master nodes in the discovery
        this.recoverAfterMasterNodes = componentSettings.getAsInt("recover_after_master_nodes", settings.getAsInt("discovery.zen.minimum_master_nodes", -1));
        this.expectedMasterNodes = componentSettings.getAsInt("expected_master_nodes", -1);

        // Add the not recovered as initial state block, we don't allow anything until
        this.clusterService.addInitialStateBlock(STATE_NOT_RECOVERED_BLOCK);
    }

    @Override
    protected void doStart() throws ElasticSearchException {
        gateway.start();
        // if we received initial state, see if we can recover within the start phase, so we hold the
        // node from starting until we recovered properly
        if (discoveryService.initialStateReceived()) {
            ClusterState clusterState = clusterService.state();
            DiscoveryNodes nodes = clusterState.nodes();
            if (clusterState.nodes().localNodeMaster() && clusterState.blocks().hasGlobalBlock(STATE_NOT_RECOVERED_BLOCK)) {
                if (clusterState.blocks().hasGlobalBlock(Discovery.NO_MASTER_BLOCK)) {
                    logger.debug("not recovering from gateway, no master elected yet");
                } else if (recoverAfterNodes != -1 && (nodes.masterAndDataNodes().size()) < recoverAfterNodes) {
                    logger.debug("not recovering from gateway, nodes_size (data+master) [" + nodes.masterAndDataNodes().size() + "] < recover_after_nodes [" + recoverAfterNodes + "]");
                } else if (recoverAfterDataNodes != -1 && nodes.dataNodes().size() < recoverAfterDataNodes) {
                    logger.debug("not recovering from gateway, nodes_size (data) [" + nodes.dataNodes().size() + "] < recover_after_data_nodes [" + recoverAfterDataNodes + "]");
                } else if (recoverAfterMasterNodes != -1 && nodes.masterNodes().size() < recoverAfterMasterNodes) {
                    logger.debug("not recovering from gateway, nodes_size (master) [" + nodes.masterNodes().size() + "] < recover_after_master_nodes [" + recoverAfterMasterNodes + "]");
                } else {
                    boolean ignoreRecoverAfterTime;
                    if (expectedNodes == -1 && expectedMasterNodes == -1 && expectedDataNodes == -1) {
                        // no expected is set, don't ignore the timeout
                        ignoreRecoverAfterTime = false;
                    } else {
                        // one of the expected is set, see if all of them meet the need, and ignore the timeout in this case
                        ignoreRecoverAfterTime = true;
                        if (expectedNodes != -1 && (nodes.masterAndDataNodes().size() < expectedNodes)) { // does not meet the expected...
                            ignoreRecoverAfterTime = false;
                        }
                        if (expectedMasterNodes != -1 && (nodes.masterNodes().size() < expectedMasterNodes)) { // does not meet the expected...
                            ignoreRecoverAfterTime = false;
                        }
                        if (expectedDataNodes != -1 && (nodes.dataNodes().size() < expectedDataNodes)) { // does not meet the expected...
                            ignoreRecoverAfterTime = false;
                        }
                    }
                    performStateRecovery(ignoreRecoverAfterTime);
                }
            }
        } else {
            logger.debug("can't wait on start for (possibly) reading state from gateway, will do it asynchronously");
        }
        clusterService.addLast(this);
    }

    @Override
    protected void doStop() throws ElasticSearchException {
        clusterService.remove(this);
        gateway.stop();
    }

    @Override
    protected void doClose() throws ElasticSearchException {
        gateway.close();
    }

    @Override
    public void clusterChanged(final ClusterChangedEvent event) {
        if (lifecycle.stoppedOrClosed()) {
            return;
        }
        if (event.state().blocks().hasGlobalBlock(Discovery.NO_MASTER_BLOCK)) {
            // we need to clear those flags, since we might need to recover again in case we disconnect
            // from the cluster and then reconnect
            recovered.set(false);
            scheduledRecovery.set(false);
        }
        if (event.localNodeMaster() && event.state().blocks().hasGlobalBlock(STATE_NOT_RECOVERED_BLOCK)) {
            ClusterState clusterState = event.state();
            DiscoveryNodes nodes = clusterState.nodes();
            if (event.state().blocks().hasGlobalBlock(Discovery.NO_MASTER_BLOCK)) {
                logger.debug("not recovering from gateway, no master elected yet");
            } else if (recoverAfterNodes != -1 && (nodes.masterAndDataNodes().size()) < recoverAfterNodes) {
                logger.debug("not recovering from gateway, nodes_size (data+master) [" + nodes.masterAndDataNodes().size() + "] < recover_after_nodes [" + recoverAfterNodes + "]");
            } else if (recoverAfterDataNodes != -1 && nodes.dataNodes().size() < recoverAfterDataNodes) {
                logger.debug("not recovering from gateway, nodes_size (data) [" + nodes.dataNodes().size() + "] < recover_after_data_nodes [" + recoverAfterDataNodes + "]");
            } else if (recoverAfterMasterNodes != -1 && nodes.masterNodes().size() < recoverAfterMasterNodes) {
                logger.debug("not recovering from gateway, nodes_size (master) [" + nodes.masterNodes().size() + "] < recover_after_master_nodes [" + recoverAfterMasterNodes + "]");
            } else {
                boolean ignoreRecoverAfterTime;
                if (expectedNodes == -1 && expectedMasterNodes == -1 && expectedDataNodes == -1) {
                    // no expected is set, don't ignore the timeout
                    ignoreRecoverAfterTime = false;
                } else {
                    // one of the expected is set, see if all of them meet the need, and ignore the timeout in this case
                    ignoreRecoverAfterTime = true;
                    if (expectedNodes != -1 && (nodes.masterAndDataNodes().size() < expectedNodes)) { // does not meet the expected...
                        ignoreRecoverAfterTime = false;
                    }
                    if (expectedMasterNodes != -1 && (nodes.masterNodes().size() < expectedMasterNodes)) { // does not meet the expected...
                        ignoreRecoverAfterTime = false;
                    }
                    if (expectedDataNodes != -1 && (nodes.dataNodes().size() < expectedDataNodes)) { // does not meet the expected...
                        ignoreRecoverAfterTime = false;
                    }
                }
                final boolean fIgnoreRecoverAfterTime = ignoreRecoverAfterTime;
                threadPool.generic().execute(new Runnable() {
                    @Override
                    public void run() {
                        performStateRecovery(fIgnoreRecoverAfterTime);
                    }
                });
            }
        }
    }

    private void performStateRecovery(boolean ignoreRecoverAfterTime) {
        final Gateway.GatewayStateRecoveredListener recoveryListener = new GatewayRecoveryListener(new CountDownLatch(1));

        if (!ignoreRecoverAfterTime && recoverAfterTime != null) {
            if (scheduledRecovery.compareAndSet(false, true)) {
                logger.debug("delaying initial state recovery for [{}]", recoverAfterTime);
                threadPool.schedule(recoverAfterTime, ThreadPool.Names.GENERIC, new Runnable() {
                    @Override
                    public void run() {
                        if (recovered.compareAndSet(false, true)) {
                            logger.trace("performing state recovery...");
                            gateway.performStateRecovery(recoveryListener);
                        }
                    }
                });
            }
        } else {
            if (recovered.compareAndSet(false, true)) {
                logger.trace("performing state recovery...");
                gateway.performStateRecovery(recoveryListener);
            }
        }
    }

    class GatewayRecoveryListener implements Gateway.GatewayStateRecoveredListener {

        private final CountDownLatch latch;

        GatewayRecoveryListener(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void onSuccess(final ClusterState recoveredState) {
            logger.trace("successful state recovery, importing cluster state...");
            clusterService.submitStateUpdateTask("local-gateway-elected-state", new ProcessedClusterStateUpdateTask() {
                @Override
                public ClusterState execute(ClusterState currentState) {
                    assert currentState.metaData().indices().isEmpty();

                    // remove the block, since we recovered from gateway
                    ClusterBlocks.Builder blocks = ClusterBlocks.builder()
                            .blocks(currentState.blocks())
                            .blocks(recoveredState.blocks())
                            .removeGlobalBlock(STATE_NOT_RECOVERED_BLOCK);

                    MetaData.Builder metaDataBuilder = MetaData.builder(recoveredState.metaData());

                    if (recoveredState.metaData().settings().getAsBoolean(MetaData.SETTING_READ_ONLY, false) || currentState.metaData().settings().getAsBoolean(MetaData.SETTING_READ_ONLY, false)) {
                        blocks.addGlobalBlock(MetaData.CLUSTER_READ_ONLY_BLOCK);
                    }

                    for (IndexMetaData indexMetaData : recoveredState.metaData()) {
                        metaDataBuilder.put(indexMetaData, false);
                        blocks.addBlocks(indexMetaData);
                    }

                    // update the state to reflect the new metadata and routing
                    ClusterState updatedState = newClusterStateBuilder().state(currentState)
                            .blocks(blocks)
                            .metaData(metaDataBuilder)
                            .build();

                    // initialize all index routing tables as empty
                    RoutingTable.Builder routingTableBuilder = RoutingTable.builder().routingTable(updatedState.routingTable());
                    for (IndexMetaData indexMetaData : updatedState.metaData().indices().values()) {
                        routingTableBuilder.addAsRecovery(indexMetaData);
                    }
                    // start with 0 based versions for routing table
                    routingTableBuilder.version(0);

                    // now, reroute
                    RoutingAllocation.Result routingResult = allocationService.reroute(newClusterStateBuilder().state(updatedState).routingTable(routingTableBuilder).build());

                    return newClusterStateBuilder().state(updatedState).routingResult(routingResult).build();
                }

                @Override
                public void onFailure(String source, Throwable t) {
                    logger.error("unexpected failure during [{}]", t, source);
                }

                @Override
                public void clusterStateProcessed(String source, ClusterState oldState, ClusterState newState) {
                    logger.info("recovered [{}] indices into cluster_state", newState.metaData().indices().size());
                    latch.countDown();
                }
            });
        }

        @Override
        public void onFailure(String message) {
            recovered.set(false);
            scheduledRecovery.set(false);
            // don't remove the block here, we don't want to allow anything in such a case
            logger.info("metadata state not restored, reason: {}", message);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5311.java