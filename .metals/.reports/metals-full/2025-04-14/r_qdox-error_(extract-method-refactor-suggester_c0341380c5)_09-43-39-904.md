error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3581.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3581.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3581.java
text:
```scala
e@@xistingMappers.put(index, existingMapper);

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

package org.elasticsearch.cluster.metadata;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.ClusterStateUpdateTask;
import org.elasticsearch.cluster.action.index.NodeIndexCreatedAction;
import org.elasticsearch.cluster.action.index.NodeIndexDeletedAction;
import org.elasticsearch.cluster.action.index.NodeMappingCreatedAction;
import org.elasticsearch.cluster.routing.IndexRoutingTable;
import org.elasticsearch.cluster.routing.RoutingTable;
import org.elasticsearch.cluster.routing.strategy.ShardsRoutingStrategy;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.mapper.DocumentMapper;
import org.elasticsearch.index.mapper.InvalidTypeNameException;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.mapper.MergeMappingException;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.indices.InvalidIndexNameException;
import org.elasticsearch.util.Strings;
import org.elasticsearch.util.TimeValue;
import org.elasticsearch.util.Tuple;
import org.elasticsearch.util.component.AbstractComponent;
import org.elasticsearch.util.settings.ImmutableSettings;
import org.elasticsearch.util.settings.Settings;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.google.common.collect.Maps.*;
import static org.elasticsearch.cluster.ClusterState.*;
import static org.elasticsearch.cluster.metadata.IndexMetaData.*;
import static org.elasticsearch.cluster.metadata.MetaData.*;
import static org.elasticsearch.index.mapper.DocumentMapper.MergeFlags.*;
import static org.elasticsearch.util.settings.ImmutableSettings.*;

/**
 * @author kimchy (Shay Banon)
 */
public class MetaDataService extends AbstractComponent {

    private final ClusterService clusterService;

    private final ShardsRoutingStrategy shardsRoutingStrategy;

    private final IndicesService indicesService;

    private final NodeIndexCreatedAction nodeIndexCreatedAction;

    private final NodeIndexDeletedAction nodeIndexDeletedAction;

    private final NodeMappingCreatedAction nodeMappingCreatedAction;

    @Inject public MetaDataService(Settings settings, ClusterService clusterService, IndicesService indicesService, ShardsRoutingStrategy shardsRoutingStrategy,
                                   NodeIndexCreatedAction nodeIndexCreatedAction, NodeIndexDeletedAction nodeIndexDeletedAction,
                                   NodeMappingCreatedAction nodeMappingCreatedAction) {
        super(settings);
        this.clusterService = clusterService;
        this.indicesService = indicesService;
        this.shardsRoutingStrategy = shardsRoutingStrategy;
        this.nodeIndexCreatedAction = nodeIndexCreatedAction;
        this.nodeIndexDeletedAction = nodeIndexDeletedAction;
        this.nodeMappingCreatedAction = nodeMappingCreatedAction;
    }

    // TODO should find nicer solution than sync here, since we block for timeout (same for other ops)

    public synchronized CreateIndexResult createIndex(final String index, final Settings indexSettings, TimeValue timeout) throws IndexAlreadyExistsException {
        if (clusterService.state().routingTable().hasIndex(index)) {
            throw new IndexAlreadyExistsException(new Index(index));
        }
        if (index.contains(" ")) {
            throw new InvalidIndexNameException(new Index(index), index, "must not contain whitespace");
        }
        if (index.contains(",")) {
            throw new InvalidIndexNameException(new Index(index), index, "must not contain ',");
        }
        if (index.contains("#")) {
            throw new InvalidIndexNameException(new Index(index), index, "must not contain '#");
        }
        if (index.charAt(0) == '_') {
            throw new InvalidIndexNameException(new Index(index), index, "must not start with '_'");
        }
        if (!index.toLowerCase().equals(index)) {
            throw new InvalidIndexNameException(new Index(index), index, "must be lowercase");
        }
        if (!Strings.validFileName(index)) {
            throw new InvalidIndexNameException(new Index(index), index, "must not contain the following characters " + Strings.INVALID_FILENAME_CHARS);
        }

        final CountDownLatch latch = new CountDownLatch(clusterService.state().nodes().size());
        NodeIndexCreatedAction.Listener nodeCreatedListener = new NodeIndexCreatedAction.Listener() {
            @Override public void onNodeIndexCreated(String mIndex, String nodeId) {
                if (index.equals(mIndex)) {
                    latch.countDown();
                }
            }
        };
        nodeIndexCreatedAction.add(nodeCreatedListener);
        clusterService.submitStateUpdateTask("create-index [" + index + "]", new ClusterStateUpdateTask() {
            @Override public ClusterState execute(ClusterState currentState) {
                RoutingTable.Builder routingTableBuilder = new RoutingTable.Builder();
                for (IndexRoutingTable indexRoutingTable : currentState.routingTable().indicesRouting().values()) {
                    routingTableBuilder.add(indexRoutingTable);
                }
                ImmutableSettings.Builder indexSettingsBuilder = settingsBuilder().putAll(indexSettings);
                if (indexSettings.get(SETTING_NUMBER_OF_SHARDS) == null) {
                    indexSettingsBuilder.putInt(SETTING_NUMBER_OF_SHARDS, settings.getAsInt(SETTING_NUMBER_OF_SHARDS, 5));
                }
                if (indexSettings.get(SETTING_NUMBER_OF_REPLICAS) == null) {
                    indexSettingsBuilder.putInt(SETTING_NUMBER_OF_REPLICAS, settings.getAsInt(SETTING_NUMBER_OF_REPLICAS, 1));
                }
                Settings actualIndexSettings = indexSettingsBuilder.build();

                IndexMetaData indexMetaData = newIndexMetaDataBuilder(index).settings(actualIndexSettings).build();
                MetaData newMetaData = newMetaDataBuilder()
                        .metaData(currentState.metaData())
                        .put(indexMetaData)
                        .build();

                IndexRoutingTable.Builder indexRoutingBuilder = new IndexRoutingTable.Builder(index)
                        .initializeEmpty(newMetaData.index(index));
                routingTableBuilder.add(indexRoutingBuilder);

                logger.info("Creating Index [{}], shards [{}]/[{}]", new Object[]{index, indexMetaData.numberOfShards(), indexMetaData.numberOfReplicas()});
                RoutingTable newRoutingTable = shardsRoutingStrategy.reroute(newClusterStateBuilder().state(currentState).routingTable(routingTableBuilder).metaData(newMetaData).build());
                return newClusterStateBuilder().state(currentState).routingTable(newRoutingTable).metaData(newMetaData).build();
            }
        });

        boolean acknowledged;
        try {
            acknowledged = latch.await(timeout.millis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            acknowledged = false;
        } finally {
            nodeIndexCreatedAction.remove(nodeCreatedListener);
        }
        return new CreateIndexResult(acknowledged);
    }

    public synchronized DeleteIndexResult deleteIndex(final String index, TimeValue timeout) throws IndexMissingException {
        RoutingTable routingTable = clusterService.state().routingTable();
        if (!routingTable.hasIndex(index)) {
            throw new IndexMissingException(new Index(index));
        }

        logger.info("Deleting index [{}]", index);

        final CountDownLatch latch = new CountDownLatch(clusterService.state().nodes().size());
        NodeIndexDeletedAction.Listener listener = new NodeIndexDeletedAction.Listener() {
            @Override public void onNodeIndexDeleted(String fIndex, String nodeId) {
                if (fIndex.equals(index)) {
                    latch.countDown();
                }
            }
        };
        nodeIndexDeletedAction.add(listener);
        clusterService.submitStateUpdateTask("delete-index [" + index + "]", new ClusterStateUpdateTask() {
            @Override public ClusterState execute(ClusterState currentState) {
                RoutingTable.Builder routingTableBuilder = new RoutingTable.Builder();
                for (IndexRoutingTable indexRoutingTable : currentState.routingTable().indicesRouting().values()) {
                    if (!indexRoutingTable.index().equals(index)) {
                        routingTableBuilder.add(indexRoutingTable);
                    }
                }
                MetaData newMetaData = newMetaDataBuilder()
                        .metaData(currentState.metaData())
                        .remove(index)
                        .build();

                RoutingTable newRoutingTable = shardsRoutingStrategy.reroute(
                        newClusterStateBuilder().state(currentState).routingTable(routingTableBuilder).metaData(newMetaData).build());
                return newClusterStateBuilder().state(currentState).routingTable(newRoutingTable).metaData(newMetaData).build();
            }
        });
        boolean acknowledged;
        try {
            acknowledged = latch.await(timeout.millis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            acknowledged = false;
        } finally {
            nodeIndexDeletedAction.remove(listener);
        }
        return new DeleteIndexResult(acknowledged);
    }

    public synchronized void updateMapping(final String index, final String type, final String mappingSource) {
        MapperService mapperService = indicesService.indexServiceSafe(index).mapperService();

        DocumentMapper existingMapper = mapperService.documentMapper(type);
        // parse the updated one
        DocumentMapper updatedMapper = mapperService.parse(type, mappingSource);
        if (existingMapper == null) {
            existingMapper = updatedMapper;
        } else {
            // merge from the updated into the existing, ignore conflicts (we know we have them, we just want the new ones)
            existingMapper.merge(updatedMapper, mergeFlags().simulate(false));
        }
        // build the updated mapping source
        final String updatedMappingSource = existingMapper.buildSource();
        logger.info("Index [" + index + "]: Update mapping [" + type + "] (dynamic) with source [" + updatedMappingSource + "]");
        // publish the new mapping
        clusterService.submitStateUpdateTask("update-mapping [" + index + "][" + type + "]", new ClusterStateUpdateTask() {
            @Override public ClusterState execute(ClusterState currentState) {
                MetaData.Builder builder = newMetaDataBuilder().metaData(currentState.metaData());
                IndexMetaData indexMetaData = currentState.metaData().index(index);
                builder.put(newIndexMetaDataBuilder(indexMetaData).putMapping(type, updatedMappingSource));
                return newClusterStateBuilder().state(currentState).metaData(builder).build();
            }
        });
    }

    public synchronized PutMappingResult putMapping(final String[] indices, String mappingType, final String mappingSource, boolean ignoreConflicts, TimeValue timeout) throws ElasticSearchException {
        ClusterState clusterState = clusterService.state();
        for (String index : indices) {
            IndexRoutingTable indexTable = clusterState.routingTable().indicesRouting().get(index);
            if (indexTable == null) {
                throw new IndexMissingException(new Index(index));
            }
        }

        Map<String, DocumentMapper> newMappers = newHashMap();
        Map<String, DocumentMapper> existingMappers = newHashMap();
        for (String index : indices) {
            IndexService indexService = indicesService.indexService(index);
            if (indexService != null) {
                // try and parse it (no need to add it here) so we can bail early in case of parsing exception
                DocumentMapper newMapper = indexService.mapperService().parse(mappingType, mappingSource);
                newMappers.put(index, newMapper);
                DocumentMapper existingMapper = indexService.mapperService().documentMapper(mappingType);
                if (existingMapper != null) {
                    // first, simulate
                    DocumentMapper.MergeResult mergeResult = existingMapper.merge(newMapper, mergeFlags().simulate(true));
                    // if we have conflicts, and we are not supposed to ignore them, throw an exception
                    if (!ignoreConflicts && mergeResult.hasConflicts()) {
                        throw new MergeMappingException(mergeResult.conflicts());
                    }
                    existingMappers.put(index, newMapper);
                }
            } else {
                throw new IndexMissingException(new Index(index));
            }
        }

        if (mappingType == null) {
            mappingType = newMappers.values().iterator().next().type();
        } else if (!mappingType.equals(newMappers.values().iterator().next().type())) {
            throw new InvalidTypeNameException("Type name provided does not match type name within mapping definition");
        }
        if (mappingType.charAt(0) == '_') {
            throw new InvalidTypeNameException("Document mapping type name can't start with '_'");
        }

        final Map<String, Tuple<String, String>> mappings = newHashMap();
        for (Map.Entry<String, DocumentMapper> entry : newMappers.entrySet()) {
            Tuple<String, String> mapping;
            String index = entry.getKey();
            // do the actual merge here on the master, and update the mapping source
            DocumentMapper newMapper = entry.getValue();
            if (existingMappers.containsKey(entry.getKey())) {
                // we have an existing mapping, do the merge here (on the master), it will automatically update the mapping source
                DocumentMapper existingMapper = existingMappers.get(entry.getKey());
                existingMapper.merge(newMapper, mergeFlags().simulate(false));
                // use the merged mapping source
                mapping = new Tuple<String, String>(existingMapper.type(), existingMapper.buildSource());
            } else {
                mapping = new Tuple<String, String>(newMapper.type(), newMapper.buildSource());
            }
            mappings.put(index, mapping);
            logger.info("Index [" + index + "]: Put mapping [" + mapping.v1() + "] with source [" + mapping.v2() + "]");
        }

        final CountDownLatch latch = new CountDownLatch(clusterService.state().nodes().size() * indices.length);
        final Set<String> indicesSet = Sets.newHashSet(indices);
        final String fMappingType = mappingType;
        NodeMappingCreatedAction.Listener listener = new NodeMappingCreatedAction.Listener() {
            @Override public void onNodeMappingCreated(NodeMappingCreatedAction.NodeMappingCreatedResponse response) {
                if (indicesSet.contains(response.index()) && response.type().equals(fMappingType)) {
                    latch.countDown();
                }
            }
        };
        nodeMappingCreatedAction.add(listener);

        clusterService.submitStateUpdateTask("put-mapping [" + mappingType + "]", new ClusterStateUpdateTask() {
            @Override public ClusterState execute(ClusterState currentState) {
                MetaData.Builder builder = newMetaDataBuilder().metaData(currentState.metaData());
                for (String indexName : indices) {
                    IndexMetaData indexMetaData = currentState.metaData().index(indexName);
                    if (indexMetaData == null) {
                        throw new IndexMissingException(new Index(indexName));
                    }
                    Tuple<String, String> mapping = mappings.get(indexName);
                    builder.put(newIndexMetaDataBuilder(indexMetaData).putMapping(mapping.v1(), mapping.v2()));
                }
                return newClusterStateBuilder().state(currentState).metaData(builder).build();
            }
        });

        boolean acknowledged;
        try {
            acknowledged = latch.await(timeout.millis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            acknowledged = false;
        } finally {
            nodeMappingCreatedAction.remove(listener);
        }

        return new PutMappingResult(acknowledged);
    }

    /**
     * The result of a putting mapping.
     */
    public static class PutMappingResult {

        private final boolean acknowledged;

        public PutMappingResult(boolean acknowledged) {
            this.acknowledged = acknowledged;
        }

        public boolean acknowledged() {
            return acknowledged;
        }
    }

    public static class CreateIndexResult {

        private final boolean acknowledged;

        public CreateIndexResult(boolean acknowledged) {
            this.acknowledged = acknowledged;
        }

        public boolean acknowledged() {
            return acknowledged;
        }
    }

    public static class DeleteIndexResult {

        private final boolean acknowledged;

        public DeleteIndexResult(boolean acknowledged) {
            this.acknowledged = acknowledged;
        }

        public boolean acknowledged() {
            return acknowledged;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3581.java