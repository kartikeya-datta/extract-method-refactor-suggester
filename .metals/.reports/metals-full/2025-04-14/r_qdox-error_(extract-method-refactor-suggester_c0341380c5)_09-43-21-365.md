error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6704.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6704.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6704.java
text:
```scala
f@@ailure = new NoShardAvailableActionException(shardIt.shardId(), null, failure);

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

package org.elasticsearch.action.support.single.shard;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.Version;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.IndicesRequest;
import org.elasticsearch.action.NoShardAvailableActionException;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.TransportAction;
import org.elasticsearch.action.support.TransportActions;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.block.ClusterBlockException;
import org.elasticsearch.cluster.block.ClusterBlockLevel;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.cluster.routing.ShardIterator;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.*;

import java.io.IOException;

import static org.elasticsearch.action.support.TransportActions.isShardNotAvailableException;

/**
 * A base class for single shard read operations.
 */
public abstract class TransportShardSingleOperationAction<Request extends SingleShardOperationRequest, Response extends ActionResponse> extends TransportAction<Request, Response> {

    protected final ClusterService clusterService;

    protected final TransportService transportService;

    final String transportShardAction;
    final String executor;

    protected TransportShardSingleOperationAction(Settings settings, String actionName, ThreadPool threadPool, ClusterService clusterService, TransportService transportService, ActionFilters actionFilters) {
        super(settings, actionName, threadPool, actionFilters);
        this.clusterService = clusterService;
        this.transportService = transportService;

        this.transportShardAction = actionName + "[s]";
        this.executor = executor();

        if (!isSubAction()) {
            transportService.registerHandler(actionName, new TransportHandler());
        }
        transportService.registerHandler(transportShardAction, new ShardTransportHandler());
    }

    /**
     * Tells whether the action is a main one or a subaction. Used to decide whether we need to register
     * the main transport handler. In fact if the action is a subaction, its execute method
     * will be called locally to its parent action.
     */
    protected boolean isSubAction() {
        return false;
    }

    @Override
    protected void doExecute(Request request, ActionListener<Response> listener) {
        new AsyncSingleAction(request, listener).start();
    }

    protected abstract String executor();

    protected abstract Response shardOperation(Request request, ShardId shardId) throws ElasticsearchException;

    protected abstract Request newRequest();

    protected abstract Response newResponse();

    protected abstract boolean resolveIndex();

    protected ClusterBlockException checkGlobalBlock(ClusterState state) {
        return state.blocks().globalBlockedException(ClusterBlockLevel.READ);
    }

    protected ClusterBlockException checkRequestBlock(ClusterState state, InternalRequest request) {
        return state.blocks().indexBlockedException(ClusterBlockLevel.READ, request.concreteIndex());
    }

    protected void resolveRequest(ClusterState state, InternalRequest request) {

    }

    protected abstract ShardIterator shards(ClusterState state, InternalRequest request) throws ElasticsearchException;

    class AsyncSingleAction {

        private final ActionListener<Response> listener;
        private final ShardIterator shardIt;
        private final InternalRequest internalRequest;
        private final DiscoveryNodes nodes;
        private volatile Throwable lastFailure;

        private AsyncSingleAction(Request request, ActionListener<Response> listener) {
            this.listener = listener;

            ClusterState clusterState = clusterService.state();
            if (logger.isTraceEnabled()) {
                logger.trace("executing [{}] based on cluster state version [{}]", request, clusterState.version());
            }
            nodes = clusterState.nodes();
            ClusterBlockException blockException = checkGlobalBlock(clusterState);
            if (blockException != null) {
                throw blockException;
            }

            String concreteSingleIndex;
            if (resolveIndex()) {
                concreteSingleIndex = clusterState.metaData().concreteSingleIndex(request.index(), request.indicesOptions());
            } else {
                concreteSingleIndex = request.index();
            }
            this.internalRequest = new InternalRequest(request, concreteSingleIndex);
            resolveRequest(clusterState, internalRequest);

            blockException = checkRequestBlock(clusterState, internalRequest);
            if (blockException != null) {
                throw blockException;
            }

            this.shardIt = shards(clusterState, internalRequest);
        }

        public void start() {
            perform(null);
        }

        private void onFailure(ShardRouting shardRouting, Throwable e) {
            if (logger.isTraceEnabled() && e != null) {
                logger.trace("{}: failed to execute [{}]", e, shardRouting, internalRequest.request());
            }
            perform(e);
        }

        private void perform(@Nullable final Throwable currentFailure) {
            Throwable lastFailure = this.lastFailure;
            if (lastFailure == null || TransportActions.isReadOverrideException(currentFailure)) {
                lastFailure = currentFailure;
                this.lastFailure = currentFailure;
            }
            final ShardRouting shardRouting = shardIt.nextOrNull();
            if (shardRouting == null) {
                Throwable failure = lastFailure;
                if (failure == null || isShardNotAvailableException(failure)) {
                    failure = new NoShardAvailableActionException(shardIt.shardId());
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("{}: failed to execute [{}]", failure, shardIt.shardId(), internalRequest.request());
                    }
                }
                listener.onFailure(failure);
                return;
            }
            if (shardRouting.currentNodeId().equals(nodes.localNodeId())) {
                if (logger.isTraceEnabled()) {
                    logger.trace("executing [{}] on shard [{}]", internalRequest.request(), shardRouting.shardId());
                }
                try {
                    if (internalRequest.request().operationThreaded()) {
                        internalRequest.request().beforeLocalFork();
                        threadPool.executor(executor).execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Response response = shardOperation(internalRequest.request(), shardRouting.shardId());
                                    listener.onResponse(response);
                                } catch (Throwable e) {
                                    onFailure(shardRouting, e);
                                }
                            }
                        });
                    } else {
                        final Response response = shardOperation(internalRequest.request(), shardRouting.shardId());
                        listener.onResponse(response);
                    }
                } catch (Throwable e) {
                    onFailure(shardRouting, e);
                }
            } else {
                DiscoveryNode node = nodes.get(shardRouting.currentNodeId());
                if (node == null) {
                    onFailure(shardRouting, new NoShardAvailableActionException(shardIt.shardId()));
                } else {
                    transportService.sendRequest(node, transportShardAction, new ShardSingleOperationRequest(internalRequest.request(), shardRouting.shardId()), new BaseTransportResponseHandler<Response>() {

                        @Override
                        public Response newInstance() {
                            return newResponse();
                        }

                        @Override
                        public String executor() {
                            return ThreadPool.Names.SAME;
                        }

                        @Override
                        public void handleResponse(final Response response) {
                            listener.onResponse(response);
                        }

                        @Override
                        public void handleException(TransportException exp) {
                            onFailure(shardRouting, exp);
                        }
                    });
                }
            }
        }
    }

    private class TransportHandler extends BaseTransportRequestHandler<Request> {

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
            // no need to have a threaded listener since we just send back a response
            request.listenerThreaded(false);
            // if we have a local operation, execute it on a thread since we don't spawn
            request.operationThreaded(true);
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
                        logger.warn("failed to send response for get", e1);
                    }
                }
            });
        }
    }

    private class ShardTransportHandler extends BaseTransportRequestHandler<ShardSingleOperationRequest> {

        @Override
        public ShardSingleOperationRequest newInstance() {
            return new ShardSingleOperationRequest();
        }

        @Override
        public String executor() {
            return executor;
        }

        @Override
        public void messageReceived(final ShardSingleOperationRequest request, final TransportChannel channel) throws Exception {
            if (logger.isTraceEnabled()) {
                logger.trace("executing [{}] on shard [{}]", request.request(), request.shardId());
            }
            Response response = shardOperation(request.request(), request.shardId());
            channel.sendResponse(response);
        }
    }

    class ShardSingleOperationRequest extends TransportRequest implements IndicesRequest {

        private Request request;

        private ShardId shardId;

        ShardSingleOperationRequest() {
        }

        ShardSingleOperationRequest(Request request, ShardId shardId) {
            super(request);
            this.request = request;
            this.shardId = shardId;
        }

        public Request request() {
            return request;
        }

        public ShardId shardId() {
            return shardId;
        }

        @Override
        public String[] indices() {
            return request.indices();
        }

        @Override
        public IndicesOptions indicesOptions() {
            return request.indicesOptions();
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            request = newRequest();
            request.readFrom(in);
            if (in.getVersion().onOrAfter(Version.V_1_4_0_Beta1)) {
                shardId = ShardId.readShardId(in);
            } else {
                //older nodes will send the concrete index as part of the request
                shardId = new ShardId(request.index(), in.readVInt());
            }
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            if (out.getVersion().before(Version.V_1_4_0_Beta1)) {
                //older nodes expect the concrete index as part of the request
                request.index(shardId.getIndex());
            }
            request.writeTo(out);
            if (out.getVersion().onOrAfter(Version.V_1_4_0_Beta1)) {
                shardId.writeTo(out);
            } else {
                out.writeVInt(shardId.id());
            }
        }
    }

    /**
     * Internal request class that gets built on each node. Holds the original request plus additional info.
     */
    protected class InternalRequest {
        final Request request;
        final String concreteIndex;

        InternalRequest(Request request, String concreteIndex) {
            this.request = request;
            this.concreteIndex = concreteIndex;
        }

        public Request request() {
            return request;
        }

        public String concreteIndex() {
            return concreteIndex;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6704.java