error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9798.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9798.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9798.java
text:
```scala
t@@his.localNode = new DiscoveryNode(settings.get("name"), settings.getAsBoolean("node.data", !settings.getAsBoolean("node.client", false)), Long.toString(nodeIdGenerator.incrementAndGet()), transportService.boundAddress().publishAddress());

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

package org.elasticsearch.discovery.local;

import com.google.inject.Inject;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.cluster.*;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.discovery.Discovery;
import org.elasticsearch.discovery.InitialStateDiscoveryListener;
import org.elasticsearch.transport.TransportService;
import org.elasticsearch.util.component.AbstractLifecycleComponent;
import org.elasticsearch.util.concurrent.jsr166y.LinkedTransferQueue;
import org.elasticsearch.util.settings.Settings;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static com.google.common.collect.Sets.*;
import static org.elasticsearch.cluster.ClusterState.*;

/**
 * @author kimchy (Shay Banon)
 */
public class LocalDiscovery extends AbstractLifecycleComponent<Discovery> implements Discovery {

    private final TransportService transportService;

    private final ClusterService clusterService;

    private final ClusterName clusterName;

    private DiscoveryNode localNode;

    private volatile boolean master = false;

    private volatile boolean firstMaster = false;

    private final AtomicBoolean initialStateSent = new AtomicBoolean();

    private final CopyOnWriteArrayList<InitialStateDiscoveryListener> initialStateListeners = new CopyOnWriteArrayList<InitialStateDiscoveryListener>();

    // use CHM here and not ConcurrentMaps#new since we want to be able to agentify this using TC later on...
    private static final ConcurrentMap<ClusterName, ClusterGroup> clusterGroups = new ConcurrentHashMap<ClusterName, ClusterGroup>();

    private static final AtomicLong nodeIdGenerator = new AtomicLong();

    @Inject public LocalDiscovery(Settings settings, ClusterName clusterName, TransportService transportService, ClusterService clusterService) {
        super(settings);
        this.clusterName = clusterName;
        this.clusterService = clusterService;
        this.transportService = transportService;
    }

    @Override protected void doStart() throws ElasticSearchException {
        synchronized (clusterGroups) {
            ClusterGroup clusterGroup = clusterGroups.get(clusterName);
            if (clusterGroup == null) {
                clusterGroup = new ClusterGroup();
                clusterGroups.put(clusterName, clusterGroup);
            }
            logger.debug("Connected to cluster [{}]", clusterName);
            this.localNode = new DiscoveryNode(settings.get("name"), settings.getAsBoolean("node.data", true), Long.toString(nodeIdGenerator.incrementAndGet()), transportService.boundAddress().publishAddress());

            clusterGroup.members().add(this);
            if (clusterGroup.members().size() == 1) {
                // we are the first master (and the master)
                master = true;
                firstMaster = true;
                clusterService.submitStateUpdateTask("local-disco-initialconnect(master)", new ProcessedClusterStateUpdateTask() {
                    @Override public ClusterState execute(ClusterState currentState) {
                        DiscoveryNodes.Builder builder = new DiscoveryNodes.Builder()
                                .localNodeId(localNode.id())
                                .masterNodeId(localNode.id())
                                        // put our local node
                                .put(localNode);
                        return newClusterStateBuilder().state(currentState).nodes(builder).build();
                    }

                    @Override public void clusterStateProcessed(ClusterState clusterState) {
                        sendInitialStateEventIfNeeded();
                    }
                });
            } else {
                // we are not the master, tell the master to send it
                LocalDiscovery master = clusterGroup.members().peek();
                master.clusterService.submitStateUpdateTask("local-disco-receive(from node[" + localNode + "])", new ProcessedClusterStateUpdateTask() {
                    @Override public ClusterState execute(ClusterState currentState) {
                        if (currentState.nodes().nodeExists(localNode.id())) {
                            // no change, the node already exists in the cluster
                            logger.warn("Received an address [{}] for an existing node [{}]", localNode.address(), localNode);
                            return currentState;
                        }
                        return newClusterStateBuilder().state(currentState).nodes(currentState.nodes().newNode(localNode)).build();
                    }

                    @Override public void clusterStateProcessed(ClusterState clusterState) {
                        sendInitialStateEventIfNeeded();
                    }
                });
            }
        }
    }

    @Override protected void doStop() throws ElasticSearchException {
        synchronized (clusterGroups) {
            ClusterGroup clusterGroup = clusterGroups.get(clusterName);
            if (clusterGroup == null) {
                logger.warn("Illegal state, should not have an empty cluster group when stopping, I should be there at teh very least...");
                return;
            }
            clusterGroup.members().remove(this);
            if (clusterGroup.members().isEmpty()) {
                // no more members, remove and return
                clusterGroups.remove(clusterName);
                return;
            }

            final LocalDiscovery masterDiscovery = clusterGroup.members().peek();
            // if the removed node is the master, make the next one as the master
            if (master) {
                masterDiscovery.master = true;
            }

            final Set<String> newMembers = newHashSet();
            for (LocalDiscovery discovery : clusterGroup.members()) {
                newMembers.add(discovery.localNode.id());
            }

            masterDiscovery.clusterService.submitStateUpdateTask("local-disco-update", new ClusterStateUpdateTask() {
                @Override public ClusterState execute(ClusterState currentState) {
                    DiscoveryNodes newNodes = currentState.nodes().removeDeadMembers(newMembers, masterDiscovery.localNode.id());
                    DiscoveryNodes.Delta delta = newNodes.delta(currentState.nodes());
                    if (delta.added()) {
                        logger.warn("No new nodes should be created when a new discovery view is accepted");
                    }
                    return newClusterStateBuilder().state(currentState).nodes(newNodes).build();
                }
            });
        }
    }

    @Override protected void doClose() throws ElasticSearchException {
    }

    @Override public void addListener(InitialStateDiscoveryListener listener) {
        this.initialStateListeners.add(listener);
    }

    @Override public void removeListener(InitialStateDiscoveryListener listener) {
        this.initialStateListeners.remove(listener);
    }

    @Override public String nodeDescription() {
        return clusterName.value() + "/" + localNode.id();
    }

    @Override public boolean firstMaster() {
        return firstMaster;
    }

    @Override public void publish(ClusterState clusterState) {
        if (!master) {
            throw new ElasticSearchIllegalStateException("Shouldn't publish state when not master");
        }
        ClusterGroup clusterGroup = clusterGroups.get(clusterName);
        if (clusterGroup == null) {
            // nothing to publish to
            return;
        }
        try {
            // we do the marshaling intentionally, to check it works well...
            final byte[] clusterStateBytes = Builder.toBytes(clusterState);
            for (LocalDiscovery discovery : clusterGroup.members()) {
                if (discovery.master) {
                    continue;
                }
                final ClusterState nodeSpecificClusterState = ClusterState.Builder.fromBytes(clusterStateBytes, discovery.settings, discovery.localNode);
                // ignore cluster state messages that do not include "me", not in the game yet...
                if (nodeSpecificClusterState.nodes().localNode() != null) {
                    discovery.clusterService.submitStateUpdateTask("local-disco-receive(from master)", new ProcessedClusterStateUpdateTask() {
                        @Override public ClusterState execute(ClusterState currentState) {
                            return nodeSpecificClusterState;
                        }

                        @Override public void clusterStateProcessed(ClusterState clusterState) {
                            sendInitialStateEventIfNeeded();
                        }
                    });
                }
            }
        } catch (Exception e) {
            // failure to marshal or un-marshal
            throw new ElasticSearchIllegalStateException("Cluster state failed to serialize", e);
        }
    }

    private void sendInitialStateEventIfNeeded() {
        if (initialStateSent.compareAndSet(false, true)) {
            for (InitialStateDiscoveryListener listener : initialStateListeners) {
                listener.initialStateProcessed();
            }
        }
    }

    private class ClusterGroup {

        private Queue<LocalDiscovery> members = new LinkedTransferQueue<LocalDiscovery>();

        Queue<LocalDiscovery> members() {
            return members;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9798.java