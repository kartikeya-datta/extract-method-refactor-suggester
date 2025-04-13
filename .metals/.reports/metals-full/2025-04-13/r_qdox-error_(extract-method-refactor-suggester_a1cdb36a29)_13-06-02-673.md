error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8057.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8057.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8057.java
text:
```scala
t@@hreadPool.generic().execute(new Runnable() {

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

package org.elasticsearch.action.support.nodes;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.FailedNodeException;
import org.elasticsearch.action.NoSuchNodeException;
import org.elasticsearch.action.support.TransportAction;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 *
 */
public abstract class TransportNodesOperationAction<Request extends NodesOperationRequest, Response extends NodesOperationResponse, NodeRequest extends NodeOperationRequest, NodeResponse extends NodeOperationResponse> extends TransportAction<Request, Response> {

    protected final ClusterName clusterName;

    protected final ClusterService clusterService;

    protected final TransportService transportService;

    final String transportAction;
    final String transportNodeAction;
    final String executor;

    @Inject
    public TransportNodesOperationAction(Settings settings, ClusterName clusterName, ThreadPool threadPool,
                                         ClusterService clusterService, TransportService transportService) {
        super(settings, threadPool);
        this.clusterName = clusterName;
        this.clusterService = clusterService;
        this.transportService = transportService;

        this.transportAction = transportAction();
        this.transportNodeAction = transportAction() + "/n";
        this.executor = executor();

        transportService.registerHandler(transportAction, new TransportHandler());
        transportService.registerHandler(transportNodeAction, new NodeTransportHandler());
    }

    @Override
    protected void doExecute(Request request, ActionListener<Response> listener) {
        new AsyncAction(request, listener).start();
    }

    protected abstract String transportAction();

    protected boolean transportCompress() {
        return false;
    }

    protected abstract String executor();

    protected abstract Request newRequest();

    protected abstract Response newResponse(Request request, AtomicReferenceArray nodesResponses);

    protected abstract NodeRequest newNodeRequest();

    protected abstract NodeRequest newNodeRequest(String nodeId, Request request);

    protected abstract NodeResponse newNodeResponse();

    protected abstract NodeResponse nodeOperation(NodeRequest request) throws ElasticSearchException;

    protected abstract boolean accumulateExceptions();

    protected String[] filterNodeIds(DiscoveryNodes nodes, String[] nodesIds) {
        return nodesIds;
    }

    private class AsyncAction {

        private final Request request;

        private final String[] nodesIds;

        private final ActionListener<Response> listener;

        private final ClusterState clusterState;

        private final AtomicReferenceArray<Object> responses;

        private final AtomicInteger index = new AtomicInteger();

        private final AtomicInteger counter = new AtomicInteger();

        private AsyncAction(Request request, ActionListener<Response> listener) {
            this.request = request;
            this.listener = listener;
            clusterState = clusterService.state();
            String[] nodesIds = clusterState.nodes().resolveNodes(request.nodesIds());
            this.nodesIds = filterNodeIds(clusterState.nodes(), nodesIds);
            this.responses = new AtomicReferenceArray<Object>(this.nodesIds.length);
        }

        private void start() {
            if (nodesIds.length == 0) {
                // nothing to notify
                threadPool.cached().execute(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResponse(newResponse(request, responses));
                    }
                });
                return;
            }
            TransportRequestOptions transportRequestOptions = TransportRequestOptions.options();
            if (request.timeout() != null) {
                transportRequestOptions.withTimeout(request.timeout());
            }
            transportRequestOptions.withCompress(transportCompress());
            for (final String nodeId : nodesIds) {
                final DiscoveryNode node = clusterState.nodes().nodes().get(nodeId);
                if (nodeId.equals("_local") || nodeId.equals(clusterState.nodes().localNodeId())) {
                    threadPool.executor(executor()).execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                onOperation(nodeOperation(newNodeRequest(clusterState.nodes().localNodeId(), request)));
                            } catch (Exception e) {
                                onFailure(clusterState.nodes().localNodeId(), e);
                            }
                        }
                    });
                } else if (nodeId.equals("_master")) {
                    threadPool.executor(executor()).execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                onOperation(nodeOperation(newNodeRequest(clusterState.nodes().masterNodeId(), request)));
                            } catch (Exception e) {
                                onFailure(clusterState.nodes().masterNodeId(), e);
                            }
                        }
                    });
                } else {
                    if (node == null) {
                        onFailure(nodeId, new NoSuchNodeException(nodeId));
                    } else {
                        NodeRequest nodeRequest = newNodeRequest(nodeId, request);
                        transportService.sendRequest(node, transportNodeAction, nodeRequest, transportRequestOptions, new BaseTransportResponseHandler<NodeResponse>() {
                            @Override
                            public NodeResponse newInstance() {
                                return newNodeResponse();
                            }

                            @Override
                            public void handleResponse(NodeResponse response) {
                                onOperation(response);
                            }

                            @Override
                            public void handleException(TransportException exp) {
                                onFailure(node.id(), exp);
                            }

                            @Override
                            public String executor() {
                                return ThreadPool.Names.SAME;
                            }
                        });
                    }
                }
            }
        }

        private void onOperation(NodeResponse nodeResponse) {
            // need two counters to avoid race conditions
            responses.set(index.getAndIncrement(), nodeResponse);
            if (counter.incrementAndGet() == responses.length()) {
                finishHim();
            }
        }

        private void onFailure(String nodeId, Throwable t) {
            if (logger.isDebugEnabled()) {
                logger.debug("failed to execute on node [{}]", t, nodeId);
            }
            int idx = index.getAndIncrement();
            if (accumulateExceptions()) {
                responses.set(idx, new FailedNodeException(nodeId, "Failed node [" + nodeId + "]", t));
            }
            if (counter.incrementAndGet() == responses.length()) {
                finishHim();
            }
        }

        private void finishHim() {
            listener.onResponse(newResponse(request, responses));
        }
    }

    private class TransportHandler extends BaseTransportRequestHandler<Request> {

        @Override
        public Request newInstance() {
            return newRequest();
        }

        @Override
        public void messageReceived(final Request request, final TransportChannel channel) throws Exception {
            request.listenerThreaded(false);
            execute(request, new ActionListener<Response>() {
                @Override
                public void onResponse(Response response) {
                    TransportResponseOptions options = TransportResponseOptions.options().withCompress(transportCompress());
                    try {
                        channel.sendResponse(response, options);
                    } catch (Exception e) {
                        onFailure(e);
                    }
                }

                @Override
                public void onFailure(Throwable e) {
                    try {
                        channel.sendResponse(e);
                    } catch (Exception e1) {
                        logger.warn("Failed to send response", e);
                    }
                }
            });
        }

        @Override
        public String executor() {
            return ThreadPool.Names.SAME;
        }

        @Override
        public String toString() {
            return transportAction;
        }
    }

    private class NodeTransportHandler extends BaseTransportRequestHandler<NodeRequest> {

        @Override
        public NodeRequest newInstance() {
            return newNodeRequest();
        }

        @Override
        public void messageReceived(final NodeRequest request, final TransportChannel channel) throws Exception {
            channel.sendResponse(nodeOperation(request));
        }

        @Override
        public String toString() {
            return transportNodeAction;
        }

        @Override
        public String executor() {
            return executor;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8057.java