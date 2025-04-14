error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7364.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7364.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7364.java
text:
```scala
private final L@@ist<RiverClusterStateListener> clusterStateListeners = new CopyOnWriteArrayList<>();

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

package org.elasticsearch.river.cluster;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.TransportService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.elasticsearch.common.util.concurrent.EsExecutors.daemonThreadFactory;

/**
 *
 */
public class RiverClusterService extends AbstractLifecycleComponent<RiverClusterService> {

    private final ClusterService clusterService;

    private final PublishRiverClusterStateAction publishAction;

    private final List<RiverClusterStateListener> clusterStateListeners = new CopyOnWriteArrayList<RiverClusterStateListener>();

    private volatile ExecutorService updateTasksExecutor;

    private volatile RiverClusterState clusterState = RiverClusterState.builder().build();

    @Inject
    public RiverClusterService(Settings settings, TransportService transportService, ClusterService clusterService) {
        super(settings);
        this.clusterService = clusterService;

        this.publishAction = new PublishRiverClusterStateAction(settings, transportService, clusterService, new UpdateClusterStateListener());
    }

    @Override
    protected void doStart() throws ElasticsearchException {
        this.updateTasksExecutor = newSingleThreadExecutor(daemonThreadFactory(settings, "riverClusterService#updateTask"));
    }

    @Override
    protected void doStop() throws ElasticsearchException {
        updateTasksExecutor.shutdown();
        try {
            updateTasksExecutor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    @Override
    protected void doClose() throws ElasticsearchException {
    }

    public void add(RiverClusterStateListener listener) {
        clusterStateListeners.add(listener);
    }

    public void remove(RiverClusterStateListener listener) {
        clusterStateListeners.remove(listener);
    }

    /**
     * The current state.
     */
    public ClusterState state() {
        return clusterService.state();
    }

    public void submitStateUpdateTask(final String source, final RiverClusterStateUpdateTask updateTask) {
        if (!lifecycle.started()) {
            return;
        }
        updateTasksExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if (!lifecycle.started()) {
                    logger.debug("processing [{}]: ignoring, cluster_service not started", source);
                    return;
                }
                logger.debug("processing [{}]: execute", source);

                RiverClusterState previousClusterState = clusterState;
                try {
                    clusterState = updateTask.execute(previousClusterState);
                } catch (Exception e) {
                    StringBuilder sb = new StringBuilder("failed to execute cluster state update, state:\nversion [").append(clusterState.version()).append("], source [").append(source).append("]\n");
                    logger.warn(sb.toString(), e);
                    return;
                }
                if (previousClusterState != clusterState) {
                    if (clusterService.state().nodes().localNodeMaster()) {
                        // only the master controls the version numbers
                        clusterState = new RiverClusterState(clusterState.version() + 1, clusterState);
                    } else {
                        // we got this cluster state from the master, filter out based on versions (don't call listeners)
                        if (clusterState.version() < previousClusterState.version()) {
                            logger.debug("got old cluster state [" + clusterState.version() + "<" + previousClusterState.version() + "] from source [" + source + "], ignoring");
                            return;
                        }
                    }

                    if (logger.isTraceEnabled()) {
                        StringBuilder sb = new StringBuilder("cluster state updated:\nversion [").append(clusterState.version()).append("], source [").append(source).append("]\n");
                        logger.trace(sb.toString());
                    } else if (logger.isDebugEnabled()) {
                        logger.debug("cluster state updated, version [{}], source [{}]", clusterState.version(), source);
                    }

                    RiverClusterChangedEvent clusterChangedEvent = new RiverClusterChangedEvent(source, clusterState, previousClusterState);

                    for (RiverClusterStateListener listener : clusterStateListeners) {
                        listener.riverClusterChanged(clusterChangedEvent);
                    }

                    // if we are the master, publish the new state to all nodes
                    if (clusterService.state().nodes().localNodeMaster()) {
                        publishAction.publish(clusterState);
                    }

                    logger.debug("processing [{}]: done applying updated cluster_state", source);
                } else {
                    logger.debug("processing [{}]: no change in cluster_state", source);
                }
            }
        });
    }

    private class UpdateClusterStateListener implements PublishRiverClusterStateAction.NewClusterStateListener {
        @Override
        public void onNewClusterState(final RiverClusterState clusterState) {
            ClusterState state = clusterService.state();
            if (state.nodes().localNodeMaster()) {
                logger.warn("master should not receive new cluster state from [{}]", state.nodes().masterNode());
                return;
            }

            submitStateUpdateTask("received_state", new RiverClusterStateUpdateTask() {
                @Override
                public RiverClusterState execute(RiverClusterState currentState) {
                    return clusterState;
                }
            });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7364.java