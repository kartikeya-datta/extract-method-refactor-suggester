error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9731.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9731.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9731.java
text:
```scala
c@@lusterService.addLast(this);

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

package org.elasticsearch.indices.store;

import org.elasticsearch.cluster.ClusterChangedEvent;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterStateListener;
import org.elasticsearch.cluster.routing.IndexRoutingTable;
import org.elasticsearch.cluster.routing.IndexShardRoutingTable;
import org.elasticsearch.cluster.routing.RoutingTable;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.cluster.routing.ShardRoutingState;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.FileSystemUtils;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.NodeEnvironment;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.indices.IndicesService;

import java.io.File;

/**
 * @author kimchy (shay.banon)
 */
public class IndicesStore extends AbstractComponent implements ClusterStateListener {

    private final NodeEnvironment nodeEnv;

    private final IndicesService indicesService;

    private final ClusterService clusterService;

    @Inject public IndicesStore(Settings settings, NodeEnvironment nodeEnv, IndicesService indicesService, ClusterService clusterService) {
        super(settings);
        this.nodeEnv = nodeEnv;
        this.indicesService = indicesService;
        this.clusterService = clusterService;
        clusterService.add(this);
    }

    public void close() {
        clusterService.remove(this);
    }

    @Override public void clusterChanged(ClusterChangedEvent event) {
        if (!event.routingTableChanged()) {
            return;
        }

        if (event.state().blocks().disableStatePersistence()) {
            return;
        }

        // when all shards are started within a shard replication group, delete an unallocated shard on this node
        RoutingTable routingTable = event.state().routingTable();
        for (IndexRoutingTable indexRoutingTable : routingTable) {
            IndexService indexService = indicesService.indexService(indexRoutingTable.index());
            if (indexService == null) {
                // we handle this later...
                continue;
            }
            // if the store is not persistent, don't bother trying to check if it can be deleted
            if (!indexService.store().persistent()) {
                continue;
            }
            for (IndexShardRoutingTable indexShardRoutingTable : indexRoutingTable) {
                // if it has been created on this node, we don't want to delete it
                if (indexService.hasShard(indexShardRoutingTable.shardId().id())) {
                    continue;
                }
                if (!indexService.store().canDeleteUnallocated(indexShardRoutingTable.shardId())) {
                    continue;
                }
                // only delete an unallocated shard if all (other shards) are started
                if (indexShardRoutingTable.countWithState(ShardRoutingState.STARTED) == indexShardRoutingTable.size()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("[{}][{}] deleting unallocated shard", indexShardRoutingTable.shardId().index().name(), indexShardRoutingTable.shardId().id());
                    }
                    try {
                        indexService.store().deleteUnallocated(indexShardRoutingTable.shardId());
                    } catch (Exception e) {
                        logger.debug("[{}][{}] failed to delete unallocated shard, ignoring", e, indexShardRoutingTable.shardId().index().name(), indexShardRoutingTable.shardId().id());
                    }
                }
            }
        }

        // do the reverse, and delete dangling indices / shards that might remain on that node
        // this can happen when deleting a closed index, or when a node joins and it has deleted indices / shards
        if (nodeEnv.hasNodeFile()) {
            // delete unused shards for existing indices
            for (IndexRoutingTable indexRoutingTable : routingTable) {
                IndexService indexService = indicesService.indexService(indexRoutingTable.index());
                if (indexService != null) { // allocated, ignore this
                    continue;
                }
                for (IndexShardRoutingTable indexShardRoutingTable : indexRoutingTable) {
                    boolean shardCanBeDeleted = true;
                    for (ShardRouting shardRouting : indexShardRoutingTable) {
                        // don't delete a shard that not all instances are active
                        if (!shardRouting.active()) {
                            shardCanBeDeleted = false;
                            break;
                        }
                        String localNodeId = clusterService.localNode().id();
                        // check if shard is active on the current node or is getting relocated to the our node
                        if (localNodeId.equals(shardRouting.currentNodeId()) || localNodeId.equals(shardRouting.relocatingNodeId())) {
                            // shard will be used locally - keep it
                            shardCanBeDeleted = false;
                            break;
                        }
                    }
                    if (shardCanBeDeleted) {
                        ShardId shardId = indexShardRoutingTable.shardId();
                        for (File shardLocation : nodeEnv.shardLocations(shardId)) {
                            if (shardLocation.exists()) {
                                logger.debug("[{}][{}] deleting shard that is no longer used", shardId.index().name(), shardId.id());
                                FileSystemUtils.deleteRecursively(shardLocation);
                            }
                        }
                    }
                }
            }

            // delete indices that are no longer part of the metadata
            for (File indicesLocation : nodeEnv.indicesLocations()) {
                File[] files = indicesLocation.listFiles();
                if (files != null) {
                    for (File file : files) {
                        // if we have the index on the metadata, don't delete it
                        if (event.state().metaData().hasIndex(file.getName())) {
                            continue;
                        }
                        logger.debug("[{}] deleting index that is no longer in the cluster meta_date from [{}]", file.getName(), file);
                        FileSystemUtils.deleteRecursively(file);
                    }
                }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9731.java