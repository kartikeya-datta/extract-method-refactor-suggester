error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7541.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7541.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7541.java
text:
```scala
i@@ndexRequest.id(UUID.randomBase64UUID());

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
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.TransportActions;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.create.TransportCreateIndexAction;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.BaseAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.common.UUID;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.collect.Maps;
import org.elasticsearch.common.collect.Sets;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.BaseTransportRequestHandler;
import org.elasticsearch.transport.TransportChannel;
import org.elasticsearch.transport.TransportService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author kimchy (shay.banon)
 */
public class TransportBulkAction extends BaseAction<BulkRequest, BulkResponse> {

    private final boolean autoCreateIndex;

    private final boolean allowIdGeneration;

    private final ThreadPool threadPool;

    private final ClusterService clusterService;

    private final IndicesService indicesService;

    private final TransportShardBulkAction shardBulkAction;

    private final TransportCreateIndexAction createIndexAction;

    @Inject public TransportBulkAction(Settings settings, ThreadPool threadPool, TransportService transportService, ClusterService clusterService, IndicesService indicesService,
                                       TransportShardBulkAction shardBulkAction, TransportCreateIndexAction createIndexAction) {
        super(settings);
        this.threadPool = threadPool;
        this.clusterService = clusterService;
        this.indicesService = indicesService;
        this.shardBulkAction = shardBulkAction;
        this.createIndexAction = createIndexAction;

        this.autoCreateIndex = settings.getAsBoolean("action.auto_create_index", true);
        this.allowIdGeneration = componentSettings.getAsBoolean("action.allow_id_generation", true);

        transportService.registerHandler(TransportActions.BULK, new TransportHandler());
    }

    @Override protected void doExecute(final BulkRequest bulkRequest, final ActionListener<BulkResponse> listener) {
        Set<String> indices = Sets.newHashSet();
        for (ActionRequest request : bulkRequest.requests) {
            if (request instanceof IndexRequest) {
                IndexRequest indexRequest = (IndexRequest) request;
                if (!indices.contains(indexRequest.index())) {
                    indices.add(indexRequest.index());
                }
            } else if (request instanceof DeleteRequest) {
                DeleteRequest deleteRequest = (DeleteRequest) request;
                if (!indices.contains(deleteRequest.index())) {
                    indices.add(deleteRequest.index());
                }
            }
        }

        if (autoCreateIndex) {
            final AtomicInteger counter = new AtomicInteger(indices.size());
            final AtomicBoolean failed = new AtomicBoolean();
            for (String index : indices) {
                if (!clusterService.state().metaData().hasConcreteIndex(index)) {
                    createIndexAction.execute(new CreateIndexRequest(index).cause("auto(bulk api)"), new ActionListener<CreateIndexResponse>() {
                        @Override public void onResponse(CreateIndexResponse result) {
                            if (counter.decrementAndGet() == 0) {
                                executeBulk(bulkRequest, listener);
                            }
                        }

                        @Override public void onFailure(Throwable e) {
                            if (ExceptionsHelper.unwrapCause(e) instanceof IndexAlreadyExistsException) {
                                // we have the index, do it
                                if (counter.decrementAndGet() == 0) {
                                    executeBulk(bulkRequest, listener);
                                }
                            } else if (failed.compareAndSet(false, true)) {
                                listener.onFailure(e);
                            }
                        }
                    });
                } else {
                    if (counter.decrementAndGet() == 0) {
                        executeBulk(bulkRequest, listener);
                    }
                }
            }
        } else {
            executeBulk(bulkRequest, listener);
        }
    }

    private void executeBulk(final BulkRequest bulkRequest, final ActionListener<BulkResponse> listener) {
        ClusterState clusterState = clusterService.state();
        for (ActionRequest request : bulkRequest.requests) {
            if (request instanceof IndexRequest) {
                IndexRequest indexRequest = (IndexRequest) request;
                indexRequest.index(clusterState.metaData().concreteIndex(indexRequest.index()));
                if (allowIdGeneration) {
                    if (indexRequest.id() == null) {
                        indexRequest.id(UUID.randomUUID().toString());
                        // since we generate the id, change it to CREATE
                        indexRequest.opType(IndexRequest.OpType.CREATE);
                    }
                }
            } else if (request instanceof DeleteRequest) {
                DeleteRequest deleteRequest = (DeleteRequest) request;
                deleteRequest.index(clusterState.metaData().concreteIndex(deleteRequest.index()));
            }
        }

        // first, go over all the requests and create a ShardId -> Operations mapping
        Map<ShardId, List<BulkItemRequest>> requestsByShard = Maps.newHashMap();
        for (int i = 0; i < bulkRequest.requests.size(); i++) {
            ActionRequest request = bulkRequest.requests.get(i);
            ShardId shardId = null;
            if (request instanceof IndexRequest) {
                IndexRequest indexRequest = (IndexRequest) request;
                shardId = indicesService.indexServiceSafe(indexRequest.index()).operationRouting().indexShards(clusterState, indexRequest.type(), indexRequest.id()).shardId();
            } else if (request instanceof DeleteRequest) {
                DeleteRequest deleteRequest = (DeleteRequest) request;
                shardId = indicesService.indexServiceSafe(deleteRequest.index()).operationRouting().deleteShards(clusterState, deleteRequest.type(), deleteRequest.id()).shardId();
            }
            List<BulkItemRequest> list = requestsByShard.get(shardId);
            if (list == null) {
                list = Lists.newArrayList();
                requestsByShard.put(shardId, list);
            }
            list.add(new BulkItemRequest(i, request));
        }

        final AtomicInteger counter = new AtomicInteger(requestsByShard.size());
        final BulkItemResponse[] responses = new BulkItemResponse[bulkRequest.requests.size()];
        for (Map.Entry<ShardId, List<BulkItemRequest>> entry : requestsByShard.entrySet()) {
            final ShardId shardId = entry.getKey();
            final List<BulkItemRequest> requests = entry.getValue();
            shardBulkAction.execute(new BulkShardRequest(shardId.index().name(), shardId.id(), requests.toArray(new BulkItemRequest[requests.size()])), new ActionListener<BulkShardResponse>() {
                @Override public void onResponse(BulkShardResponse bulkShardResponse) {
                    synchronized (responses) {
                        for (BulkItemResponse bulkItemResponse : bulkShardResponse.responses()) {
                            responses[bulkItemResponse.itemId()] = bulkItemResponse;
                        }
                    }
                    if (counter.decrementAndGet() == 0) {
                        finishHim();
                    }
                }

                @Override public void onFailure(Throwable e) {
                    // create failures for all relevant requests
                    String message = ExceptionsHelper.detailedMessage(e);
                    synchronized (responses) {
                        for (BulkItemRequest request : requests) {
                            if (request.request() instanceof IndexRequest) {
                                IndexRequest indexRequest = (IndexRequest) request.request();
                                responses[request.id()] = new BulkItemResponse(request.id(), indexRequest.opType().toString().toLowerCase(),
                                        new BulkItemResponse.Failure(indexRequest.index(), indexRequest.type(), indexRequest.id(), message));
                            } else if (request.request() instanceof DeleteRequest) {
                                DeleteRequest deleteRequest = (DeleteRequest) request.request();
                                responses[request.id()] = new BulkItemResponse(request.id(), "delete",
                                        new BulkItemResponse.Failure(deleteRequest.index(), deleteRequest.type(), deleteRequest.id(), message));
                            }
                        }
                    }
                    if (counter.decrementAndGet() == 0) {
                        finishHim();
                    }
                }

                private void finishHim() {
                    if (bulkRequest.listenerThreaded()) {
                        threadPool.execute(new Runnable() {
                            @Override public void run() {
                                listener.onResponse(new BulkResponse(responses));
                            }
                        });
                    } else {
                        listener.onResponse(new BulkResponse(responses));
                    }
                }
            });
        }
    }

    class TransportHandler extends BaseTransportRequestHandler<BulkRequest> {

        @Override public BulkRequest newInstance() {
            return new BulkRequest();
        }

        @Override public void messageReceived(final BulkRequest request, final TransportChannel channel) throws Exception {
            // no need to use threaded listener, since we just send a response
            request.listenerThreaded(false);
            execute(request, new ActionListener<BulkResponse>() {
                @Override public void onResponse(BulkResponse result) {
                    try {
                        channel.sendResponse(result);
                    } catch (Exception e) {
                        onFailure(e);
                    }
                }

                @Override public void onFailure(Throwable e) {
                    try {
                        channel.sendResponse(e);
                    } catch (Exception e1) {
                        logger.warn("Failed to send error response for action [" + TransportActions.BULK + "] and request [" + request + "]", e1);
                    }
                }
            });
        }

        @Override public boolean spawn() {
            // no need to spawn, since in the doExecute we always execute with threaded operation set to true
            return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7541.java