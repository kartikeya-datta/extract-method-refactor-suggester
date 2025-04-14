error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7727.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7727.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,18]

error in qdox parser
file content:
```java
offset: 18
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7727.java
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

package org.elasticsearch.discovery.zen.ping.unicast;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.util.concurrent.jsr166y.LinkedTransferQueue;
import org.elasticsearch.discovery.zen.DiscoveryNodesProvider;
import org.elasticsearch.discovery.zen.ping.ZenPing;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.elasticsearch.common.collect.Lists.*;
import static org.elasticsearch.common.settings.ImmutableSettings.Builder.*;
import static org.elasticsearch.common.unit.TimeValue.*;
import static org.elasticsearch.common.util.concurrent.ConcurrentCollections.*;
import static org.elasticsearch.discovery.zen.ping.ZenPing.PingResponse.*;

/**
 * @author kimchy (shay.banon)
 */
public class UnicastZenPing extends AbstractLifecycleComponent<ZenPing> implements ZenPing {

    private final ThreadPool threadPool;

    private final TransportService transportService;

    private final ClusterName clusterName;


    private final DiscoveryNode[] nodes;

    private volatile DiscoveryNodesProvider nodesProvider;

    private final AtomicInteger pingIdGenerator = new AtomicInteger();

    private final Map<Integer, ConcurrentMap<DiscoveryNode, PingResponse>> receivedResponses = newConcurrentMap();

    // a list of temporal responses a node will return for a request (holds requests from other nodes)
    private final Queue<PingResponse> temporalResponses = new LinkedTransferQueue<PingResponse>();

    private final CopyOnWriteArrayList<UnicastHostsProvider> hostsProviders = new CopyOnWriteArrayList<UnicastHostsProvider>();

    public UnicastZenPing(ThreadPool threadPool, TransportService transportService, ClusterName clusterName) {
        this(EMPTY_SETTINGS, threadPool, transportService, clusterName);
    }

    public UnicastZenPing(Settings settings, ThreadPool threadPool, TransportService transportService, ClusterName clusterName) {
        super(settings);
        this.threadPool = threadPool;
        this.transportService = transportService;
        this.clusterName = clusterName;

        List<String> hosts = Lists.newArrayList(componentSettings.getAsArray("hosts"));
        if (componentSettings.get("hosts") != null) {
            hosts.addAll(Strings.commaDelimitedListToSet(componentSettings.get("hosts")));
        }

        logger.debug("using initial hosts {}", hosts);

        List<DiscoveryNode> nodes = Lists.newArrayList();
        int idCounter = 0;
        for (String host : hosts) {
            try {
                for (TransportAddress address : transportService.addressesFromString(host)) {
                    nodes.add(new DiscoveryNode("#zen_unicast_" + (++idCounter) + "#", address));
                }
            } catch (Exception e) {
                throw new ElasticSearchIllegalArgumentException("Failed to resolve address for [" + host + "]", e);
            }
        }
        this.nodes = nodes.toArray(new DiscoveryNode[nodes.size()]);

        transportService.registerHandler(UnicastPingRequestHandler.ACTION, new UnicastPingRequestHandler());
    }

    @Override protected void doStart() throws ElasticSearchException {
    }

    @Override protected void doStop() throws ElasticSearchException {
    }

    @Override protected void doClose() throws ElasticSearchException {
        transportService.removeHandler(UnicastPingRequestHandler.ACTION);
    }

    public void addHostsProvider(UnicastHostsProvider provider) {
        hostsProviders.add(provider);
    }

    public void removeHostsProvider(UnicastHostsProvider provider) {
        hostsProviders.remove(provider);
    }

    @Override public void setNodesProvider(DiscoveryNodesProvider nodesProvider) {
        this.nodesProvider = nodesProvider;
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

    @Override public void ping(final PingListener listener, final TimeValue timeout) throws ElasticSearchException {
        final int id = pingIdGenerator.incrementAndGet();
        receivedResponses.put(id, new ConcurrentHashMap<DiscoveryNode, PingResponse>());
        sendPings(id, timeout, false);
        threadPool.schedule(new Runnable() {
            @Override public void run() {
                sendPings(id, timeout, true);
                ConcurrentMap<DiscoveryNode, PingResponse> responses = receivedResponses.remove(id);
                listener.onPing(responses.values().toArray(new PingResponse[responses.size()]));
            }
        }, timeout);
    }

    private void sendPings(int id, TimeValue timeout, boolean wait) {
        UnicastPingRequest pingRequest = new UnicastPingRequest();
        pingRequest.id = id;
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
            boolean disconnectX;
            DiscoveryNode nodeToSendX = discoNodes.findByAddress(node.address());
            if (nodeToSendX != null) {
                disconnectX = false;
            } else {
                nodeToSendX = node;
                disconnectX = true;
            }
            final DiscoveryNode nodeToSend = nodeToSendX;
            try {
                transportService.connectToNode(nodeToSend);
            } catch (ConnectTransportException e) {
                latch.countDown();
                // can't connect to the node
                continue;
            }

            final boolean disconnect = disconnectX;
            transportService.sendRequest(nodeToSend, UnicastPingRequestHandler.ACTION, pingRequest, TransportRequestOptions.options().withTimeout((long) (timeout.millis() * 1.25)), new BaseTransportResponseHandler<UnicastPingResponse>() {

                @Override public UnicastPingResponse newInstance() {
                    return new UnicastPingResponse();
                }

                @Override public void handleResponse(UnicastPingResponse response) {
                    try {
                        DiscoveryNodes discoveryNodes = nodesProvider.nodes();
                        for (PingResponse pingResponse : response.pingResponses) {
                            if (disconnect) {
                                transportService.disconnectFromNode(nodeToSend);
                            }
                            if (pingResponse.target().id().equals(discoveryNodes.localNodeId())) {
                                // that's us, ignore
                                continue;
                            }
                            if (!pingResponse.clusterName().equals(clusterName)) {
                                // not part of the cluster
                                return;
                            }
                            ConcurrentMap<DiscoveryNode, PingResponse> responses = receivedResponses.get(response.id);
                            if (responses == null) {
                                logger.warn("received ping response with no matching id [{}]", response.id);
                            } else {
                                responses.put(pingResponse.target(), pingResponse);
                            }
                        }
                    } finally {
                        latch.countDown();
                    }
                }

                @Override public void handleException(RemoteTransportException exp) {
                    latch.countDown();
                    if (exp instanceof ConnectTransportException) {
                        // ok, not connected...
                    } else {
                        if (disconnect) {
                            transportService.disconnectFromNode(nodeToSend);
                        }
                        logger.warn("failed to send ping to [{}]", exp, node);
                    }
                }

                @Override public boolean spawn() {
                    return false;
                }
            });
        }
        if (wait) {
            try {
                latch.await(timeout.millis() * 5, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }

    private UnicastPingResponse handlePingRequest(final UnicastPingRequest request) {
        temporalResponses.add(request.pingResponse);
        threadPool.schedule(new Runnable() {
            @Override public void run() {
                temporalResponses.remove(request.pingResponse);
            }
        }, request.timeout.millis() * 2, TimeUnit.MILLISECONDS);

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

        @Override public UnicastPingRequest newInstance() {
            return new UnicastPingRequest();
        }

        @Override public void messageReceived(UnicastPingRequest request, TransportChannel channel) throws Exception {
            channel.sendResponse(handlePingRequest(request));
        }

        @Override public boolean spawn() {
            return false;
        }
    }

    static class UnicastPingRequest implements Streamable {

        int id;

        TimeValue timeout;

        PingResponse pingResponse;

        UnicastPingRequest() {
        }

        @Override public void readFrom(StreamInput in) throws IOException {
            id = in.readInt();
            timeout = readTimeValue(in);
            pingResponse = readPingResponse(in);
        }

        @Override public void writeTo(StreamOutput out) throws IOException {
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

        @Override public void readFrom(StreamInput in) throws IOException {
            id = in.readInt();
            pingResponses = new PingResponse[in.readVInt()];
            for (int i = 0; i < pingResponses.length; i++) {
                pingResponses[i] = readPingResponse(in);
            }
        }

        @Override public void writeTo(StreamOutput out) throws IOException {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7727.java