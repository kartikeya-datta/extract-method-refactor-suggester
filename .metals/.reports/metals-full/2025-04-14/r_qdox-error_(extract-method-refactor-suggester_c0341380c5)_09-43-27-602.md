error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3027.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3027.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3027.java
text:
```scala
S@@tringBuilder sb = new StringBuilder("routing_table:\n");

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

import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.common.collect.ImmutableMap;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.collect.UnmodifiableIterator;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.util.concurrent.Immutable;
import org.elasticsearch.index.Index;
import org.elasticsearch.indices.IndexMissingException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.collect.Maps.*;

/**
 * @author kimchy (Shay Banon)
 */
@Immutable
public class RoutingTable implements Iterable<IndexRoutingTable> {

    public static final RoutingTable EMPTY_ROUTING_TABLE = newRoutingTableBuilder().build();

    // index to IndexRoutingTable map
    private final ImmutableMap<String, IndexRoutingTable> indicesRouting;

    RoutingTable(Map<String, IndexRoutingTable> indicesRouting) {
        this.indicesRouting = ImmutableMap.copyOf(indicesRouting);
    }

    @Override public UnmodifiableIterator<IndexRoutingTable> iterator() {
        return indicesRouting.values().iterator();
    }

    public boolean hasIndex(String index) {
        return indicesRouting.containsKey(index);
    }

    public IndexRoutingTable index(String index) {
        return indicesRouting.get(index);
    }

    public Map<String, IndexRoutingTable> indicesRouting() {
        return indicesRouting;
    }

    public Map<String, IndexRoutingTable> getIndicesRouting() {
        return indicesRouting();
    }

    public RoutingNodes routingNodes(MetaData metaData) {
        return new RoutingNodes(metaData, this);
    }

    public RoutingTable validateRaiseException(MetaData metaData) throws RoutingValidationException {
        RoutingTableValidation validation = validate(metaData);
        if (!validation.valid()) {
            throw new RoutingValidationException(validation);
        }
        return this;
    }

    public RoutingTableValidation validate(MetaData metaData) {
        RoutingTableValidation validation = new RoutingTableValidation();
        for (IndexRoutingTable indexRoutingTable : this) {
            indexRoutingTable.validate(validation, metaData);
        }
        return validation;
    }

    /**
     * All the shards (replicas) for the provided indices.
     *
     * @param indices The indices to return all the shards (replicas), can be <tt>null</tt> or empty array to indicate all indices
     * @return All the shards matching the specific index
     * @throws IndexMissingException If an index passed does not exists
     */
    public List<ShardRouting> allShards(String... indices) throws IndexMissingException {
        List<ShardRouting> shards = Lists.newArrayList();
        if (indices == null || indices.length == 0) {
            indices = indicesRouting.keySet().toArray(new String[indicesRouting.keySet().size()]);
        }
        for (String index : indices) {
            IndexRoutingTable indexRoutingTable = index(index);
            if (indexRoutingTable == null) {
                throw new IndexMissingException(new Index(index));
            }
            for (IndexShardRoutingTable indexShardRoutingTable : indexRoutingTable) {
                for (ShardRouting shardRouting : indexShardRoutingTable) {
                    shards.add(shardRouting);
                }
            }
        }
        return shards;
    }

    /**
     * All the shards (replicas) for the provided indices grouped (each group is a single element, consisting
     * of the shard). This is handy for components that expect to get group iterators, but still want in some
     * cases to iterate over all the shards (and not just one shard in replication group).
     *
     * @param indices The indices to return all the shards (replicas), can be <tt>null</tt> or empty array to indicate all indices
     * @return All the shards grouped into a single shard element group each
     * @throws IndexMissingException If an index passed does not exists
     * @see IndexRoutingTable#groupByAllIt()
     */
    public GroupShardsIterator allShardsGrouped(String... indices) throws IndexMissingException {
        GroupShardsIterator its = new GroupShardsIterator();
        if (indices == null || indices.length == 0) {
            indices = indicesRouting.keySet().toArray(new String[indicesRouting.keySet().size()]);
        }
        for (String index : indices) {
            IndexRoutingTable indexRoutingTable = index(index);
            if (indexRoutingTable == null) {
                throw new IndexMissingException(new Index(index));
            }
            for (IndexShardRoutingTable indexShardRoutingTable : indexRoutingTable) {
                for (ShardRouting shardRouting : indexShardRoutingTable) {
                    its.add(shardRouting.shardsIt());
                }
            }
        }
        return its;
    }

    public static Builder newRoutingTableBuilder() {
        return new Builder();
    }

    public static class Builder {

        private final Map<String, IndexRoutingTable> indicesRouting = newHashMap();

        public Builder add(IndexRoutingTable indexRoutingTable) {
            indexRoutingTable.validate();
            indicesRouting.put(indexRoutingTable.index(), indexRoutingTable);
            return this;
        }

        public Builder add(IndexRoutingTable.Builder indexRoutingTableBuilder) {
            add(indexRoutingTableBuilder.build());
            return this;
        }

        public Builder updateNodes(RoutingNodes routingNodes) {
            Map<String, IndexRoutingTable.Builder> indexRoutingTableBuilders = newHashMap();
            for (RoutingNode routingNode : routingNodes) {
                for (MutableShardRouting shardRoutingEntry : routingNode) {
                    // every relocating shard has a double entry, ignore the target one.
                    if (shardRoutingEntry.state() == ShardRoutingState.INITIALIZING && shardRoutingEntry.relocatingNodeId() != null)
                        continue;

                    String index = shardRoutingEntry.index();
                    IndexRoutingTable.Builder indexBuilder = indexRoutingTableBuilders.get(index);
                    if (indexBuilder == null) {
                        indexBuilder = new IndexRoutingTable.Builder(index);
                        indexRoutingTableBuilders.put(index, indexBuilder);
                    }
                    indexBuilder.addShard(new ImmutableShardRouting(shardRoutingEntry));
                }
            }
            for (MutableShardRouting shardRoutingEntry : routingNodes.unassigned()) {
                String index = shardRoutingEntry.index();
                IndexRoutingTable.Builder indexBuilder = indexRoutingTableBuilders.get(index);
                if (indexBuilder == null) {
                    indexBuilder = new IndexRoutingTable.Builder(index);
                    indexRoutingTableBuilders.put(index, indexBuilder);
                }
                indexBuilder.addShard(new ImmutableShardRouting(shardRoutingEntry));
            }
            for (IndexRoutingTable.Builder indexBuilder : indexRoutingTableBuilders.values()) {
                add(indexBuilder);
            }
            return this;
        }

        public RoutingTable build() {
            return new RoutingTable(indicesRouting);
        }

        public static RoutingTable readFrom(StreamInput in) throws IOException {
            Builder builder = new Builder();
            int size = in.readVInt();
            for (int i = 0; i < size; i++) {
                IndexRoutingTable index = IndexRoutingTable.Builder.readFrom(in);
                builder.add(index);
            }

            return builder.build();
        }

        public static void writeTo(RoutingTable table, StreamOutput out) throws IOException {
            out.writeVInt(table.indicesRouting.size());
            for (IndexRoutingTable index : table.indicesRouting.values()) {
                IndexRoutingTable.Builder.writeTo(index, out);
            }
        }
    }

    public String prettyPrint() {
        StringBuilder sb = new StringBuilder("Routing Table:\n");
        for (Map.Entry<String, IndexRoutingTable> entry : indicesRouting.entrySet()) {
            sb.append(entry.getValue().prettyPrint()).append('\n');
        }
        return sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3027.java