error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1428.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1428.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1428.java
text:
```scala
t@@his.initialStateTimeout = componentSettings.getAsTime("initial_state_timeout", TimeValue.timeValueSeconds(30));

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

package org.elasticsearch.gateway;

import com.google.inject.Inject;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.cluster.*;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.cluster.metadata.MetaDataService;
import org.elasticsearch.discovery.DiscoveryService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.util.Nullable;
import org.elasticsearch.util.TimeValue;
import org.elasticsearch.util.component.AbstractLifecycleComponent;
import org.elasticsearch.util.settings.Settings;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.Executors.*;
import static org.elasticsearch.cluster.ClusterState.*;
import static org.elasticsearch.cluster.metadata.MetaData.*;
import static org.elasticsearch.util.TimeValue.*;
import static org.elasticsearch.util.concurrent.DynamicExecutors.*;

/**
 * @author kimchy (Shay Banon)
 */
public class GatewayService extends AbstractLifecycleComponent<GatewayService> implements ClusterStateListener {

    private final TimeValue initialStateTimeout;

    private final Gateway gateway;

    private final ThreadPool threadPool;

    private volatile ExecutorService executor;

    private final ClusterService clusterService;

    private final DiscoveryService discoveryService;

    private final MetaDataService metaDataService;

    private final AtomicBoolean firstMasterRead = new AtomicBoolean();

    @Inject public GatewayService(Settings settings, Gateway gateway, ClusterService clusterService, DiscoveryService discoveryService,
                                  ThreadPool threadPool, MetaDataService metaDataService) {
        super(settings);
        this.gateway = gateway;
        this.clusterService = clusterService;
        this.discoveryService = discoveryService;
        this.threadPool = threadPool;
        this.metaDataService = metaDataService;
        this.initialStateTimeout = componentSettings.getAsTime("initialStateTimeout", TimeValue.timeValueSeconds(30));
    }

    @Override protected void doStart() throws ElasticSearchException {
        gateway.start();
        this.executor = newSingleThreadExecutor(daemonThreadFactory(settings, "gateway"));
        // if we received initial state, see if we can recover within the start phase, so we hold the
        // server from starting until we recovered properly
        if (discoveryService.initialStateReceived()) {
            if (discoveryService.firstMaster()) {
                if (firstMasterRead.compareAndSet(false, true)) {
                    boolean waited = readFromGateway(initialStateTimeout);
                    if (!waited) {
                        logger.warn("Waited for {} for indices to be created from the gateway, and not all have been created", initialStateTimeout);
                    }
                }
            }
        } else {
            logger.debug("Can't wait on start for (possibly) reading state from gateway, will do it asynchronously");
        }
        clusterService.add(this);
    }

    @Override protected void doStop() throws ElasticSearchException {
        clusterService.remove(this);
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // ignore
        }
        gateway.stop();
    }

    @Override protected void doClose() throws ElasticSearchException {
        gateway.close();
    }

    @Override public void clusterChanged(final ClusterChangedEvent event) {
        if (lifecycle.started() && event.localNodeMaster()) {
            if (event.firstMaster() && firstMasterRead.compareAndSet(false, true)) {
                executor.execute(new Runnable() {
                    @Override public void run() {
                        readFromGateway(null);
                    }
                });
            } else {
                writeToGateway(event);
            }
        }
    }

    private void writeToGateway(final ClusterChangedEvent event) {
        if (!event.metaDataChanged()) {
            return;
        }
        executor.execute(new Runnable() {
            @Override public void run() {
                logger.debug("Writing to gateway");
                try {
                    gateway.write(event.state().metaData());
                    // TODO, we need to remember that we failed, maybe add a retry scheduler?
                } catch (Exception e) {
                    logger.error("Failed to write to gateway", e);
                }
            }
        });
    }

    /**
     * Reads from the gateway. If the waitTimeout is set, will wait till all the indices
     * have been created from the meta data read from the gateway. Return value only applicable
     * when waiting, and indicates that everything was created within teh wait timeout.
     */
    private boolean readFromGateway(@Nullable TimeValue waitTimeout) {
        // we are the first master, go ahead and read and create indices
        logger.debug("First master in the cluster, reading state from gateway");
        MetaData metaData;
        try {
            metaData = gateway.read();
        } catch (Exception e) {
            logger.error("Failed to read from gateway", e);
            return false;
        }
        if (metaData == null) {
            logger.debug("No state read from gateway");
            return false;
        }
        final MetaData fMetaData = metaData;
        final CountDownLatch latch = new CountDownLatch(fMetaData.indices().size());
        clusterService.submitStateUpdateTask("gateway (recovered meta-data)", new ClusterStateUpdateTask() {
            @Override public ClusterState execute(ClusterState currentState) {
                MetaData.Builder metaDataBuilder = newMetaDataBuilder()
                        .metaData(currentState.metaData()).maxNumberOfShardsPerNode(fMetaData.maxNumberOfShardsPerNode());
                // go over the meta data and create indices, we don't really need to copy over
                // the meta data per index, since we create the index and it will be added automatically
                for (final IndexMetaData indexMetaData : fMetaData) {
                    threadPool.execute(new Runnable() {
                        @Override public void run() {
                            try {
                                metaDataService.createIndex("gateway", indexMetaData.index(), indexMetaData.settings(), indexMetaData.mappings(), timeValueMillis(initialStateTimeout.millis() - 1000));
                            } catch (Exception e) {
                                logger.error("Failed to create index [" + indexMetaData.index() + "]", e);
                            } finally {
                                latch.countDown();
                            }
                        }
                    });
                }
                return newClusterStateBuilder().state(currentState).metaData(metaDataBuilder).build();
            }
        });
        if (waitTimeout != null) {
            try {
                return latch.await(waitTimeout.millis(), TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // ignore
            }
        }
        return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1428.java