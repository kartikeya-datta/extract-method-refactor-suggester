error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3281.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3281.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3281.java
text:
```scala
s@@endExecuteFirstPhase(node, internalSearchRequest(shard, shardsIts.size(), request), new SearchServiceListener<FirstResult>() {

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

package org.elasticsearch.action.search.type;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.support.BaseAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.block.ClusterBlockLevel;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.cluster.routing.GroupShardsIterator;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.cluster.routing.ShardsIterator;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.trove.ExtTIntArrayList;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.search.SearchShardTarget;
import org.elasticsearch.search.action.SearchServiceListener;
import org.elasticsearch.search.action.SearchServiceTransportAction;
import org.elasticsearch.search.controller.SearchPhaseController;
import org.elasticsearch.search.controller.ShardDoc;
import org.elasticsearch.search.internal.InternalSearchRequest;
import org.elasticsearch.search.query.QuerySearchResultProvider;
import org.elasticsearch.threadpool.ThreadPool;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.elasticsearch.action.search.type.TransportSearchHelper.*;

/**
 * @author kimchy (shay.banon)
 */
public abstract class TransportSearchTypeAction extends BaseAction<SearchRequest, SearchResponse> {

    protected final ThreadPool threadPool;

    protected final ClusterService clusterService;

    protected final IndicesService indicesService;

    protected final SearchServiceTransportAction searchService;

    protected final SearchPhaseController searchPhaseController;

    protected final TransportSearchCache searchCache;

    public TransportSearchTypeAction(Settings settings, ThreadPool threadPool, ClusterService clusterService, IndicesService indicesService,
                                     TransportSearchCache searchCache, SearchServiceTransportAction searchService, SearchPhaseController searchPhaseController) {
        super(settings);
        this.threadPool = threadPool;
        this.clusterService = clusterService;
        this.searchCache = searchCache;
        this.indicesService = indicesService;
        this.searchService = searchService;
        this.searchPhaseController = searchPhaseController;
    }

    protected abstract class BaseAsyncAction<FirstResult> {

        protected final ActionListener<SearchResponse> listener;

        protected final GroupShardsIterator shardsIts;

        protected final SearchRequest request;

        protected final DiscoveryNodes nodes;

        protected final int expectedSuccessfulOps;

        protected final int expectedTotalOps;

        protected final AtomicInteger successulOps = new AtomicInteger();

        protected final AtomicInteger totalOps = new AtomicInteger();

        protected final Collection<ShardSearchFailure> shardFailures = searchCache.obtainShardFailures();

        protected volatile ShardDoc[] sortedShardList;

        protected BaseAsyncAction(SearchRequest request, ActionListener<SearchResponse> listener) {
            this.request = request;
            this.listener = listener;

            ClusterState clusterState = clusterService.state();

            nodes = clusterState.nodes();

            request.indices(clusterState.metaData().concreteIndices(request.indices()));

            for (String index : request.indices()) {
                clusterState.blocks().indexBlockedRaiseException(ClusterBlockLevel.READ, index);
            }

            shardsIts = indicesService.searchShards(clusterState, request.indices(), request.queryHint());
            expectedSuccessfulOps = shardsIts.size();
            expectedTotalOps = shardsIts.totalSizeActive();

            if (expectedSuccessfulOps == 0) {
                // not search shards to search on...
                throw new SearchPhaseExecutionException("initial", "No indices / shards to search on, requested indices are " + Arrays.toString(request.indices()), buildShardFailures());
            }
        }

        public void start() {
            // count the local operations, and perform the non local ones
            int localOperations = 0;
            for (final ShardsIterator shardIt : shardsIts) {
                final ShardRouting shard = shardIt.nextActiveOrNull();
                if (shard != null) {
                    if (shard.currentNodeId().equals(nodes.localNodeId())) {
                        localOperations++;
                    } else {
                        // do the remote operation here, the localAsync flag is not relevant
                        performFirstPhase(shardIt.reset());
                    }
                } else {
                    // really, no shards active in this group
                    onFirstPhaseResult(shard, shardIt, null);
                }
            }
            // we have local operations, perform them now
            if (localOperations > 0) {
                if (request.operationThreading() == SearchOperationThreading.SINGLE_THREAD) {
                    request.beforeLocalFork();
                    threadPool.execute(new Runnable() {
                        @Override public void run() {
                            for (final ShardsIterator shardIt : shardsIts) {
                                final ShardRouting shard = shardIt.reset().nextActiveOrNull();
                                if (shard != null) {
                                    if (shard.currentNodeId().equals(nodes.localNodeId())) {
                                        performFirstPhase(shardIt.reset());
                                    }
                                }
                            }
                        }
                    });
                } else {
                    boolean localAsync = request.operationThreading() == SearchOperationThreading.THREAD_PER_SHARD;
                    if (localAsync) {
                        request.beforeLocalFork();
                    }
                    for (final ShardsIterator shardIt : shardsIts) {
                        final ShardRouting shard = shardIt.reset().nextActiveOrNull();
                        if (shard != null) {
                            if (shard.currentNodeId().equals(nodes.localNodeId())) {
                                if (localAsync) {
                                    threadPool.execute(new Runnable() {
                                        @Override public void run() {
                                            performFirstPhase(shardIt.reset());
                                        }
                                    });
                                } else {
                                    performFirstPhase(shardIt.reset());
                                }
                            }
                        }
                    }
                }
            }
        }

        private void performFirstPhase(final ShardsIterator shardIt) {
            final ShardRouting shard = shardIt.nextActiveOrNull();
            if (shard == null) {
                // no more active shards... (we should not really get here, but just for safety)
                onFirstPhaseResult(shard, shardIt, null);
            } else {
                DiscoveryNode node = nodes.get(shard.currentNodeId());
                if (node == null) {
                    onFirstPhaseResult(shard, shardIt, null);
                } else {
                    sendExecuteFirstPhase(node, internalSearchRequest(shard, request), new SearchServiceListener<FirstResult>() {
                        @Override public void onResult(FirstResult result) {
                            onFirstPhaseResult(shard, result, shardIt);
                        }

                        @Override public void onFailure(Throwable t) {
                            onFirstPhaseResult(shard, shardIt, t);
                        }
                    });
                }
            }
        }

        private void onFirstPhaseResult(ShardRouting shard, FirstResult result, ShardsIterator shardIt) {
            processFirstPhaseResult(shard, result);
            // increment all the "future" shards to update the total ops since we some may work and some may not...
            // and when that happens, we break on total ops, so we must maintain them
            while (shardIt.hasNextActive()) {
                totalOps.incrementAndGet();
                shardIt.nextActive();
            }
            if (successulOps.incrementAndGet() == expectedSuccessfulOps ||
                    totalOps.incrementAndGet() == expectedTotalOps) {
                try {
                    moveToSecondPhase();
                } catch (Exception e) {
                    invokeListener(new ReduceSearchPhaseException(firstPhaseName(), "", e, buildShardFailures()));
                }
            }
        }

        private void onFirstPhaseResult(ShardRouting shard, final ShardsIterator shardIt, Throwable t) {
            if (totalOps.incrementAndGet() == expectedTotalOps) {
                // e is null when there is no next active....
                if (logger.isDebugEnabled()) {
                    if (t != null) {
                        if (shard != null) {
                            logger.debug(shard.shortSummary() + ": Failed to execute [" + request + "]", t);
                        } else {
                            logger.debug(shardIt.shardId() + ": Failed to execute [" + request + "]", t);
                        }
                    }
                }
                // no more shards, add a failure
                if (t == null) {
                    // no active shards
                    shardFailures.add(new ShardSearchFailure("No active shards", new SearchShardTarget(null, shardIt.shardId().index().name(), shardIt.shardId().id())));
                } else {
                    shardFailures.add(new ShardSearchFailure(t));
                }
                if (successulOps.get() == 0) {
                    // no successful ops, raise an exception
                    invokeListener(new SearchPhaseExecutionException(firstPhaseName(), "total failure", buildShardFailures()));
                } else {
                    try {
                        moveToSecondPhase();
                    } catch (Exception e) {
                        invokeListener(new ReduceSearchPhaseException(firstPhaseName(), "", e, buildShardFailures()));
                    }
                }
            } else {
                if (shardIt.hasNextActive()) {
                    // trace log this exception
                    if (logger.isTraceEnabled()) {
                        if (t != null) {
                            if (shard != null) {
                                logger.trace(shard.shortSummary() + ": Failed to execute [" + request + "]", t);
                            } else {
                                logger.trace(shardIt.shardId() + ": Failed to execute [" + request + "]", t);
                            }
                        }
                    }
                    performFirstPhase(shardIt);
                } else {
                    // no more shards active, add a failure
                    // e is null when there is no next active....
                    if (logger.isDebugEnabled()) {
                        if (t != null) {
                            if (shard != null) {
                                logger.debug(shard.shortSummary() + ": Failed to execute [" + request + "]", t);
                            } else {
                                logger.debug(shardIt.shardId() + ": Failed to execute [" + request + "]", t);
                            }
                        }
                    }
                    if (t == null) {
                        // no active shards
                        shardFailures.add(new ShardSearchFailure("No active shards", new SearchShardTarget(null, shardIt.shardId().index().name(), shardIt.shardId().id())));
                    } else {
                        shardFailures.add(new ShardSearchFailure(t));
                    }
                }
            }
        }

        /**
         * Builds the shard failures, and releases the cache (meaning this should only be called once!).
         */
        protected ShardSearchFailure[] buildShardFailures() {
            return TransportSearchHelper.buildShardFailures(shardFailures, searchCache);
        }

        /**
         * Releases shard targets that are not used in the docsIdsToLoad.
         */
        protected void releaseIrrelevantSearchContexts(Map<SearchShardTarget, QuerySearchResultProvider> queryResults,
                                                       Map<SearchShardTarget, ExtTIntArrayList> docIdsToLoad) {
            if (docIdsToLoad == null) {
                return;
            }
            for (Map.Entry<SearchShardTarget, QuerySearchResultProvider> entry : queryResults.entrySet()) {
                if (!docIdsToLoad.containsKey(entry.getKey())) {
                    DiscoveryNode node = nodes.get(entry.getKey().nodeId());
                    if (node != null) { // should not happen (==null) but safeguard anyhow
                        searchService.sendFreeContext(node, entry.getValue().id());
                    }
                }
            }
        }

        protected void invokeListener(final SearchResponse response) {
            if (request.listenerThreaded()) {
                threadPool.execute(new Runnable() {
                    @Override public void run() {
                        listener.onResponse(response);
                    }
                });
            } else {
                listener.onResponse(response);
            }
        }

        protected void invokeListener(final Throwable t) {
            if (request.listenerThreaded()) {
                threadPool.execute(new Runnable() {
                    @Override public void run() {
                        listener.onFailure(t);
                    }
                });
            } else {
                listener.onFailure(t);
            }
        }

        protected abstract void sendExecuteFirstPhase(DiscoveryNode node, InternalSearchRequest request, SearchServiceListener<FirstResult> listener);

        protected abstract void processFirstPhaseResult(ShardRouting shard, FirstResult result);

        protected abstract void moveToSecondPhase();

        protected abstract String firstPhaseName();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3281.java