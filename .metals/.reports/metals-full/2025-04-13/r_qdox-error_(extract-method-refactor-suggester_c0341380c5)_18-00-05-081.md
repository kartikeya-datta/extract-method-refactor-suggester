error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4019.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4019.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4019.java
text:
```scala
b@@uilder.byteSizeField(Fields.SIZE_IN_BYTES, Fields.SIZE, segment.getSizeInBytes());

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

package org.elasticsearch.action.admin.indices.segments;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.elasticsearch.action.ShardOperationFailedException;
import org.elasticsearch.action.support.broadcast.BroadcastOperationResponse;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentBuilderString;
import org.elasticsearch.index.engine.Segment;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IndicesSegmentResponse extends BroadcastOperationResponse implements ToXContent {

    private ShardSegments[] shards;

    private Map<String, IndexSegments> indicesSegments;

    IndicesSegmentResponse() {

    }

    IndicesSegmentResponse(ShardSegments[] shards, ClusterState clusterState, int totalShards, int successfulShards, int failedShards, List<ShardOperationFailedException> shardFailures) {
        super(totalShards, successfulShards, failedShards, shardFailures);
        this.shards = shards;
    }

    public Map<String, IndexSegments> getIndices() {
        if (indicesSegments != null) {
            return indicesSegments;
        }
        Map<String, IndexSegments> indicesSegments = Maps.newHashMap();

        Set<String> indices = Sets.newHashSet();
        for (ShardSegments shard : shards) {
            indices.add(shard.getIndex());
        }

        for (String index : indices) {
            List<ShardSegments> shards = Lists.newArrayList();
            for (ShardSegments shard : this.shards) {
                if (shard.getShardRouting().index().equals(index)) {
                    shards.add(shard);
                }
            }
            indicesSegments.put(index, new IndexSegments(index, shards.toArray(new ShardSegments[shards.size()])));
        }
        this.indicesSegments = indicesSegments;
        return indicesSegments;
    }


    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        shards = new ShardSegments[in.readVInt()];
        for (int i = 0; i < shards.length; i++) {
            shards[i] = ShardSegments.readShardSegments(in);
        }
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeVInt(shards.length);
        for (ShardSegments shard : shards) {
            shard.writeTo(out);
        }
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject(Fields.INDICES);

        for (IndexSegments indexSegments : getIndices().values()) {
            builder.startObject(indexSegments.getIndex(), XContentBuilder.FieldCaseConversion.NONE);

            builder.startObject(Fields.SHARDS);
            for (IndexShardSegments indexSegment : indexSegments) {
                builder.startArray(Integer.toString(indexSegment.getShardId().id()));
                for (ShardSegments shardSegments : indexSegment) {
                    builder.startObject();

                    builder.startObject(Fields.ROUTING);
                    builder.field(Fields.STATE, shardSegments.getShardRouting().state());
                    builder.field(Fields.PRIMARY, shardSegments.getShardRouting().primary());
                    builder.field(Fields.NODE, shardSegments.getShardRouting().currentNodeId());
                    if (shardSegments.getShardRouting().relocatingNodeId() != null) {
                        builder.field(Fields.RELOCATING_NODE, shardSegments.getShardRouting().relocatingNodeId());
                    }
                    builder.endObject();

                    builder.field(Fields.NUM_COMMITTED_SEGMENTS, shardSegments.getNumberOfCommitted());
                    builder.field(Fields.NUM_SEARCH_SEGMENTS, shardSegments.getNumberOfSearch());

                    builder.startObject(Fields.SEGMENTS);
                    for (Segment segment : shardSegments) {
                        builder.startObject(segment.getName());
                        builder.field(Fields.GENERATION, segment.getGeneration());
                        builder.field(Fields.NUM_DOCS, segment.getNumDocs());
                        builder.field(Fields.DELETED_DOCS, segment.getDeletedDocs());
                        builder.byteSizeField(Fields.SIZE, Fields.SIZE_IN_BYTES, segment.getSizeInBytes());
                        builder.field(Fields.COMMITTED, segment.isCommitted());
                        builder.field(Fields.SEARCH, segment.isSearch());
                        if (segment.getVersion() != null) {
                            builder.field(Fields.VERSION, segment.getVersion());
                        }
                        if (segment.isCompound() != null) {
                            builder.field(Fields.COMPOUND, segment.isCompound());
                        }
                        builder.endObject();
                    }
                    builder.endObject();

                    builder.endObject();
                }
                builder.endArray();
            }
            builder.endObject();

            builder.endObject();
        }

        builder.endObject();
        return builder;
    }

    static final class Fields {
        static final XContentBuilderString INDICES = new XContentBuilderString("indices");
        static final XContentBuilderString SHARDS = new XContentBuilderString("shards");
        static final XContentBuilderString ROUTING = new XContentBuilderString("routing");
        static final XContentBuilderString STATE = new XContentBuilderString("state");
        static final XContentBuilderString PRIMARY = new XContentBuilderString("primary");
        static final XContentBuilderString NODE = new XContentBuilderString("node");
        static final XContentBuilderString RELOCATING_NODE = new XContentBuilderString("relocating_node");

        static final XContentBuilderString SEGMENTS = new XContentBuilderString("segments");
        static final XContentBuilderString GENERATION = new XContentBuilderString("generation");
        static final XContentBuilderString NUM_COMMITTED_SEGMENTS = new XContentBuilderString("num_committed_segments");
        static final XContentBuilderString NUM_SEARCH_SEGMENTS = new XContentBuilderString("num_search_segments");
        static final XContentBuilderString NUM_DOCS = new XContentBuilderString("num_docs");
        static final XContentBuilderString DELETED_DOCS = new XContentBuilderString("deleted_docs");
        static final XContentBuilderString SIZE = new XContentBuilderString("size");
        static final XContentBuilderString SIZE_IN_BYTES = new XContentBuilderString("size_in_bytes");
        static final XContentBuilderString COMMITTED = new XContentBuilderString("committed");
        static final XContentBuilderString SEARCH = new XContentBuilderString("search");
        static final XContentBuilderString VERSION = new XContentBuilderString("version");
        static final XContentBuilderString COMPOUND = new XContentBuilderString("compound");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4019.java