error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7159.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7159.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7159.java
text:
```scala
t@@his.updateTasksExecutor = EsExecutors.newSinglePrioritizing(daemonThreadFactory(settings, "clusterService#updateTask"));

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

package org.elasticsearch.cluster.service;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.cluster.*;
import org.elasticsearch.cluster.block.ClusterBlock;
import org.elasticsearch.cluster.block.ClusterBlocks;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.cluster.metadata.ProcessClusterEventTimeoutException;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.cluster.routing.RoutingTable;
import org.elasticsearch.cluster.routing.operation.OperationRouting;
import org.elasticsearch.common.Priority;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.StringText;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.util.concurrent.ConcurrentCollections;
import org.elasticsearch.common.util.concurrent.EsExecutors;
import org.elasticsearch.common.util.concurrent.PrioritizedEsThreadPoolExecutor;
import org.elasticsearch.common.util.concurrent.PrioritizedRunnable;
import org.elasticsearch.discovery.Discovery;
import org.elasticsearch.discovery.DiscoveryService;
import org.elasticsearch.node.settings.NodeSettingsService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

import static org.elasticsearch.cluster.ClusterState.Builder;
import static org.elasticsearch.cluster.ClusterState.newClusterStateBuilder;
import static org.elasticsearch.common.util.concurrent.EsExecutors.daemonThreadFactory;

/**
 *
 */
public class InternalClusterService extends AbstractLifecycleComponent<ClusterService> implements ClusterService {

    private final ThreadPool threadPool;

    private final DiscoveryService discoveryService;

    private final OperationRouting operationRouting;

    private final TransportService transportService;

    private final NodeSettingsService nodeSettingsService;

    private final TimeValue reconnectInterval;

    private volatile PrioritizedEsThreadPoolExecutor updateTasksExecutor;

    private final List<ClusterStateListener> priorityClusterStateListeners = new CopyOnWriteArrayList<ClusterStateListener>();
    private final List<ClusterStateListener> clusterStateListeners = new CopyOnWriteArrayList<ClusterStateListener>();
    private final List<ClusterStateListener> lastClusterStateListeners = new CopyOnWriteArrayList<ClusterStateListener>();
    private final LocalNodeMasterListeners localNodeMasterListeners;

    private final Queue<NotifyTimeout> onGoingTimeouts = ConcurrentCollections.newQueue();

    private volatile ClusterState clusterState = newClusterStateBuilder().build();

    private final ClusterBlocks.Builder initialBlocks = ClusterBlocks.builder().addGlobalBlock(Discovery.NO_MASTER_BLOCK);

    private volatile ScheduledFuture reconnectToNodes;

    @Inject
    public InternalClusterService(Settings settings, DiscoveryService discoveryService, OperationRouting operationRouting, TransportService transportService,
                                  NodeSettingsService nodeSettingsService, ThreadPool threadPool) {
        super(settings);
        this.operationRouting = operationRouting;
        this.transportService = transportService;
        this.discoveryService = discoveryService;
        this.threadPool = threadPool;
        this.nodeSettingsService = nodeSettingsService;

        this.nodeSettingsService.setClusterService(this);

        this.reconnectInterval = componentSettings.getAsTime("reconnect_interval", TimeValue.timeValueSeconds(10));

        localNodeMasterListeners = new LocalNodeMasterListeners(threadPool);
    }

    public NodeSettingsService settingsService() {
        return this.nodeSettingsService;
    }

    public void addInitialStateBlock(ClusterBlock block) throws ElasticSearchIllegalStateException {
        if (lifecycle.started()) {
            throw new ElasticSearchIllegalStateException("can't set initial block when started");
        }
        initialBlocks.addGlobalBlock(block);
    }

    @Override
    protected void doStart() throws ElasticSearchException {
        add(localNodeMasterListeners);
        this.clusterState = newClusterStateBuilder().blocks(initialBlocks).build();
        this.updateTasksExecutor = EsExecutors.newSinglePrioritizingThreadExecutor(daemonThreadFactory(settings, "clusterService#updateTask"));
        this.reconnectToNodes = threadPool.schedule(reconnectInterval, ThreadPool.Names.GENERIC, new ReconnectToNodes());
    }

    @Override
    protected void doStop() throws ElasticSearchException {
        this.reconnectToNodes.cancel(true);
        for (NotifyTimeout onGoingTimeout : onGoingTimeouts) {
            onGoingTimeout.cancel();
            onGoingTimeout.listener.onClose();
        }
        updateTasksExecutor.shutdown();
        try {
            updateTasksExecutor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // ignore
        }
        remove(localNodeMasterListeners);
    }

    @Override
    protected void doClose() throws ElasticSearchException {
    }

    @Override
    public DiscoveryNode localNode() {
        return discoveryService.localNode();
    }

    @Override
    public OperationRouting operationRouting() {
        return operationRouting;
    }

    public ClusterState state() {
        return this.clusterState;
    }

    public void addFirst(ClusterStateListener listener) {
        priorityClusterStateListeners.add(listener);
    }

    public void addLast(ClusterStateListener listener) {
        lastClusterStateListeners.add(listener);
    }

    public void add(ClusterStateListener listener) {
        clusterStateListeners.add(listener);
    }

    public void remove(ClusterStateListener listener) {
        clusterStateListeners.remove(listener);
        priorityClusterStateListeners.remove(listener);
        lastClusterStateListeners.remove(listener);
        for (Iterator<NotifyTimeout> it = onGoingTimeouts.iterator(); it.hasNext(); ) {
            NotifyTimeout timeout = it.next();
            if (timeout.listener.equals(listener)) {
                timeout.cancel();
                it.remove();
            }
        }
    }

    @Override
    public void add(LocalNodeMasterListener listener) {
        localNodeMasterListeners.add(listener);
    }

    @Override
    public void remove(LocalNodeMasterListener listener) {
        localNodeMasterListeners.remove(listener);
    }

    public void add(final TimeValue timeout, final TimeoutClusterStateListener listener) {
        if (lifecycle.stoppedOrClosed()) {
            listener.onClose();
            return;
        }
        // call the post added notification on the same event thread
        updateTasksExecutor.execute(new PrioritizedRunnable(Priority.HIGH) {
            @Override
            public void run() {
                NotifyTimeout notifyTimeout = new NotifyTimeout(listener, timeout);
                notifyTimeout.future = threadPool.schedule(timeout, ThreadPool.Names.GENERIC, notifyTimeout);
                onGoingTimeouts.add(notifyTimeout);
                clusterStateListeners.add(listener);
                listener.postAdded();
            }
        });
    }

    public void submitStateUpdateTask(final String source, final ClusterStateUpdateTask updateTask) {
        submitStateUpdateTask(source, Priority.NORMAL, updateTask);
    }

    public void submitStateUpdateTask(final String source, Priority priority, final ClusterStateUpdateTask updateTask) {
        if (!lifecycle.started()) {
            return;
        }
        final UpdateTask task = new UpdateTask(source, priority, updateTask);
        if (updateTask instanceof TimeoutClusterStateUpdateTask) {
            final TimeoutClusterStateUpdateTask timeoutUpdateTask = (TimeoutClusterStateUpdateTask) updateTask;
            updateTasksExecutor.execute(task, threadPool.scheduler(), timeoutUpdateTask.timeout(), new Runnable() {
                @Override
                public void run() {
                    threadPool.generic().execute(new Runnable() {
                        @Override
                        public void run() {
                            timeoutUpdateTask.onFailure(task.source, new ProcessClusterEventTimeoutException(timeoutUpdateTask.timeout(), task.source));
                        }
                    });
                }
            });
        } else {
            updateTasksExecutor.execute(task);
        }
    }

    @Override
    public List<PendingClusterTask> pendingTasks() {
        long now = System.currentTimeMillis();
        PrioritizedEsThreadPoolExecutor.Pending[] pendings = updateTasksExecutor.getPending();
        List<PendingClusterTask> pendingClusterTasks = new ArrayList<PendingClusterTask>(pendings.length);
        for (PrioritizedEsThreadPoolExecutor.Pending pending : pendings) {
            final String source;
            final long timeInQueue;
            if (pending.task instanceof UpdateTask) {
                UpdateTask updateTask = (UpdateTask) pending.task;
                source = updateTask.source;
                timeInQueue = now - updateTask.addedAt;
            } else {
                source = "unknown";
                timeInQueue = -1;
            }

            pendingClusterTasks.add(new PendingClusterTask(pending.insertionOrder, pending.priority, new StringText(source), timeInQueue));
        }
        return pendingClusterTasks;
    }

    class UpdateTask extends PrioritizedRunnable {

        public final String source;
        public final ClusterStateUpdateTask updateTask;
        public final long addedAt = System.currentTimeMillis();

        UpdateTask(String source, Priority priority, ClusterStateUpdateTask updateTask) {
            super(priority);
            this.source = source;
            this.updateTask = updateTask;
        }

        @Override
        public void run() {
            if (!lifecycle.started()) {
                logger.debug("processing [{}]: ignoring, cluster_service not started", source);
                return;
            }
            logger.debug("processing [{}]: execute", source);
            ClusterState previousClusterState = clusterState;
            ClusterState newClusterState;
            try {
                newClusterState = updateTask.execute(previousClusterState);
            } catch (Throwable e) {
                if (logger.isTraceEnabled()) {
                    StringBuilder sb = new StringBuilder("failed to execute cluster state update, state:\nversion [").append(previousClusterState.version()).append("], source [").append(source).append("]\n");
                    sb.append(previousClusterState.nodes().prettyPrint());
                    sb.append(previousClusterState.routingTable().prettyPrint());
                    sb.append(previousClusterState.readOnlyRoutingNodes().prettyPrint());
                    logger.trace(sb.toString(), e);
                }
                updateTask.onFailure(source, e);
                return;
            }

            if (previousClusterState == newClusterState) {
                logger.debug("processing [{}]: no change in cluster_state", source);
                if (updateTask instanceof ProcessedClusterStateUpdateTask) {
                    ((ProcessedClusterStateUpdateTask) updateTask).clusterStateProcessed(source, previousClusterState, newClusterState);
                }
                return;
            }

            try {
                if (newClusterState.nodes().localNodeMaster()) {
                    // only the master controls the version numbers
                    Builder builder = ClusterState.builder().state(newClusterState).version(newClusterState.version() + 1);
                    if (previousClusterState.routingTable() != newClusterState.routingTable()) {
                        builder.routingTable(RoutingTable.builder().routingTable(newClusterState.routingTable()).version(newClusterState.routingTable().version() + 1));
                    }
                    if (previousClusterState.metaData() != newClusterState.metaData()) {
                        builder.metaData(MetaData.builder().metaData(newClusterState.metaData()).version(newClusterState.metaData().version() + 1));
                    }
                    newClusterState = builder.build();
                } else {
                    if (previousClusterState.blocks().hasGlobalBlock(Discovery.NO_MASTER_BLOCK) && !newClusterState.blocks().hasGlobalBlock(Discovery.NO_MASTER_BLOCK)) {
                        // force an update, its a fresh update from the master as we transition from a start of not having a master to having one
                        // have a fresh instances of routing and metadata to remove the chance that version might be the same
                        Builder builder = ClusterState.builder().state(newClusterState);
                        builder.routingTable(RoutingTable.builder().routingTable(newClusterState.routingTable()));
                        builder.metaData(MetaData.builder().metaData(newClusterState.metaData()));
                        newClusterState = builder.build();
                        logger.debug("got first state from fresh master [{}]", newClusterState.nodes().masterNodeId());
                    } else if (newClusterState.version() < previousClusterState.version()) {
                        // we got this cluster state from the master, filter out based on versions (don't call listeners)
                        logger.debug("got old cluster state [" + newClusterState.version() + "<" + previousClusterState.version() + "] from source [" + source + "], ignoring");
                        return;
                    }
                }

                if (logger.isTraceEnabled()) {
                    StringBuilder sb = new StringBuilder("cluster state updated:\nversion [").append(newClusterState.version()).append("], source [").append(source).append("]\n");
                    sb.append(newClusterState.nodes().prettyPrint());
                    sb.append(newClusterState.routingTable().prettyPrint());
                    sb.append(newClusterState.readOnlyRoutingNodes().prettyPrint());
                    logger.trace(sb.toString());
                } else if (logger.isDebugEnabled()) {
                    logger.debug("cluster state updated, version [{}], source [{}]", newClusterState.version(), source);
                }

                ClusterChangedEvent clusterChangedEvent = new ClusterChangedEvent(source, newClusterState, previousClusterState);
                // new cluster state, notify all listeners
                final DiscoveryNodes.Delta nodesDelta = clusterChangedEvent.nodesDelta();
                if (nodesDelta.hasChanges() && logger.isInfoEnabled()) {
                    String summary = nodesDelta.shortSummary();
                    if (summary.length() > 0) {
                        logger.info("{}, reason: {}", summary, source);
                    }
                }

                // TODO, do this in parallel (and wait)
                for (DiscoveryNode node : nodesDelta.addedNodes()) {
                    if (!nodeRequiresConnection(node)) {
                        continue;
                    }
                    try {
                        transportService.connectToNode(node);
                    } catch (Exception e) {
                        // the fault detection will detect it as failed as well
                        logger.warn("failed to connect to node [" + node + "]", e);
                    }
                }

                // if we are the master, publish the new state to all nodes
                // we publish here before we send a notification to all the listeners, since if it fails
                // we don't want to notify
                if (newClusterState.nodes().localNodeMaster()) {
                    logger.debug("Publishing cluster state version {}", newClusterState.version());
                    discoveryService.publish(newClusterState);
                }

                // update the current cluster state
                clusterState = newClusterState;
                logger.debug("Set cluster state to version {}. Broadcasting to listeners.", newClusterState.version());

                for (ClusterStateListener listener : priorityClusterStateListeners) {
                    listener.clusterChanged(clusterChangedEvent);
                }
                for (ClusterStateListener listener : clusterStateListeners) {
                    listener.clusterChanged(clusterChangedEvent);
                }
                for (ClusterStateListener listener : lastClusterStateListeners) {
                    listener.clusterChanged(clusterChangedEvent);
                }

                if (!nodesDelta.removedNodes().isEmpty()) {
                    threadPool.generic().execute(new Runnable() {
                        @Override
                        public void run() {
                            for (DiscoveryNode node : nodesDelta.removedNodes()) {
                                transportService.disconnectFromNode(node);
                            }
                        }
                    });
                }


                if (updateTask instanceof ProcessedClusterStateUpdateTask) {
                    ((ProcessedClusterStateUpdateTask) updateTask).clusterStateProcessed(source, previousClusterState, newClusterState);
                }

                logger.debug("processing [{}]: done applying updated cluster_state (version: {})", source, newClusterState.version());
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder("failed to apply updated cluster state:\nversion [").append(newClusterState.version()).append("], source [").append(source).append("]\n");
                sb.append(newClusterState.nodes().prettyPrint());
                sb.append(newClusterState.routingTable().prettyPrint());
                sb.append(newClusterState.readOnlyRoutingNodes().prettyPrint());
                logger.warn(sb.toString(), e);
                // TODO: do we want to call updateTask.onFailure here?
            }
        }
    }

    class NotifyTimeout implements Runnable {
        final TimeoutClusterStateListener listener;
        final TimeValue timeout;
        ScheduledFuture future;

        NotifyTimeout(TimeoutClusterStateListener listener, TimeValue timeout) {
            this.listener = listener;
            this.timeout = timeout;
        }

        public void cancel() {
            future.cancel(false);
        }

        @Override
        public void run() {
            if (future.isCancelled()) {
                return;
            }
            if (lifecycle.stoppedOrClosed()) {
                listener.onClose();
            } else {
                listener.onTimeout(this.timeout);
            }
            // note, we rely on the listener to remove itself in case of timeout if needed
        }
    }

    private class ReconnectToNodes implements Runnable {

        private ConcurrentMap<DiscoveryNode, Integer> failureCount = ConcurrentCollections.newConcurrentMap();

        @Override
        public void run() {
            // master node will check against all nodes if its alive with certain discoveries implementations,
            // but we can't rely on that, so we check on it as well
            for (DiscoveryNode node : clusterState.nodes()) {
                if (lifecycle.stoppedOrClosed()) {
                    return;
                }
                if (!nodeRequiresConnection(node)) {
                    continue;
                }
                if (clusterState.nodes().nodeExists(node.id())) { // we double check existence of node since connectToNode might take time...
                    if (!transportService.nodeConnected(node)) {
                        try {
                            transportService.connectToNode(node);
                        } catch (Exception e) {
                            if (lifecycle.stoppedOrClosed()) {
                                return;
                            }
                            if (clusterState.nodes().nodeExists(node.id())) { // double check here as well, maybe its gone?
                                Integer nodeFailureCount = failureCount.get(node);
                                if (nodeFailureCount == null) {
                                    nodeFailureCount = 1;
                                } else {
                                    nodeFailureCount = nodeFailureCount + 1;
                                }
                                // log every 6th failure
                                if ((nodeFailureCount % 6) == 0) {
                                    // reset the failure count...
                                    nodeFailureCount = 0;
                                    logger.warn("failed to reconnect to node {}", e, node);
                                }
                                failureCount.put(node, nodeFailureCount);
                            }
                        }
                    }
                }
            }
            // go over and remove failed nodes that have been removed
            DiscoveryNodes nodes = clusterState.nodes();
            for (Iterator<DiscoveryNode> failedNodesIt = failureCount.keySet().iterator(); failedNodesIt.hasNext(); ) {
                DiscoveryNode failedNode = failedNodesIt.next();
                if (!nodes.nodeExists(failedNode.id())) {
                    failedNodesIt.remove();
                }
            }
            if (lifecycle.started()) {
                reconnectToNodes = threadPool.schedule(reconnectInterval, ThreadPool.Names.GENERIC, this);
            }
        }
    }

    private boolean nodeRequiresConnection(DiscoveryNode node) {
        return localNode().shouldConnectTo(node);
    }

    private static class LocalNodeMasterListeners implements ClusterStateListener {

        private final List<LocalNodeMasterListener> listeners = new CopyOnWriteArrayList<LocalNodeMasterListener>();
        private final ThreadPool threadPool;
        private volatile boolean master = false;

        private LocalNodeMasterListeners(ThreadPool threadPool) {
            this.threadPool = threadPool;
        }

        @Override
        public void clusterChanged(ClusterChangedEvent event) {
            if (!master && event.localNodeMaster()) {
                master = true;
                for (LocalNodeMasterListener listener : listeners) {
                    Executor executor = threadPool.executor(listener.executorName());
                    executor.execute(new OnMasterRunnable(listener));
                }
                return;
            }

            if (master && !event.localNodeMaster()) {
                master = false;
                for (LocalNodeMasterListener listener : listeners) {
                    Executor executor = threadPool.executor(listener.executorName());
                    executor.execute(new OffMasterRunnable(listener));
                }
            }
        }

        private void add(LocalNodeMasterListener listener) {
            listeners.add(listener);
        }

        private void remove(LocalNodeMasterListener listener) {
            listeners.remove(listener);
        }

        private void clear() {
            listeners.clear();
        }
    }

    private static class OnMasterRunnable implements Runnable {

        private final LocalNodeMasterListener listener;

        private OnMasterRunnable(LocalNodeMasterListener listener) {
            this.listener = listener;
        }

        @Override
        public void run() {
            listener.onMaster();
        }
    }

    private static class OffMasterRunnable implements Runnable {

        private final LocalNodeMasterListener listener;

        private OffMasterRunnable(LocalNodeMasterListener listener) {
            this.listener = listener;
        }

        @Override
        public void run() {
            listener.offMaster();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7159.java