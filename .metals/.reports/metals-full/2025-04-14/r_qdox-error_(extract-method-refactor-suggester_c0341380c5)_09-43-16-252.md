error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6437.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6437.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6437.java
text:
```scala
r@@eceivedResponses.put(sendPingsHandler.id(), ConcurrentCollections.<DiscoveryNode, PingResponse>newConcurrentMap());

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

package org.elasticsearch.discovery.zen.ping.unicast;

import com.google.common.collect.Lists;
import jsr166y.LinkedTransferQueue;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.util.concurrent.ConcurrentCollections;
import org.elasticsearch.common.util.concurrent.EsExecutors;
import org.elasticsearch.discovery.zen.DiscoveryNodesProvider;
import org.elasticsearch.discovery.zen.ping.ZenPing;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.collect.Lists.newArrayList;
import static org.elasticsearch.common.settings.ImmutableSettings.Builder.EMPTY_SETTINGS;
import static org.elasticsearch.common.unit.TimeValue.readTimeValue;
import static org.elasticsearch.common.util.concurrent.ConcurrentCollections.newConcurrentMap;
import static org.elasticsearch.discovery.zen.ping.ZenPing.PingResponse.readPingResponse;

/**
 *
 */
public class UnicastZenPing extends AbstractLifecycleComponent<ZenPing> implements ZenPing {

    public static final int LIMIT_PORTS_COUNT = 1;

    private final ThreadPool threadPool;

    private final TransportService transportService;

    private final ClusterName clusterName;

    private final int concurrentConnects;

    private final DiscoveryNode[] nodes;

    private volatile DiscoveryNodesProvider nodesProvider;

    private final AtomicInteger pingIdGenerator = new AtomicInteger();

    private final Map<Integer, ConcurrentMap<DiscoveryNode, PingResponse>> receivedResponses = newConcurrentMap();

    // a list of temporal responses a node will return for a request (holds requests from other nodes)
    private final Queue<PingResponse> temporalResponses = new LinkedTransferQueue<PingResponse>();

    private final CopyOnWriteArrayList<UnicastHostsProvider> hostsProviders = new CopyOnWriteArrayList<UnicastHostsProvider>();

    public UnicastZenPing(ThreadPool threadPool, TransportService transportService, ClusterName clusterName) {
        this(EMPTY_SETTINGS, threadPool, transportService, clusterName, null);
    }

    public UnicastZenPing(Settings settings, ThreadPool threadPool, TransportService transportService, ClusterName clusterName, @Nullable Set<UnicastHostsProvider> unicastHostsProviders) {
        super(settings);
        this.threadPool = threadPool;
        this.transportService = transportService;
        this.clusterName = clusterName;

        if (unicastHostsProviders != null) {
            for (UnicastHostsProvider unicastHostsProvider : unicastHostsProviders) {
                addHostsProvider(unicastHostsProvider);
            }
        }

        this.concurrentConnects = componentSettings.getAsInt("concurrent_connects", 10);
        String[] hostArr = componentSettings.getAsArray("hosts");
        // trim the hosts
        for (int i = 0; i < hostArr.length; i++) {
            hostArr[i] = hostArr[i].trim();
        }
        List<String> hosts = Lists.newArrayList(hostArr);
        logger.debug("using initial hosts {}, with concurrent_connects [{}]", hosts, concurrentConnects);

        List<DiscoveryNode> nodes = Lists.newArrayList();
        int idCounter = 0;
        for (String host : hosts) {
            try {
                TransportAddress[] addresses = transportService.addressesFromString(host);
                // we only limit to 1 addresses, makes no sense to ping 100 ports
                for (int i = 0; (i < addresses.length && i < LIMIT_PORTS_COUNT); i++) {
                    nodes.add(new DiscoveryNode("#zen_unicast_" + (++idCounter) + "#", addresses[i]));
                }
            } catch (Exception e) {
                throw new ElasticSearchIllegalArgumentException("Failed to resolve address for [" + host + "]", e);
            }
        }
        this.nodes = nodes.toArray(new DiscoveryNode[nodes.size()]);

        transportService.registerHandler(UnicastPingRequestHandler.ACTION, new UnicastPingRequestHandler());
    }

    @Override
    protected void doStart() throws ElasticSearchException {
    }

    @Override
    protected void doStop() throws ElasticSearchException {
    }

    @Override
    protected void doClose() throws ElasticSearchException {
        transportService.removeHandler(UnicastPingRequestHandler.ACTION);
    }

    public void addHostsProvider(UnicastHostsProvider provider) {
        hostsProviders.add(provider);
    }

    public void removeHostsProvider(UnicastHostsProvider provider) {
        hostsProviders.remove(provider);
    }

    @Override
    public void setNodesProvider(DiscoveryNodesProvider nodesProvider) {
        this.nodesProvider = nodesProvider;
    }

    public PingResponse[] pingAndWait(TimeValue timeout) {
        final AtomicReference<PingResponse[]> response = new AtomicReference<PingResponse[]>();
        final CountDownLatch latch = new CountDownLatch(1);
        ping(new PingListener() {
            @Override
            public void onPing(PingResponse[] pings) {
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

    @Override
    public void ping(final PingListener listener, final TimeValue timeout) throws ElasticSearchException {
        final SendPingsHandler sendPingsHandler = new SendPingsHandler(pingIdGenerator.incrementAndGet());
        receivedResponses.put(sendPingsHandler.id(), new ConcurrentHashMap<DiscoveryNode, PingResponse>());
        sendPings(timeout, null, sendPingsHandler);
        threadPool.schedule(TimeValue.timeValueMillis(timeout.millis() / 2), ThreadPool.Names.GENERIC, new Runnable() {
            @Override
            public void run() {
                sendPings(timeout, null, sendPingsHandler);
                threadPool.schedule(TimeValue.timeValueMillis(timeout.millis() / 2), ThreadPool.Names.GENERIC, new Runnable() {
                    @Override
                    public void run() {
                        sendPings(timeout, TimeValue.timeValueMillis(timeout.millis() / 2), sendPingsHandler);
                        ConcurrentMap<DiscoveryNode, PingResponse> responses = receivedResponses.remove(sendPingsHandler.id());
                        listener.onPing(responses.values().toArray(new PingResponse[responses.size()]));
                        for (DiscoveryNode node : sendPingsHandler.nodeToDisconnect) {
                            logger.trace("[{}] disconnecting from {}", sendPingsHandler.id(), node);
                            transportService.disconnectFromNode(node);
                        }
                        sendPingsHandler.close();
                    }
                });
            }
        });
    }

    class SendPingsHandler {
        private final int id;
        private volatile ExecutorService executor;
        private final Set<DiscoveryNode> nodeToDisconnect = ConcurrentCollections.newConcurrentSet();
        private volatile boolean closed;

        SendPingsHandler(int id) {
            this.id = id;
        }

        public int id() {
            return this.id;
        }

        public boolean isClosed() {
            return this.closed;
        }

        public Executor executor() {
            if (executor == null) {
                ThreadFactory threadFactory = EsExecutors.daemonThreadFactory(settings, "[unicast_connect]");
                executor = EsExecutors.newScalingExecutorService(0, concurrentConnects, 60, TimeUnit.SECONDS, threadFactory);
            }
            return executor;
        }

        public void close() {
            closed = true;
            if (executor != null) {
                executor.shutdownNow();
                executor = null;
            }
            nodeToDisconnect.clear();
        }
    }

    void sendPings(final TimeValue timeout, @Nullable TimeValue waitTime, final SendPingsHandler sendPingsHandler) {
        final UnicastPingRequest pingRequest = new UnicastPingRequest();
        pingRequest.id = sendPingsHandler.id();
        pingRequest.timeout = timeout;
        DiscoveryNodes discoNodes = nodesProvider.nodes();
        pingRequest.pingResponse = new PingResponse(discoNodes.localNode(), discoNodes.masterNode(), clusterName);

        List<DiscoveryNode> nodesToPing = newArrayList(nodes);
        for (UnicastHostsProvider provider : hostsProviders) {
            nodesToPing.addAll(provider.buildDynamicNodes());
        }

        final CountDownLatch latch = new CountDownLatch(nodesToPing.size());
        for (final DiscoveryNode node : nodesToPing) {
            // make sure we are connected
            boolean nodeFoundByAddressX;
            DiscoveryNode nodeToSendX = discoNodes.findByAddress(node.address());
            if (nodeToSendX != null) {
                nodeFoundByAddressX = true;
            } else {
                nodeToSendX = node;
                nodeFoundByAddressX = false;
            }
            final DiscoveryNode nodeToSend = nodeToSendX;

            final boolean nodeFoundByAddress = nodeFoundByAddressX;
            if (!transportService.nodeConnected(nodeToSend)) {
                if (sendPingsHandler.isClosed()) {
                    return;
                }
                sendPingsHandler.nodeToDisconnect.add(nodeToSend);
                // fork the connection to another thread
                sendPingsHandler.executor().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // connect to the node, see if we manage to do it, if not, bail
                            if (!nodeFoundByAddress) {
                                logger.trace("[{}] connecting (light) to {}", sendPingsHandler.id(), nodeToSend);
                                transportService.connectToNodeLight(nodeToSend);
                            } else {
                                logger.trace("[{}] connecting to {}", sendPingsHandler.id(), nodeToSend);
                                transportService.connectToNode(nodeToSend);
                            }
                            logger.trace("[{}] connected to {}", sendPingsHandler.id(), node);
                            if (receivedResponses.containsKey(sendPingsHandler.id())) {
                                // we are connected and still in progress, send the ping request
                                sendPingRequestToNode(sendPingsHandler.id(), timeout, pingRequest, latch, node, nodeToSend);
                            } else {
                                // connect took too long, just log it and bail
                                latch.countDown();
                                logger.trace("[{}] connect to {} was too long outside of ping window, bailing", sendPingsHandler.id(), node);
                            }
                        } catch (ConnectTransportException e) {
                            // can't connect to the node
                            logger.trace("[{}] failed to connect to {}", e, sendPingsHandler.id(), nodeToSend);
                            latch.countDown();
                        }
                    }
                });
            } else {
                sendPingRequestToNode(sendPingsHandler.id(), timeout, pingRequest, latch, node, nodeToSend);
            }
        }
        if (waitTime != null) {
            try {
                latch.await(waitTime.millis(), TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }

    private void sendPingRequestToNode(final int id, TimeValue timeout, UnicastPingRequest pingRequest, final CountDownLatch latch, final DiscoveryNode node, final DiscoveryNode nodeToSend) {
        logger.trace("[{}] sending to {}", id, nodeToSend);
        transportService.sendRequest(nodeToSend, UnicastPingRequestHandler.ACTION, pingRequest, TransportRequestOptions.options().withTimeout((long) (timeout.millis() * 1.25)), new BaseTransportResponseHandler<UnicastPingResponse>() {

            @Override
            public UnicastPingResponse newInstance() {
                return new UnicastPingResponse();
            }

            @Override
            public String executor() {
                return ThreadPool.Names.SAME;
            }

            @Override
            public void handleResponse(UnicastPingResponse response) {
                logger.trace("[{}] received response from {}: {}", id, nodeToSend, Arrays.toString(response.pingResponses));
                try {
                    DiscoveryNodes discoveryNodes = nodesProvider.nodes();
                    for (PingResponse pingResponse : response.pingResponses) {
                        if (pingResponse.target().id().equals(discoveryNodes.localNodeId())) {
                            // that's us, ignore
                            continue;
                        }
                        if (!pingResponse.clusterName().equals(clusterName)) {
                            // not part of the cluster
                            logger.debug("[{}] filtering out response from {}, not same cluster_name [{}]", id, pingResponse.target(), pingResponse.clusterName().value());
                            continue;
                        }
                        ConcurrentMap<DiscoveryNode, PingResponse> responses = receivedResponses.get(response.id);
                        if (responses == null) {
                            logger.warn("received ping response {} with no matching id [{}]", pingResponse, response.id);
                        } else {
                            responses.put(pingResponse.target(), pingResponse);
                        }
                    }
                } finally {
                    latch.countDown();
                }
            }

            @Override
            public void handleException(TransportException exp) {
                latch.countDown();
                if (exp instanceof ConnectTransportException) {
                    // ok, not connected...
                    logger.trace("failed to connect to {}", exp, nodeToSend);
                } else {
                    logger.warn("failed to send ping to [{}]", exp, node);
                }
            }
        });
    }

    private UnicastPingResponse handlePingRequest(final UnicastPingRequest request) {
        if (lifecycle.stoppedOrClosed()) {
            throw new ElasticSearchIllegalStateException("received ping request while stopped/closed");
        }
        temporalResponses.add(request.pingResponse);
        threadPool.schedule(TimeValue.timeValueMillis(request.timeout.millis() * 2), ThreadPool.Names.SAME, new Runnable() {
            @Override
            public void run() {
                temporalResponses.remove(request.pingResponse);
            }
        });

        List<PingResponse> pingResponses = newArrayList(temporalResponses);
        DiscoveryNodes discoNodes = nodesProvider.nodes();
        pingResponses.add(new PingResponse(discoNodes.localNode(), discoNodes.masterNode(), clusterName));


        UnicastPingResponse unicastPingResponse = new UnicastPingResponse();
        unicastPingResponse.id = request.id;
        unicastPingResponse.pingResponses = pingResponses.toArray(new PingResponse[pingResponses.size()]);

        return unicastPingResponse;
    }

    class UnicastPingRequestHandler extends BaseTransportRequestHandler<UnicastPingRequest> {

        static final String ACTION = "discovery/zen/unicast";

        @Override
        public UnicastPingRequest newInstance() {
            return new UnicastPingRequest();
        }

        @Override
        public String executor() {
            return ThreadPool.Names.SAME;
        }

        @Override
        public void messageReceived(UnicastPingRequest request, TransportChannel channel) throws Exception {
            channel.sendResponse(handlePingRequest(request));
        }
    }

    static class UnicastPingRequest implements Streamable {

        int id;

        TimeValue timeout;

        PingResponse pingResponse;

        UnicastPingRequest() {
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            id = in.readInt();
            timeout = readTimeValue(in);
            pingResponse = readPingResponse(in);
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            out.writeInt(id);
            timeout.writeTo(out);
            pingResponse.writeTo(out);
        }
    }

    static class UnicastPingResponse implements Streamable {

        int id;

        PingResponse[] pingResponses;

        UnicastPingResponse() {
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            id = in.readInt();
            pingResponses = new PingResponse[in.readVInt()];
            for (int i = 0; i < pingResponses.length; i++) {
                pingResponses[i] = readPingResponse(in);
            }
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            out.writeInt(id);
            out.writeVInt(pingResponses.length);
            for (PingResponse pingResponse : pingResponses) {
                pingResponse.writeTo(out);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6437.java