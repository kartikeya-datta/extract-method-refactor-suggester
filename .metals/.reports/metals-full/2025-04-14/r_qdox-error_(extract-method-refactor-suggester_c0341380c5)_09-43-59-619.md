error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2223.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2223.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2223.java
text:
```scala
r@@eturn indexShard.primaryActiveShardIt();

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

package org.elasticsearch.cluster.routing.operation.plain;

import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.cluster.routing.GroupShardsIterator;
import org.elasticsearch.cluster.routing.IndexRoutingTable;
import org.elasticsearch.cluster.routing.IndexShardRoutingTable;
import org.elasticsearch.cluster.routing.ShardIterator;
import org.elasticsearch.cluster.routing.allocation.decider.AwarenessAllocationDecider;
import org.elasticsearch.cluster.routing.operation.OperationRouting;
import org.elasticsearch.cluster.routing.operation.hash.HashFunction;
import org.elasticsearch.cluster.routing.operation.hash.djb.DjbHashFunction;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.IndexShardMissingException;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.indices.IndexMissingException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class PlainOperationRouting extends AbstractComponent implements OperationRouting {

    private final HashFunction hashFunction;

    private final boolean useType;

    private final AwarenessAllocationDecider awarenessAllocationDecider;

    @Inject
    public PlainOperationRouting(Settings indexSettings, HashFunction hashFunction, AwarenessAllocationDecider awarenessAllocationDecider) {
        super(indexSettings);
        this.hashFunction = hashFunction;
        this.useType = indexSettings.getAsBoolean("cluster.routing.operation.use_type", false);
        this.awarenessAllocationDecider = awarenessAllocationDecider;
    }

    @Override
    public ShardIterator indexShards(ClusterState clusterState, String index, String type, String id, @Nullable String routing) throws IndexMissingException, IndexShardMissingException {
        return shards(clusterState, index, type, id, routing).shardsIt();
    }

    @Override
    public ShardIterator deleteShards(ClusterState clusterState, String index, String type, String id, @Nullable String routing) throws IndexMissingException, IndexShardMissingException {
        return shards(clusterState, index, type, id, routing).shardsIt();
    }

    @Override
    public ShardIterator getShards(ClusterState clusterState, String index, String type, String id, @Nullable String routing, @Nullable String preference) throws IndexMissingException, IndexShardMissingException {
        return preferenceActiveShardIterator(shards(clusterState, index, type, id, routing), clusterState.nodes().localNodeId(), clusterState.nodes(), preference);
    }

    @Override
    public ShardIterator getShards(ClusterState clusterState, String index, int shardId, @Nullable String preference) throws IndexMissingException, IndexShardMissingException {
        return preferenceActiveShardIterator(shards(clusterState, index, shardId), clusterState.nodes().localNodeId(), clusterState.nodes(), preference);
    }

    @Override
    public GroupShardsIterator broadcastDeleteShards(ClusterState clusterState, String index) throws IndexMissingException {
        return indexRoutingTable(clusterState, index).groupByShardsIt();
    }

    @Override
    public GroupShardsIterator deleteByQueryShards(ClusterState clusterState, String index, @Nullable Set<String> routing) throws IndexMissingException {
        if (routing == null || routing.isEmpty()) {
            return indexRoutingTable(clusterState, index).groupByShardsIt();
        }

        // we use set here and not identity set since we might get duplicates
        HashSet<ShardIterator> set = new HashSet<ShardIterator>();
        IndexRoutingTable indexRouting = indexRoutingTable(clusterState, index);
        for (String r : routing) {
            int shardId = shardId(clusterState, index, null, null, r);
            IndexShardRoutingTable indexShard = indexRouting.shard(shardId);
            if (indexShard == null) {
                throw new IndexShardMissingException(new ShardId(index, shardId));
            }
            set.add(indexShard.shardsRandomIt());
        }
        return new GroupShardsIterator(set);
    }

    @Override
    public int searchShardsCount(ClusterState clusterState, String[] indices, String[] concreteIndices, @Nullable Map<String, Set<String>> routing, @Nullable String preference) throws IndexMissingException {
        if (concreteIndices == null || concreteIndices.length == 0) {
            concreteIndices = clusterState.metaData().concreteAllOpenIndices();
        }
        if (routing != null) {
            HashSet<ShardId> set = new HashSet<ShardId>();
            for (String index : concreteIndices) {
                IndexRoutingTable indexRouting = indexRoutingTable(clusterState, index);
                Set<String> effectiveRouting = routing.get(index);
                if (effectiveRouting != null) {
                    for (String r : effectiveRouting) {
                        int shardId = shardId(clusterState, index, null, null, r);
                        IndexShardRoutingTable indexShard = indexRouting.shard(shardId);
                        if (indexShard == null) {
                            throw new IndexShardMissingException(new ShardId(index, shardId));
                        }
                        // we might get duplicates, but that's ok, its an estimated count? (we just want to know if its 1 or not)
                        set.add(indexShard.shardId());
                    }
                }
            }
            return set.size();
        } else {
            // we use list here since we know we are not going to create duplicates
            int count = 0;
            for (String index : concreteIndices) {
                IndexRoutingTable indexRouting = indexRoutingTable(clusterState, index);
                count += indexRouting.shards().size();
            }
            return count;
        }
    }
    
    private static final Map<String, Set<String>> EMPTY_ROUTING = Collections.emptyMap();

    @Override
    public GroupShardsIterator searchShards(ClusterState clusterState, String[] indices, String[] concreteIndices, @Nullable Map<String, Set<String>> routing, @Nullable String preference) throws IndexMissingException {
        if (concreteIndices == null || concreteIndices.length == 0) {
            concreteIndices = clusterState.metaData().concreteAllOpenIndices();
        }
        routing = routing == null ? EMPTY_ROUTING : routing; // just use an empty map 
        final Set<ShardIterator> set = new HashSet<ShardIterator>();
            // we use set here and not list since we might get duplicates
            for (String index : concreteIndices) {
                final IndexRoutingTable indexRouting = indexRoutingTable(clusterState, index);
                final Set<String> effectiveRouting = routing.get(index);
                if (effectiveRouting != null) {
                    for (String r : effectiveRouting) {
                        int shardId = shardId(clusterState, index, null, null, r);
                        IndexShardRoutingTable indexShard = indexRouting.shard(shardId);
                        if (indexShard == null) {
                            throw new IndexShardMissingException(new ShardId(index, shardId));
                        }
                        // we might get duplicates, but that's ok, they will override one another
                        ShardIterator iterator = preferenceActiveShardIterator(indexShard, clusterState.nodes().localNodeId(), clusterState.nodes(), preference);
                        if (iterator != null) {
                            set.add(iterator);
                        }
                    }
                } else {
                    for (IndexShardRoutingTable indexShard : indexRouting) {
                        ShardIterator iterator = preferenceActiveShardIterator(indexShard, clusterState.nodes().localNodeId(), clusterState.nodes(), preference);
                        if (iterator != null) {
                            set.add(iterator);
                        }
                    }
                }
            }
        return new GroupShardsIterator(set);
    }

    private ShardIterator preferenceActiveShardIterator(IndexShardRoutingTable indexShard, String localNodeId, DiscoveryNodes nodes, @Nullable String preference) {
        if (preference == null) {
            String[] awarenessAttributes = awarenessAllocationDecider.awarenessAttributes();
            if (awarenessAttributes.length == 0) {
                return indexShard.activeShardsRandomIt();
            } else {
                return indexShard.preferAttributesActiveShardsIt(awarenessAttributes, nodes);
            }
        }
        if (preference.charAt(0) == '_') {
            if (preference.startsWith("_shards:")) {
                // starts with _shards, so execute on specific ones
                int index = preference.indexOf(';');
                String shards;
                if (index == -1) {
                    shards = preference.substring("_shards:".length());
                } else {
                    shards = preference.substring("_shards:".length(), index);
                }
                String[] ids = Strings.splitStringByCommaToArray(shards);
                boolean found = false;
                for (String id : ids) {
                    if (Integer.parseInt(id) == indexShard.shardId().id()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return null;
                }
                // no more preference
                if (index == -1 || index == preference.length() - 1) {
                    String[] awarenessAttributes = awarenessAllocationDecider.awarenessAttributes();
                    if (awarenessAttributes.length == 0) {
                        return indexShard.activeShardsRandomIt();
                    } else {
                        return indexShard.preferAttributesActiveShardsIt(awarenessAttributes, nodes);
                    }
                } else {
                    // update the preference and continue
                    preference = preference.substring(index + 1);
                }
            }
            if (preference.startsWith("_prefer_node:")) {
                return indexShard.preferNodeActiveShardsIt(preference.substring("_prefer_node:".length()));
            }
            if ("_local".equals(preference)) {
                return indexShard.preferNodeActiveShardsIt(localNodeId);
            }
            if ("_primary".equals(preference)) {
                return indexShard.primaryShardIt();
            }
            if ("_primary_first".equals(preference) || "_primaryFirst".equals(preference)) {
                return indexShard.primaryFirstActiveShardsIt();
            }
            if ("_only_local".equals(preference) || "_onlyLocal".equals(preference)) {
                return indexShard.onlyNodeActiveShardsIt(localNodeId);
            }
            if (preference.startsWith("_only_node:")) {
                return indexShard.onlyNodeActiveShardsIt(preference.substring("_only_node:".length()));
            }
        }
        // if not, then use it as the index
        String[] awarenessAttributes = awarenessAllocationDecider.awarenessAttributes();
        if (awarenessAttributes.length == 0) {
            return indexShard.activeShardsIt(DjbHashFunction.DJB_HASH(preference));
        } else {
            return indexShard.preferAttributesActiveShardsIt(awarenessAttributes, nodes, DjbHashFunction.DJB_HASH(preference));
        }
    }

    public IndexMetaData indexMetaData(ClusterState clusterState, String index) {
        IndexMetaData indexMetaData = clusterState.metaData().index(index);
        if (indexMetaData == null) {
            throw new IndexMissingException(new Index(index));
        }
        return indexMetaData;
    }

    protected IndexRoutingTable indexRoutingTable(ClusterState clusterState, String index) {
        IndexRoutingTable indexRouting = clusterState.routingTable().index(index);
        if (indexRouting == null) {
            throw new IndexMissingException(new Index(index));
        }
        return indexRouting;
    }


    // either routing is set, or type/id are set

    protected IndexShardRoutingTable shards(ClusterState clusterState, String index, String type, String id, String routing) {
        int shardId = shardId(clusterState, index, type, id, routing);
        return shards(clusterState, index, shardId);
    }

    protected IndexShardRoutingTable shards(ClusterState clusterState, String index, int shardId) {
        IndexShardRoutingTable indexShard = indexRoutingTable(clusterState, index).shard(shardId);
        if (indexShard == null) {
            throw new IndexShardMissingException(new ShardId(index, shardId));
        }
        return indexShard;
    }

    private int shardId(ClusterState clusterState, String index, String type, @Nullable String id, @Nullable String routing) {
        if (routing == null) {
            if (!useType) {
                return Math.abs(hash(id) % indexMetaData(clusterState, index).numberOfShards());
            } else {
                return Math.abs(hash(type, id) % indexMetaData(clusterState, index).numberOfShards());
            }
        }
        return Math.abs(hash(routing) % indexMetaData(clusterState, index).numberOfShards());
    }

    protected int hash(String routing) {
        return hashFunction.hash(routing);
    }

    protected int hash(String type, String id) {
        if (type == null || "_all".equals(type)) {
            throw new ElasticSearchIllegalArgumentException("Can't route an operation with no type and having type part of the routing (for backward comp)");
        }
        return hashFunction.hash(type, id);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2223.java