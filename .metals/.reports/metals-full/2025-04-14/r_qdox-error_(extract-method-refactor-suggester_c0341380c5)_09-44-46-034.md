error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7726.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7726.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,18]

error in qdox parser
file content:
```java
offset: 18
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7726.java
text:
```scala
@Override public v@@oid handleException(TransportException exp) {

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

package org.elasticsearch.discovery.zen.fd;

import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.elasticsearch.cluster.node.DiscoveryNodes.*;
import static org.elasticsearch.common.unit.TimeValue.*;
import static org.elasticsearch.common.util.concurrent.ConcurrentCollections.*;
import static org.elasticsearch.transport.TransportRequestOptions.*;

/**
 * A fault detection of multiple nodes.
 *
 * @author kimchy (shay.banon)
 */
public class NodesFaultDetection extends AbstractComponent {

    public static interface Listener {

        void onNodeFailure(DiscoveryNode node, String reason);
    }

    private final ThreadPool threadPool;

    private final TransportService transportService;


    private final boolean connectOnNetworkDisconnect;

    private final TimeValue pingInterval;

    private final TimeValue pingRetryTimeout;

    private final int pingRetryCount;

    // used mainly for testing, should always be true
    private final boolean registerConnectionListener;


    private final CopyOnWriteArrayList<Listener> listeners = new CopyOnWriteArrayList<Listener>();

    private final ConcurrentMap<DiscoveryNode, NodeFD> nodesFD = newConcurrentMap();

    private final FDConnectionListener connectionListener;

    private volatile DiscoveryNodes latestNodes = EMPTY_NODES;

    private volatile boolean running = false;

    public NodesFaultDetection(Settings settings, ThreadPool threadPool, TransportService transportService) {
        super(settings);
        this.threadPool = threadPool;
        this.transportService = transportService;

        this.connectOnNetworkDisconnect = componentSettings.getAsBoolean("connect_on_network_disconnect", false);
        this.pingInterval = componentSettings.getAsTime("ping_interval", timeValueSeconds(1));
        this.pingRetryTimeout = componentSettings.getAsTime("ping_timeout", timeValueSeconds(30));
        this.pingRetryCount = componentSettings.getAsInt("ping_retries", 3);
        this.registerConnectionListener = componentSettings.getAsBoolean("register_connection_listener", true);

        logger.debug("[node  ] uses ping_interval [{}], ping_timeout [{}], ping_retries [{}]", pingInterval, pingRetryTimeout, pingRetryCount);

        transportService.registerHandler(PingRequestHandler.ACTION, new PingRequestHandler());

        this.connectionListener = new FDConnectionListener();
        if (registerConnectionListener) {
            transportService.addConnectionListener(connectionListener);
        }
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public void updateNodes(DiscoveryNodes nodes) {
        DiscoveryNodes prevNodes = latestNodes;
        this.latestNodes = nodes;
        if (!running) {
            return;
        }
        DiscoveryNodes.Delta delta = nodes.delta(prevNodes);
        for (DiscoveryNode newNode : delta.addedNodes()) {
            if (newNode.id().equals(nodes.localNodeId())) {
                // no need to monitor the local node
                continue;
            }
            if (!nodesFD.containsKey(newNode)) {
                nodesFD.put(newNode, new NodeFD());
                threadPool.schedule(new SendPingRequest(newNode), pingInterval);
            }
        }
        for (DiscoveryNode removedNode : delta.removedNodes()) {
            nodesFD.remove(removedNode);
        }
    }

    public NodesFaultDetection start() {
        if (running) {
            return this;
        }
        running = true;
        return this;
    }

    public NodesFaultDetection stop() {
        if (!running) {
            return this;
        }
        running = false;
        return this;
    }

    public void close() {
        stop();
        transportService.removeHandler(PingRequestHandler.ACTION);
        transportService.removeConnectionListener(connectionListener);
    }

    private void handleTransportDisconnect(DiscoveryNode node) {
        if (!latestNodes.nodeExists(node.id())) {
            return;
        }
        NodeFD nodeFD = nodesFD.remove(node);
        if (nodeFD == null) {
            return;
        }
        if (!running) {
            return;
        }
        if (connectOnNetworkDisconnect) {
            try {
                transportService.connectToNode(node);
            } catch (Exception e) {
                logger.trace("[node  ] [{}] transport disconnected (with verified connect)", node);
                notifyNodeFailure(node, "transport disconnected (with verified connect)");
            }
        } else {
            logger.trace("[node  ] [{}] transport disconnected", node);
            notifyNodeFailure(node, "transport disconnected");
        }
    }

    private void notifyNodeFailure(final DiscoveryNode node, final String reason) {
        threadPool.cached().execute(new Runnable() {
            @Override public void run() {
                for (Listener listener : listeners) {
                    listener.onNodeFailure(node, reason);
                }
            }
        });
    }

    private class SendPingRequest implements Runnable {

        private final DiscoveryNode node;

        private SendPingRequest(DiscoveryNode node) {
            this.node = node;
        }

        @Override public void run() {
            if (!running) {
                return;
            }
            transportService.sendRequest(node, PingRequestHandler.ACTION, new PingRequest(node.id()), options().withTimeout(pingRetryTimeout),
                    new BaseTransportResponseHandler<PingResponse>() {
                        @Override public PingResponse newInstance() {
                            return new PingResponse();
                        }

                        @Override public void handleResponse(PingResponse response) {
                            if (!running) {
                                return;
                            }
                            NodeFD nodeFD = nodesFD.get(node);
                            if (nodeFD != null) {
                                nodeFD.retryCount = 0;
                                threadPool.schedule(SendPingRequest.this, pingInterval);
                            }
                        }

                        @Override public void handleException(RemoteTransportException exp) {
                            // check if the master node did not get switched on us...
                            if (!running) {
                                return;
                            }
                            NodeFD nodeFD = nodesFD.get(node);
                            if (nodeFD != null) {
                                int retryCount = ++nodeFD.retryCount;
                                logger.trace("[node  ] failed to ping [{}], retry [{}] out of [{}]", exp, node, retryCount, pingRetryCount);
                                if (retryCount >= pingRetryCount) {
                                    logger.debug("[node  ] failed to ping [{}], tried [{}] times, each with  maximum [{}] timeout", node, pingRetryCount, pingRetryTimeout);
                                    // not good, failure
                                    if (nodesFD.remove(node) != null) {
                                        notifyNodeFailure(node, "failed to ping, tried [" + pingRetryCount + "] times, each with maximum [" + pingRetryTimeout + "] timeout");
                                    }
                                } else {
                                    // resend the request, not reschedule, rely on send timeout
                                    transportService.sendRequest(node, PingRequestHandler.ACTION, new PingRequest(node.id()), options().withTimeout(pingRetryTimeout), this);
                                }
                            }
                        }

                        @Override public boolean spawn() {
                            return false; // no need to spawn, we hardly do anything
                        }
                    });
        }
    }

    static class NodeFD {
        volatile int retryCount;
    }

    private class FDConnectionListener implements TransportConnectionListener {
        @Override public void onNodeConnected(DiscoveryNode node) {
        }

        @Override public void onNodeDisconnected(DiscoveryNode node) {
            handleTransportDisconnect(node);
        }
    }


    class PingRequestHandler extends BaseTransportRequestHandler<PingRequest> {

        public static final String ACTION = "discovery/zen/fd/ping";

        @Override public PingRequest newInstance() {
            return new PingRequest();
        }

        @Override public void messageReceived(PingRequest request, TransportChannel channel) throws Exception {
            // if we are not the node we are supposed to be pinged, send an exception
            // this can happen when a kill -9 is sent, and another node is started using the same port
            if (!latestNodes.localNodeId().equals(request.nodeId)) {
                throw new ElasticSearchIllegalStateException("Got pinged as node [" + request.nodeId + "], but I am node [" + latestNodes.localNodeId() + "]");
            }
            channel.sendResponse(new PingResponse());
        }

        @Override public boolean spawn() {
            // no need to spawn here, we just send a response
            return false;
        }
    }


    static class PingRequest implements Streamable {

        // the (assumed) node id we are pinging
        private String nodeId;

        PingRequest() {
        }

        PingRequest(String nodeId) {
            this.nodeId = nodeId;
        }

        @Override public void readFrom(StreamInput in) throws IOException {
            nodeId = in.readUTF();
        }

        @Override public void writeTo(StreamOutput out) throws IOException {
            out.writeUTF(nodeId);
        }
    }

    private static class PingResponse implements Streamable {

        private PingResponse() {
        }

        @Override public void readFrom(StreamInput in) throws IOException {
        }

        @Override public void writeTo(StreamOutput out) throws IOException {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7726.java