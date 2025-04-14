error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6560.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6560.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6560.java
text:
```scala
c@@lusterState = ClusterState.Builder.readFrom(in, nodesProvider.nodes().localNode());

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

package org.elasticsearch.discovery.zen.membership;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.io.stream.VoidStreamable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.discovery.zen.DiscoveryNodesProvider;
import org.elasticsearch.transport.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author kimchy (shay.banon)
 */
public class MembershipAction extends AbstractComponent {

    public static interface MembershipListener {
        ClusterState onJoin(DiscoveryNode node);

        void onLeave(DiscoveryNode node);
    }

    private final TransportService transportService;

    private final DiscoveryNodesProvider nodesProvider;

    private final MembershipListener listener;

    public MembershipAction(Settings settings, TransportService transportService, DiscoveryNodesProvider nodesProvider, MembershipListener listener) {
        super(settings);
        this.transportService = transportService;
        this.nodesProvider = nodesProvider;
        this.listener = listener;

        transportService.registerHandler(JoinRequestRequestHandler.ACTION, new JoinRequestRequestHandler());
        transportService.registerHandler(LeaveRequestRequestHandler.ACTION, new LeaveRequestRequestHandler());
    }

    public void close() {
        transportService.removeHandler(JoinRequestRequestHandler.ACTION);
        transportService.removeHandler(LeaveRequestRequestHandler.ACTION);
    }

    public void sendLeaveRequest(DiscoveryNode masterNode, DiscoveryNode node) {
        transportService.sendRequest(node, LeaveRequestRequestHandler.ACTION, new LeaveRequest(masterNode), VoidTransportResponseHandler.INSTANCE_NOSPAWN);
    }

    public void sendLeaveRequestBlocking(DiscoveryNode masterNode, DiscoveryNode node, TimeValue timeout) throws ElasticSearchException {
        transportService.submitRequest(masterNode, LeaveRequestRequestHandler.ACTION, new LeaveRequest(node), VoidTransportResponseHandler.INSTANCE_NOSPAWN).txGet(timeout.millis(), TimeUnit.MILLISECONDS);
    }

    public void sendJoinRequest(DiscoveryNode masterNode, DiscoveryNode node) {
        transportService.sendRequest(masterNode, JoinRequestRequestHandler.ACTION, new JoinRequest(node, false), VoidTransportResponseHandler.INSTANCE_NOSPAWN);
    }

    public ClusterState sendJoinRequestBlocking(DiscoveryNode masterNode, DiscoveryNode node, TimeValue timeout) throws ElasticSearchException {
        return transportService.submitRequest(masterNode, JoinRequestRequestHandler.ACTION, new JoinRequest(node, true), new FutureTransportResponseHandler<JoinResponse>() {
            @Override public JoinResponse newInstance() {
                return new JoinResponse();
            }
        }).txGet(timeout.millis(), TimeUnit.MILLISECONDS).clusterState;
    }

    static class JoinRequest implements Streamable {

        DiscoveryNode node;

        boolean withClusterState;

        private JoinRequest() {
        }

        private JoinRequest(DiscoveryNode node, boolean withClusterState) {
            this.node = node;
            this.withClusterState = withClusterState;
        }

        @Override public void readFrom(StreamInput in) throws IOException {
            node = DiscoveryNode.readNode(in);
            withClusterState = in.readBoolean();
        }

        @Override public void writeTo(StreamOutput out) throws IOException {
            node.writeTo(out);
            out.writeBoolean(withClusterState);
        }
    }

    class JoinResponse implements Streamable {

        ClusterState clusterState;

        JoinResponse() {
        }

        JoinResponse(ClusterState clusterState) {
            this.clusterState = clusterState;
        }

        @Override public void readFrom(StreamInput in) throws IOException {
            clusterState = ClusterState.Builder.readFrom(in, settings, nodesProvider.nodes().localNode());
        }

        @Override public void writeTo(StreamOutput out) throws IOException {
            ClusterState.Builder.writeTo(clusterState, out);
        }
    }

    private class JoinRequestRequestHandler extends BaseTransportRequestHandler<JoinRequest> {

        static final String ACTION = "discovery/zen/join";

        @Override public JoinRequest newInstance() {
            return new JoinRequest();
        }

        @Override public void messageReceived(JoinRequest request, TransportChannel channel) throws Exception {
            ClusterState clusterState = listener.onJoin(request.node);
            if (request.withClusterState) {
                channel.sendResponse(new JoinResponse(clusterState));
            } else {
                channel.sendResponse(VoidStreamable.INSTANCE);
            }
        }
    }

    private static class LeaveRequest implements Streamable {

        private DiscoveryNode node;

        private LeaveRequest() {
        }

        private LeaveRequest(DiscoveryNode node) {
            this.node = node;
        }

        @Override public void readFrom(StreamInput in) throws IOException {
            node = DiscoveryNode.readNode(in);
        }

        @Override public void writeTo(StreamOutput out) throws IOException {
            node.writeTo(out);
        }
    }

    private class LeaveRequestRequestHandler extends BaseTransportRequestHandler<LeaveRequest> {

        static final String ACTION = "discovery/zen/leave";

        @Override public LeaveRequest newInstance() {
            return new LeaveRequest();
        }

        @Override public void messageReceived(LeaveRequest request, TransportChannel channel) throws Exception {
            listener.onLeave(request.node);
            channel.sendResponse(VoidStreamable.INSTANCE);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6560.java