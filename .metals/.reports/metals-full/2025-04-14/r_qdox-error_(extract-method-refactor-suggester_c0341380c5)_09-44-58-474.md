error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7480.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7480.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7480.java
text:
```scala
n@@odeIndexDeletedAction.nodeIndexStoreDeleted(event.state(), indexDeleted, event.state().nodes().localNodeId());

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

package org.elasticsearch.gateway.shared;

import com.google.common.collect.Sets;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.ExceptionsHelper;
import org.elasticsearch.cluster.ClusterChangedEvent;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.ClusterStateListener;
import org.elasticsearch.cluster.action.index.NodeIndexDeletedAction;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.common.StopWatch;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.FileSystemUtils;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.NodeEnvironment;
import org.elasticsearch.gateway.Gateway;
import org.elasticsearch.gateway.GatewayException;
import org.elasticsearch.index.Index;
import org.elasticsearch.threadpool.ThreadPool;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.elasticsearch.common.util.concurrent.EsExecutors.daemonThreadFactory;

/**
 *
 */
public abstract class SharedStorageGateway extends AbstractLifecycleComponent<Gateway> implements Gateway, ClusterStateListener {

    private final ClusterService clusterService;

    private final ThreadPool threadPool;

    private ExecutorService writeStateExecutor;

    private volatile MetaData currentMetaData;

    private NodeEnvironment nodeEnv;

    private NodeIndexDeletedAction nodeIndexDeletedAction;

    public SharedStorageGateway(Settings settings, ThreadPool threadPool, ClusterService clusterService) {
        super(settings);
        this.threadPool = threadPool;
        this.clusterService = clusterService;
        this.writeStateExecutor = newSingleThreadExecutor(daemonThreadFactory(settings, "gateway#writeMetaData"));
        clusterService.addLast(this);
        logger.warn("shared gateway has been deprecated, please use the (default) local gateway");
    }

    @Inject
    public void setNodeEnv(NodeEnvironment nodeEnv) {
        this.nodeEnv = nodeEnv;
    }

    // here as setter injection not to break backward comp. with extensions of this class..
    @Inject
    public void setNodeIndexDeletedAction(NodeIndexDeletedAction nodeIndexDeletedAction) {
        this.nodeIndexDeletedAction = nodeIndexDeletedAction;
    }

    @Override
    protected void doStart() throws ElasticSearchException {
    }

    @Override
    protected void doStop() throws ElasticSearchException {
    }

    @Override
    protected void doClose() throws ElasticSearchException {
        clusterService.remove(this);
        writeStateExecutor.shutdown();
        try {
            writeStateExecutor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    @Override
    public void performStateRecovery(final GatewayStateRecoveredListener listener) throws GatewayException {
        threadPool.generic().execute(new Runnable() {
            @Override
            public void run() {
                logger.debug("reading state from gateway {} ...", this);
                StopWatch stopWatch = new StopWatch().start();
                MetaData metaData;
                try {
                    metaData = read();
                    logger.debug("read state from gateway {}, took {}", this, stopWatch.stop().totalTime());
                    if (metaData == null) {
                        logger.debug("no state read from gateway");
                        listener.onSuccess(ClusterState.builder().build());
                    } else {
                        listener.onSuccess(ClusterState.builder().metaData(metaData).build());
                    }
                } catch (Exception e) {
                    logger.error("failed to read from gateway", e);
                    listener.onFailure(ExceptionsHelper.detailedMessage(e));
                }
            }
        });
    }

    @Override
    public void clusterChanged(final ClusterChangedEvent event) {
        if (!lifecycle.started()) {
            return;
        }

        // nothing to do until we actually recover from the gateway or any other block indicates we need to disable persistency
        if (event.state().blocks().disableStatePersistence()) {
            this.currentMetaData = null;
            return;
        }

        if (!event.metaDataChanged()) {
            return;
        }
        writeStateExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Set<String> indicesDeleted = Sets.newHashSet();
                if (event.localNodeMaster()) {
                    logger.debug("writing to gateway {} ...", this);
                    StopWatch stopWatch = new StopWatch().start();
                    try {
                        write(event.state().metaData());
                        logger.debug("wrote to gateway {}, took {}", this, stopWatch.stop().totalTime());
                        // TODO, we need to remember that we failed, maybe add a retry scheduler?
                    } catch (Exception e) {
                        logger.error("failed to write to gateway", e);
                    }
                    if (currentMetaData != null) {
                        for (IndexMetaData current : currentMetaData) {
                            if (!event.state().metaData().hasIndex(current.index())) {
                                delete(current);
                                indicesDeleted.add(current.index());
                            }
                        }
                    }
                }
                if (nodeEnv != null && nodeEnv.hasNodeFile()) {
                    if (currentMetaData != null) {
                        for (IndexMetaData current : currentMetaData) {
                            if (!event.state().metaData().hasIndex(current.index())) {
                                FileSystemUtils.deleteRecursively(nodeEnv.indexLocations(new Index(current.index())));
                                indicesDeleted.add(current.index());
                            }
                        }
                    }
                }
                currentMetaData = event.state().metaData();

                for (String indexDeleted : indicesDeleted) {
                    try {
                        nodeIndexDeletedAction.nodeIndexStoreDeleted(indexDeleted, event.state().nodes().masterNodeId());
                    } catch (Exception e) {
                        logger.debug("[{}] failed to notify master on local index store deletion", e, indexDeleted);
                    }
                }
            }
        });
    }

    protected abstract MetaData read() throws ElasticSearchException;

    protected abstract void write(MetaData metaData) throws ElasticSearchException;

    protected abstract void delete(IndexMetaData indexMetaData) throws ElasticSearchException;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7480.java