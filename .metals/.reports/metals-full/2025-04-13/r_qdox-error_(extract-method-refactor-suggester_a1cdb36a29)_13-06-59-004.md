error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/539.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/539.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/539.java
text:
```scala
s@@uper(settings, MultiPercolateAction.NAME, threadPool);

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

package org.elasticsearch.action.percolate;

import com.carrotsearch.hppc.IntArrayList;
import org.elasticsearch.ExceptionsHelper;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.UnavailableShardsException;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.support.TransportAction;
import org.elasticsearch.action.support.broadcast.BroadcastShardOperationFailedException;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.block.ClusterBlockLevel;
import org.elasticsearch.cluster.routing.GroupShardsIterator;
import org.elasticsearch.cluster.routing.ShardIterator;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.concurrent.AtomicArray;
import org.elasticsearch.index.engine.DocumentMissingException;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.percolator.PercolatorService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.BaseTransportRequestHandler;
import org.elasticsearch.transport.TransportChannel;
import org.elasticsearch.transport.TransportService;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 */
public class TransportMultiPercolateAction extends TransportAction<MultiPercolateRequest, MultiPercolateResponse> {

    private final ClusterService clusterService;
    private final PercolatorService percolatorService;

    private final TransportMultiGetAction multiGetAction;
    private final TransportShardMultiPercolateAction shardMultiPercolateAction;

    @Inject
    public TransportMultiPercolateAction(Settings settings, ThreadPool threadPool, TransportShardMultiPercolateAction shardMultiPercolateAction,
                                         ClusterService clusterService, TransportService transportService, PercolatorService percolatorService,
                                         TransportMultiGetAction multiGetAction) {
        super(settings, threadPool);
        this.shardMultiPercolateAction = shardMultiPercolateAction;
        this.clusterService = clusterService;
        this.percolatorService = percolatorService;
        this.multiGetAction = multiGetAction;

        transportService.registerHandler(MultiPercolateAction.NAME, new TransportHandler());
    }

    @Override
    protected void doExecute(final MultiPercolateRequest request, final ActionListener<MultiPercolateResponse> listener) {
        final ClusterState clusterState = clusterService.state();
        clusterState.blocks().globalBlockedRaiseException(ClusterBlockLevel.READ);

        final List<Object> percolateRequests = new ArrayList<>(request.requests().size());
        // Can have a mixture of percolate requests. (normal percolate requests & percolate existing doc),
        // so we need to keep track for what percolate request we had a get request
        final IntArrayList getRequestSlots = new IntArrayList();
        List<GetRequest> existingDocsRequests = new ArrayList<>();
        for (int slot = 0;  slot < request.requests().size(); slot++) {
            PercolateRequest percolateRequest = request.requests().get(slot);
            percolateRequest.startTime = System.currentTimeMillis();
            percolateRequests.add(percolateRequest);
            if (percolateRequest.getRequest() != null) {
                existingDocsRequests.add(percolateRequest.getRequest());
                getRequestSlots.add(slot);
            }
        }

        if (!existingDocsRequests.isEmpty()) {
            final MultiGetRequest multiGetRequest = new MultiGetRequest();
            for (GetRequest getRequest : existingDocsRequests) {
                multiGetRequest.add(
                        new MultiGetRequest.Item(getRequest.index(), getRequest.type(), getRequest.id())
                        .routing(getRequest.routing())
                );
            }

            multiGetAction.execute(multiGetRequest, new ActionListener<MultiGetResponse>() {

                @Override
                public void onResponse(MultiGetResponse multiGetItemResponses) {
                    for (int i = 0; i < multiGetItemResponses.getResponses().length; i++) {
                        MultiGetItemResponse itemResponse = multiGetItemResponses.getResponses()[i];
                        int slot = getRequestSlots.get(i);
                        if (!itemResponse.isFailed()) {
                            GetResponse getResponse = itemResponse.getResponse();
                            if (getResponse.isExists()) {
                                PercolateRequest originalRequest = (PercolateRequest) percolateRequests.get(slot);
                                percolateRequests.set(slot, new PercolateRequest(originalRequest, getResponse.getSourceAsBytesRef()));
                            } else {
                                logger.trace("mpercolate existing doc, item[{}] doesn't exist", slot);
                                percolateRequests.set(slot, new DocumentMissingException(null, getResponse.getType(), getResponse.getId()));
                            }
                        } else {
                            logger.trace("mpercolate existing doc, item[{}] failure {}", slot, itemResponse.getFailure());
                            percolateRequests.set(slot, itemResponse.getFailure());
                        }
                    }
                    new ASyncAction(percolateRequests, listener, clusterState).run();
                }

                @Override
                public void onFailure(Throwable e) {
                    listener.onFailure(e);
                }
            });
        } else {
            new ASyncAction(percolateRequests, listener, clusterState).run();
        }

    }


    private class ASyncAction {

        final ActionListener<MultiPercolateResponse> finalListener;
        final Map<ShardId, TransportShardMultiPercolateAction.Request> requestsByShard;
        final List<Object> percolateRequests;

        final Map<ShardId, IntArrayList> shardToSlots;
        final AtomicInteger expectedOperations;
        final AtomicArray<Object> reducedResponses;
        final AtomicReferenceArray<AtomicInteger> expectedOperationsPerItem;
        final AtomicReferenceArray<AtomicReferenceArray> responsesByItemAndShard;

        ASyncAction(List<Object> percolateRequests, ActionListener<MultiPercolateResponse> finalListener, ClusterState clusterState) {
            this.finalListener = finalListener;
            this.percolateRequests = percolateRequests;
            responsesByItemAndShard = new AtomicReferenceArray<>(percolateRequests.size());
            expectedOperationsPerItem = new AtomicReferenceArray<>(percolateRequests.size());
            reducedResponses = new AtomicArray<>(percolateRequests.size());

            // Resolving concrete indices and routing and grouping the requests by shard
            requestsByShard = new HashMap<>();
            // Keep track what slots belong to what shard, in case a request to a shard fails on all copies
            shardToSlots = new HashMap<>();
            int expectedResults = 0;
            for (int slot = 0;  slot < percolateRequests.size(); slot++) {
                Object element = percolateRequests.get(slot);
                assert element != null;
                if (element instanceof PercolateRequest) {
                    PercolateRequest percolateRequest = (PercolateRequest) element;
                    String[] concreteIndices;
                    try {
                         concreteIndices = clusterState.metaData().concreteIndices(percolateRequest.indicesOptions(), percolateRequest.indices());
                    } catch (IndexMissingException e) {
                        reducedResponses.set(slot, e);
                        responsesByItemAndShard.set(slot, new AtomicReferenceArray(0));
                        expectedOperationsPerItem.set(slot, new AtomicInteger(0));
                        continue;
                    }
                    Map<String, Set<String>> routing = clusterState.metaData().resolveSearchRouting(percolateRequest.routing(), percolateRequest.indices());
                    // TODO: I only need shardIds, ShardIterator(ShardRouting) is only needed in TransportShardMultiPercolateAction
                    GroupShardsIterator shards = clusterService.operationRouting().searchShards(
                            clusterState, percolateRequest.indices(), concreteIndices, routing, percolateRequest.preference()
                    );
                    if (shards.size() == 0) {
                        reducedResponses.set(slot, new UnavailableShardsException(null, "No shards available"));
                        responsesByItemAndShard.set(slot, new AtomicReferenceArray(0));
                        expectedOperationsPerItem.set(slot, new AtomicInteger(0));
                        continue;
                    }

                    responsesByItemAndShard.set(slot, new AtomicReferenceArray(shards.size()));
                    expectedOperationsPerItem.set(slot, new AtomicInteger(shards.size()));
                    for (ShardIterator shard : shards) {
                        ShardId shardId = shard.shardId();
                        TransportShardMultiPercolateAction.Request requests = requestsByShard.get(shardId);
                        if (requests == null) {
                            requestsByShard.put(shardId, requests = new TransportShardMultiPercolateAction.Request(shard.shardId().getIndex(), shardId.id(), percolateRequest.preference()));
                        }
                        logger.trace("Adding shard[{}] percolate request for item[{}]", shardId, slot);
                        requests.add(new TransportShardMultiPercolateAction.Request.Item(slot, new PercolateShardRequest(shardId, percolateRequest)));

                        IntArrayList items = shardToSlots.get(shardId);
                        if (items == null) {
                            shardToSlots.put(shardId, items = new IntArrayList());
                        }
                        items.add(slot);
                    }
                    expectedResults++;
                } else if (element instanceof Throwable || element instanceof MultiGetResponse.Failure) {
                    logger.trace("item[{}] won't be executed, reason: {}", slot, element);
                    reducedResponses.set(slot, element);
                    responsesByItemAndShard.set(slot, new AtomicReferenceArray(0));
                    expectedOperationsPerItem.set(slot, new AtomicInteger(0));
                }
            }
            expectedOperations = new AtomicInteger(expectedResults);
        }

        void run() {
            if (expectedOperations.get() == 0) {
                finish();
                return;
            }

            logger.trace("mpercolate executing for shards {}", requestsByShard.keySet());
            for (Map.Entry<ShardId, TransportShardMultiPercolateAction.Request> entry : requestsByShard.entrySet()) {
                final ShardId shardId = entry.getKey();
                TransportShardMultiPercolateAction.Request shardRequest = entry.getValue();
                shardMultiPercolateAction.execute(shardRequest, new ActionListener<TransportShardMultiPercolateAction.Response>() {

                    @Override
                    public void onResponse(TransportShardMultiPercolateAction.Response response) {
                        onShardResponse(shardId, response);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        onShardFailure(shardId, e);
                    }

                });
            }
        }

        @SuppressWarnings("unchecked")
        void onShardResponse(ShardId shardId, TransportShardMultiPercolateAction.Response response) {
            logger.debug("{} Percolate shard response", shardId);
            try {
                for (TransportShardMultiPercolateAction.Response.Item item : response.items()) {
                    AtomicReferenceArray shardResults = responsesByItemAndShard.get(item.slot());
                    if (shardResults == null) {
                        assert false : "shardResults can't be null";
                        continue;
                    }

                    if (item.failed()) {
                        shardResults.set(shardId.id(), new BroadcastShardOperationFailedException(shardId, item.error().string()));
                    } else {
                        shardResults.set(shardId.id(), item.response());
                    }

                    assert expectedOperationsPerItem.get(item.slot()).get() >= 1 : "slot[" + item.slot() + "] can't be lower than one";
                    if (expectedOperationsPerItem.get(item.slot()).decrementAndGet() == 0) {
                        // Failure won't bubble up, since we fail the whole request now via the catch clause below,
                        // so expectedOperationsPerItem will not be decremented twice.
                        reduce(item.slot());
                    }
                }
            } catch (Throwable e) {
                logger.error("{} Percolate original reduce error", e, shardId);
                finalListener.onFailure(e);
            }
        }

        @SuppressWarnings("unchecked")
        void onShardFailure(ShardId shardId, Throwable e) {
            logger.debug("{} Shard multi percolate failure", e, shardId);
            try {
                IntArrayList slots = shardToSlots.get(shardId);
                for (int i = 0; i < slots.size(); i++) {
                    int slot = slots.get(i);
                    AtomicReferenceArray shardResults = responsesByItemAndShard.get(slot);
                    if (shardResults == null) {
                        continue;
                    }

                    shardResults.set(shardId.id(), new BroadcastShardOperationFailedException(shardId, e));
                    assert expectedOperationsPerItem.get(slot).get() >= 1 : "slot[" + slot + "] can't be lower than one. Caused by: " + e.getMessage();
                    if (expectedOperationsPerItem.get(slot).decrementAndGet() == 0) {
                        reduce(slot);
                    }
                }
            } catch (Throwable t) {
                logger.error("{} Percolate original reduce error, original error {}", t, shardId, e);
                finalListener.onFailure(t);
            }
        }

        void reduce(int slot) {
            AtomicReferenceArray shardResponses = responsesByItemAndShard.get(slot);
            PercolateResponse reducedResponse = TransportPercolateAction.reduce((PercolateRequest) percolateRequests.get(slot), shardResponses, percolatorService);
            reducedResponses.set(slot, reducedResponse);
            assert expectedOperations.get() >= 1 : "slot[" + slot + "] expected options should be >= 1 but is " + expectedOperations.get();
            if (expectedOperations.decrementAndGet() == 0) {
                finish();
            }
        }

        void finish() {
            MultiPercolateResponse.Item[] finalResponse = new MultiPercolateResponse.Item[reducedResponses.length()];
            for (int slot = 0; slot < reducedResponses.length(); slot++) {
                Object element = reducedResponses.get(slot);
                assert element != null : "Element[" + slot + "] shouldn't be null";
                if (element instanceof PercolateResponse) {
                    finalResponse[slot] = new MultiPercolateResponse.Item((PercolateResponse) element);
                } else if (element instanceof Throwable) {
                    finalResponse[slot] = new MultiPercolateResponse.Item(ExceptionsHelper.detailedMessage((Throwable) element));
                } else if (element instanceof MultiGetResponse.Failure) {
                    finalResponse[slot] = new MultiPercolateResponse.Item(((MultiGetResponse.Failure)element).getMessage());
                }
            }
            finalListener.onResponse(new MultiPercolateResponse(finalResponse));
        }

    }


    class TransportHandler extends BaseTransportRequestHandler<MultiPercolateRequest> {

        @Override
        public MultiPercolateRequest newInstance() {
            return new MultiPercolateRequest();
        }

        @Override
        public void messageReceived(final MultiPercolateRequest request, final TransportChannel channel) throws Exception {
            // no need to use threaded listener, since we just send a response
            request.listenerThreaded(false);
            execute(request, new ActionListener<MultiPercolateResponse>() {
                @Override
                public void onResponse(MultiPercolateResponse response) {
                    try {
                        channel.sendResponse(response);
                    } catch (Throwable e) {
                        onFailure(e);
                    }
                }

                @Override
                public void onFailure(Throwable e) {
                    try {
                        channel.sendResponse(e);
                    } catch (Exception e1) {
                        logger.warn("Failed to send error response for action [mpercolate] and request [" + request + "]", e1);
                    }
                }
            });
        }

        @Override
        public String executor() {
            return ThreadPool.Names.SAME;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/539.java