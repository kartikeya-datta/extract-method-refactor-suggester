error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/400.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/400.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/400.java
text:
```scala
l@@istener.onFailure(new NodeClosedException(clusterService.localNode()));

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

package org.elasticsearch.action.support.replication;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.ExceptionsHelper;
import org.elasticsearch.action.*;
import org.elasticsearch.action.support.TransportAction;
import org.elasticsearch.action.support.TransportActions;
import org.elasticsearch.cluster.ClusterChangedEvent;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.TimeoutClusterStateListener;
import org.elasticsearch.cluster.action.shard.ShardStateAction;
import org.elasticsearch.cluster.block.ClusterBlockException;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.routing.ShardIterator;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.engine.DocumentAlreadyExistsException;
import org.elasticsearch.index.engine.VersionConflictEngineException;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.node.NodeClosedException;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.elasticsearch.ExceptionsHelper.detailedMessage;

/**
 */
public abstract class TransportShardReplicationOperationAction<Request extends ShardReplicationOperationRequest, ReplicaRequest extends ActionRequest, Response extends ActionResponse> extends TransportAction<Request, Response> {

    protected final TransportService transportService;

    protected final ClusterService clusterService;

    protected final IndicesService indicesService;

    protected final ShardStateAction shardStateAction;

    protected final ReplicationType defaultReplicationType;

    protected final WriteConsistencyLevel defaultWriteConsistencyLevel;

    protected final TransportRequestOptions transportOptions;

    final String transportAction;
    final String transportReplicaAction;
    final String executor;
    final boolean checkWriteConsistency;

    protected TransportShardReplicationOperationAction(Settings settings, TransportService transportService,
                                                       ClusterService clusterService, IndicesService indicesService,
                                                       ThreadPool threadPool, ShardStateAction shardStateAction) {
        super(settings, threadPool);
        this.transportService = transportService;
        this.clusterService = clusterService;
        this.indicesService = indicesService;
        this.shardStateAction = shardStateAction;

        this.transportAction = transportAction();
        this.transportReplicaAction = transportReplicaAction();
        this.executor = executor();
        this.checkWriteConsistency = checkWriteConsistency();

        transportService.registerHandler(transportAction, new OperationTransportHandler());
        transportService.registerHandler(transportReplicaAction, new ReplicaOperationTransportHandler());

        this.transportOptions = transportOptions();

        this.defaultReplicationType = ReplicationType.fromString(settings.get("action.replication_type", "sync"));
        this.defaultWriteConsistencyLevel = WriteConsistencyLevel.fromString(settings.get("action.write_consistency", "quorum"));
    }

    @Override
    protected void doExecute(Request request, ActionListener<Response> listener) {
        new AsyncShardOperationAction(request, listener).start();
    }

    protected abstract Request newRequestInstance();

    protected abstract ReplicaRequest newReplicaRequestInstance();

    protected abstract Response newResponseInstance();

    protected abstract String transportAction();

    protected abstract String executor();

    protected abstract PrimaryResponse<Response, ReplicaRequest> shardOperationOnPrimary(ClusterState clusterState, PrimaryOperationRequest shardRequest);

    protected abstract void shardOperationOnReplica(ReplicaOperationRequest shardRequest);

    /**
     * Called once replica operations have been dispatched on the
     */
    protected void postPrimaryOperation(Request request, PrimaryResponse<Response, ReplicaRequest> response) {

    }

    protected abstract ShardIterator shards(ClusterState clusterState, Request request) throws ElasticSearchException;

    protected abstract boolean checkWriteConsistency();

    protected abstract ClusterBlockException checkGlobalBlock(ClusterState state, Request request);

    protected abstract ClusterBlockException checkRequestBlock(ClusterState state, Request request);

    /**
     * Resolves the request, by default, simply setting the concrete index (if its aliased one). If the resolve
     * means a different execution, then return false here to indicate not to continue and execute this request.
     */
    protected boolean resolveRequest(ClusterState state, Request request, ActionListener<Response> listener) {
        request.index(state.metaData().concreteIndex(request.index()));
        return true;
    }

    protected TransportRequestOptions transportOptions() {
        return TransportRequestOptions.EMPTY;
    }

    /**
     * Should the operations be performed on the replicas as well. Defaults to <tt>false</tt> meaning operations
     * will be executed on the replica.
     */
    protected boolean ignoreReplicas() {
        return false;
    }

    private String transportReplicaAction() {
        return transportAction() + "/replica";
    }

    protected boolean retryPrimaryException(Throwable e) {
        return TransportActions.isShardNotAvailableException(e);
    }

    /**
     * Should an exception be ignored when the operation is performed on the replica.
     */
    boolean ignoreReplicaException(Throwable e) {
        if (TransportActions.isShardNotAvailableException(e)) {
            return true;
        }
        Throwable cause = ExceptionsHelper.unwrapCause(e);
        if (cause instanceof ConnectTransportException) {
            return true;
        }
        // on version conflict or document missing, it means
        // that a news change has crept into the replica, and its fine
        if (cause instanceof VersionConflictEngineException) {
            return true;
        }
        // same here
        if (cause instanceof DocumentAlreadyExistsException) {
            return true;
        }
        return false;
    }

    class OperationTransportHandler extends BaseTransportRequestHandler<Request> {

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
                    } catch (Throwable e1) {
                        logger.warn("Failed to send response for " + transportAction, e1);
                    }
                }
            });
        }
    }

    class ReplicaOperationTransportHandler extends BaseTransportRequestHandler<ReplicaOperationRequest> {

        @Override
        public ReplicaOperationRequest newInstance() {
            return new ReplicaOperationRequest();
        }

        @Override
        public String executor() {
            return executor;
        }

        @Override
        public void messageReceived(final ReplicaOperationRequest request, final TransportChannel channel) throws Exception {
            shardOperationOnReplica(request);
            channel.sendResponse(TransportResponse.Empty.INSTANCE);
        }
    }

    protected class PrimaryOperationRequest implements Streamable {

        public int shardId;

        public Request request;

        public PrimaryOperationRequest() {
        }

        public PrimaryOperationRequest(int shardId, Request request) {
            this.shardId = shardId;
            this.request = request;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            shardId = in.readVInt();
            request = newRequestInstance();
            request.readFrom(in);
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            out.writeVInt(shardId);
            request.writeTo(out);
        }
    }

    protected class ReplicaOperationRequest extends TransportRequest {

        public int shardId;
        public ReplicaRequest request;

        public ReplicaOperationRequest() {
        }

        public ReplicaOperationRequest(int shardId, ReplicaRequest request) {
            super(request);
            this.shardId = shardId;
            this.request = request;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            shardId = in.readVInt();
            request = newReplicaRequestInstance();
            request.readFrom(in);
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            out.writeVInt(shardId);
            request.writeTo(out);
        }
    }

    protected class AsyncShardOperationAction {

        private final ActionListener<Response> listener;

        private final Request request;

        private ClusterState clusterState;

        private ShardIterator shardIt;

        private final AtomicBoolean primaryOperationStarted = new AtomicBoolean();

        private final ReplicationType replicationType;

        AsyncShardOperationAction(Request request, ActionListener<Response> listener) {
            this.request = request;
            this.listener = listener;

            if (request.replicationType() != ReplicationType.DEFAULT) {
                replicationType = request.replicationType();
            } else {
                replicationType = defaultReplicationType;
            }
        }

        public void start() {
            start(false);
        }

        /**
         * Returns <tt>true</tt> if the action starting to be performed on the primary (or is done).
         */
        public boolean start(final boolean fromClusterEvent) throws ElasticSearchException {
            this.clusterState = clusterService.state();
            try {
                ClusterBlockException blockException = checkGlobalBlock(clusterState, request);
                if (blockException != null) {
                    if (blockException.retryable()) {
                        retry(fromClusterEvent, blockException);
                        return false;
                    } else {
                        throw blockException;
                    }
                }
                // check if we need to execute, and if not, return
                if (!resolveRequest(clusterState, request, listener)) {
                    return true;
                }
                blockException = checkRequestBlock(clusterState, request);
                if (blockException != null) {
                    if (blockException.retryable()) {
                        retry(fromClusterEvent, blockException);
                        return false;
                    } else {
                        throw blockException;
                    }
                }
                shardIt = shards(clusterState, request);
            } catch (Throwable e) {
                listener.onFailure(e);
                return true;
            }

            // no shardIt, might be in the case between index gateway recovery and shardIt initialization
            if (shardIt.size() == 0) {
                retry(fromClusterEvent, null);
                return false;
            }

            boolean foundPrimary = false;
            ShardRouting shardX;
            while ((shardX = shardIt.nextOrNull()) != null) {
                final ShardRouting shard = shardX;
                // we only deal with primary shardIt here...
                if (!shard.primary()) {
                    continue;
                }
                if (!shard.active() || !clusterState.nodes().nodeExists(shard.currentNodeId())) {
                    retry(fromClusterEvent, null);
                    return false;
                }

                // check here for consistency
                if (checkWriteConsistency) {
                    WriteConsistencyLevel consistencyLevel = defaultWriteConsistencyLevel;
                    if (request.consistencyLevel() != WriteConsistencyLevel.DEFAULT) {
                        consistencyLevel = request.consistencyLevel();
                    }
                    int requiredNumber = 1;
                    if (consistencyLevel == WriteConsistencyLevel.QUORUM && shardIt.size() > 2) {
                        // only for more than 2 in the number of shardIt it makes sense, otherwise its 1 shard with 1 replica, quorum is 1 (which is what it is initialized to)
                        requiredNumber = (shardIt.size() / 2) + 1;
                    } else if (consistencyLevel == WriteConsistencyLevel.ALL) {
                        requiredNumber = shardIt.size();
                    }

                    if (shardIt.sizeActive() < requiredNumber) {
                        retry(fromClusterEvent, null);
                        return false;
                    }
                }

                if (!primaryOperationStarted.compareAndSet(false, true)) {
                    return true;
                }

                foundPrimary = true;
                if (shard.currentNodeId().equals(clusterState.nodes().localNodeId())) {
                    if (request.operationThreaded()) {
                        request.beforeLocalFork();
                        threadPool.executor(executor).execute(new Runnable() {
                            @Override
                            public void run() {
                                performOnPrimary(shard.id(), fromClusterEvent, shard, clusterState);
                            }
                        });
                    } else {
                        performOnPrimary(shard.id(), fromClusterEvent, shard, clusterState);
                    }
                } else {
                    DiscoveryNode node = clusterState.nodes().get(shard.currentNodeId());
                    transportService.sendRequest(node, transportAction, request, transportOptions, new BaseTransportResponseHandler<Response>() {

                        @Override
                        public Response newInstance() {
                            return newResponseInstance();
                        }

                        @Override
                        public String executor() {
                            return ThreadPool.Names.SAME;
                        }

                        @Override
                        public void handleResponse(Response response) {
                            listener.onResponse(response);
                        }

                        @Override
                        public void handleException(TransportException exp) {
                            // if we got disconnected from the node, or the node / shard is not in the right state (being closed)
                            if (exp.unwrapCause() instanceof ConnectTransportException || exp.unwrapCause() instanceof NodeClosedException ||
                                    retryPrimaryException(exp)) {
                                primaryOperationStarted.set(false);
                                // we already marked it as started when we executed it (removed the listener) so pass false
                                // to re-add to the cluster listener
                                retry(false, null);
                            } else {
                                listener.onFailure(exp);
                            }
                        }
                    });
                }
                break;
            }
            // we won't find a primary if there are no shards in the shard iterator, retry...
            if (!foundPrimary) {
                retry(fromClusterEvent, null);
                return false;
            }
            return true;
        }

        void retry(boolean fromClusterEvent, @Nullable final Throwable failure) {
            if (!fromClusterEvent) {
                // make it threaded operation so we fork on the discovery listener thread
                request.beforeLocalFork();
                request.operationThreaded(true);
                clusterService.add(request.timeout(), new TimeoutClusterStateListener() {
                    @Override
                    public void postAdded() {
                        if (start(true)) {
                            // if we managed to start and perform the operation on the primary, we can remove this listener
                            clusterService.remove(this);
                        }
                    }

                    @Override
                    public void onClose() {
                        clusterService.remove(this);
                        listener.onFailure(new NodeClosedException(clusterState.nodes().localNode()));
                    }

                    @Override
                    public void clusterChanged(ClusterChangedEvent event) {
                        if (start(true)) {
                            // if we managed to start and perform the operation on the primary, we can remove this listener
                            clusterService.remove(this);
                        }
                    }

                    @Override
                    public void onTimeout(TimeValue timeValue) {
                        // just to be on the safe side, see if we can start it now?
                        if (start(true)) {
                            clusterService.remove(this);
                            return;
                        }
                        clusterService.remove(this);
                        Throwable listenerFailure = failure;
                        if (listenerFailure == null) {
                            if (shardIt == null) {
                                listenerFailure = new UnavailableShardsException(null, "no available shards: Timeout waiting for [" + timeValue + "], request: " + request.toString());
                            } else {
                                listenerFailure = new UnavailableShardsException(shardIt.shardId(), "[" + shardIt.size() + "] shardIt, [" + shardIt.sizeActive() + "] active : Timeout waiting for [" + timeValue + "], request: " + request.toString());
                            }
                        }
                        listener.onFailure(listenerFailure);
                    }
                });
            }
        }

        void performOnPrimary(int primaryShardId, boolean fromDiscoveryListener, final ShardRouting shard, ClusterState clusterState) {
            try {
                PrimaryResponse<Response, ReplicaRequest> response = shardOperationOnPrimary(clusterState, new PrimaryOperationRequest(primaryShardId, request));
                performReplicas(response);
            } catch (Throwable e) {
                // shard has not been allocated yet, retry it here
                if (retryPrimaryException(e)) {
                    primaryOperationStarted.set(false);
                    retry(fromDiscoveryListener, null);
                    return;
                }
                if (e instanceof ElasticSearchException && ((ElasticSearchException) e).status() == RestStatus.CONFLICT) {
                    if (logger.isTraceEnabled()) {
                        logger.trace(shard.shortSummary() + ": Failed to execute [" + request + "]", e);
                    }
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug(shard.shortSummary() + ": Failed to execute [" + request + "]", e);
                    }
                }
                listener.onFailure(e);
            }
        }

        void performReplicas(final PrimaryResponse<Response, ReplicaRequest> response) {
            if (ignoreReplicas()) {
                postPrimaryOperation(request, response);
                listener.onResponse(response.response());
                return;
            }

            ShardRouting shard;

            // we double check on the state, if it got changed we need to make sure we take the latest one cause
            // maybe a replica shard started its recovery process and we need to apply it there...

            // we also need to make sure if the new state has a new primary shard (that we indexed to before) started
            // and assigned to another node (while the indexing happened). In that case, we want to apply it on the
            // new primary shard as well...
            ClusterState newState = clusterService.state();
            ShardRouting newPrimaryShard = null;
            if (clusterState != newState) {
                shardIt.reset();
                ShardRouting originalPrimaryShard = null;
                while ((shard = shardIt.nextOrNull()) != null) {
                    if (shard.primary()) {
                        originalPrimaryShard = shard;
                        break;
                    }
                }
                if (originalPrimaryShard == null || !originalPrimaryShard.active()) {
                    throw new ElasticSearchIllegalStateException("unexpected state, failed to find primary shard on an index operation that succeeded");
                }

                clusterState = newState;
                shardIt = shards(newState, request);
                while ((shard = shardIt.nextOrNull()) != null) {
                    if (shard.primary()) {
                        if (originalPrimaryShard.currentNodeId().equals(shard.currentNodeId())) {
                            newPrimaryShard = null;
                        } else {
                            newPrimaryShard = shard;
                        }
                        break;
                    }
                }
            }

            // initialize the counter
            int replicaCounter = shardIt.assignedReplicasIncludingRelocating();

            if (newPrimaryShard != null) {
                replicaCounter++;
            }

            if (replicaCounter == 0) {
                postPrimaryOperation(request, response);
                listener.onResponse(response.response());
                return;
            }

            if (replicationType == ReplicationType.ASYNC) {
                postPrimaryOperation(request, response);
                // async replication, notify the listener
                listener.onResponse(response.response());
                // now, trick the counter so it won't decrease to 0 and notify the listeners
                replicaCounter = Integer.MIN_VALUE;
            }

            // we add one to the replica count to do the postPrimaryOperation
            replicaCounter++;
            AtomicInteger counter = new AtomicInteger(replicaCounter);

            if (newPrimaryShard != null) {
                performOnReplica(response, counter, newPrimaryShard, newPrimaryShard.currentNodeId());
            }

            shardIt.reset(); // reset the iterator
            while ((shard = shardIt.nextOrNull()) != null) {
                // if its unassigned, nothing to do here...
                if (shard.unassigned()) {
                    continue;
                }

                // if the shard is primary and relocating, add one to the counter since we perform it on the replica as well
                // (and we already did it on the primary)
                boolean doOnlyOnRelocating = false;
                if (shard.primary()) {
                    if (shard.relocating()) {
                        doOnlyOnRelocating = true;
                    } else {
                        continue;
                    }
                }
                // we index on a replica that is initializing as well since we might not have got the event
                // yet that it was started. We will get an exception IllegalShardState exception if its not started
                // and that's fine, we will ignore it
                if (!doOnlyOnRelocating) {
                    performOnReplica(response, counter, shard, shard.currentNodeId());
                }
                if (shard.relocating()) {
                    performOnReplica(response, counter, shard, shard.relocatingNodeId());
                }
            }

            // now do the postPrimary operation, and check if the listener needs to be invoked
            postPrimaryOperation(request, response);
            // we also invoke here in case replicas finish before postPrimaryAction does
            if (counter.decrementAndGet() == 0) {
                listener.onResponse(response.response());
            }
        }

        void performOnReplica(final PrimaryResponse<Response, ReplicaRequest> response, final AtomicInteger counter, final ShardRouting shard, String nodeId) {
            // if we don't have that node, it means that it might have failed and will be created again, in
            // this case, we don't have to do the operation, and just let it failover
            if (!clusterState.nodes().nodeExists(nodeId)) {
                if (counter.decrementAndGet() == 0) {
                    listener.onResponse(response.response());
                }
                return;
            }

            final ReplicaOperationRequest shardRequest = new ReplicaOperationRequest(shardIt.shardId().id(), response.replicaRequest());
            if (!nodeId.equals(clusterState.nodes().localNodeId())) {
                DiscoveryNode node = clusterState.nodes().get(nodeId);
                transportService.sendRequest(node, transportReplicaAction, shardRequest, transportOptions, new EmptyTransportResponseHandler(ThreadPool.Names.SAME) {
                    @Override
                    public void handleResponse(TransportResponse.Empty vResponse) {
                        finishIfPossible();
                    }

                    @Override
                    public void handleException(TransportException exp) {
                        if (!ignoreReplicaException(exp.unwrapCause())) {
                            logger.warn("Failed to perform " + transportAction + " on replica " + shardIt.shardId(), exp);
                            shardStateAction.shardFailed(shard, "Failed to perform [" + transportAction + "] on replica, message [" + detailedMessage(exp) + "]");
                        }
                        finishIfPossible();
                    }

                    private void finishIfPossible() {
                        if (counter.decrementAndGet() == 0) {
                            listener.onResponse(response.response());
                        }
                    }
                });
            } else {
                if (request.operationThreaded()) {
                    request.beforeLocalFork();
                    threadPool.executor(executor).execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                shardOperationOnReplica(shardRequest);
                            } catch (Throwable e) {
                                if (!ignoreReplicaException(e)) {
                                    logger.warn("Failed to perform " + transportAction + " on replica " + shardIt.shardId(), e);
                                    shardStateAction.shardFailed(shard, "Failed to perform [" + transportAction + "] on replica, message [" + detailedMessage(e) + "]");
                                }
                            }
                            if (counter.decrementAndGet() == 0) {
                                listener.onResponse(response.response());
                            }
                        }
                    });
                } else {
                    try {
                        shardOperationOnReplica(shardRequest);
                    } catch (Throwable e) {
                        if (!ignoreReplicaException(e)) {
                            logger.warn("Failed to perform " + transportAction + " on replica" + shardIt.shardId(), e);
                            shardStateAction.shardFailed(shard, "Failed to perform [" + transportAction + "] on replica, message [" + detailedMessage(e) + "]");
                        }
                    }
                    if (counter.decrementAndGet() == 0) {
                        listener.onResponse(response.response());
                    }
                }
            }
        }
    }

    public static class PrimaryResponse<Response, ReplicaRequest> {
        private final ReplicaRequest replicaRequest;
        private final Response response;
        private final Object payload;

        public PrimaryResponse(ReplicaRequest replicaRequest, Response response, Object payload) {
            this.replicaRequest = replicaRequest;
            this.response = response;
            this.payload = payload;
        }

        public ReplicaRequest replicaRequest() {
            return this.replicaRequest;
        }

        public Response response() {
            return response;
        }

        public Object payload() {
            return payload;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/400.java