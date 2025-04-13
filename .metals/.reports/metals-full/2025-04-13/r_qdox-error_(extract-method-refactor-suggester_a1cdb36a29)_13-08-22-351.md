error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7729.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7729.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,18]

error in qdox parser
file content:
```java
offset: 18
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7729.java
text:
```scala
@Override public v@@oid handleException(TransportException exp) {

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

package org.elasticsearch.river.cluster;

import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.io.stream.VoidStreamable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.*;

import java.io.IOException;

/**
 * @author kimchy (shay.banon)
 */
public class PublishRiverClusterStateAction extends AbstractComponent {

    public static interface NewClusterStateListener {
        void onNewClusterState(RiverClusterState clusterState);
    }

    private final TransportService transportService;

    private final ClusterService clusterService;

    private final NewClusterStateListener listener;

    public PublishRiverClusterStateAction(Settings settings, TransportService transportService, ClusterService clusterService,
                                          NewClusterStateListener listener) {
        super(settings);
        this.transportService = transportService;
        this.clusterService = clusterService;
        this.listener = listener;
        transportService.registerHandler(PublishClusterStateRequestHandler.ACTION, new PublishClusterStateRequestHandler());
    }

    public void close() {
        transportService.removeHandler(PublishClusterStateRequestHandler.ACTION);
    }

    public void publish(RiverClusterState clusterState) {
        final DiscoveryNodes discoNodes = clusterService.state().nodes();
        for (final DiscoveryNode node : discoNodes) {
            if (node.equals(discoNodes.localNode())) {
                // no need to send to our self
                continue;
            }

            // we only want to send nodes that are either possible master nodes or river nodes
            // master nodes because they will handle the state and the allocation of rivers
            // and river nodes since they will end up creating indexes

            if (!node.masterNode() && !RiverNodeHelper.isRiverNode(node)) {
                continue;
            }

            transportService.sendRequest(node, PublishClusterStateRequestHandler.ACTION, new PublishClusterStateRequest(clusterState), new VoidTransportResponseHandler(false) {
                @Override public void handleException(RemoteTransportException exp) {
                    logger.debug("failed to send cluster state to [{}], should be detected as failed soon...", exp, node);
                }
            });
        }
    }

    private class PublishClusterStateRequest implements Streamable {

        private RiverClusterState clusterState;

        private PublishClusterStateRequest() {
        }

        private PublishClusterStateRequest(RiverClusterState clusterState) {
            this.clusterState = clusterState;
        }

        @Override public void readFrom(StreamInput in) throws IOException {
            clusterState = RiverClusterState.Builder.readFrom(in);
        }

        @Override public void writeTo(StreamOutput out) throws IOException {
            RiverClusterState.Builder.writeTo(clusterState, out);
        }
    }

    private class PublishClusterStateRequestHandler extends BaseTransportRequestHandler<PublishClusterStateRequest> {

        static final String ACTION = "river/state/publish";

        @Override public PublishClusterStateRequest newInstance() {
            return new PublishClusterStateRequest();
        }

        @Override public void messageReceived(PublishClusterStateRequest request, TransportChannel channel) throws Exception {
            listener.onNewClusterState(request.clusterState);
            channel.sendResponse(VoidStreamable.INSTANCE);
        }

        /**
         * No need to spawn, we add submit a new cluster state directly. This allows for faster application.
         */
        @Override public boolean spawn() {
            return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7729.java