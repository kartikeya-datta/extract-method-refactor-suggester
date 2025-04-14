error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3847.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3847.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3847.java
text:
```scala
r@@equest.index(clusterState.metaData().concreteSingleIndex(request.index(), request.indicesOptions()));

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

package org.elasticsearch.action.support.replication;

import com.google.common.collect.Lists;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.ShardOperationFailedException;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.DefaultShardOperationFailedException;
import org.elasticsearch.action.support.TransportAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.block.ClusterBlockException;
import org.elasticsearch.cluster.routing.GroupShardsIterator;
import org.elasticsearch.cluster.routing.ShardIterator;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.BaseTransportRequestHandler;
import org.elasticsearch.transport.TransportChannel;
import org.elasticsearch.transport.TransportService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 *
 */
public abstract class TransportIndexReplicationOperationAction<Request extends IndexReplicationOperationRequest, Response extends ActionResponse, ShardRequest extends ShardReplicationOperationRequest, ShardReplicaRequest extends ShardReplicationOperationRequest, ShardResponse extends ActionResponse>
        extends TransportAction<Request, Response> {

    protected final ClusterService clusterService;

    protected final TransportShardReplicationOperationAction<ShardRequest, ShardReplicaRequest, ShardResponse> shardAction;

    protected TransportIndexReplicationOperationAction(Settings settings, String actionName, TransportService transportService, ClusterService clusterService,
                                                       ThreadPool threadPool, TransportShardReplicationOperationAction<ShardRequest, ShardReplicaRequest, ShardResponse> shardAction, ActionFilters actionFilters) {
        super(settings, actionName, threadPool, actionFilters);
        this.clusterService = clusterService;
        this.shardAction = shardAction;

        transportService.registerHandler(actionName, new TransportHandler());
    }

    @Override
    protected void doExecute(final Request request, final ActionListener<Response> listener) {
        ClusterState clusterState = clusterService.state();
        ClusterBlockException blockException = checkGlobalBlock(clusterState, request);
        if (blockException != null) {
            throw blockException;
        }
        // update to concrete index
        request.index(clusterState.metaData().concreteSingleIndex(request.index()));
        blockException = checkRequestBlock(clusterState, request);
        if (blockException != null) {
            throw blockException;
        }

        GroupShardsIterator groups;
        try {
            groups = shards(request);
        } catch (Throwable e) {
            listener.onFailure(e);
            return;
        }
        final AtomicInteger indexCounter = new AtomicInteger();
        final AtomicInteger failureCounter = new AtomicInteger();
        final AtomicInteger completionCounter = new AtomicInteger(groups.size());
        final AtomicReferenceArray<ShardActionResult> shardsResponses = new AtomicReferenceArray<>(groups.size());

        for (final ShardIterator shardIt : groups) {
            ShardRequest shardRequest = newShardRequestInstance(request, shardIt.shardId().id());

            // TODO for now, we fork operations on shardIt of the index
            shardRequest.beforeLocalFork(); // optimize for local fork
            shardRequest.operationThreaded(true);

            // no need for threaded listener, we will fork when its done based on the index request
            shardRequest.listenerThreaded(false);
            shardAction.execute(shardRequest, new ActionListener<ShardResponse>() {
                @Override
                public void onResponse(ShardResponse result) {
                    shardsResponses.set(indexCounter.getAndIncrement(), new ShardActionResult(result));
                    returnIfNeeded();
                }

                @Override
                public void onFailure(Throwable e) {
                    failureCounter.getAndIncrement();
                    int index = indexCounter.getAndIncrement();
                    if (accumulateExceptions()) {
                        shardsResponses.set(index, new ShardActionResult(
                                new DefaultShardOperationFailedException(request.index, shardIt.shardId().id(), e)));
                    }
                    returnIfNeeded();
                }

                private void returnIfNeeded() {
                    if (completionCounter.decrementAndGet() == 0) {
                        List<ShardResponse> responses = Lists.newArrayList();
                        List<ShardOperationFailedException> failures = Lists.newArrayList();
                        for (int i = 0; i < shardsResponses.length(); i++) {
                            ShardActionResult shardActionResult = shardsResponses.get(i);
                            if (shardActionResult == null) {
                                assert !accumulateExceptions();
                                continue;
                            }
                            if (shardActionResult.isFailure()) {
                                assert accumulateExceptions() && shardActionResult.shardFailure != null;
                                failures.add(shardActionResult.shardFailure);
                            } else {
                                responses.add(shardActionResult.shardResponse);
                            }
                        }

                        assert failures.size() == 0 || failures.size() == failureCounter.get();
                        listener.onResponse(newResponseInstance(request, responses, failureCounter.get(), failures));
                    }
                }
            });
        }
    }

    protected abstract Request newRequestInstance();

    protected abstract Response newResponseInstance(Request request, List<ShardResponse> shardResponses, int failuresCount, List<ShardOperationFailedException> shardFailures);

    protected abstract GroupShardsIterator shards(Request request) throws ElasticsearchException;

    protected abstract ShardRequest newShardRequestInstance(Request request, int shardId);

    protected abstract boolean accumulateExceptions();

    protected abstract ClusterBlockException checkGlobalBlock(ClusterState state, Request request);

    protected abstract ClusterBlockException checkRequestBlock(ClusterState state, Request request);

    private class ShardActionResult {

        private final ShardResponse shardResponse;
        private final ShardOperationFailedException shardFailure;

        private ShardActionResult(ShardResponse shardResponse) {
            assert shardResponse != null;
            this.shardResponse = shardResponse;
            this.shardFailure = null;
        }

        private ShardActionResult(ShardOperationFailedException shardOperationFailedException) {
            assert shardOperationFailedException != null;
            this.shardFailure = shardOperationFailedException;
            this.shardResponse = null;
        }

        boolean isFailure() {
            return shardFailure != null;
        }
    }

    private class TransportHandler extends BaseTransportRequestHandler<Request> {

        @Override
        public Request newInstance() {
            return newRequestInstance();
        }

        @Override
        public String executor() {
            return ThreadPool.Names.SAME;
        }

        @Override
        public void messageReceived(final Request request, final TransportChannel channel) throws Exception {
            // no need to use threaded listener, since we just send a response
            request.listenerThreaded(false);
            execute(request, new ActionListener<Response>() {
                @Override
                public void onResponse(Response result) {
                    try {
                        channel.sendResponse(result);
                    } catch (Throwable e) {
                        onFailure(e);
                    }
                }

                @Override
                public void onFailure(Throwable e) {
                    try {
                        channel.sendResponse(e);
                    } catch (Exception e1) {
                        logger.warn("Failed to send error response for action [" + actionName + "] and request [" + request + "]", e1);
                    }
                }
            });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3847.java