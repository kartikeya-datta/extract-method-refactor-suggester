error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6578.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6578.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6578.java
text:
```scala
E@@ngineException[] failures = indexShard.bulk(new Engine.Bulk(ops).refresh(request.refresh()));

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

package org.elasticsearch.action.bulk;

import org.elasticsearch.ExceptionsHelper;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.RoutingMissingException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.TransportShardReplicationOperationAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.action.index.MappingUpdatedAction;
import org.elasticsearch.cluster.action.shard.ShardStateAction;
import org.elasticsearch.cluster.block.ClusterBlockLevel;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.cluster.routing.ShardIterator;
import org.elasticsearch.common.collect.Sets;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.engine.EngineException;
import org.elasticsearch.index.mapper.DocumentMapper;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.mapper.ParsedDocument;
import org.elasticsearch.index.mapper.SourceToParse;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.shard.service.IndexShard;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportRequestOptions;
import org.elasticsearch.transport.TransportService;

import java.io.IOException;
import java.util.Set;

/**
 * Performs the index operation.
 *
 * @author kimchy (shay.banon)
 */
public class TransportShardBulkAction extends TransportShardReplicationOperationAction<BulkShardRequest, BulkShardResponse> {

    private final MappingUpdatedAction mappingUpdatedAction;

    @Inject public TransportShardBulkAction(Settings settings, TransportService transportService, ClusterService clusterService,
                                            IndicesService indicesService, ThreadPool threadPool, ShardStateAction shardStateAction,
                                            MappingUpdatedAction mappingUpdatedAction) {
        super(settings, transportService, clusterService, indicesService, threadPool, shardStateAction);
        this.mappingUpdatedAction = mappingUpdatedAction;
    }

    @Override protected boolean checkWriteConsistency() {
        return true;
    }

    @Override protected TransportRequestOptions transportOptions() {
        return TransportRequestOptions.options().withCompress(true);
    }

    @Override protected BulkShardRequest newRequestInstance() {
        return new BulkShardRequest();
    }

    @Override protected BulkShardResponse newResponseInstance() {
        return new BulkShardResponse();
    }

    @Override protected String transportAction() {
        return "indices/index/shard/bulk";
    }

    @Override protected void checkBlock(BulkShardRequest request, ClusterState state) {
        state.blocks().indexBlockedRaiseException(ClusterBlockLevel.WRITE, request.index());
    }

    @Override protected ShardIterator shards(ClusterState clusterState, BulkShardRequest request) {
        return clusterState.routingTable().index(request.index()).shard(request.shardId()).shardsIt();
    }

    @Override protected BulkShardResponse shardOperationOnPrimary(ClusterState clusterState, ShardOperationRequest shardRequest) {
        IndexShard indexShard = indexShard(shardRequest);
        final BulkShardRequest request = shardRequest.request;
        BulkItemResponse[] responses = new BulkItemResponse[request.items().length];
        Engine.Operation[] ops = new Engine.Operation[request.items().length];
        for (int i = 0; i < ops.length; i++) {
            BulkItemRequest item = request.items()[i];
            if (item.request() instanceof IndexRequest) {
                IndexRequest indexRequest = (IndexRequest) item.request();
                try {

                    // validate, if routing is required, that we got routing
                    MappingMetaData mappingMd = clusterState.metaData().index(request.index()).mapping(indexRequest.type());
                    if (mappingMd != null && mappingMd.routing().required()) {
                        if (indexRequest.routing() == null) {
                            throw new RoutingMissingException(indexRequest.index(), indexRequest.type(), indexRequest.id());
                        }
                    }

                    SourceToParse sourceToParse = SourceToParse.source(indexRequest.source()).type(indexRequest.type()).id(indexRequest.id()).routing(indexRequest.routing());
                    if (indexRequest.opType() == IndexRequest.OpType.INDEX) {
                        ops[i] = indexShard.prepareIndex(sourceToParse);
                    } else {
                        ops[i] = indexShard.prepareCreate(sourceToParse);
                    }
                } catch (Exception e) {
                    responses[i] = new BulkItemResponse(item.id(), indexRequest.opType().toString().toLowerCase(),
                            new BulkItemResponse.Failure(indexRequest.index(), indexRequest.type(), indexRequest.id(), ExceptionsHelper.detailedMessage(e)));
                }
            } else if (item.request() instanceof DeleteRequest) {
                DeleteRequest deleteRequest = (DeleteRequest) item.request();
                try {
                    ops[i] = indexShard.prepareDelete(deleteRequest.type(), deleteRequest.id());
                } catch (Exception e) {
                    responses[i] = new BulkItemResponse(item.id(), "delete",
                            new BulkItemResponse.Failure(deleteRequest.index(), deleteRequest.type(), deleteRequest.id(), ExceptionsHelper.detailedMessage(e)));
                }
            }
        }

        EngineException[] failures = indexShard.bulk(new Engine.Bulk(ops));
        // process failures and mappings
        Set<String> processedTypes = Sets.newHashSet();
        for (int i = 0; i < ops.length; i++) {
            // failed to parse, already set the failure, skip
            if (ops[i] == null) {
                continue;
            }

            BulkItemRequest item = request.items()[i];
            if (item.request() instanceof IndexRequest) {
                IndexRequest indexRequest = (IndexRequest) item.request();
                if (indexRequest.opType() == IndexRequest.OpType.INDEX) {
                    Engine.Index engineIndex = (Engine.Index) ops[i];
                    if (!processedTypes.contains(engineIndex.type())) {
                        processedTypes.add(engineIndex.type());
                        ParsedDocument doc = engineIndex.parsedDoc();
                        if (doc.mappersAdded()) {
                            updateMappingOnMaster(indexRequest);
                        }
                    }
                } else {
                    Engine.Create engineCreate = (Engine.Create) ops[i];
                    if (!processedTypes.contains(engineCreate.type())) {
                        processedTypes.add(engineCreate.type());
                        ParsedDocument doc = engineCreate.parsedDoc();
                        if (doc.mappersAdded()) {
                            updateMappingOnMaster(indexRequest);
                        }
                    }
                }
                if (failures != null && failures[i] != null) {
                    responses[i] = new BulkItemResponse(item.id(), indexRequest.opType().toString().toLowerCase(),
                            new BulkItemResponse.Failure(indexRequest.index(), indexRequest.type(), indexRequest.id(), ExceptionsHelper.detailedMessage(failures[i])));
                } else {
                    responses[i] = new BulkItemResponse(item.id(), indexRequest.opType().toString().toLowerCase(),
                            new IndexResponse(indexRequest.index(), indexRequest.type(), indexRequest.id()));
                }
            } else if (item.request() instanceof DeleteRequest) {
                DeleteRequest deleteRequest = (DeleteRequest) item.request();
                if (failures != null && failures[i] != null) {
                    responses[i] = new BulkItemResponse(item.id(), "delete",
                            new BulkItemResponse.Failure(deleteRequest.index(), deleteRequest.type(), deleteRequest.id(), ExceptionsHelper.detailedMessage(failures[i])));
                } else {
                    responses[i] = new BulkItemResponse(item.id(), "delete",
                            new DeleteResponse(deleteRequest.index(), deleteRequest.type(), deleteRequest.id()));
                }
            }
        }
        return new BulkShardResponse(new ShardId(request.index(), request.shardId()), responses);
    }

    @Override protected void shardOperationOnReplica(ShardOperationRequest shardRequest) {
        IndexShard indexShard = indexShard(shardRequest);
        final BulkShardRequest request = shardRequest.request;
        Engine.Operation[] ops = new Engine.Operation[request.items().length];
        for (int i = 0; i < ops.length; i++) {
            BulkItemRequest item = request.items()[i];
            if (item.request() instanceof IndexRequest) {
                IndexRequest indexRequest = (IndexRequest) item.request();
                try {
                    SourceToParse sourceToParse = SourceToParse.source(indexRequest.source()).type(indexRequest.type()).id(indexRequest.id()).routing(indexRequest.routing());
                    if (indexRequest.opType() == IndexRequest.OpType.INDEX) {
                        ops[i] = indexShard.prepareIndex(sourceToParse);
                    } else {
                        ops[i] = indexShard.prepareCreate(sourceToParse);
                    }
                } catch (Exception e) {
                    // ignore, we are on backup
                }
            } else if (item.request() instanceof DeleteRequest) {
                DeleteRequest deleteRequest = (DeleteRequest) item.request();
                try {
                    ops[i] = indexShard.prepareDelete(deleteRequest.type(), deleteRequest.id());
                } catch (Exception e) {
                    // ignore, we are on backup
                }
            }
        }

        indexShard.bulk(new Engine.Bulk(ops));
    }

    private void updateMappingOnMaster(final IndexRequest request) {
        try {
            MapperService mapperService = indicesService.indexServiceSafe(request.index()).mapperService();
            final DocumentMapper documentMapper = mapperService.documentMapper(request.type());
            documentMapper.refreshSource();

            mappingUpdatedAction.execute(new MappingUpdatedAction.MappingUpdatedRequest(request.index(), request.type(), documentMapper.mappingSource()), new ActionListener<MappingUpdatedAction.MappingUpdatedResponse>() {
                @Override public void onResponse(MappingUpdatedAction.MappingUpdatedResponse mappingUpdatedResponse) {
                    // all is well
                }

                @Override public void onFailure(Throwable e) {
                    try {
                        logger.warn("Failed to update master on updated mapping for index [" + request.index() + "], type [" + request.type() + "] and source [" + documentMapper.mappingSource().string() + "]", e);
                    } catch (IOException e1) {
                        // ignore
                    }
                }
            });
        } catch (Exception e) {
            logger.warn("Failed to update master on updated mapping for index [" + request.index() + "], type [" + request.type() + "]", e);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6578.java