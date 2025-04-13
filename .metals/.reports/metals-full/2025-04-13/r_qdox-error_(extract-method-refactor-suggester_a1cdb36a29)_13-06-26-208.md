error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7243.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7243.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7243.java
text:
```scala
private final L@@ist<Listener> listeners = new CopyOnWriteArrayList<>();

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

package org.elasticsearch.cluster.action.index;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 */
public class NodeIndexDeletedAction extends AbstractComponent {

    private final ThreadPool threadPool;
    private final TransportService transportService;
    private final List<Listener> listeners = new CopyOnWriteArrayList<Listener>();

    @Inject
    public NodeIndexDeletedAction(Settings settings, ThreadPool threadPool, TransportService transportService) {
        super(settings);
        this.threadPool = threadPool;
        this.transportService = transportService;
        transportService.registerHandler(NodeIndexDeletedTransportHandler.ACTION, new NodeIndexDeletedTransportHandler());
        transportService.registerHandler(NodeIndexStoreDeletedTransportHandler.ACTION, new NodeIndexStoreDeletedTransportHandler());
    }

    public void add(Listener listener) {
        listeners.add(listener);
    }

    public void remove(Listener listener) {
        listeners.remove(listener);
    }

    public void nodeIndexDeleted(final ClusterState clusterState, final String index, final String nodeId) throws ElasticsearchException {
        DiscoveryNodes nodes = clusterState.nodes();
        if (nodes.localNodeMaster()) {
            threadPool.generic().execute(new Runnable() {
                @Override
                public void run() {
                    innerNodeIndexDeleted(index, nodeId);
                }
            });
        } else {
            transportService.sendRequest(clusterState.nodes().masterNode(),
                    NodeIndexDeletedTransportHandler.ACTION, new NodeIndexDeletedMessage(index, nodeId), EmptyTransportResponseHandler.INSTANCE_SAME);
        }
    }

    public void nodeIndexStoreDeleted(final ClusterState clusterState, final String index, final String nodeId) throws ElasticsearchException {
        DiscoveryNodes nodes = clusterState.nodes();
        if (nodes.localNodeMaster()) {
            threadPool.generic().execute(new Runnable() {
                @Override
                public void run() {
                    innerNodeIndexStoreDeleted(index, nodeId);
                }
            });
        } else {
            transportService.sendRequest(clusterState.nodes().masterNode(),
                    NodeIndexStoreDeletedTransportHandler.ACTION, new NodeIndexStoreDeletedMessage(index, nodeId), EmptyTransportResponseHandler.INSTANCE_SAME);
        }
    }

    private void innerNodeIndexDeleted(String index, String nodeId) {
        for (Listener listener : listeners) {
            listener.onNodeIndexDeleted(index, nodeId);
        }
    }

    private void innerNodeIndexStoreDeleted(String index, String nodeId) {
        for (Listener listener : listeners) {
            listener.onNodeIndexStoreDeleted(index, nodeId);
        }
    }

    public static interface Listener {
        void onNodeIndexDeleted(String index, String nodeId);

        void onNodeIndexStoreDeleted(String index, String nodeId);
    }

    private class NodeIndexDeletedTransportHandler extends BaseTransportRequestHandler<NodeIndexDeletedMessage> {

        static final String ACTION = "cluster/nodeIndexDeleted";

        @Override
        public NodeIndexDeletedMessage newInstance() {
            return new NodeIndexDeletedMessage();
        }

        @Override
        public void messageReceived(NodeIndexDeletedMessage message, TransportChannel channel) throws Exception {
            innerNodeIndexDeleted(message.index, message.nodeId);
            channel.sendResponse(TransportResponse.Empty.INSTANCE);
        }

        @Override
        public String executor() {
            return ThreadPool.Names.SAME;
        }
    }

    private class NodeIndexStoreDeletedTransportHandler extends BaseTransportRequestHandler<NodeIndexStoreDeletedMessage> {

        static final String ACTION = "cluster/nodeIndexStoreDeleted";

        @Override
        public NodeIndexStoreDeletedMessage newInstance() {
            return new NodeIndexStoreDeletedMessage();
        }

        @Override
        public void messageReceived(NodeIndexStoreDeletedMessage message, TransportChannel channel) throws Exception {
            innerNodeIndexStoreDeleted(message.index, message.nodeId);
            channel.sendResponse(TransportResponse.Empty.INSTANCE);
        }

        @Override
        public String executor() {
            return ThreadPool.Names.SAME;
        }
    }

    static class NodeIndexDeletedMessage extends TransportRequest {

        String index;
        String nodeId;

        NodeIndexDeletedMessage() {
        }

        NodeIndexDeletedMessage(String index, String nodeId) {
            this.index = index;
            this.nodeId = nodeId;
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            out.writeString(index);
            out.writeString(nodeId);
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            index = in.readString();
            nodeId = in.readString();
        }
    }

    static class NodeIndexStoreDeletedMessage extends TransportRequest {

        String index;
        String nodeId;

        NodeIndexStoreDeletedMessage() {
        }

        NodeIndexStoreDeletedMessage(String index, String nodeId) {
            this.index = index;
            this.nodeId = nodeId;
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            out.writeString(index);
            out.writeString(nodeId);
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            index = in.readString();
            nodeId = in.readString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7243.java