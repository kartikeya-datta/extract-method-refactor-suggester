error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3497.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3497.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3497.java
text:
```scala
S@@tring[] concreteIndices = clusterState.metaData().concreteIndices(request.indicesOptions(), request.indices());

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

package org.elasticsearch.action.support.broadcast;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.NoShardAvailableActionException;
import org.elasticsearch.action.support.TransportAction;
import org.elasticsearch.action.support.TransportActions;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.block.ClusterBlockException;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.cluster.routing.GroupShardsIterator;
import org.elasticsearch.cluster.routing.ShardIterator;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 *
 */
public abstract class TransportBroadcastOperationAction<Request extends BroadcastOperationRequest, Response extends BroadcastOperationResponse, ShardRequest extends BroadcastShardOperationRequest, ShardResponse extends BroadcastShardOperationResponse>
        extends TransportAction<Request, Response> {

    protected final ThreadPool threadPool;
    protected final ClusterService clusterService;
    protected final TransportService transportService;

    final String transportAction;
    final String transportShardAction;
    final String executor;

    protected TransportBroadcastOperationAction(Settings settings, ThreadPool threadPool, ClusterService clusterService, TransportService transportService) {
        super(settings, threadPool);
        this.clusterService = clusterService;
        this.transportService = transportService;
        this.threadPool = threadPool;

        this.transportAction = transportAction();
        this.transportShardAction = transportAction() + "/s";
        this.executor = executor();

        transportService.registerHandler(transportAction, new TransportHandler());
        transportService.registerHandler(transportShardAction, new ShardTransportHandler());
    }

    @Override
    protected void doExecute(Request request, ActionListener<Response> listener) {
        new AsyncBroadcastAction(request, listener).start();
    }

    protected abstract String transportAction();

    protected abstract String executor();

    protected abstract Request newRequest();

    protected abstract Response newResponse(Request request, AtomicReferenceArray shardsResponses, ClusterState clusterState);

    protected abstract ShardRequest newShardRequest();

    protected abstract ShardRequest newShardRequest(ShardRouting shard, Request request);

    protected abstract ShardResponse newShardResponse();

    protected abstract ShardResponse shardOperation(ShardRequest request) throws ElasticsearchException;

    protected abstract GroupShardsIterator shards(ClusterState clusterState, Request request, String[] concreteIndices);

    protected abstract ClusterBlockException checkGlobalBlock(ClusterState state, Request request);

    protected abstract ClusterBlockException checkRequestBlock(ClusterState state, Request request, String[] concreteIndices);

    class AsyncBroadcastAction {

        private final Request request;
        private final ActionListener<Response> listener;
        private final ClusterState clusterState;
        private final DiscoveryNodes nodes;
        private final GroupShardsIterator shardsIts;
        private final int expectedOps;
        private final AtomicInteger counterOps = new AtomicInteger();
        private final AtomicReferenceArray shardsResponses;

        AsyncBroadcastAction(Request request, ActionListener<Response> listener) {
            this.request = request;
            this.listener = listener;

            clusterState = clusterService.state();

            ClusterBlockException blockException = checkGlobalBlock(clusterState, request);
            if (blockException != null) {
                throw blockException;
            }
            // update to concrete indices
            String[] concreteIndices = clusterState.metaData().concreteIndices(request.indices(), request.indicesOptions());
            blockException = checkRequestBlock(clusterState, request, concreteIndices);
            if (blockException != null) {
                throw blockException;
            }

            nodes = clusterState.nodes();
            shardsIts = shards(clusterState, request, concreteIndices);
            expectedOps = shardsIts.size();

            shardsResponses = new AtomicReferenceArray<Object>(expectedOps);
        }

        public void start() {
            if (shardsIts.size() == 0) {
                // no shards
                try {
                    listener.onResponse(newResponse(request, new AtomicReferenceArray(0), clusterState));
                } catch (Throwable e) {
                    listener.onFailure(e);
                }
                return;
            }
            request.beforeStart();
            // count the local operations, and perform the non local ones
            int shardIndex = -1;
            for (final ShardIterator shardIt : shardsIts) {
                shardIndex++;
                final ShardRouting shard = shardIt.nextOrNull();
                if (shard != null) {
                    performOperation(shardIt, shard, shardIndex);
                } else {
                    // really, no shards active in this group
                    onOperation(null, shardIt, shardIndex, new NoShardAvailableActionException(shardIt.shardId()));
                }
            }
        }

        void performOperation(final ShardIterator shardIt, final ShardRouting shard, final int shardIndex) {
            if (shard == null) {
                // no more active shards... (we should not really get here, just safety)
                onOperation(null, shardIt, shardIndex, new NoShardAvailableActionException(shardIt.shardId()));
            } else {
                try {
                    final ShardRequest shardRequest = newShardRequest(shard, request);
                    if (shard.currentNodeId().equals(nodes.localNodeId())) {
                        threadPool.executor(executor).execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    onOperation(shard, shardIndex, shardOperation(shardRequest));
                                } catch (Throwable e) {
                                    onOperation(shard, shardIt, shardIndex, e);
                                }
                            }
                        });
                    } else {
                        DiscoveryNode node = nodes.get(shard.currentNodeId());
                        if (node == null) {
                            // no node connected, act as failure
                            onOperation(shard, shardIt, shardIndex, new NoShardAvailableActionException(shardIt.shardId()));
                        } else {
                            transportService.sendRequest(node, transportShardAction, shardRequest, new BaseTransportResponseHandler<ShardResponse>() {
                                @Override
                                public ShardResponse newInstance() {
                                    return newShardResponse();
                                }

                                @Override
                                public String executor() {
                                    return ThreadPool.Names.SAME;
                                }

                                @Override
                                public void handleResponse(ShardResponse response) {
                                    onOperation(shard, shardIndex, response);
                                }

                                @Override
                                public void handleException(TransportException e) {
                                    onOperation(shard, shardIt, shardIndex, e);
                                }
                            });
                        }
                    }
                } catch (Throwable e) {
                    onOperation(shard, shardIt, shardIndex, e);
                }
            }
        }

        @SuppressWarnings({"unchecked"})
        void onOperation(ShardRouting shard, int shardIndex, ShardResponse response) {
            shardsResponses.set(shardIndex, response);
            if (expectedOps == counterOps.incrementAndGet()) {
                finishHim();
            }
        }

        @SuppressWarnings({"unchecked"})
        void onOperation(@Nullable ShardRouting shard, final ShardIterator shardIt, int shardIndex, Throwable t) {
            // we set the shard failure always, even if its the first in the replication group, and the next one
            // will work (it will just override it...)
            setFailure(shardIt, shardIndex, t);

            ShardRouting nextShard = shardIt.nextOrNull();
            if (nextShard != null) {
                if (t != null) {
                    if (logger.isTraceEnabled()) {
                        if (!TransportActions.isShardNotAvailableException(t)) {
                            logger.trace("{}: failed to execute [{}]", t, shard != null ? shard.shortSummary() : shardIt.shardId(), request);
                        }
                    }
                }
                performOperation(shardIt, nextShard, shardIndex);
            } else {
                if (logger.isDebugEnabled()) {
                    if (t != null) {
                        if (!TransportActions.isShardNotAvailableException(t)) {
                            logger.debug("{}: failed to executed [{}]", t, shard != null ? shard.shortSummary() : shardIt.shardId(), request);
                        }
                    }
                }
                if (expectedOps == counterOps.incrementAndGet()) {
                    finishHim();
                }
            }
        }

        void finishHim() {
            try {
                listener.onResponse(newResponse(request, shardsResponses, clusterState));
            } catch (Throwable e) {
                listener.onFailure(e);
            }
        }

        void setFailure(ShardIterator shardIt, int shardIndex, Throwable t) {
            // we don't aggregate shard failures on non active shards (but do keep the header counts right)
            if (TransportActions.isShardNotAvailableException(t)) {
                return;
            }

            if (!(t instanceof BroadcastShardOperationFailedException)) {
                t = new BroadcastShardOperationFailedException(shardIt.shardId(), t);
            }

            Object response = shardsResponses.get(shardIndex);
            if (response == null) {
                // just override it and return
                shardsResponses.set(shardIndex, t);
            }

            if (!(response instanceof Throwable)) {
                // we should never really get here...
                return;
            }

            // the failure is already present, try and not override it with an exception that is less meaningless
            // for example, getting illegal shard state
            if (TransportActions.isReadOverrideException(t)) {
                shardsResponses.set(shardIndex, t);
            }
        }
    }

    class TransportHandler extends BaseTransportRequestHandler<Request> {

        @Override
        public Request newInstance() {
            return newRequest();
        }

        @Override
        public String executor() {
            return ThreadPool.Names.SAME;
        }

        @Override
        public void messageReceived(Request request, final TransportChannel channel) throws Exception {
            // we just send back a response, no need to fork a listener
            request.listenerThreaded(false);
            execute(request, new ActionListener<Response>() {
                @Override
                public void onResponse(Response response) {
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
                        logger.warn("Failed to send response", e1);
                    }
                }
            });
        }
    }

    class ShardTransportHandler extends BaseTransportRequestHandler<ShardRequest> {

        @Override
        public ShardRequest newInstance() {
            return newShardRequest();
        }

        @Override
        public String executor() {
            return executor;
        }

        @Override
        public void messageReceived(final ShardRequest request, final TransportChannel channel) throws Exception {
            channel.sendResponse(shardOperation(request));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3497.java