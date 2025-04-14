error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5608.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5608.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5608.java
text:
```scala
c@@lusterService.submitStateUpdateTask("cluster_update_settings", Priority.IMMEDIATE, new AckedClusterStateUpdateTask() {

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

package org.elasticsearch.action.admin.cluster.settings;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.master.TransportMasterNodeOperationAction;
import org.elasticsearch.cluster.AckedClusterStateUpdateTask;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.block.ClusterBlocks;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.routing.allocation.AllocationService;
import org.elasticsearch.cluster.routing.allocation.RoutingAllocation;
import org.elasticsearch.cluster.settings.ClusterDynamicSettings;
import org.elasticsearch.cluster.settings.DynamicSettings;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.Priority;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.util.Map;

import static org.elasticsearch.cluster.ClusterState.builder;

/**
 *
 */
public class TransportClusterUpdateSettingsAction extends TransportMasterNodeOperationAction<ClusterUpdateSettingsRequest, ClusterUpdateSettingsResponse> {

    private final AllocationService allocationService;

    private final DynamicSettings dynamicSettings;

    @Inject
    public TransportClusterUpdateSettingsAction(Settings settings, TransportService transportService, ClusterService clusterService, ThreadPool threadPool,
                                                AllocationService allocationService, @ClusterDynamicSettings DynamicSettings dynamicSettings) {
        super(settings, transportService, clusterService, threadPool);
        this.allocationService = allocationService;
        this.dynamicSettings = dynamicSettings;
    }

    @Override
    protected String executor() {
        return ThreadPool.Names.SAME;
    }

    @Override
    protected String transportAction() {
        return ClusterUpdateSettingsAction.NAME;
    }

    @Override
    protected ClusterUpdateSettingsRequest newRequest() {
        return new ClusterUpdateSettingsRequest();
    }

    @Override
    protected ClusterUpdateSettingsResponse newResponse() {
        return new ClusterUpdateSettingsResponse();
    }

    @Override
    protected void masterOperation(final ClusterUpdateSettingsRequest request, final ClusterState state, final ActionListener<ClusterUpdateSettingsResponse> listener) throws ElasticsearchException {
        final ImmutableSettings.Builder transientUpdates = ImmutableSettings.settingsBuilder();
        final ImmutableSettings.Builder persistentUpdates = ImmutableSettings.settingsBuilder();

        clusterService.submitStateUpdateTask("cluster_update_settings", Priority.URGENT, new AckedClusterStateUpdateTask() {

            private volatile boolean changed = false;

            @Override
            public boolean mustAck(DiscoveryNode discoveryNode) {
                return true;
            }

            @Override
            public void onAllNodesAcked(@Nullable Throwable t) {
                if (changed) {
                    reroute(true);
                } else {
                    listener.onResponse(new ClusterUpdateSettingsResponse(true, transientUpdates.build(), persistentUpdates.build()));
                }

            }

            @Override
            public void onAckTimeout() {
                if (changed) {
                    reroute(false);
                } else {
                    listener.onResponse(new ClusterUpdateSettingsResponse(false, transientUpdates.build(), persistentUpdates.build()));
                }
            }

            private void reroute(final boolean updateSettingsAcked) {
                clusterService.submitStateUpdateTask("reroute_after_cluster_update_settings", Priority.URGENT, new AckedClusterStateUpdateTask() {

                    @Override
                    public boolean mustAck(DiscoveryNode discoveryNode) {
                        //we wait for the reroute ack only if the update settings was acknowledged
                        return updateSettingsAcked;
                    }

                    @Override
                    public void onAllNodesAcked(@Nullable Throwable t) {
                        //we return when the cluster reroute is acked (the acknowledged flag depends on whether the update settings was acknowledged)
                        listener.onResponse(new ClusterUpdateSettingsResponse(updateSettingsAcked, transientUpdates.build(), persistentUpdates.build()));
                    }

                    @Override
                    public void onAckTimeout() {
                        //we return when the cluster reroute ack times out (acknowledged false)
                        listener.onResponse(new ClusterUpdateSettingsResponse(false, transientUpdates.build(), persistentUpdates.build()));
                    }

                    @Override
                    public TimeValue ackTimeout() {
                        return request.timeout();
                    }

                    @Override
                    public TimeValue timeout() {
                        return request.masterNodeTimeout();
                    }

                    @Override
                    public void onFailure(String source, Throwable t) {
                        //if the reroute fails we only log
                        logger.debug("failed to perform [{}]", t, source);
                    }

                    @Override
                    public ClusterState execute(final ClusterState currentState) {
                        // now, reroute in case things that require it changed (e.g. number of replicas)
                        RoutingAllocation.Result routingResult = allocationService.reroute(currentState);
                        if (!routingResult.changed()) {
                            return currentState;
                        }
                        return ClusterState.builder(currentState).routingResult(routingResult).build();
                    }

                    @Override
                    public void clusterStateProcessed(String source, ClusterState oldState, ClusterState newState) {
                    }
                });
            }

            @Override
            public TimeValue ackTimeout() {
                return request.timeout();
            }

            @Override
            public TimeValue timeout() {
                return request.masterNodeTimeout();
            }

            @Override
            public void onFailure(String source, Throwable t) {
                logger.debug("failed to perform [{}]", t, source);
                listener.onFailure(t);
            }

            @Override
            public ClusterState execute(final ClusterState currentState) {
                ImmutableSettings.Builder transientSettings = ImmutableSettings.settingsBuilder();
                transientSettings.put(currentState.metaData().transientSettings());
                for (Map.Entry<String, String> entry : request.transientSettings().getAsMap().entrySet()) {
                    if (dynamicSettings.hasDynamicSetting(entry.getKey()) || entry.getKey().startsWith("logger.")) {
                        String error = dynamicSettings.validateDynamicSetting(entry.getKey(), entry.getValue());
                        if (error == null) {
                            transientSettings.put(entry.getKey(), entry.getValue());
                            transientUpdates.put(entry.getKey(), entry.getValue());
                            changed = true;
                        } else {
                            logger.warn("ignoring transient setting [{}], [{}]", entry.getKey(), error);
                        }
                    } else {
                        logger.warn("ignoring transient setting [{}], not dynamically updateable", entry.getKey());
                    }
                }

                ImmutableSettings.Builder persistentSettings = ImmutableSettings.settingsBuilder();
                persistentSettings.put(currentState.metaData().persistentSettings());
                for (Map.Entry<String, String> entry : request.persistentSettings().getAsMap().entrySet()) {
                    if (dynamicSettings.hasDynamicSetting(entry.getKey()) || entry.getKey().startsWith("logger.")) {
                        String error = dynamicSettings.validateDynamicSetting(entry.getKey(), entry.getValue());
                        if (error == null) {
                            persistentSettings.put(entry.getKey(), entry.getValue());
                            persistentUpdates.put(entry.getKey(), entry.getValue());
                            changed = true;
                        } else {
                            logger.warn("ignoring persistent setting [{}], [{}]", entry.getKey(), error);
                        }
                    } else {
                        logger.warn("ignoring persistent setting [{}], not dynamically updateable", entry.getKey());
                    }
                }

                if (!changed) {
                    return currentState;
                }

                MetaData.Builder metaData = MetaData.builder(currentState.metaData())
                        .persistentSettings(persistentSettings.build())
                        .transientSettings(transientSettings.build());

                ClusterBlocks.Builder blocks = ClusterBlocks.builder().blocks(currentState.blocks());
                boolean updatedReadOnly = metaData.persistentSettings().getAsBoolean(MetaData.SETTING_READ_ONLY, false) || metaData.transientSettings().getAsBoolean(MetaData.SETTING_READ_ONLY, false);
                if (updatedReadOnly) {
                    blocks.addGlobalBlock(MetaData.CLUSTER_READ_ONLY_BLOCK);
                } else {
                    blocks.removeGlobalBlock(MetaData.CLUSTER_READ_ONLY_BLOCK);
                }

                return builder(currentState).metaData(metaData).blocks(blocks).build();
            }

            @Override
            public void clusterStateProcessed(String source, ClusterState oldState, ClusterState newState) {

            }
        });
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5608.java