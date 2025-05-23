error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5571.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5571.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5571.java
text:
```scala
a@@llocation.routingNodes().assign(shardRouting, routingNode.nodeId());

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
import org.elasticsearch.cluster.routing.allocation.RoutingAllocation;
import org.elasticsearch.cluster.routing.allocation.decider.Decision;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.shard.ShardId;

import java.io.IOException;
import java.util.Iterator;

/**
 * Allocates an unassigned shard to a specific node. Note, primary allocation will "force"
 * allocation which might mean one will loose data if using local gateway..., use with care
 * with the <tt>allowPrimary</tt> flag.
 */
public class AllocateAllocationCommand implements AllocationCommand {

    public static final String NAME = "allocate";

    public static class Factory implements AllocationCommand.Factory<AllocateAllocationCommand> {

        @Override
        public AllocateAllocationCommand readFrom(StreamInput in) throws IOException {
            return new AllocateAllocationCommand(ShardId.readShardId(in), in.readString(), in.readBoolean());
        }

        @Override
        public void writeTo(AllocateAllocationCommand command, StreamOutput out) throws IOException {
            command.shardId().writeTo(out);
            out.writeString(command.node());
            out.writeBoolean(command.allowPrimary());
        }

        @Override
        public AllocateAllocationCommand fromXContent(XContentParser parser) throws IOException {
            String index = null;
            int shardId = -1;
            String nodeId = null;
            boolean allowPrimary = false;

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
                    } else if ("node".equals(currentFieldName)) {
                        nodeId = parser.text();
                    } else if ("allow_primary".equals(currentFieldName) || "allowPrimary".equals(currentFieldName)) {
                        allowPrimary = parser.booleanValue();
                    } else {
                        throw new ElasticSearchParseException("[allocate] command does not support field [" + currentFieldName + "]");
                    }
                } else {
                    throw new ElasticSearchParseException("[allocate] command does not support complex json tokens [" + token + "]");
                }
            }
            if (index == null) {
                throw new ElasticSearchParseException("[allocate] command missing the index parameter");
            }
            if (shardId == -1) {
                throw new ElasticSearchParseException("[allocate] command missing the shard parameter");
            }
            if (nodeId == null) {
                throw new ElasticSearchParseException("[allocate] command missing the node parameter");
            }
            return new AllocateAllocationCommand(new ShardId(index, shardId), nodeId, allowPrimary);
        }

        @Override
        public void toXContent(AllocateAllocationCommand command, XContentBuilder builder, ToXContent.Params params) throws IOException {
            builder.startObject();
            builder.field("index", command.shardId().index());
            builder.field("shard", command.shardId().id());
            builder.field("node", command.node());
            builder.field("allow_primary", command.allowPrimary());
            builder.endObject();
        }
    }

    private final ShardId shardId;
    private final String node;
    private final boolean allowPrimary;

    /**
     * Create a new {@link AllocateAllocationCommand}
     *
     * @param shardId      {@link ShardId} of the shrad to assign
     * @param node         Node to assign the shard to
     * @param allowPrimary should the node be allow to allocate the shard as primary
     */
    public AllocateAllocationCommand(ShardId shardId, String node, boolean allowPrimary) {
        this.shardId = shardId;
        this.node = node;
        this.allowPrimary = allowPrimary;
    }

    @Override
    public String name() {
        return NAME;
    }

    /**
     * Get the shards id
     *
     * @return id of the shard
     */
    public ShardId shardId() {
        return this.shardId;
    }

    /**
     * Get the id of the Node
     *
     * @return id of the Node
     */
    public String node() {
        return this.node;
    }

    /**
     * Determine if primary allocation is allowed
     *
     * @return <code>true</code> if primary allocation is allowed. Otherwise <code>false</code>
     */
    public boolean allowPrimary() {
        return this.allowPrimary;
    }

    @Override
    public void execute(RoutingAllocation allocation) throws ElasticSearchException {
        DiscoveryNode discoNode = allocation.nodes().resolveNode(node);

        MutableShardRouting shardRouting = null;
        for (MutableShardRouting routing : allocation.routingNodes().unassigned()) {
            if (routing.shardId().equals(shardId)) {
                // prefer primaries first to allocate
                if (shardRouting == null || routing.primary()) {
                    shardRouting = routing;
                }
            }
        }

        if (shardRouting == null) {
            throw new ElasticSearchIllegalArgumentException("[allocate] failed to find " + shardId + " on the list of unassigned shards");
        }

        if (shardRouting.primary() && !allowPrimary) {
            throw new ElasticSearchIllegalArgumentException("[allocate] trying to allocate a primary shard " + shardId + "], which is disabled");
        }

        RoutingNode routingNode = allocation.routingNodes().node(discoNode.id());
        Decision decision = allocation.deciders().canAllocate(shardRouting, routingNode, allocation);
        if (decision.type() == Decision.Type.NO) {
            throw new ElasticSearchIllegalArgumentException("[allocate] allocation of " + shardId + " on node " + discoNode + " is not allowed, reason: " + decision);
        }
        // go over and remove it from the unassigned
        for (Iterator<MutableShardRouting> it = allocation.routingNodes().unassigned().iterator(); it.hasNext(); ) {
            if (it.next() != shardRouting) {
                continue;
            }
            it.remove();
            routingNode.add(shardRouting);
            if (shardRouting.primary()) {
                // we need to clear the post allocation flag, since its an explicit allocation of the primary shard
                // and we want to force allocate it (and create a new index for it)
                allocation.routingNodes().addClearPostAllocationFlag(shardRouting.shardId());
            }
            break;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5571.java