error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6896.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6896.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6896.java
text:
```scala
final C@@oncurrentMapLong<RequestHolder> clientHandlers = ConcurrentCollections.newConcurrentMapLongWithAggressiveConcurrency();

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

package org.elasticsearch.transport;

import com.google.common.collect.ImmutableMap;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.collect.MapBuilder;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.metrics.MeanMetric;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.BoundTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.util.concurrent.ConcurrentCollections;
import org.elasticsearch.common.util.concurrent.ConcurrentMapLong;
import org.elasticsearch.threadpool.ThreadPool;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;

import static org.elasticsearch.common.settings.ImmutableSettings.Builder.EMPTY_SETTINGS;

/**
 *
 */
public class TransportService extends AbstractLifecycleComponent<TransportService> {

    private final Transport transport;

    private final ThreadPool threadPool;

    volatile ImmutableMap<String, TransportRequestHandler> serverHandlers = ImmutableMap.of();
    final Object serverHandlersMutex = new Object();

    final ConcurrentMapLong<RequestHolder> clientHandlers = ConcurrentCollections.newConcurrentMapLong();

    final AtomicLong requestIds = new AtomicLong();

    final CopyOnWriteArrayList<TransportConnectionListener> connectionListeners = new CopyOnWriteArrayList<TransportConnectionListener>();

    // An LRU (don't really care about concurrency here) that holds the latest timed out requests so if they
    // do show up, we can print more descriptive information about them
    final Map<Long, TimeoutInfoHolder> timeoutInfoHandlers = Collections.synchronizedMap(new LinkedHashMap<Long, TimeoutInfoHolder>(100, .75F, true) {
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > 100;
        }
    });

    private boolean throwConnectException = false;
    private final TransportService.Adapter adapter = new Adapter();

    public TransportService(Transport transport, ThreadPool threadPool) {
        this(EMPTY_SETTINGS, transport, threadPool);
    }

    @Inject
    public TransportService(Settings settings, Transport transport, ThreadPool threadPool) {
        super(settings);
        this.transport = transport;
        this.threadPool = threadPool;
    }

    @Override
    protected void doStart() throws ElasticSearchException {
        adapter.rxMetric.clear();
        adapter.txMetric.clear();
        transport.transportServiceAdapter(adapter);
        transport.start();
        if (transport.boundAddress() != null && logger.isInfoEnabled()) {
            logger.info("{}", transport.boundAddress());
        }
    }

    @Override
    protected void doStop() throws ElasticSearchException {
        transport.stop();
    }

    @Override
    protected void doClose() throws ElasticSearchException {
        transport.close();
    }

    public boolean addressSupported(Class<? extends TransportAddress> address) {
        return transport.addressSupported(address);
    }

    public TransportInfo info() {
        return new TransportInfo(boundAddress());
    }

    public TransportStats stats() {
        return new TransportStats(transport.serverOpen(), adapter.rxMetric.count(), adapter.rxMetric.sum(), adapter.txMetric.count(), adapter.txMetric.sum());
    }

    public BoundTransportAddress boundAddress() {
        return transport.boundAddress();
    }

    public boolean nodeConnected(DiscoveryNode node) {
        return transport.nodeConnected(node);
    }

    public void connectToNode(DiscoveryNode node) throws ConnectTransportException {
        transport.connectToNode(node);
    }

    public void connectToNodeLight(DiscoveryNode node) throws ConnectTransportException {
        transport.connectToNodeLight(node);
    }

    public void disconnectFromNode(DiscoveryNode node) {
        transport.disconnectFromNode(node);
    }

    public void addConnectionListener(TransportConnectionListener listener) {
        connectionListeners.add(listener);
    }

    public void removeConnectionListener(TransportConnectionListener listener) {
        connectionListeners.remove(listener);
    }

    /**
     * Set to <tt>true</tt> to indicate that a {@link ConnectTransportException} should be thrown when
     * sending a message (otherwise, it will be passed to the response handler). Defaults to <tt>false</tt>.
     * <p/>
     * <p>This is useful when logic based on connect failure is needed without having to wrap the handler,
     * for example, in case of retries across several nodes.
     */
    public void throwConnectException(boolean throwConnectException) {
        this.throwConnectException = throwConnectException;
    }

    public <T extends TransportResponse> TransportFuture<T> submitRequest(DiscoveryNode node, String action, TransportRequest request,
                                                                          TransportResponseHandler<T> handler) throws TransportException {
        return submitRequest(node, action, request, TransportRequestOptions.EMPTY, handler);
    }

    public <T extends TransportResponse> TransportFuture<T> submitRequest(DiscoveryNode node, String action, TransportRequest request,
                                                                          TransportRequestOptions options, TransportResponseHandler<T> handler) throws TransportException {
        PlainTransportFuture<T> futureHandler = new PlainTransportFuture<T>(handler);
        sendRequest(node, action, request, options, futureHandler);
        return futureHandler;
    }

    public <T extends TransportResponse> void sendRequest(final DiscoveryNode node, final String action, final TransportRequest request,
                                                          final TransportResponseHandler<T> handler) throws TransportException {
        sendRequest(node, action, request, TransportRequestOptions.EMPTY, handler);
    }

    public <T extends TransportResponse> void sendRequest(final DiscoveryNode node, final String action, final TransportRequest request,
                                                          final TransportRequestOptions options, final TransportResponseHandler<T> handler) throws TransportException {
        final long requestId = newRequestId();
        TimeoutHandler timeoutHandler = null;
        try {
            if (options.timeout() != null) {
                timeoutHandler = new TimeoutHandler(requestId);
                timeoutHandler.future = threadPool.schedule(options.timeout(), ThreadPool.Names.GENERIC, timeoutHandler);
            }
            clientHandlers.put(requestId, new RequestHolder<T>(handler, node, action, timeoutHandler));
            transport.sendRequest(node, requestId, action, request, options);
        } catch (final Exception e) {
            // usually happen either because we failed to connect to the node
            // or because we failed serializing the message
            clientHandlers.remove(requestId);
            if (timeoutHandler != null) {
                timeoutHandler.future.cancel(false);
            }
            if (throwConnectException) {
                if (e instanceof ConnectTransportException) {
                    throw (ConnectTransportException) e;
                }
            }
            // callback that an exception happened, but on a different thread since we don't
            // want handlers to worry about stack overflows
            final SendRequestTransportException sendRequestException = new SendRequestTransportException(node, action, e);
            threadPool.executor(ThreadPool.Names.GENERIC).execute(new Runnable() {
                @Override
                public void run() {
                    handler.handleException(sendRequestException);
                }
            });
        }
    }

    private long newRequestId() {
        return requestIds.getAndIncrement();
    }

    public TransportAddress[] addressesFromString(String address) throws Exception {
        return transport.addressesFromString(address);
    }

    public void registerHandler(String action, TransportRequestHandler handler) {
        synchronized (serverHandlersMutex) {
            TransportRequestHandler handlerReplaced = serverHandlers.get(action);
            serverHandlers = MapBuilder.newMapBuilder(serverHandlers).put(action, handler).immutableMap();
            if (handlerReplaced != null) {
                logger.warn("Registered two transport handlers for action {}, handlers: {}, {}", action, handler, handlerReplaced);
            }
        }
    }

    public void removeHandler(String action) {
        synchronized (serverHandlersMutex) {
            serverHandlers = MapBuilder.newMapBuilder(serverHandlers).remove(action).immutableMap();
        }
    }

    class Adapter implements TransportServiceAdapter {

        final MeanMetric rxMetric = new MeanMetric();
        final MeanMetric txMetric = new MeanMetric();

        @Override
        public void received(long size) {
            rxMetric.inc(size);
        }

        @Override
        public void sent(long size) {
            txMetric.inc(size);
        }

        @Override
        public TransportRequestHandler handler(String action) {
            return serverHandlers.get(action);
        }

        @Override
        public TransportResponseHandler remove(long requestId) {
            RequestHolder holder = clientHandlers.remove(requestId);
            if (holder == null) {
                // lets see if its in the timeout holder
                TimeoutInfoHolder timeoutInfoHolder = timeoutInfoHandlers.remove(requestId);
                if (timeoutInfoHolder != null) {
                    long time = System.currentTimeMillis();
                    logger.warn("Received response for a request that has timed out, sent [{}ms] ago, timed out [{}ms] ago, action [{}], node [{}], id [{}]", time - timeoutInfoHolder.sentTime(), time - timeoutInfoHolder.timeoutTime(), timeoutInfoHolder.action(), timeoutInfoHolder.node(), requestId);
                } else {
                    logger.warn("Transport response handler not found of id [{}]", requestId);
                }
                return null;
            }
            holder.cancel();
            return holder.handler();
        }

        @Override
        public void raiseNodeConnected(final DiscoveryNode node) {
            threadPool.generic().execute(new Runnable() {
                @Override
                public void run() {
                    for (TransportConnectionListener connectionListener : connectionListeners) {
                        connectionListener.onNodeConnected(node);
                    }
                }
            });
        }

        @Override
        public void raiseNodeDisconnected(final DiscoveryNode node) {
            if (lifecycle.stoppedOrClosed()) {
                return;
            }
            threadPool.generic().execute(new Runnable() {
                @Override
                public void run() {
                    for (TransportConnectionListener connectionListener : connectionListeners) {
                        connectionListener.onNodeDisconnected(node);
                    }
                    // node got disconnected, raise disconnection on possible ongoing handlers
                    for (Map.Entry<Long, RequestHolder> entry : clientHandlers.entrySet()) {
                        RequestHolder holder = entry.getValue();
                        if (holder.node().equals(node)) {
                            final RequestHolder holderToNotify = clientHandlers.remove(entry.getKey());
                            if (holderToNotify != null) {
                                // callback that an exception happened, but on a different thread since we don't
                                // want handlers to worry about stack overflows
                                threadPool.generic().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        holderToNotify.handler().handleException(new NodeDisconnectedException(node, holderToNotify.action()));
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
    }

    class TimeoutHandler implements Runnable {

        private final long requestId;

        private final long sentTime = System.currentTimeMillis();

        ScheduledFuture future;

        TimeoutHandler(long requestId) {
            this.requestId = requestId;
        }

        public long sentTime() {
            return sentTime;
        }

        @Override
        public void run() {
            if (future.isCancelled()) {
                return;
            }
            final RequestHolder holder = clientHandlers.remove(requestId);
            if (holder != null) {
                // add it to the timeout information holder, in case we are going to get a response later
                long timeoutTime = System.currentTimeMillis();
                timeoutInfoHandlers.put(requestId, new TimeoutInfoHolder(holder.node(), holder.action(), sentTime, timeoutTime));
                holder.handler().handleException(new ReceiveTimeoutTransportException(holder.node(), holder.action(), "request_id [" + requestId + "] timed out after [" + (timeoutTime - sentTime) + "ms]"));
            }
        }
    }


    static class TimeoutInfoHolder {

        private final DiscoveryNode node;

        private final String action;

        private final long sentTime;

        private final long timeoutTime;

        TimeoutInfoHolder(DiscoveryNode node, String action, long sentTime, long timeoutTime) {
            this.node = node;
            this.action = action;
            this.sentTime = sentTime;
            this.timeoutTime = timeoutTime;
        }

        public DiscoveryNode node() {
            return node;
        }

        public String action() {
            return action;
        }

        public long sentTime() {
            return sentTime;
        }

        public long timeoutTime() {
            return timeoutTime;
        }
    }

    static class RequestHolder<T extends TransportResponse> {

        private final TransportResponseHandler<T> handler;

        private final DiscoveryNode node;

        private final String action;

        private final TimeoutHandler timeout;

        RequestHolder(TransportResponseHandler<T> handler, DiscoveryNode node, String action, TimeoutHandler timeout) {
            this.handler = handler;
            this.node = node;
            this.action = action;
            this.timeout = timeout;
        }

        public TransportResponseHandler<T> handler() {
            return handler;
        }

        public DiscoveryNode node() {
            return this.node;
        }

        public String action() {
            return this.action;
        }

        public void cancel() {
            if (timeout != null) {
                timeout.future.cancel(false);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6896.java