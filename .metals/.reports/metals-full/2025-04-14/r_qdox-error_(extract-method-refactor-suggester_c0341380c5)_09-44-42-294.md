error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9925.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9925.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9925.java
text:
```scala
i@@nt index = Math.abs(counter.getAndIncrement());

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

package org.elasticsearch.cluster.routing;

import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.collect.UnmodifiableIterator;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.util.concurrent.jsr166y.ThreadLocalRandom;
import org.elasticsearch.index.shard.ShardId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.elasticsearch.common.collect.Lists.*;

/**
 * @author kimchy (shay.banon)
 */
public class IndexShardRoutingTable implements Iterable<ShardRouting> {

    final ShardId shardId;

    final ImmutableList<ShardRouting> shards;

    final AtomicInteger counter;

    final boolean allocatedPostApi;

    IndexShardRoutingTable(ShardId shardId, ImmutableList<ShardRouting> shards, boolean allocatedPostApi) {
        this.shardId = shardId;
        this.shards = shards;
        this.allocatedPostApi = allocatedPostApi;
        this.counter = new AtomicInteger(ThreadLocalRandom.current().nextInt(shards.size()));
    }

    /**
     * Normalizes all shard routings to the same version.
     */
    public IndexShardRoutingTable normalizeVersions() {
        if (shards.isEmpty()) {
            return this;
        }
        if (shards.size() == 1) {
            return this;
        }
        long highestVersion = shards.get(0).version();
        boolean requiresNormalization = false;
        for (int i = 1; i < shards.size(); i++) {
            if (shards.get(i).version() != highestVersion) {
                requiresNormalization = true;
            }
            if (shards.get(i).version() > highestVersion) {
                highestVersion = shards.get(i).version();
            }
        }
        if (!requiresNormalization) {
            return this;
        }
        List<ShardRouting> shardRoutings = new ArrayList<ShardRouting>(shards.size());
        for (int i = 0; i < shards.size(); i++) {
            if (shards.get(i).version() == highestVersion) {
                shardRoutings.add(shards.get(i));
            } else {
                shardRoutings.add(new ImmutableShardRouting(shards.get(i), highestVersion));
            }
        }
        return new IndexShardRoutingTable(shardId, ImmutableList.copyOf(shardRoutings), allocatedPostApi);
    }

    /**
     * Has this shard group primary shard been allocated post API creation. Will be set to
     * <tt>true</tt> if it was created because of recovery action.
     */
    public boolean allocatedPostApi() {
        return allocatedPostApi;
    }

    public ShardId shardId() {
        return shardId;
    }

    public ShardId getShardId() {
        return shardId();
    }

    @Override public UnmodifiableIterator<ShardRouting> iterator() {
        return shards.iterator();
    }

    public int size() {
        return shards.size();
    }

    public int getSize() {
        return size();
    }

    public ImmutableList<ShardRouting> shards() {
        return shards;
    }

    public ImmutableList<ShardRouting> getShards() {
        return shards();
    }

    public int countWithState(ShardRoutingState state) {
        int count = 0;
        for (ShardRouting shard : this) {
            if (state == shard.state()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns a regular shard iterator.
     */
    public ShardIterator shardsIt() {
        return new PlainShardIterator(shardId, shards);
    }

    public ShardIterator shardsIt(int index) {
        return new PlainShardIterator(shardId, shards, index);
    }

    /**
     * Returns an iterator only on the primary shard.
     */
    public ShardIterator primaryShardIt() {
        ShardRouting primary = primaryShard();
        if (primary == null) {
            return new PlainShardIterator(shardId, ImmutableList.<ShardRouting>of());
        }
        return new PlainShardIterator(shardId, ImmutableList.of(primary));
    }

    /**
     * Prefers execution on the local node if applicable.
     */
    public ShardIterator preferLocalShardsIt(String nodeId) {
        ArrayList<ShardRouting> ordered = new ArrayList<ShardRouting>(this.shards.size());
        // fill it in a randomized fashion
        int index = counter.getAndIncrement();
        for (int i = 0; i < this.shards.size(); i++) {
            int loc = (index + i) % this.shards.size();
            ordered.add(this.shards.get(loc));
        }
        // find the local one, and push it upfront
        for (int i = 0; i < ordered.size(); i++) {
            ShardRouting current = ordered.get(i);
            if (nodeId.equals(current.currentNodeId())) {
                ordered.set(i, ordered.get(0));
                ordered.set(0, current);
                break;
            }
        }
        return new PlainShardIterator(shardId, ordered);
    }

    /**
     * Returns a random shards iterator.
     */
    public ShardIterator shardsRandomIt() {
        return new PlainShardIterator(shardId, shards, counter.getAndIncrement());
    }

    public ShardRouting primaryShard() {
        for (ShardRouting shardRouting : this) {
            if (shardRouting.primary()) {
                return shardRouting;
            }
        }
        return null;
    }

    public List<ShardRouting> replicaShards() {
        List<ShardRouting> replicaShards = Lists.newArrayListWithCapacity(2);
        for (ShardRouting shardRouting : this) {
            if (!shardRouting.primary()) {
                replicaShards.add(shardRouting);
            }
        }
        return replicaShards;
    }

    public List<ShardRouting> shardsWithState(ShardRoutingState... states) {
        List<ShardRouting> shards = newArrayList();
        for (ShardRouting shardEntry : this) {
            for (ShardRoutingState state : states) {
                if (shardEntry.state() == state) {
                    shards.add(shardEntry);
                }
            }
        }
        return shards;
    }

    public static class Builder {

        private ShardId shardId;

        private final List<ShardRouting> shards;

        private boolean allocatedPostApi;

        public Builder(IndexShardRoutingTable indexShard) {
            this.shardId = indexShard.shardId;
            this.shards = newArrayList(indexShard.shards);
            this.allocatedPostApi = indexShard.allocatedPostApi();
        }

        public Builder(ShardId shardId, boolean allocatedPostApi) {
            this.shardId = shardId;
            this.shards = newArrayList();
            this.allocatedPostApi = allocatedPostApi;
        }

        public Builder addShard(ImmutableShardRouting shardEntry) {
            for (ShardRouting shard : shards) {
                // don't add two that map to the same node id
                // we rely on the fact that a node does not have primary and backup of the same shard
                if (shard.assignedToNode() && shardEntry.assignedToNode()
                        && shard.currentNodeId().equals(shardEntry.currentNodeId())) {
                    return this;
                }
            }
            shards.add(shardEntry);
            return this;
        }

        public Builder removeShard(ShardRouting shardEntry) {
            shards.remove(shardEntry);
            return this;
        }

        public IndexShardRoutingTable build() {
            // we can automatically set allocatedPostApi to true if the primary is active
            if (!allocatedPostApi) {
                for (ShardRouting shardRouting : shards) {
                    if (shardRouting.primary() && shardRouting.active()) {
                        allocatedPostApi = true;
                    }
                }
            }
            return new IndexShardRoutingTable(shardId, ImmutableList.copyOf(shards), allocatedPostApi);
        }

        public static IndexShardRoutingTable readFrom(StreamInput in) throws IOException {
            String index = in.readUTF();
            return readFromThin(in, index);
        }

        public static IndexShardRoutingTable readFromThin(StreamInput in, String index) throws IOException {
            int iShardId = in.readVInt();
            boolean allocatedPostApi = in.readBoolean();
            Builder builder = new Builder(new ShardId(index, iShardId), allocatedPostApi);

            int size = in.readVInt();
            for (int i = 0; i < size; i++) {
                ImmutableShardRouting shard = ImmutableShardRouting.readShardRoutingEntry(in, index, iShardId);
                builder.addShard(shard);
            }

            return builder.build();
        }

        public static void writeTo(IndexShardRoutingTable indexShard, StreamOutput out) throws IOException {
            out.writeUTF(indexShard.shardId().index().name());
            writeToThin(indexShard, out);
        }

        public static void writeToThin(IndexShardRoutingTable indexShard, StreamOutput out) throws IOException {
            out.writeVInt(indexShard.shardId.id());
            out.writeBoolean(indexShard.allocatedPostApi());

            out.writeVInt(indexShard.shards.size());
            for (ShardRouting entry : indexShard) {
                entry.writeToThin(out);
            }
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9925.java