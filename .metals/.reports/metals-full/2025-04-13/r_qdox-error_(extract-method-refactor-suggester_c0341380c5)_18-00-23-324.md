error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2731.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2731.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2731.java
text:
```scala
t@@oRoutingNode.nodeId(), shardRouting.currentNodeId(), shardRouting.restoreSource(),

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

package org.elasticsearch.cluster.routing.allocation.command;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.ElasticSearchParseException;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.routing.MutableShardRouting;
import org.elasticsearch.cluster.routing.RoutingNode;
import org.elasticsearch.cluster.routing.ShardRoutingState;
import org.elasticsearch.cluster.routing.allocation.RoutingAllocation;
import org.elasticsearch.cluster.routing.allocation.decider.Decision;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.shard.ShardId;

import java.io.IOException;

/**
 * A command that moves a shard from a specific node to another node.<br />
 * <b>Note:</b> The shard needs to be in the state
 * {@link ShardRoutingState#STARTED} in order to be moved.
 */
public class MoveAllocationCommand implements AllocationCommand {

    public static final String NAME = "move";

    public static class Factory implements AllocationCommand.Factory<MoveAllocationCommand> {

        @Override
        public MoveAllocationCommand readFrom(StreamInput in) throws IOException {
            return new MoveAllocationCommand(ShardId.readShardId(in), in.readString(), in.readString());
        }

        @Override
        public void writeTo(MoveAllocationCommand command, StreamOutput out) throws IOException {
            command.shardId().writeTo(out);
            out.writeString(command.fromNode());
            out.writeString(command.toNode());
        }

        @Override
        public MoveAllocationCommand fromXContent(XContentParser parser) throws IOException {
            String index = null;
            int shardId = -1;
            String fromNode = null;
            String toNode = null;

            String currentFieldName = null;
            XContentParser.Token token;
            while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                if (token == XContentParser.Token.FIELD_NAME) {
                    currentFieldName = parser.currentName();
                } else if (token.isValue()) {
                    if ("index".equals(currentFieldName)) {
                        index = parser.text();
                    } else if ("shard".equals(currentFieldName)) {
                        shardId = parser.intValue();
                    } else if ("from_node".equals(currentFieldName) || "fromNode".equals(currentFieldName)) {
                        fromNode = parser.text();
                    } else if ("to_node".equals(currentFieldName) || "toNode".equals(currentFieldName)) {
                        toNode = parser.text();
                    } else {
                        throw new ElasticSearchParseException("[move] command does not support field [" + currentFieldName + "]");
                    }
                } else {
                    throw new ElasticSearchParseException("[move] command does not support complex json tokens [" + token + "]");
                }
            }
            if (index == null) {
                throw new ElasticSearchParseException("[move] command missing the index parameter");
            }
            if (shardId == -1) {
                throw new ElasticSearchParseException("[move] command missing the shard parameter");
            }
            if (fromNode == null) {
                throw new ElasticSearchParseException("[move] command missing the from_node parameter");
            }
            if (toNode == null) {
                throw new ElasticSearchParseException("[move] command missing the to_node parameter");
            }
            return new MoveAllocationCommand(new ShardId(index, shardId), fromNode, toNode);
        }

        @Override
        public void toXContent(MoveAllocationCommand command, XContentBuilder builder, ToXContent.Params params) throws IOException {
            builder.startObject();
            builder.field("index", command.shardId().index());
            builder.field("shard", command.shardId().id());
            builder.field("from_node", command.fromNode());
            builder.field("to_node", command.toNode());
            builder.endObject();
        }
    }

    private final ShardId shardId;
    private final String fromNode;
    private final String toNode;

    public MoveAllocationCommand(ShardId shardId, String fromNode, String toNode) {
        this.shardId = shardId;
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    @Override
    public String name() {
        return NAME;
    }

    public ShardId shardId() {
        return this.shardId;
    }

    public String fromNode() {
        return this.fromNode;
    }

    public String toNode() {
        return this.toNode;
    }

    @Override
    public void execute(RoutingAllocation allocation) throws ElasticSearchException {
        DiscoveryNode fromDiscoNode = allocation.nodes().resolveNode(fromNode);
        DiscoveryNode toDiscoNode = allocation.nodes().resolveNode(toNode);

        boolean found = false;
        for (MutableShardRouting shardRouting : allocation.routingNodes().node(fromDiscoNode.id())) {
            if (!shardRouting.shardId().equals(shardId)) {
                continue;
            }
            found = true;

            // TODO we can possibly support also relocating cases, where we cancel relocation and move...
            if (!shardRouting.started()) {
                throw new ElasticSearchIllegalArgumentException("[move_allocation] can't move " + shardId + ", shard is not started (state = " + shardRouting.state() + "]");
            }

            RoutingNode toRoutingNode = allocation.routingNodes().node(toDiscoNode.id());
            Decision decision = allocation.deciders().canAllocate(shardRouting, toRoutingNode, allocation);
            if (decision.type() == Decision.Type.NO) {
                throw new ElasticSearchIllegalArgumentException("[move_allocation] can't move " + shardId + ", from " + fromDiscoNode + ", to " + toDiscoNode + ", since its not allowed, reason: " + decision);
            }
            if (decision.type() == Decision.Type.THROTTLE) {
                // its being throttled, maybe have a flag to take it into account and fail? for now, just do it since the "user" wants it...
            }

            toRoutingNode.add(new MutableShardRouting(shardRouting.index(), shardRouting.id(),
                    toRoutingNode.nodeId(), shardRouting.currentNodeId(),
                    shardRouting.primary(), ShardRoutingState.INITIALIZING, shardRouting.version() + 1));

            shardRouting.relocate(toRoutingNode.nodeId());
        }

        if (!found) {
            throw new ElasticSearchIllegalArgumentException("[move_allocation] can't move " + shardId + ", failed to find it on node " + fromDiscoNode);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2731.java