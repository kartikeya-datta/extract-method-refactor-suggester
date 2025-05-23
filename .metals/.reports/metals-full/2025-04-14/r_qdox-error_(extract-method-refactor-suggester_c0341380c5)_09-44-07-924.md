error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10195.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10195.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10195.java
text:
```scala
t@@his.requestedSize = context.size();

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
package org.elasticsearch.action.percolate;

import com.google.common.collect.ImmutableList;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.action.support.broadcast.BroadcastShardOperationResponse;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.percolator.PercolateContext;
import org.elasticsearch.search.aggregations.InternalAggregations;
import org.elasticsearch.search.facet.InternalFacets;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.query.QuerySearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class PercolateShardResponse extends BroadcastShardOperationResponse {

    private static final BytesRef[] EMPTY_MATCHES = new BytesRef[0];
    private static final float[] EMPTY_SCORES = new float[0];
    private static final List<Map<String, HighlightField>> EMPTY_HL = ImmutableList.of();

    private long count;
    private float[] scores;
    private BytesRef[] matches;
    private List<Map<String, HighlightField>> hls;
    private byte percolatorTypeId;
    private int requestedSize;

    private InternalFacets facets;
    private InternalAggregations aggregations;

    PercolateShardResponse() {
        hls = new ArrayList<Map<String, HighlightField>>();
    }

    public PercolateShardResponse(BytesRef[] matches, List<Map<String, HighlightField>> hls, long count, float[] scores, PercolateContext context, String index, int shardId) {
        super(index, shardId);
        this.matches = matches;
        this.hls = hls;
        this.count = count;
        this.scores = scores;
        this.percolatorTypeId = context.percolatorTypeId;
        this.requestedSize = context.size;
        QuerySearchResult result = context.queryResult();
        if (result != null) {
            if (result.facets() != null) {
                this.facets = new InternalFacets(result.facets().facets());
            }
            if (result.aggregations() != null) {
                this.aggregations = (InternalAggregations) result.aggregations();
            }
        }
    }

    public PercolateShardResponse(BytesRef[] matches, long count, float[] scores, PercolateContext context, String index, int shardId) {
        this(matches, EMPTY_HL, count, scores, context, index, shardId);
    }

    public PercolateShardResponse(BytesRef[] matches, List<Map<String, HighlightField>> hls, long count, PercolateContext context, String index, int shardId) {
        this(matches, hls, count, EMPTY_SCORES, context, index, shardId);
    }

    public PercolateShardResponse(long count, PercolateContext context, String index, int shardId) {
        this(EMPTY_MATCHES, EMPTY_HL, count, EMPTY_SCORES, context, index, shardId);
    }

    public PercolateShardResponse(PercolateContext context, String index, int shardId) {
        this(EMPTY_MATCHES, EMPTY_HL, 0, EMPTY_SCORES, context, index, shardId);
    }

    public BytesRef[] matches() {
        return matches;
    }

    public float[] scores() {
        return scores;
    }

    public long count() {
        return count;
    }

    public int requestedSize() {
        return requestedSize;
    }

    public List<Map<String, HighlightField>> hls() {
        return hls;
    }

    public InternalFacets facets() {
        return facets;
    }

    public InternalAggregations aggregations() {
        return aggregations;
    }

    public byte percolatorTypeId() {
        return percolatorTypeId;
    }

    public boolean isEmpty() {
        return percolatorTypeId == 0x00;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        percolatorTypeId = in.readByte();
        requestedSize = in.readVInt();
        count = in.readVLong();
        matches = new BytesRef[in.readVInt()];
        for (int i = 0; i < matches.length; i++) {
            matches[i] = in.readBytesRef();
        }
        scores = new float[in.readVInt()];
        for (int i = 0; i < scores.length; i++) {
            scores[i] = in.readFloat();
        }
        int size = in.readVInt();
        for (int i = 0; i < size; i++) {
            int mSize = in.readVInt();
            Map<String, HighlightField> fields = new HashMap<String, HighlightField>();
            for (int j = 0; j < mSize; j++) {
                fields.put(in.readString(), HighlightField.readHighlightField(in));
            }
            hls.add(fields);
        }
        facets = InternalFacets.readOptionalFacets(in);
        aggregations = InternalAggregations.readOptionalAggregations(in);
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeByte(percolatorTypeId);
        out.writeVLong(requestedSize);
        out.writeVLong(count);
        out.writeVInt(matches.length);
        for (BytesRef match : matches) {
            out.writeBytesRef(match);
        }
        out.writeVLong(scores.length);
        for (float score : scores) {
            out.writeFloat(score);
        }
        out.writeVInt(hls.size());
        for (Map<String, HighlightField> hl : hls) {
            out.writeVInt(hl.size());
            for (Map.Entry<String, HighlightField> entry : hl.entrySet()) {
                out.writeString(entry.getKey());
                entry.getValue().writeTo(out);
            }
        }
        out.writeOptionalStreamable(facets);
        out.writeOptionalStreamable(aggregations);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10195.java