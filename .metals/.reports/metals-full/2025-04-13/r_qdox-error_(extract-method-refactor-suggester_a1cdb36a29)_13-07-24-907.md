error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9692.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9692.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9692.java
text:
```scala
t@@his.disabled = settings.getAsBoolean("action.disable_shutdown", componentSettings.getAsBoolean("disabled", false));

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

package org.elasticsearch.action.admin.cluster.node.shutdown;

import com.google.common.collect.Sets;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.action.TransportActions;
import org.elasticsearch.action.support.master.TransportMasterNodeOperationAction;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.io.stream.VoidStreamable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.node.Node;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.*;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 *
 */
public class TransportNodesShutdownAction extends TransportMasterNodeOperationAction<NodesShutdownRequest, NodesShutdownResponse> {

    private final Node node;

    private final ClusterName clusterName;

    private final boolean disabled;

    private final TimeValue delay;

    @Inject
    public TransportNodesShutdownAction(Settings settings, TransportService transportService, ClusterService clusterService, ThreadPool threadPool,
                                        Node node, ClusterName clusterName) {
        super(settings, transportService, clusterService, threadPool);
        this.node = node;
        this.clusterName = clusterName;
        this.disabled = componentSettings.getAsBoolean("disabled", false);
        this.delay = componentSettings.getAsTime("delay", TimeValue.timeValueMillis(200));

        this.transportService.registerHandler(NodeShutdownRequestHandler.ACTION, new NodeShutdownRequestHandler());
    }

    @Override
    protected String executor() {
        return ThreadPool.Names.CACHED;
    }

    @Override
    protected String transportAction() {
        return TransportActions.Admin.Cluster.Node.SHUTDOWN;
    }

    @Override
    protected NodesShutdownRequest newRequest() {
        return new NodesShutdownRequest();
    }

    @Override
    protected NodesShutdownResponse newResponse() {
        return new NodesShutdownResponse();
    }

    @Override
    protected void processBeforeDelegationToMaster(NodesShutdownRequest request, ClusterState state) {
        String[] nodesIds = request.nodesIds;
        if (nodesIds != null) {
            for (int i = 0; i < nodesIds.length; i++) {
                // replace the _local one, since it looses its meaning when going over to the master...
                if ("_local".equals(nodesIds[i])) {
                    nodesIds[i] = state.nodes().localNodeId();
                }
            }
        }
    }

    @Override
    protected NodesShutdownResponse masterOperation(final NodesShutdownRequest request, final ClusterState state) throws ElasticSearchException {
        if (disabled) {
            throw new ElasticSearchIllegalStateException("Shutdown is disabled");
        }
        Set<DiscoveryNode> nodes = Sets.newHashSet();
        if (state.nodes().isAllNodes(request.nodesIds)) {
            logger.info("[cluster_shutdown]: requested, shutting down in [{}]", request.delay);
            nodes.addAll(state.nodes().dataNodes().values());
            nodes.addAll(state.nodes().masterNodes().values());
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(request.delay.millis());
                    } catch (InterruptedException e) {
                        // ignore
                    }
                    // first, stop the cluster service
                    logger.trace("[cluster_shutdown]: stopping the cluster service so no re-routing will occur");
                    clusterService.stop();

                    final CountDownLatch latch = new CountDownLatch(state.nodes().size());
                    for (final DiscoveryNode node : state.nodes()) {
                        if (node.id().equals(state.nodes().masterNodeId())) {
                            // don't shutdown the master yet...
                            latch.countDown();
                        } else {
                            logger.trace("[cluster_shutdown]: sending shutdown request to [{}]", node);
                            transportService.sendRequest(node, NodeShutdownRequestHandler.ACTION, new NodeShutdownRequest(request.exit), new VoidTransportResponseHandler(ThreadPool.Names.SAME) {
                                @Override
                                public void handleResponse(VoidStreamable response) {
                                    logger.trace("[cluster_shutdown]: received shutdown response from [{}]", node);
                                    latch.countDown();
                                }

                                @Override
                                public void handleException(TransportException exp) {
                                    logger.warn("[cluster_shutdown]: received failed shutdown response from [{}]", exp, node);
                                    latch.countDown();
                                }
                            });
                        }
                    }
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        // ignore
                    }
                    logger.info("[cluster_shutdown]: done shutting down all nodes except master, proceeding to master");

                    // now, kill the master
                    logger.trace("[cluster_shutdown]: shutting down the master [{}]", state.nodes().masterNode());
                    transportService.sendRequest(state.nodes().masterNode(), NodeShutdownRequestHandler.ACTION, new NodeShutdownRequest(request.exit), new VoidTransportResponseHandler(ThreadPool.Names.SAME) {
                        @Override
                        public void handleResponse(VoidStreamable response) {
                            logger.trace("[cluster_shutdown]: received shutdown response from master");
                        }

                        @Override
                        public void handleException(TransportException exp) {
                            logger.warn("[cluster_shutdown]: received failed shutdown response master", exp);
                        }
                    });
                }
            });
            t.start();
        } else {
            final String[] nodesIds = state.nodes().resolveNodes(request.nodesIds);
            logger.info("[partial_cluster_shutdown]: requested, shutting down [{}] in [{}]", nodesIds, request.delay);

            for (String nodeId : nodesIds) {
                final DiscoveryNode node = state.nodes().get(nodeId);
                if (node != null) {
                    nodes.add(node);
                }
            }

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(request.delay.millis());
                    } catch (InterruptedException e) {
                        // ignore
                    }

                    final CountDownLatch latch = new CountDownLatch(nodesIds.length);
                    for (String nodeId : nodesIds) {
                        final DiscoveryNode node = state.nodes().get(nodeId);
                        if (node == null) {
                            logger.warn("[partial_cluster_shutdown]: no node to shutdown for node_id [{}]", nodeId);
                            latch.countDown();
                            continue;
                        }

                        logger.trace("[partial_cluster_shutdown]: sending shutdown request to [{}]", node);
                        transportService.sendRequest(node, NodeShutdownRequestHandler.ACTION, new NodeShutdownRequest(request.exit), new VoidTransportResponseHandler(ThreadPool.Names.SAME) {
                            @Override
                            public void handleResponse(VoidStreamable response) {
                                logger.trace("[partial_cluster_shutdown]: received shutdown response from [{}]", node);
                                latch.countDown();
                            }

                            @Override
                            public void handleException(TransportException exp) {
                                logger.warn("[partial_cluster_shutdown]: received failed shutdown response from [{}]", exp, node);
                                latch.countDown();
                            }
                        });
                    }

                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        // ignore
                    }

                    logger.info("[partial_cluster_shutdown]: done shutting down [{}]", ((Object) nodesIds));
                }
            });
            t.start();
        }
        return new NodesShutdownResponse(clusterName, nodes.toArray(new DiscoveryNode[nodes.size()]));
    }

    private class NodeShutdownRequestHandler extends BaseTransportRequestHandler<NodeShutdownRequest> {

        static final String ACTION = "/cluster/nodes/shutdown/node";

        @Override
        public NodeShutdownRequest newInstance() {
            return new NodeShutdownRequest();
        }

        @Override
        public String executor() {
            return ThreadPool.Names.SAME;
        }

        @Override
        public void messageReceived(final NodeShutdownRequest request, TransportChannel channel) throws Exception {
            if (disabled) {
                throw new ElasticSearchIllegalStateException("Shutdown is disabled");
            }
            logger.info("shutting down in [{}]", delay);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(delay.millis());
                    } catch (InterruptedException e) {
                        // ignore
                    }
                    if (!request.exit) {
                        logger.info("initiating requested shutdown (no exit)...");
                        try {
                            node.close();
                        } catch (Exception e) {
                            logger.warn("Failed to shutdown", e);
                        }
                        return;
                    }
                    boolean shutdownWithWrapper = false;
                    if (System.getProperty("elasticsearch-service") != null) {
                        try {
                            Class wrapperManager = settings.getClassLoader().loadClass("org.tanukisoftware.wrapper.WrapperManager");
                            logger.info("initiating requested shutdown (using service)");
                            wrapperManager.getMethod("stopAndReturn", int.class).invoke(null, 0);
                            shutdownWithWrapper = true;
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                    if (!shutdownWithWrapper) {
                        logger.info("initiating requested shutdown...");
                        try {
                            node.close();
                        } catch (Exception e) {
                            logger.warn("Failed to shutdown", e);
                        } finally {
                            // make sure we initiate the shutdown hooks, so the Bootstrap#main thread will exit
                            System.exit(0);
                        }
                    }
                }
            });
            t.start();

            channel.sendResponse(VoidStreamable.INSTANCE);
        }
    }

    static class NodeShutdownRequest implements Streamable {

        boolean exit;

        NodeShutdownRequest() {
        }

        NodeShutdownRequest(boolean exit) {
            this.exit = exit;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            exit = in.readBoolean();
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            out.writeBoolean(exit);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9692.java