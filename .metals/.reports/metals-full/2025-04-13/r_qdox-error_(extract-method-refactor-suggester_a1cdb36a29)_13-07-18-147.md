error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7843.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7843.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7843.java
text:
```scala
.@@mappingsMetaData(indexMetaData.mappings())

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

package org.elasticsearch.gateway.shared;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.cluster.*;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.cluster.metadata.MetaDataCreateIndexService;
import org.elasticsearch.common.StopWatch;
import org.elasticsearch.common.collect.ImmutableSet;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.gateway.Gateway;
import org.elasticsearch.gateway.GatewayException;
import org.elasticsearch.gateway.GatewayService;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.Executors.*;
import static org.elasticsearch.cluster.ClusterState.*;
import static org.elasticsearch.cluster.metadata.MetaData.*;
import static org.elasticsearch.common.unit.TimeValue.*;
import static org.elasticsearch.common.util.concurrent.EsExecutors.*;

/**
 * @author kimchy (shay.banon)
 */
public abstract class SharedStorageGateway extends AbstractLifecycleComponent<Gateway> implements Gateway, ClusterStateListener {

    private final ClusterService clusterService;

    private final MetaDataCreateIndexService createIndexService;

    private volatile boolean performedStateRecovery = false;

    private volatile ExecutorService executor;

    public SharedStorageGateway(Settings settings, ClusterService clusterService, MetaDataCreateIndexService createIndexService) {
        super(settings);
        this.clusterService = clusterService;
        this.createIndexService = createIndexService;
    }

    @Override protected void doStart() throws ElasticSearchException {
        this.executor = newSingleThreadExecutor(daemonThreadFactory(settings, "gateway"));
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
    }

    @Override protected void doClose() throws ElasticSearchException {
    }

    @Override public void performStateRecovery(final GatewayStateRecoveredListener listener) throws GatewayException {
        performedStateRecovery = true;
        executor.execute(new Runnable() {
            @Override public void run() {
                logger.debug("reading state from gateway {} ...", this);
                StopWatch stopWatch = new StopWatch().start();
                MetaData metaData;
                try {
                    metaData = read();
                    logger.debug("read state from gateway {}, took {}", this, stopWatch.stop().totalTime());
                    if (metaData == null) {
                        logger.debug("no state read from gateway");
                        listener.onSuccess();
                    } else {
                        updateClusterStateFromGateway(metaData, listener);
                    }
                } catch (Exception e) {
                    logger.error("failed to read from gateway", e);
                    listener.onFailure(e);
                }
            }
        });
    }

    @Override public void clusterChanged(final ClusterChangedEvent event) {
        if (!lifecycle.started()) {
            return;
        }
        if (!performedStateRecovery) {
            return;
        }
        if (event.localNodeMaster()) {
            if (!event.metaDataChanged()) {
                return;
            }
            executor.execute(new Runnable() {
                @Override public void run() {
                    logger.debug("writing to gateway {} ...", this);
                    StopWatch stopWatch = new StopWatch().start();
                    try {
                        write(event.state().metaData());
                        logger.debug("wrote to gateway {}, took {}", this, stopWatch.stop().totalTime());
                        // TODO, we need to remember that we failed, maybe add a retry scheduler?
                    } catch (Exception e) {
                        logger.error("failed to write to gateway", e);
                    }
                }
            });
        }
    }

    private void updateClusterStateFromGateway(final MetaData fMetaData, final GatewayStateRecoveredListener listener) {
        final AtomicInteger indicesCounter = new AtomicInteger(fMetaData.indices().size());
        clusterService.submitStateUpdateTask("gateway (recovered meta-data)", new ProcessedClusterStateUpdateTask() {
            @Override public ClusterState execute(ClusterState currentState) {
                MetaData.Builder metaDataBuilder = newMetaDataBuilder()
                        .metaData(currentState.metaData());
                // mark the metadata as read from gateway
                metaDataBuilder.markAsRecoveredFromGateway();
                return newClusterStateBuilder().state(currentState).metaData(metaDataBuilder).build();
            }

            @Override public void clusterStateProcessed(ClusterState clusterState) {
                // go over the meta data and create indices, we don't really need to copy over
                // the meta data per index, since we create the index and it will be added automatically
                for (final IndexMetaData indexMetaData : fMetaData) {
                    try {
                        createIndexService.createIndex(new MetaDataCreateIndexService.Request("gateway", indexMetaData.index())
                                .settings(indexMetaData.settings())
                                .mappingsCompressed(indexMetaData.mappings())
                                .state(indexMetaData.state())
                                .blocks(ImmutableSet.of(GatewayService.INDEX_NOT_RECOVERED_BLOCK))
                                .timeout(timeValueSeconds(30)),

                                new MetaDataCreateIndexService.Listener() {
                                    @Override public void onResponse(MetaDataCreateIndexService.Response response) {
                                        if (indicesCounter.decrementAndGet() == 0) {
                                            listener.onSuccess();
                                        }
                                    }

                                    @Override public void onFailure(Throwable t) {
                                        logger.error("failed to create index [{}]", t, indexMetaData.index());
                                    }
                                });
                    } catch (IOException e) {
                        logger.error("failed to create index [{}]", e, indexMetaData.index());
                    }
                }
            }
        });
    }

    protected abstract MetaData read() throws ElasticSearchException;

    protected abstract void write(MetaData metaData) throws ElasticSearchException;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7843.java