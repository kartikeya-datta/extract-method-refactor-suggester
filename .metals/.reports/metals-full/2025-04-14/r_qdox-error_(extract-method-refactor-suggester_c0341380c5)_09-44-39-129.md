error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3529.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3529.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3529.java
text:
```scala
t@@hrow new ZenPingException("Failed to send ping request over multicast on " + multicastSocket, e);

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

package org.elasticsearch.discovery.zen.ping.multicast;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.io.stream.*;
import org.elasticsearch.common.network.NetworkService;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.discovery.DiscoveryException;
import org.elasticsearch.discovery.zen.DiscoveryNodesProvider;
import org.elasticsearch.discovery.zen.ping.ZenPing;
import org.elasticsearch.discovery.zen.ping.ZenPingException;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.elasticsearch.cluster.node.DiscoveryNode.*;
import static org.elasticsearch.common.settings.ImmutableSettings.Builder.*;
import static org.elasticsearch.common.util.concurrent.ConcurrentCollections.*;
import static org.elasticsearch.common.util.concurrent.EsExecutors.*;

/**
 * @author kimchy (shay.banon)
 */
public class MulticastZenPing extends AbstractLifecycleComponent<ZenPing> implements ZenPing {

    private final String address;

    private final int port;

    private final String group;

    private final int bufferSize;

    private final int ttl;

    private final ThreadPool threadPool;

    private final TransportService transportService;

    private final ClusterName clusterName;

    private final NetworkService networkService;


    private volatile DiscoveryNodesProvider nodesProvider;

    private volatile Receiver receiver;

    private volatile Thread receiverThread;

    private MulticastSocket multicastSocket;

    private DatagramPacket datagramPacketSend;

    private DatagramPacket datagramPacketReceive;

    private final AtomicInteger pingIdGenerator = new AtomicInteger();

    private final Map<Integer, ConcurrentMap<DiscoveryNode, PingResponse>> receivedResponses = newConcurrentMap();

    private final Object sendMutex = new Object();

    private final Object receiveMutex = new Object();

    public MulticastZenPing(ThreadPool threadPool, TransportService transportService, ClusterName clusterName) {
        this(EMPTY_SETTINGS, threadPool, transportService, clusterName, new NetworkService(EMPTY_SETTINGS));
    }

    public MulticastZenPing(Settings settings, ThreadPool threadPool, TransportService transportService, ClusterName clusterName, NetworkService networkService) {
        super(settings);
        this.threadPool = threadPool;
        this.transportService = transportService;
        this.clusterName = clusterName;
        this.networkService = networkService;

        this.address = componentSettings.get("address");
        this.port = componentSettings.getAsInt("port", 54328);
        this.group = componentSettings.get("group", "224.2.2.4");
        this.bufferSize = componentSettings.getAsInt("buffer_size", 2048);
        this.ttl = componentSettings.getAsInt("ttl", 3);

        logger.debug("using group [{}], with port [{}], ttl [{}], and address [{}]", group, port, ttl, address);

        this.transportService.registerHandler(MulticastPingResponseRequestHandler.ACTION, new MulticastPingResponseRequestHandler());
    }

    @Override public void setNodesProvider(DiscoveryNodesProvider nodesProvider) {
        if (lifecycle.started()) {
            throw new ElasticSearchIllegalStateException("Can't set nodes provider when started");
        }
        this.nodesProvider = nodesProvider;
    }

    @Override protected void doStart() throws ElasticSearchException {
        try {
            this.datagramPacketReceive = new DatagramPacket(new byte[bufferSize], bufferSize);
            this.datagramPacketSend = new DatagramPacket(new byte[bufferSize], bufferSize, InetAddress.getByName(group), port);
        } catch (Exception e) {
            throw new DiscoveryException("Failed to set datagram packets", e);
        }

        try {
            MulticastSocket multicastSocket;
//            if (NetworkUtils.canBindToMcastAddress()) {
//                try {
//                    multicastSocket = new MulticastSocket(new InetSocketAddress(group, port));
//                } catch (Exception e) {
//                    logger.debug("Failed to create multicast socket by binding to group address, binding to port", e);
//                    multicastSocket = new MulticastSocket(port);
//                }
//            } else {
            multicastSocket = new MulticastSocket(port);
//            }

            multicastSocket.setTimeToLive(ttl);

            // set the send interface
            InetAddress multicastInterface = networkService.resolvePublishHostAddress(address);
            multicastSocket.setInterface(multicastInterface);
            multicastSocket.joinGroup(InetAddress.getByName(group));

            multicastSocket.setReceiveBufferSize(bufferSize);
            multicastSocket.setSendBufferSize(bufferSize);
            multicastSocket.setSoTimeout(60000);

            this.multicastSocket = multicastSocket;
        } catch (Exception e) {
            throw new DiscoveryException("Failed to setup multicast socket", e);
        }

        this.receiver = new Receiver();
        this.receiverThread = daemonThreadFactory(settings, "discovery#multicast#received").newThread(receiver);
        this.receiverThread.start();
    }

    @Override protected void doStop() throws ElasticSearchException {
        receiver.stop();
        receiverThread.interrupt();
        multicastSocket.close();
    }

    @Override protected void doClose() throws ElasticSearchException {
    }

    public PingResponse[] pingAndWait(TimeValue timeout) {
        final AtomicReference<PingResponse[]> response = new AtomicReference<PingResponse[]>();
        final CountDownLatch latch = new CountDownLatch(1);
        ping(new PingListener() {
            @Override public void onPing(PingResponse[] pings) {
                response.set(pings);
                latch.countDown();
            }
        }, timeout);
        try {
            latch.await();
            return response.get();
        } catch (InterruptedException e) {
            return null;
        }
    }

    @Override public void ping(final PingListener listener, final TimeValue timeout) {
        final int id = pingIdGenerator.incrementAndGet();
        receivedResponses.put(id, new ConcurrentHashMap<DiscoveryNode, PingResponse>());
        sendPingRequest(id);
        // try and send another ping request halfway through (just in case someone woke up during it...)
        // this can be a good trade-off to nailing the initial lookup or un-delivered messages
        threadPool.schedule(new Runnable() {
            @Override public void run() {
                try {
                    sendPingRequest(id);
                } catch (Exception e) {
                    logger.debug("[{}] failed to send second ping request", e, id);
                }
            }
        }, timeout.millis() / 2, TimeUnit.MILLISECONDS);
        threadPool.schedule(new Runnable() {
            @Override public void run() {
                ConcurrentMap<DiscoveryNode, PingResponse> responses = receivedResponses.remove(id);
                listener.onPing(responses.values().toArray(new PingResponse[responses.size()]));
            }
        }, timeout);
    }

    private void sendPingRequest(int id) {
        synchronized (sendMutex) {
            try {
                HandlesStreamOutput out = CachedStreamOutput.cachedHandlesBytes();
                out.writeInt(id);
                clusterName.writeTo(out);
                nodesProvider.nodes().localNode().writeTo(out);
                datagramPacketSend.setData(((BytesStreamOutput) out.wrappedOut()).copiedByteArray());
            } catch (IOException e) {
                receivedResponses.remove(id);
                throw new ZenPingException("Failed to serialize ping request", e);
            }
            try {
                multicastSocket.send(datagramPacketSend);
                if (logger.isTraceEnabled()) {
                    logger.trace("[{}] sending ping request", id);
                }
            } catch (IOException e) {
                receivedResponses.remove(id);
                throw new ZenPingException("Failed to send ping request over multicast", e);
            }
        }
    }

    class MulticastPingResponseRequestHandler extends BaseTransportRequestHandler<MulticastPingResponse> {

        static final String ACTION = "discovery/zen/multicast";

        @Override public MulticastPingResponse newInstance() {
            return new MulticastPingResponse();
        }

        @Override public void messageReceived(MulticastPingResponse request, TransportChannel channel) throws Exception {
            if (logger.isTraceEnabled()) {
                logger.trace("[{}] received {}", request.id, request.pingResponse);
            }
            ConcurrentMap<DiscoveryNode, PingResponse> responses = receivedResponses.get(request.id);
            if (responses == null) {
                logger.warn("received ping response with no matching id [{}]", request.id);
            } else {
                responses.put(request.pingResponse.target(), request.pingResponse);
            }
            channel.sendResponse(VoidStreamable.INSTANCE);
        }

        @Override public boolean spawn() {
            return false;
        }
    }

    static class MulticastPingResponse implements Streamable {

        int id;

        PingResponse pingResponse;

        MulticastPingResponse() {
        }

        @Override public void readFrom(StreamInput in) throws IOException {
            id = in.readInt();
            pingResponse = PingResponse.readPingResponse(in);
        }

        @Override public void writeTo(StreamOutput out) throws IOException {
            out.writeInt(id);
            pingResponse.writeTo(out);
        }
    }


    private class Receiver implements Runnable {

        private volatile boolean running = true;

        public void stop() {
            running = false;
        }

        @Override public void run() {
            while (running) {
                try {
                    int id;
                    DiscoveryNode requestingNodeX;
                    ClusterName clusterName;
                    synchronized (receiveMutex) {
                        try {
                            multicastSocket.receive(datagramPacketReceive);
                        } catch (SocketTimeoutException ignore) {
                            continue;
                        } catch (Exception e) {
                            if (running) {
                                logger.warn("failed to receive packet", e);
                            }
                            continue;
                        }
                        try {
                            StreamInput input = CachedStreamInput.cachedHandles(new BytesStreamInput(datagramPacketReceive.getData(), datagramPacketReceive.getOffset(), datagramPacketReceive.getLength()));
                            id = input.readInt();
                            clusterName = ClusterName.readClusterName(input);
                            requestingNodeX = readNode(input);
                        } catch (Exception e) {
                            logger.warn("failed to read requesting node from {}", e, datagramPacketReceive.getSocketAddress());
                            continue;
                        }
                    }
                    DiscoveryNodes discoveryNodes = nodesProvider.nodes();
                    final DiscoveryNode requestingNode = requestingNodeX;
                    if (requestingNode.id().equals(discoveryNodes.localNodeId())) {
                        // that's me, ignore
                        continue;
                    }
                    if (!clusterName.equals(MulticastZenPing.this.clusterName)) {
                        // not our cluster, ignore it...
                        continue;
                    }
                    final MulticastPingResponse multicastPingResponse = new MulticastPingResponse();
                    multicastPingResponse.id = id;
                    multicastPingResponse.pingResponse = new PingResponse(discoveryNodes.localNode(), discoveryNodes.masterNode(), clusterName);

                    if (logger.isTraceEnabled()) {
                        logger.trace("[{}] received ping_request from [{}], sending {}", id, requestingNode, multicastPingResponse.pingResponse);
                    }

                    if (!transportService.nodeConnected(requestingNode)) {
                        // do the connect and send on a thread pool
                        threadPool.cached().execute(new Runnable() {
                            @Override public void run() {
                                // connect to the node if possible
                                try {
                                    transportService.connectToNode(requestingNode);
                                    transportService.sendRequest(requestingNode, MulticastPingResponseRequestHandler.ACTION, multicastPingResponse, new VoidTransportResponseHandler(false) {
                                        @Override public void handleException(TransportException exp) {
                                            logger.warn("failed to receive confirmation on sent ping response to [{}]", exp, requestingNode);
                                        }
                                    });
                                } catch (Exception e) {
                                    logger.warn("failed to connect to requesting node {}", e, requestingNode);
                                }
                            }
                        });
                    } else {
                        transportService.sendRequest(requestingNode, MulticastPingResponseRequestHandler.ACTION, multicastPingResponse, new VoidTransportResponseHandler(false) {
                            @Override public void handleException(TransportException exp) {
                                logger.warn("failed to receive confirmation on sent ping response to [{}]", exp, requestingNode);
                            }
                        });
                    }
                } catch (Exception e) {
                    logger.warn("unexpected exception in multicast receiver", e);
                }
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3529.java