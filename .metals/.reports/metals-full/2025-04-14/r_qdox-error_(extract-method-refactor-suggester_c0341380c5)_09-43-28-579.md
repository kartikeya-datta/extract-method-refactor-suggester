error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2074.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2074.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2074.java
text:
```scala
l@@ogger.debug("Moving to second phase, based on results from: {} (cluster state version: {})", sb, clusterState.version());

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

package org.elasticsearch.action.search.type;

import org.apache.lucene.search.ScoreDoc;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.NoShardAvailableActionException;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.support.TransportAction;
import org.elasticsearch.action.support.TransportActions;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.block.ClusterBlockLevel;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.cluster.routing.GroupShardsIterator;
import org.elasticsearch.cluster.routing.ShardIterator;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.trove.ExtTIntArrayList;
import org.elasticsearch.common.util.concurrent.AtomicArray;
import org.elasticsearch.search.SearchPhaseResult;
import org.elasticsearch.search.SearchShardTarget;
import org.elasticsearch.search.action.SearchServiceListener;
import org.elasticsearch.search.action.SearchServiceTransportAction;
import org.elasticsearch.search.controller.SearchPhaseController;
import org.elasticsearch.search.internal.ShardSearchRequest;
import org.elasticsearch.search.query.QuerySearchResultProvider;
import org.elasticsearch.threadpool.ThreadPool;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.elasticsearch.action.search.type.TransportSearchHelper.internalSearchRequest;

/**
 *
 */
public abstract class TransportSearchTypeAction extends TransportAction<SearchRequest, SearchResponse> {

    protected final ClusterService clusterService;

    protected final SearchServiceTransportAction searchService;

    protected final SearchPhaseController searchPhaseController;

    public TransportSearchTypeAction(Settings settings, ThreadPool threadPool, ClusterService clusterService,
                                     SearchServiceTransportAction searchService, SearchPhaseController searchPhaseController) {
        super(settings, threadPool);
        this.clusterService = clusterService;
        this.searchService = searchService;
        this.searchPhaseController = searchPhaseController;
    }

    protected abstract class BaseAsyncAction<FirstResult extends SearchPhaseResult> {

        protected final ActionListener<SearchResponse> listener;

        protected final GroupShardsIterator shardsIts;

        protected final SearchRequest request;

        protected final ClusterState clusterState;
        protected final DiscoveryNodes nodes;

        protected final int expectedSuccessfulOps;
        private final int expectedTotalOps;

        protected final AtomicInteger successulOps = new AtomicInteger();
        private final AtomicInteger totalOps = new AtomicInteger();

        protected final AtomicArray<FirstResult> firstResults;
        private volatile AtomicArray<ShardSearchFailure> shardFailures;
        private final Object shardFailuresMutex = new Object();
        protected volatile ScoreDoc[] sortedShardList;

        protected final long startTime = System.currentTimeMillis();

        protected BaseAsyncAction(SearchRequest request, ActionListener<SearchResponse> listener) {
            this.request = request;
            this.listener = listener;

            this.clusterState = clusterService.state();
            nodes = clusterState.nodes();

            clusterState.blocks().globalBlockedRaiseException(ClusterBlockLevel.READ);

            String[] concreteIndices = clusterState.metaData().concreteIndices(request.indices(), request.ignoreIndices(), true);

            for (String index : concreteIndices) {
                clusterState.blocks().indexBlockedRaiseException(ClusterBlockLevel.READ, index);
            }

            Map<String, Set<String>> routingMap = clusterState.metaData().resolveSearchRouting(request.routing(), request.indices());

            shardsIts = clusterService.operationRouting().searchShards(clusterState, request.indices(), concreteIndices, routingMap, request.preference());
            expectedSuccessfulOps = shardsIts.size();
            // we need to add 1 for non active partition, since we count it in the total!
            expectedTotalOps = shardsIts.totalSizeWith1ForEmpty();

            if (expectedSuccessfulOps == 0) {
                // not search shards to search on...
                throw new SearchPhaseExecutionException("initial", "No indices / shards to search on, requested indices are " + Arrays.toString(request.indices()), buildShardFailures());
            }

            firstResults = new AtomicArray<FirstResult>(shardsIts.size());
        }

        public void start() {
            request.beforeStart();
            // count the local operations, and perform the non local ones
            int localOperations = 0;
            int shardIndex = -1;
            for (final ShardIterator shardIt : shardsIts) {
                shardIndex++;
                final ShardRouting shard = shardIt.firstOrNull();
                if (shard != null) {
                    if (shard.currentNodeId().equals(nodes.localNodeId())) {
                        localOperations++;
                    } else {
                        // do the remote operation here, the localAsync flag is not relevant
                        performFirstPhase(shardIndex, shardIt);
                    }
                } else {
                    // really, no shards active in this group
                    onFirstPhaseResult(shardIndex, null, null, shardIt, new NoShardAvailableActionException(shardIt.shardId()));
                }
            }
            // we have local operations, perform them now
            if (localOperations > 0) {
                if (request.operationThreading() == SearchOperationThreading.SINGLE_THREAD) {
                    request.beforeLocalFork();
                    threadPool.executor(ThreadPool.Names.SEARCH).execute(new Runnable() {
                        @Override
                        public void run() {
                            int shardIndex = -1;
                            for (final ShardIterator shardIt : shardsIts) {
                                shardIndex++;
                                final ShardRouting shard = shardIt.firstOrNull();
                                if (shard != null) {
                                    if (shard.currentNodeId().equals(nodes.localNodeId())) {
                                        performFirstPhase(shardIndex, shardIt);
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
                    shardIndex = -1;
                    for (final ShardIterator shardIt : shardsIts) {
                        shardIndex++;
                        final int fShardIndex = shardIndex;
                        final ShardRouting shard = shardIt.firstOrNull();
                        if (shard != null) {
                            if (shard.currentNodeId().equals(nodes.localNodeId())) {
                                if (localAsync) {
                                    try {
                                        threadPool.executor(ThreadPool.Names.SEARCH).execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                performFirstPhase(fShardIndex, shardIt);
                                            }
                                        });
                                    } catch (Throwable t) {
                                        onFirstPhaseResult(shardIndex, shard, shard.currentNodeId(), shardIt, t);
                                    }
                                } else {
                                    performFirstPhase(fShardIndex, shardIt);
                                }
                            }
                        }
                    }
                }
            }
        }

        void performFirstPhase(final int shardIndex, final ShardIterator shardIt) {
            performFirstPhase(shardIndex, shardIt, shardIt.nextOrNull());
        }

        void performFirstPhase(final int shardIndex, final ShardIterator shardIt, final ShardRouting shard) {
            if (shard == null) {
                // no more active shards... (we should not really get here, but just for safety)
                onFirstPhaseResult(shardIndex, null, null, shardIt, new NoShardAvailableActionException(shardIt.shardId()));
            } else {
                final DiscoveryNode node = nodes.get(shard.currentNodeId());
                if (node == null) {
                    onFirstPhaseResult(shardIndex, shard, null, shardIt, new NoShardAvailableActionException(shardIt.shardId()));
                } else {
                    String[] filteringAliases = clusterState.metaData().filteringAliases(shard.index(), request.indices());
                    sendExecuteFirstPhase(node, internalSearchRequest(shard, shardsIts.size(), request, filteringAliases, startTime), new SearchServiceListener<FirstResult>() {
                        @Override
                        public void onResult(FirstResult result) {
                            onFirstPhaseResult(shardIndex, shard, result, shardIt);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            onFirstPhaseResult(shardIndex, shard, node.id(), shardIt, t);
                        }
                    });
                }
            }
        }

        void onFirstPhaseResult(int shardIndex, ShardRouting shard, FirstResult result, ShardIterator shardIt) {
            result.shardTarget(new SearchShardTarget(shard.currentNodeId(), shard.index(), shard.id()));
            processFirstPhaseResult(shardIndex, shard, result);

            // increment all the "future" shards to update the total ops since we some may work and some may not...
            // and when that happens, we break on total ops, so we must maintain them
            int xTotalOps = totalOps.addAndGet(shardIt.remaining() + 1);
            successulOps.incrementAndGet();
            if (xTotalOps == expectedTotalOps) {
                try {
                    innerMoveToSecondPhase();
                } catch (Throwable e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(shardIt.shardId() + ": Failed to execute [" + request + "] while moving to second phase", e);
                    }
                    listener.onFailure(new ReduceSearchPhaseException(firstPhaseName(), "", e, buildShardFailures()));
                }
            }
        }

        void onFirstPhaseResult(final int shardIndex, @Nullable ShardRouting shard, @Nullable String nodeId, final ShardIterator shardIt, Throwable t) {
            // we always add the shard failure for a specific shard instance
            // we do make sure to clean it on a successful response from a shard
            SearchShardTarget shardTarget = new SearchShardTarget(nodeId, shardIt.shardId().getIndex(), shardIt.shardId().getId());
            addShardFailure(shardIndex, shardTarget, t);

            if (totalOps.incrementAndGet() == expectedTotalOps) {
                if (logger.isDebugEnabled()) {
                    if (t != null && !TransportActions.isShardNotAvailableException(t)) {
                        if (shard != null) {
                            logger.debug(shard.shortSummary() + ": Failed to execute [" + request + "]", t);
                        } else {
                            logger.debug(shardIt.shardId() + ": Failed to execute [" + request + "]", t);
                        }
                    }
                }
                if (successulOps.get() == 0) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("All shards failed for phase: [{}]", firstPhaseName(), t);
                    }
                    // no successful ops, raise an exception
                    listener.onFailure(new SearchPhaseExecutionException(firstPhaseName(), "all shards failed", buildShardFailures()));
                } else {
                    try {
                        innerMoveToSecondPhase();
                    } catch (Throwable e) {
                        listener.onFailure(new ReduceSearchPhaseException(firstPhaseName(), "", e, buildShardFailures()));
                    }
                }
            } else {
                ShardRouting nextShard = shardIt.nextOrNull();
                if (nextShard != null) {
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
                    performFirstPhase(shardIndex, shardIt, nextShard);
                } else {
                    // no more shards active, add a failure
                    if (logger.isDebugEnabled()) {
                        if (t != null && !TransportActions.isShardNotAvailableException(t)) {
                            if (shard != null) {
                                logger.debug(shard.shortSummary() + ": Failed to execute [" + request + "]", t);
                            } else {
                                logger.debug(shardIt.shardId() + ": Failed to execute [" + request + "]", t);
                            }
                        }
                    }
                }
            }
        }

        /**
         * Builds how long it took to execute the search.
         */
        protected final long buildTookInMillis() {
            return System.currentTimeMillis() - startTime;
        }

        protected final ShardSearchFailure[] buildShardFailures() {
            AtomicArray<ShardSearchFailure> shardFailures = this.shardFailures;
            if (shardFailures == null) {
                return ShardSearchFailure.EMPTY_ARRAY;
            }
            List<AtomicArray.Entry<ShardSearchFailure>> entries = shardFailures.asList();
            ShardSearchFailure[] failures = new ShardSearchFailure[entries.size()];
            for (int i = 0; i < failures.length; i++) {
                failures[i] = entries.get(i).value;
            }
            return failures;
        }

        protected final void addShardFailure(final int shardIndex, @Nullable SearchShardTarget shardTarget, Throwable t) {
            // we don't aggregate shard failures on non active shards (but do keep the header counts right)
            if (TransportActions.isShardNotAvailableException(t)) {
                return;
            }

            // lazily create shard failures, so we can early build the empty shard failure list in most cases (no failures)
            if (shardFailures == null) {
                synchronized (shardFailuresMutex) {
                    if (shardFailures == null) {
                        shardFailures = new AtomicArray<ShardSearchFailure>(shardsIts.size());
                    }
                }
            }
            ShardSearchFailure failure = shardFailures.get(shardIndex);
            if (failure == null) {
                shardFailures.set(shardIndex, new ShardSearchFailure(t, shardTarget));
            } else {
                // the failure is already present, try and not override it with an exception that is less meaningless
                // for example, getting illegal shard state
                if (TransportActions.isReadOverrideException(t)) {
                    shardFailures.set(shardIndex, new ShardSearchFailure(t, shardTarget));
                }
            }
        }

        /**
         * Releases shard targets that are not used in the docsIdsToLoad.
         */
        protected void releaseIrrelevantSearchContexts(AtomicArray<? extends QuerySearchResultProvider> queryResults,
                                                       AtomicArray<ExtTIntArrayList> docIdsToLoad) {
            if (docIdsToLoad == null) {
                return;
            }
            // we only release search context that we did not fetch from if we are not scrolling
            if (request.scroll() == null) {
                for (AtomicArray.Entry<? extends QuerySearchResultProvider> entry : queryResults.asList()) {
                    if (docIdsToLoad.get(entry.index) == null) {
                        DiscoveryNode node = nodes.get(entry.value.queryResult().shardTarget().nodeId());
                        if (node != null) { // should not happen (==null) but safeguard anyhow
                            searchService.sendFreeContext(node, entry.value.queryResult().id(), request);
                        }
                    }
                }
            }
        }

        protected abstract void sendExecuteFirstPhase(DiscoveryNode node, ShardSearchRequest request, SearchServiceListener<FirstResult> listener);

        protected final void processFirstPhaseResult(int shardIndex, ShardRouting shard, FirstResult result) {
            firstResults.set(shardIndex, result);

            // clean a previous error on this shard group (note, this code will be serialized on the same shardIndex value level
            // so its ok concurrency wise to miss potentially the shard failures being created because of another failure
            // in the #addShardFailure, because by definition, it will happen on *another* shardIndex
            AtomicArray<ShardSearchFailure> shardFailures = this.shardFailures;
            if (shardFailures != null) {
                shardFailures.set(shardIndex, null);
            }
        }

        final void innerMoveToSecondPhase() throws Exception {
            if (logger.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder();
                boolean hadOne = false;
                for (int i = 0; i < firstResults.length(); i++) {
                    FirstResult result = firstResults.get(i);
                    if (result == null) {
                        continue; // failure
                    }
                    if (hadOne) {
                        sb.append(",");
                    } else {
                        hadOne = true;
                    }
                    sb.append(result.shardTarget());
                }

                logger.debug("Moving to second phase, based on results from: {}", sb);
            }
            moveToSecondPhase();
        }

        protected abstract void moveToSecondPhase() throws Exception;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2074.java