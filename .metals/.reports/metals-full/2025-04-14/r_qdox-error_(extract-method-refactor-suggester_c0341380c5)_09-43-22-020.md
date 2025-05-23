error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5066.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5066.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5066.java
text:
```scala
G@@roupShardsIterator group = clusterService.operationRouting().deleteByQueryShards(clusterService.state(), request.index());

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

package org.elasticsearch.action.deletebyquery;

import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.action.support.replication.TransportShardReplicationOperationAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.action.shard.ShardStateAction;
import org.elasticsearch.cluster.block.ClusterBlockLevel;
import org.elasticsearch.cluster.routing.GroupShardsIterator;
import org.elasticsearch.cluster.routing.ShardsIterator;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

/**
 * @author kimchy (Shay Banon)
 */
public class TransportShardDeleteByQueryAction extends TransportShardReplicationOperationAction<ShardDeleteByQueryRequest, ShardDeleteByQueryResponse> {

    @Inject public TransportShardDeleteByQueryAction(Settings settings, TransportService transportService,
                                                     ClusterService clusterService, IndicesService indicesService, ThreadPool threadPool,
                                                     ShardStateAction shardStateAction) {
        super(settings, transportService, clusterService, indicesService, threadPool, shardStateAction);
    }

    @Override protected ShardDeleteByQueryRequest newRequestInstance() {
        return new ShardDeleteByQueryRequest();
    }

    @Override protected ShardDeleteByQueryResponse newResponseInstance() {
        return new ShardDeleteByQueryResponse();
    }

    @Override protected String transportAction() {
        return "indices/index/shard/deleteByQuery";
    }

    @Override protected void checkBlock(ShardDeleteByQueryRequest request, ClusterState state) {
        state.blocks().indexBlockedRaiseException(ClusterBlockLevel.WRITE, request.index());
    }

    @Override protected ShardDeleteByQueryResponse shardOperationOnPrimary(ShardOperationRequest shardRequest) {
        ShardDeleteByQueryRequest request = shardRequest.request;
        indexShard(shardRequest).deleteByQuery(request.querySource(), request.queryParserName(), request.types());
        return new ShardDeleteByQueryResponse();
    }

    @Override protected void shardOperationOnReplica(ShardOperationRequest shardRequest) {
        ShardDeleteByQueryRequest request = shardRequest.request;
        indexShard(shardRequest).deleteByQuery(request.querySource(), request.queryParserName(), request.types());
    }

    @Override protected ShardsIterator shards(ClusterState clusterState, ShardDeleteByQueryRequest request) {
        GroupShardsIterator group = indicesService.indexServiceSafe(request.index()).operationRouting().deleteByQueryShards(clusterService.state());
        for (ShardsIterator shards : group) {
            if (shards.shardId().id() == request.shardId()) {
                return shards;
            }
        }
        throw new ElasticSearchIllegalStateException("No shards iterator found for shard [" + request.shardId() + "]");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5066.java