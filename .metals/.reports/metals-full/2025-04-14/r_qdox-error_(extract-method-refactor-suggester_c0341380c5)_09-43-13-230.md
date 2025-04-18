error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5209.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5209.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5209.java
text:
```scala
l@@ogger.trace("[{}] executed  [{}]/[{}], took [{}]", executionId, request.numberOfActions(), new ByteSizeValue(request.estimatedSizeInBytes()), response.getTook());

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

package org.elasticsearch.bulk.udp;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.bytes.ChannelBufferBytesReference;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.network.NetworkService;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.PortsRange;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static org.elasticsearch.common.util.concurrent.EsExecutors.daemonThreadFactory;

/**
 */
public class BulkUdpService extends AbstractLifecycleComponent<BulkUdpService> {

    private final Client client;
    private final NetworkService networkService;

    private final boolean enabled;

    final String host;
    final String port;

    final ByteSizeValue receiveBufferSize;
    final ReceiveBufferSizePredictorFactory receiveBufferSizePredictorFactory;
    final int bulkActions;
    final ByteSizeValue bulkSize;
    final TimeValue flushInterval;
    final int concurrentRequests;

    private BulkProcessor bulkProcessor;
    private ConnectionlessBootstrap bootstrap;
    private Channel channel;

    @Inject
    public BulkUdpService(Settings settings, Client client, NetworkService networkService) {
        super(settings);
        this.client = client;
        this.networkService = networkService;

        this.host = componentSettings.get("host");
        this.port = componentSettings.get("port", "9700-9800");

        this.bulkActions = componentSettings.getAsInt("bulk_actions", 1000);
        this.bulkSize = componentSettings.getAsBytesSize("bulk_size", new ByteSizeValue(5, ByteSizeUnit.MB));
        this.flushInterval = componentSettings.getAsTime("flush_interval", TimeValue.timeValueSeconds(5));
        this.concurrentRequests = componentSettings.getAsInt("concurrent_requests", 4);

        this.receiveBufferSize = componentSettings.getAsBytesSize("receive_buffer_size", new ByteSizeValue(10, ByteSizeUnit.MB));
        this.receiveBufferSizePredictorFactory = new FixedReceiveBufferSizePredictorFactory(componentSettings.getAsBytesSize("receive_predictor_size", receiveBufferSize).bytesAsInt());

        this.enabled = componentSettings.getAsBoolean("enabled", false);

        logger.debug("using enabled [{}], host [{}], port [{}], bulk_actions [{}], bulk_size [{}], flush_interval [{}], concurrent_requests [{}]",
                enabled, host, port, bulkActions, bulkSize, flushInterval, concurrentRequests);
    }

    @Override
    protected void doStart() throws ElasticSearchException {
        if (!enabled) {
            return;
        }
        bulkProcessor = BulkProcessor.builder(client, new BulkListener())
                .setBulkActions(bulkActions)
                .setBulkSize(bulkSize)
                .setFlushInterval(flushInterval)
                .setConcurrentRequests(concurrentRequests)
                .build();


        bootstrap = new ConnectionlessBootstrap(new NioDatagramChannelFactory(Executors.newCachedThreadPool(daemonThreadFactory(settings, "bulk_udp_worker"))));

        bootstrap.setOption("receiveBufferSize", receiveBufferSize.bytesAsInt());
        bootstrap.setOption("receiveBufferSizePredictorFactory", receiveBufferSizePredictorFactory);

        // Enable broadcast
        bootstrap.setOption("broadcast", "false");

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new Handler());
            }
        });


        InetAddress hostAddressX;
        try {
            hostAddressX = networkService.resolveBindHostAddress(host);
        } catch (IOException e) {
            logger.warn("failed to resolve host {}", e, host);
            return;
        }
        final InetAddress hostAddress = hostAddressX;

        PortsRange portsRange = new PortsRange(port);
        final AtomicReference<Exception> lastException = new AtomicReference<Exception>();
        boolean success = portsRange.iterate(new PortsRange.PortCallback() {
            @Override
            public boolean onPortNumber(int portNumber) {
                try {
                    channel = bootstrap.bind(new InetSocketAddress(hostAddress, portNumber));
                } catch (Exception e) {
                    lastException.set(e);
                    return false;
                }
                return true;
            }
        });
        if (!success) {
            logger.warn("failed to bind to {}/{}", lastException.get(), hostAddress, port);
            return;
        }

        logger.info("address {}", channel.getLocalAddress());
    }

    @Override
    protected void doStop() throws ElasticSearchException {
        if (!enabled) {
            return;
        }
        if (channel != null) {
            channel.close().awaitUninterruptibly();
        }
        if (bootstrap != null) {
            bootstrap.releaseExternalResources();
        }
        bulkProcessor.close();
    }

    @Override
    protected void doClose() throws ElasticSearchException {
    }

    class Handler extends SimpleChannelUpstreamHandler {

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
            logger.trace("received message size [{}]", buffer.readableBytes());
            try {
                bulkProcessor.add(new ChannelBufferBytesReference(buffer), false, null, null);
            } catch (Exception e1) {
                logger.warn("failed to execute bulk request", e1);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            if (e.getCause() instanceof BindException) {
                // ignore, this happens when we retry binding to several ports, its fine if we fail...
                return;
            }
            logger.warn("failure caught", e.getCause());
        }
    }

    class BulkListener implements BulkProcessor.Listener {

        @Override
        public void beforeBulk(long executionId, BulkRequest request) {
            if (logger.isTraceEnabled()) {
                logger.trace("[{}] executing [{}]/[{}]", executionId, request.numberOfActions(), new ByteSizeValue(request.estimatedSizeInBytes()));
            }
        }

        @Override
        public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
            if (logger.isTraceEnabled()) {
                logger.trace("[{}] executed  [{}]/[{}], took [{}]", executionId, request.numberOfActions(), new ByteSizeValue(request.estimatedSizeInBytes()), response.took());
            }
            if (response.hasFailures()) {
                logger.warn("[{}] failed to execute bulk request: {}", executionId, response.buildFailureMessage());
            }
        }

        @Override
        public void afterBulk(long executionId, BulkRequest request, Throwable e) {
            logger.warn("[{}] failed to execute bulk request", e, executionId);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5209.java