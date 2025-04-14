error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5174.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5174.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5174.java
text:
```scala
final i@@nt NUMBER_OF_CLIENTS = 10;

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

package org.elasticsearch.benchmark.transport;

import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.StopWatch;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.*;
import org.elasticsearch.transport.local.LocalTransport;
import org.elasticsearch.transport.netty.NettyTransport;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
public class TransportBenchmark {

    static enum Type {
        LOCAL {
            @Override
            public Transport newTransport(Settings settings, ThreadPool threadPool) {
                return new LocalTransport(settings, threadPool);
            }
        },
        NETTY {
            @Override
            public Transport newTransport(Settings settings, ThreadPool threadPool) {
                return new NettyTransport(settings, threadPool);
            }
        };

        public abstract Transport newTransport(Settings settings, ThreadPool threadPool);
    }

    public static void main(String[] args) {
        final String executor = ThreadPool.Names.GENERIC;
        final boolean waitForRequest = true;
        final ByteSizeValue payloadSize = new ByteSizeValue(100, ByteSizeUnit.BYTES);
        final int NUMBER_OF_CLIENTS = 1;
        final int NUMBER_OF_ITERATIONS = 100000;
        final byte[] payload = new byte[(int) payloadSize.bytes()];
        final AtomicLong idGenerator = new AtomicLong();
        final Type type = Type.NETTY;


        Settings settings = ImmutableSettings.settingsBuilder()
                .build();

        final ThreadPool serverThreadPool = new ThreadPool();
        final TransportService serverTransportService = new TransportService(type.newTransport(settings, serverThreadPool), serverThreadPool).start();

        final ThreadPool clientThreadPool = new ThreadPool();
        final TransportService clientTransportService = new TransportService(type.newTransport(settings, clientThreadPool), clientThreadPool).start();

        final DiscoveryNode node = new DiscoveryNode("server", serverTransportService.boundAddress().publishAddress());

        serverTransportService.registerHandler("benchmark", new BaseTransportRequestHandler<BenchmarkMessage>() {
            @Override
            public BenchmarkMessage newInstance() {
                return new BenchmarkMessage();
            }

            @Override
            public String executor() {
                return executor;
            }

            @Override
            public void messageReceived(BenchmarkMessage request, TransportChannel channel) throws Exception {
                channel.sendResponse(request);
            }
        });

        clientTransportService.connectToNode(node);

        for (int i = 0; i < 10000; i++) {
            BenchmarkMessage message = new BenchmarkMessage(1, payload);
            clientTransportService.submitRequest(node, "benchmark", message, new BaseTransportResponseHandler<BenchmarkMessage>() {
                @Override
                public BenchmarkMessage newInstance() {
                    return new BenchmarkMessage();
                }

                @Override
                public String executor() {
                    return ThreadPool.Names.SAME;
                }

                @Override
                public void handleResponse(BenchmarkMessage response) {
                }

                @Override
                public void handleException(TransportException exp) {
                    exp.printStackTrace();
                }
            }).txGet();
        }


        Thread[] clients = new Thread[NUMBER_OF_CLIENTS];
        final CountDownLatch latch = new CountDownLatch(NUMBER_OF_CLIENTS * NUMBER_OF_ITERATIONS);
        for (int i = 0; i < NUMBER_OF_CLIENTS; i++) {
            clients[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < NUMBER_OF_ITERATIONS; j++) {
                        final long id = idGenerator.incrementAndGet();
                        BenchmarkMessage message = new BenchmarkMessage(id, payload);
                        BaseTransportResponseHandler<BenchmarkMessage> handler = new BaseTransportResponseHandler<BenchmarkMessage>() {
                            @Override
                            public BenchmarkMessage newInstance() {
                                return new BenchmarkMessage();
                            }

                            @Override
                            public String executor() {
                                return executor;
                            }

                            @Override
                            public void handleResponse(BenchmarkMessage response) {
                                if (response.id != id) {
                                    System.out.println("NO ID MATCH [" + response.id + "] and [" + id + "]");
                                }
                                latch.countDown();
                            }

                            @Override
                            public void handleException(TransportException exp) {
                                exp.printStackTrace();
                                latch.countDown();
                            }
                        };

                        if (waitForRequest) {
                            clientTransportService.submitRequest(node, "benchmark", message, handler).txGet();
                        } else {
                            clientTransportService.sendRequest(node, "benchmark", message, handler);
                        }
                    }
                }
            });
        }

        StopWatch stopWatch = new StopWatch().start();
        for (int i = 0; i < NUMBER_OF_CLIENTS; i++) {
            clients[i].start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();

        System.out.println("Ran [" + NUMBER_OF_CLIENTS + "], each with [" + NUMBER_OF_ITERATIONS + "] iterations, payload [" + payloadSize + "]: took [" + stopWatch.totalTime() + "], TPS: " + (NUMBER_OF_CLIENTS * NUMBER_OF_ITERATIONS) / stopWatch.totalTime().secondsFrac());

        clientTransportService.close();
        clientThreadPool.shutdownNow();

        serverTransportService.close();
        serverThreadPool.shutdownNow();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5174.java