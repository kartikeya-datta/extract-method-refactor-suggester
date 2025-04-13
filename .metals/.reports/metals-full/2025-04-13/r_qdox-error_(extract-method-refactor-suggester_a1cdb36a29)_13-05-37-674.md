error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1123.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1123.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1123.java
text:
```scala
l@@ogger.debug("Master [{}] failed on ping, tried [{}] times, each with [{}] timeout", masterNode, pingRetryCount, pingRetryTimeout);

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

import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.discovery.zen.DiscoveryNodesProvider;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.*;
import org.elasticsearch.util.TimeValue;
import org.elasticsearch.util.component.AbstractComponent;
import org.elasticsearch.util.io.stream.StreamInput;
import org.elasticsearch.util.io.stream.StreamOutput;
import org.elasticsearch.util.io.stream.Streamable;
import org.elasticsearch.util.settings.Settings;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.elasticsearch.cluster.node.DiscoveryNode.*;
import static org.elasticsearch.util.TimeValue.*;

/**
 * A fault detection that pings the master periodically to see if its alive.
 *
 * @author kimchy (shay.banon)
 */
public class MasterFaultDetection extends AbstractComponent {

    public static interface Listener {

        void onMasterFailure(DiscoveryNode masterNode);

        void onDisconnectedFromMaster();
    }

    private final ThreadPool threadPool;

    private final TransportService transportService;

    private final DiscoveryNodesProvider nodesProvider;

    private final CopyOnWriteArrayList<Listener> listeners = new CopyOnWriteArrayList<Listener>();


    private final boolean connectOnNetworkDisconnect;

    private final TimeValue pingInterval;

    private final TimeValue pingRetryTimeout;

    private final int pingRetryCount;

    private final FDConnectionListener connectionListener;

    private volatile DiscoveryNode masterNode;

    private volatile int retryCount;

    private final AtomicBoolean notifiedMasterFailure = new AtomicBoolean();

    public MasterFaultDetection(Settings settings, ThreadPool threadPool, TransportService transportService, DiscoveryNodesProvider nodesProvider) {
        super(settings);
        this.threadPool = threadPool;
        this.transportService = transportService;
        this.nodesProvider = nodesProvider;

        this.connectOnNetworkDisconnect = componentSettings.getAsBoolean("connect_on_network_disconnect", false);
        this.pingInterval = componentSettings.getAsTime("ping_interval", timeValueSeconds(1));
        this.pingRetryTimeout = componentSettings.getAsTime("ping_timeout", timeValueSeconds(6));
        this.pingRetryCount = componentSettings.getAsInt("ping_retries", 5);

        logger.debug("Master FD uses ping_interval [{}], ping_timeout [{}], ping_retries [{}]", pingInterval, pingRetryTimeout, pingRetryCount);

        this.connectionListener = new FDConnectionListener();
        transportService.addConnectionListener(connectionListener);

        transportService.registerHandler(MasterPingRequestHandler.ACTION, new MasterPingRequestHandler());
    }

    public DiscoveryNode masterNode() {
        return this.masterNode;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public void restart(DiscoveryNode masterNode) {
        stop();
        start(masterNode);
    }

    public void start(DiscoveryNode masterNode) {
        this.masterNode = masterNode;
        this.retryCount = 0;
        this.notifiedMasterFailure.set(false);

        // try and connect to make sure we are connected
        try {
            transportService.connectToNode(masterNode);
        } catch (Exception e) {
            notifyMasterFailure(masterNode);
        }

        // start the ping process
        threadPool.schedule(new SendPingRequest(), pingInterval);
    }

    public void stop() {
        // also will stop the next ping schedule
        this.retryCount = 0;
        this.masterNode = null;
    }

    public void close() {
        stop();
        this.listeners.clear();
        transportService.removeConnectionListener(connectionListener);
        transportService.removeHandler(MasterPingRequestHandler.ACTION);
    }

    private void handleTransportDisconnect(DiscoveryNode node) {
        if (!node.equals(this.masterNode)) {
            return;
        }
        if (connectOnNetworkDisconnect) {
            try {
                transportService.connectToNode(node);
            } catch (Exception e) {
                logger.trace("Master [{}] failed on disconnect (with verified connect)", masterNode);
                notifyMasterFailure(masterNode);
            }
        } else {
            logger.trace("Master [{}] failed on disconnect", masterNode);
            notifyMasterFailure(masterNode);
        }
    }

    private void notifyDisconnectedFromMaster() {
        for (Listener listener : listeners) {
            listener.onDisconnectedFromMaster();
        }
        // we don't stop on disconnection from master, we keep pinging it
    }

    private void notifyMasterFailure(DiscoveryNode masterNode) {
        if (notifiedMasterFailure.compareAndSet(false, true)) {
            for (Listener listener : listeners) {
                listener.onMasterFailure(masterNode);
            }
            stop();
        }
    }

    private class FDConnectionListener implements TransportConnectionListener {
        @Override public void onNodeConnected(DiscoveryNode node) {
        }

        @Override public void onNodeDisconnected(DiscoveryNode node) {
            handleTransportDisconnect(node);
        }
    }

    private class SendPingRequest implements Runnable {
        @Override public void run() {
            if (masterNode != null) {
                final DiscoveryNode sentToNode = masterNode;
                transportService.sendRequest(masterNode, MasterPingRequestHandler.ACTION, new MasterPingRequest(nodesProvider.nodes().localNode()), pingRetryTimeout,
                        new BaseTransportResponseHandler<MasterPingResponseResponse>() {
                            @Override public MasterPingResponseResponse newInstance() {
                                return new MasterPingResponseResponse();
                            }

                            @Override public void handleResponse(MasterPingResponseResponse response) {
                                // reset the counter, we got a good result
                                MasterFaultDetection.this.retryCount = 0;
                                // check if the master node did not get switched on us...
                                if (sentToNode.equals(MasterFaultDetection.this.masterNode())) {
                                    if (!response.connectedToMaster) {
                                        logger.trace("Master [{}] does not have us registered with it...", masterNode);
                                        notifyDisconnectedFromMaster();
                                    } else {
                                        threadPool.schedule(SendPingRequest.this, pingInterval);
                                    }
                                }
                            }

                            @Override public void handleException(RemoteTransportException exp) {
                                // check if the master node did not get switched on us...
                                if (sentToNode.equals(MasterFaultDetection.this.masterNode())) {
                                    int retryCount = ++MasterFaultDetection.this.retryCount;
                                    logger.trace("Master [{}] failed to ping, retry [{}] out of [{}]", exp, masterNode, retryCount, pingRetryCount);
                                    if (retryCount >= pingRetryCount) {
                                        logger.trace("Master [{}] failed on ping", masterNode);
                                        // not good, failure
                                        notifyMasterFailure(sentToNode);
                                    }
                                }
                            }
                        });
            }
        }
    }

    private class MasterPingRequestHandler extends BaseTransportRequestHandler<MasterPingRequest> {

        public static final String ACTION = "discovery/zen/fd/masterPing";

        @Override public MasterPingRequest newInstance() {
            return new MasterPingRequest();
        }

        @Override public void messageReceived(MasterPingRequest request, TransportChannel channel) throws Exception {
            DiscoveryNodes nodes = nodesProvider.nodes();
            channel.sendResponse(new MasterPingResponseResponse(nodes.nodeExists(request.node.id())));
        }
    }


    private class MasterPingRequest implements Streamable {

        private DiscoveryNode node;

        private MasterPingRequest() {
        }

        private MasterPingRequest(DiscoveryNode node) {
            this.node = node;
        }

        @Override public void readFrom(StreamInput in) throws IOException {
            node = readNode(in);
        }

        @Override public void writeTo(StreamOutput out) throws IOException {
            node.writeTo(out);
        }
    }

    private class MasterPingResponseResponse implements Streamable {

        private boolean connectedToMaster;

        private MasterPingResponseResponse() {
        }

        private MasterPingResponseResponse(boolean connectedToMaster) {
            this.connectedToMaster = connectedToMaster;
        }

        @Override public void readFrom(StreamInput in) throws IOException {
            connectedToMaster = in.readBoolean();
        }

        @Override public void writeTo(StreamOutput out) throws IOException {
            out.writeBoolean(connectedToMaster);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1123.java