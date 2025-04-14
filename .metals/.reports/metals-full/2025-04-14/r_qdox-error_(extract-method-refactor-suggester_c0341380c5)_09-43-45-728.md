error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8563.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8563.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8563.java
text:
```scala
final T@@hreadPool threadPool = new ThreadPool("BenchmarkNettyLargeMessages");

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

package org.elasticsearch.benchmark.transport;

import org.apache.lucene.util.BytesRef;
import org.elasticsearch.Version;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.network.NetworkService;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.util.BigArrays;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.*;
import org.elasticsearch.transport.netty.NettyTransport;

import java.util.concurrent.CountDownLatch;

import static org.elasticsearch.transport.TransportRequestOptions.options;

/**
 *
 */
public class BenchmarkNettyLargeMessages {

    public static void main(String[] args) throws InterruptedException {
        final ByteSizeValue payloadSize = new ByteSizeValue(10, ByteSizeUnit.MB);
        final int NUMBER_OF_ITERATIONS = 100000;
        final int NUMBER_OF_CLIENTS = 5;
        final byte[] payload = new byte[(int) payloadSize.bytes()];

        Settings settings = ImmutableSettings.settingsBuilder()
                .build();

        NetworkService networkService = new NetworkService(settings);

        final ThreadPool threadPool = new ThreadPool();
        final TransportService transportServiceServer = new TransportService(new NettyTransport(settings, threadPool, networkService, BigArrays.NON_RECYCLING_INSTANCE, Version.CURRENT), threadPool).start();
        final TransportService transportServiceClient = new TransportService(new NettyTransport(settings, threadPool, networkService, BigArrays.NON_RECYCLING_INSTANCE, Version.CURRENT), threadPool).start();

        final DiscoveryNode bigNode = new DiscoveryNode("big", new InetSocketTransportAddress("localhost", 9300), Version.CURRENT);
//        final DiscoveryNode smallNode = new DiscoveryNode("small", new InetSocketTransportAddress("localhost", 9300));
        final DiscoveryNode smallNode = bigNode;

        transportServiceClient.connectToNode(bigNode);
        transportServiceClient.connectToNode(smallNode);

        transportServiceServer.registerHandler("benchmark", new BaseTransportRequestHandler<BenchmarkMessageRequest>() {
            @Override
            public BenchmarkMessageRequest newInstance() {
                return new BenchmarkMessageRequest();
            }

            @Override
            public String executor() {
                return ThreadPool.Names.GENERIC;
            }

            @Override
            public void messageReceived(BenchmarkMessageRequest request, TransportChannel channel) throws Exception {
                channel.sendResponse(new BenchmarkMessageResponse(request));
            }
        });

        final CountDownLatch latch = new CountDownLatch(NUMBER_OF_CLIENTS);
        for (int i = 0; i < NUMBER_OF_CLIENTS; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
                        BenchmarkMessageRequest message = new BenchmarkMessageRequest(1, payload);
                        transportServiceClient.submitRequest(bigNode, "benchmark", message, options().withType(TransportRequestOptions.Type.BULK), new BaseTransportResponseHandler<BenchmarkMessageResponse>() {
                            @Override
                            public BenchmarkMessageResponse newInstance() {
                                return new BenchmarkMessageResponse();
                            }

                            @Override
                            public String executor() {
                                return ThreadPool.Names.SAME;
                            }

                            @Override
                            public void handleResponse(BenchmarkMessageResponse response) {
                            }

                            @Override
                            public void handleException(TransportException exp) {
                                exp.printStackTrace();
                            }
                        }).txGet();
                    }
                    latch.countDown();
                }
            }).start();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1; i++) {
                    BenchmarkMessageRequest message = new BenchmarkMessageRequest(2, BytesRef.EMPTY_BYTES);
                    long start = System.currentTimeMillis();
                    transportServiceClient.submitRequest(smallNode, "benchmark", message, options().withType(TransportRequestOptions.Type.STATE), new BaseTransportResponseHandler<BenchmarkMessageResponse>() {
                        @Override
                        public BenchmarkMessageResponse newInstance() {
                            return new BenchmarkMessageResponse();
                        }

                        @Override
                        public String executor() {
                            return ThreadPool.Names.SAME;
                        }

                        @Override
                        public void handleResponse(BenchmarkMessageResponse response) {
                        }

                        @Override
                        public void handleException(TransportException exp) {
                            exp.printStackTrace();
                        }
                    }).txGet();
                    long took = System.currentTimeMillis() - start;
                    System.out.println("Took " + took + "ms");
                }
            }
        }).start();

        latch.await();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8563.java