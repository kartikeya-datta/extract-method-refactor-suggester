error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7291.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7291.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7291.java
text:
```scala
final A@@tomicReference<PingResponse[]> response = new AtomicReference<>();

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

package org.elasticsearch.discovery.zen.ping;

import com.google.common.collect.ImmutableList;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchIllegalStateException;
import org.elasticsearch.Version;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.network.NetworkService;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.util.concurrent.ConcurrentCollections;
import org.elasticsearch.common.util.concurrent.EsRejectedExecutionException;
import org.elasticsearch.discovery.zen.DiscoveryNodesProvider;
import org.elasticsearch.discovery.zen.ping.multicast.MulticastZenPing;
import org.elasticsearch.discovery.zen.ping.unicast.UnicastHostsProvider;
import org.elasticsearch.discovery.zen.ping.unicast.UnicastZenPing;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 */
public class ZenPingService extends AbstractLifecycleComponent<ZenPing> implements ZenPing {

    private volatile ImmutableList<? extends ZenPing> zenPings = ImmutableList.of();

    // here for backward comp. with discovery plugins
    public ZenPingService(Settings settings, ThreadPool threadPool, TransportService transportService, ClusterName clusterName, NetworkService networkService,
                          @Nullable Set<UnicastHostsProvider> unicastHostsProviders) {
        this(settings, threadPool, transportService, clusterName, networkService, Version.CURRENT, unicastHostsProviders);
    }

    @Inject
    public ZenPingService(Settings settings, ThreadPool threadPool, TransportService transportService, ClusterName clusterName, NetworkService networkService,
                          Version version, @Nullable Set<UnicastHostsProvider> unicastHostsProviders) {
        super(settings);
        ImmutableList.Builder<ZenPing> zenPingsBuilder = ImmutableList.builder();
        if (componentSettings.getAsBoolean("multicast.enabled", true)) {
            zenPingsBuilder.add(new MulticastZenPing(settings, threadPool, transportService, clusterName, networkService, version));
        }
        // always add the unicast hosts, so it will be able to receive unicast requests even when working in multicast
        zenPingsBuilder.add(new UnicastZenPing(settings, threadPool, transportService, clusterName, version, unicastHostsProviders));

        this.zenPings = zenPingsBuilder.build();
    }

    public ImmutableList<? extends ZenPing> zenPings() {
        return this.zenPings;
    }

    public void zenPings(ImmutableList<? extends ZenPing> pings) {
        this.zenPings = pings;
        if (lifecycle.started()) {
            for (ZenPing zenPing : zenPings) {
                zenPing.start();
            }
        } else if (lifecycle.stopped()) {
            for (ZenPing zenPing : zenPings) {
                zenPing.stop();
            }
        }
    }

    @Override
    public void setNodesProvider(DiscoveryNodesProvider nodesProvider) {
        if (lifecycle.started()) {
            throw new ElasticsearchIllegalStateException("Can't set nodes provider when started");
        }
        for (ZenPing zenPing : zenPings) {
            zenPing.setNodesProvider(nodesProvider);
        }
    }

    @Override
    protected void doStart() throws ElasticsearchException {
        for (ZenPing zenPing : zenPings) {
            zenPing.start();
        }
    }

    @Override
    protected void doStop() throws ElasticsearchException {
        for (ZenPing zenPing : zenPings) {
            zenPing.stop();
        }
    }

    @Override
    protected void doClose() throws ElasticsearchException {
        for (ZenPing zenPing : zenPings) {
            zenPing.close();
        }
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
            logger.trace("pingAndWait interrupted");
            return null;
        }
    }

    @Override
    public void ping(PingListener listener, TimeValue timeout) throws ElasticsearchException {
        ImmutableList<? extends ZenPing> zenPings = this.zenPings;
        CompoundPingListener compoundPingListener = new CompoundPingListener(listener, zenPings);
        for (ZenPing zenPing : zenPings) {
            try {
                zenPing.ping(compoundPingListener, timeout);
            } catch (EsRejectedExecutionException ex) {
                logger.debug("Ping execution rejected", ex);
                compoundPingListener.onPing(null);
            }
        }
    }

    private static class CompoundPingListener implements PingListener {

        private final PingListener listener;

        private final AtomicInteger counter;

        private ConcurrentMap<DiscoveryNode, PingResponse> responses = ConcurrentCollections.newConcurrentMap();

        private CompoundPingListener(PingListener listener, ImmutableList<? extends ZenPing> zenPings) {
            this.listener = listener;
            this.counter = new AtomicInteger(zenPings.size());
        }

        @Override
        public void onPing(PingResponse[] pings) {
            if (pings != null) {
                for (PingResponse pingResponse : pings) {
                    responses.put(pingResponse.target(), pingResponse);
                }
            }
            if (counter.decrementAndGet() == 0) {
                listener.onPing(responses.values().toArray(new PingResponse[responses.size()]));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7291.java