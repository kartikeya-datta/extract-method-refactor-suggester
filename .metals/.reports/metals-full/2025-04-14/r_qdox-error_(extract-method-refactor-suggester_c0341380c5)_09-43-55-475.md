error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5310.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5310.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5310.java
text:
```scala
R@@outingTable.Builder routingTableBuilder = RoutingTable.builder(currentState.routingTable());

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

package org.elasticsearch.cluster.metadata;

import com.google.common.collect.Sets;
import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.action.admin.indices.settings.UpdateSettingsClusterStateUpdateRequest;
import org.elasticsearch.cluster.*;
import org.elasticsearch.cluster.ack.ClusterStateUpdateListener;
import org.elasticsearch.cluster.ack.ClusterStateUpdateResponse;
import org.elasticsearch.cluster.block.ClusterBlocks;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.routing.RoutingTable;
import org.elasticsearch.cluster.routing.allocation.AllocationService;
import org.elasticsearch.cluster.routing.allocation.RoutingAllocation;
import org.elasticsearch.cluster.settings.DynamicSettings;
import org.elasticsearch.common.Booleans;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.Priority;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.settings.IndexDynamicSettings;

import java.util.*;

import static org.elasticsearch.cluster.ClusterState.newClusterStateBuilder;

/**
 * Service responsible for submitting update index settings requests
 */
public class MetaDataUpdateSettingsService extends AbstractComponent implements ClusterStateListener {

    private final ClusterService clusterService;

    private final AllocationService allocationService;

    private final DynamicSettings dynamicSettings;

    @Inject
    public MetaDataUpdateSettingsService(Settings settings, ClusterService clusterService, AllocationService allocationService, @IndexDynamicSettings DynamicSettings dynamicSettings) {
        super(settings);
        this.clusterService = clusterService;
        this.clusterService.add(this);
        this.allocationService = allocationService;
        this.dynamicSettings = dynamicSettings;
    }

    @Override
    public void clusterChanged(ClusterChangedEvent event) {
        // update an index with number of replicas based on data nodes if possible
        if (!event.state().nodes().localNodeMaster()) {
            return;
        }

        Map<Integer, List<String>> nrReplicasChanged = new HashMap<Integer, List<String>>();

        // we need to do this each time in case it was changed by update settings
        for (final IndexMetaData indexMetaData : event.state().metaData()) {
            String autoExpandReplicas = indexMetaData.settings().get(IndexMetaData.SETTING_AUTO_EXPAND_REPLICAS);
            if (autoExpandReplicas != null && Booleans.parseBoolean(autoExpandReplicas, true)) { // Booleans only work for false values, just as we want it here
                try {
                    int min;
                    int max;
                    try {
                        min = Integer.parseInt(autoExpandReplicas.substring(0, autoExpandReplicas.indexOf('-')));
                        String sMax = autoExpandReplicas.substring(autoExpandReplicas.indexOf('-') + 1);
                        if (sMax.equals("all")) {
                            max = event.state().nodes().dataNodes().size() - 1;
                        } else {
                            max = Integer.parseInt(sMax);
                        }
                    } catch (Exception e) {
                        logger.warn("failed to set [{}], wrong format [{}]", e, IndexMetaData.SETTING_AUTO_EXPAND_REPLICAS, autoExpandReplicas);
                        continue;
                    }

                    int numberOfReplicas = event.state().nodes().dataNodes().size() - 1;
                    if (numberOfReplicas < min) {
                        numberOfReplicas = min;
                    } else if (numberOfReplicas > max) {
                        numberOfReplicas = max;
                    }

                    // same value, nothing to do there
                    if (numberOfReplicas == indexMetaData.numberOfReplicas()) {
                        continue;
                    }

                    if (numberOfReplicas >= min && numberOfReplicas <= max) {

                        if (!nrReplicasChanged.containsKey(numberOfReplicas)) {
                            nrReplicasChanged.put(numberOfReplicas, new ArrayList<String>());
                        }

                        nrReplicasChanged.get(numberOfReplicas).add(indexMetaData.index());
                    }
                } catch (Exception e) {
                    logger.warn("[{}] failed to parse auto expand replicas", e, indexMetaData.index());
                }
            }
        }

        if (nrReplicasChanged.size() > 0) {
            for (final Integer fNumberOfReplicas : nrReplicasChanged.keySet()) {
                Settings settings = ImmutableSettings.settingsBuilder().put(IndexMetaData.SETTING_NUMBER_OF_REPLICAS, fNumberOfReplicas).build();
                final List<String> indices = nrReplicasChanged.get(fNumberOfReplicas);

                UpdateSettingsClusterStateUpdateRequest updateRequest = new UpdateSettingsClusterStateUpdateRequest()
                        .indices(indices.toArray(new String[indices.size()])).settings(settings)
                        .ackTimeout(TimeValue.timeValueMillis(0)) //no need to wait for ack here
                        .masterNodeTimeout(TimeValue.timeValueMinutes(10));

                updateSettings(updateRequest, new ClusterStateUpdateListener() {
                    @Override
                    public void onResponse(ClusterStateUpdateResponse response) {
                        for (String index : indices) {
                            logger.info("[{}] auto expanded replicas to [{}]", index, fNumberOfReplicas);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        for (String index : indices) {
                            logger.warn("[{}] fail to auto expand replicas to [{}]", index, fNumberOfReplicas);
                        }
                    }
                });
            }
        }
    }

    public void updateSettings(final UpdateSettingsClusterStateUpdateRequest request, final ClusterStateUpdateListener listener) {
        ImmutableSettings.Builder updatedSettingsBuilder = ImmutableSettings.settingsBuilder();
        for (Map.Entry<String, String> entry : request.settings().getAsMap().entrySet()) {
            if (entry.getKey().equals("index")) {
                continue;
            }
            if (!entry.getKey().startsWith("index.")) {
                updatedSettingsBuilder.put("index." + entry.getKey(), entry.getValue());
            } else {
                updatedSettingsBuilder.put(entry.getKey(), entry.getValue());
            }
        }
        // never allow to change the number of shards
        for (String key : updatedSettingsBuilder.internalMap().keySet()) {
            if (key.equals(IndexMetaData.SETTING_NUMBER_OF_SHARDS)) {
                listener.onFailure(new ElasticSearchIllegalArgumentException("can't change the number of shards for an index"));
                return;
            }
        }

        final Settings closeSettings = updatedSettingsBuilder.build();

        final Set<String> removedSettings = Sets.newHashSet();
        final Set<String> errors = Sets.newHashSet();
        for (Map.Entry<String, String> setting : updatedSettingsBuilder.internalMap().entrySet()) {
            if (!dynamicSettings.hasDynamicSetting(setting.getKey())) {
                removedSettings.add(setting.getKey());
            } else {
                String error = dynamicSettings.validateDynamicSetting(setting.getKey(), setting.getValue());
                if (error != null) {
                    errors.add("[" + setting.getKey() + "] - " + error);
                }
            }
        }

        if (!errors.isEmpty()) {
            listener.onFailure(new ElasticSearchIllegalArgumentException("can't process the settings: " + errors.toString()));
            return;
        }

        if (!removedSettings.isEmpty()) {
            for (String removedSetting : removedSettings) {
                updatedSettingsBuilder.remove(removedSetting);
            }
        }
        final Settings openSettings = updatedSettingsBuilder.build();

        clusterService.submitStateUpdateTask("update-settings", Priority.URGENT, new AckedClusterStateUpdateTask() {

            @Override
            public boolean mustAck(DiscoveryNode discoveryNode) {
                return true;
            }

            @Override
            public void onAllNodesAcked(@Nullable Throwable t) {
                listener.onResponse(new ClusterStateUpdateResponse(true));
            }

            @Override
            public void onAckTimeout() {
                listener.onResponse(new ClusterStateUpdateResponse(false));
            }

            @Override
            public TimeValue ackTimeout() {
                return request.ackTimeout();
            }

            @Override
            public TimeValue timeout() {
                return request.masterNodeTimeout();
            }

            @Override
            public void onFailure(String source, Throwable t) {
                listener.onFailure(t);
            }

            @Override
            public ClusterState execute(ClusterState currentState) {
                String[] actualIndices = currentState.metaData().concreteIndices(request.indices());
                RoutingTable.Builder routingTableBuilder = RoutingTable.builder().routingTable(currentState.routingTable());
                MetaData.Builder metaDataBuilder = MetaData.builder(currentState.metaData());

                // allow to change any settings to a close index, and only allow dynamic settings to be changed
                // on an open index
                Set<String> openIndices = Sets.newHashSet();
                Set<String> closeIndices = Sets.newHashSet();
                for (String index : actualIndices) {
                    if (currentState.metaData().index(index).state() == IndexMetaData.State.OPEN) {
                        openIndices.add(index);
                    } else {
                        closeIndices.add(index);
                    }
                }

                if (!removedSettings.isEmpty() && !openIndices.isEmpty()) {
                    throw new ElasticSearchIllegalArgumentException(String.format(Locale.ROOT,
                            "Can't update non dynamic settings[%s] for open indices[%s]",
                            removedSettings,
                            openIndices
                    ));
                }

                int updatedNumberOfReplicas = openSettings.getAsInt(IndexMetaData.SETTING_NUMBER_OF_REPLICAS, -1);
                if (updatedNumberOfReplicas != -1) {
                    routingTableBuilder.updateNumberOfReplicas(updatedNumberOfReplicas, actualIndices);
                    metaDataBuilder.updateNumberOfReplicas(updatedNumberOfReplicas, actualIndices);
                    logger.info("updating number_of_replicas to [{}] for indices {}", updatedNumberOfReplicas, actualIndices);
                }

                ClusterBlocks.Builder blocks = ClusterBlocks.builder().blocks(currentState.blocks());
                Boolean updatedReadOnly = openSettings.getAsBoolean(IndexMetaData.SETTING_READ_ONLY, null);
                if (updatedReadOnly != null) {
                    for (String index : actualIndices) {
                        if (updatedReadOnly) {
                            blocks.addIndexBlock(index, IndexMetaData.INDEX_READ_ONLY_BLOCK);
                        } else {
                            blocks.removeIndexBlock(index, IndexMetaData.INDEX_READ_ONLY_BLOCK);
                        }
                    }
                }
                Boolean updateMetaDataBlock = openSettings.getAsBoolean(IndexMetaData.SETTING_BLOCKS_METADATA, null);
                if (updateMetaDataBlock != null) {
                    for (String index : actualIndices) {
                        if (updateMetaDataBlock) {
                            blocks.addIndexBlock(index, IndexMetaData.INDEX_METADATA_BLOCK);
                        } else {
                            blocks.removeIndexBlock(index, IndexMetaData.INDEX_METADATA_BLOCK);
                        }
                    }
                }

                Boolean updateWriteBlock = openSettings.getAsBoolean(IndexMetaData.SETTING_BLOCKS_WRITE, null);
                if (updateWriteBlock != null) {
                    for (String index : actualIndices) {
                        if (updateWriteBlock) {
                            blocks.addIndexBlock(index, IndexMetaData.INDEX_WRITE_BLOCK);
                        } else {
                            blocks.removeIndexBlock(index, IndexMetaData.INDEX_WRITE_BLOCK);
                        }
                    }
                }

                Boolean updateReadBlock = openSettings.getAsBoolean(IndexMetaData.SETTING_BLOCKS_READ, null);
                if (updateReadBlock != null) {
                    for (String index : actualIndices) {
                        if (updateReadBlock) {
                            blocks.addIndexBlock(index, IndexMetaData.INDEX_READ_BLOCK);
                        } else {
                            blocks.removeIndexBlock(index, IndexMetaData.INDEX_READ_BLOCK);
                        }
                    }
                }

                if (!openIndices.isEmpty()) {
                    String[] indices = openIndices.toArray(new String[openIndices.size()]);
                    metaDataBuilder.updateSettings(openSettings, indices);
                }

                if (!closeIndices.isEmpty()) {
                    String[] indices = closeIndices.toArray(new String[closeIndices.size()]);
                    metaDataBuilder.updateSettings(closeSettings, indices);
                }


                ClusterState updatedState = ClusterState.builder().state(currentState).metaData(metaDataBuilder).routingTable(routingTableBuilder).blocks(blocks).build();

                // now, reroute in case things change that require it (like number of replicas)
                RoutingAllocation.Result routingResult = allocationService.reroute(updatedState);
                updatedState = newClusterStateBuilder().state(updatedState).routingResult(routingResult).build();

                return updatedState;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5310.java