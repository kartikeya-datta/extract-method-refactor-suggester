error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2859.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2859.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2859.java
text:
```scala
public static final I@@nternalSearchHit[] EMPTY = new InternalSearchHit[0];

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

package org.elasticsearch.search.internal;

import org.elasticsearch.common.collect.Iterators;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.thread.ThreadLocals;
import org.elasticsearch.common.trove.map.hash.TIntObjectHashMap;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentBuilderString;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.SearchShardTarget;

import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

import static org.elasticsearch.search.SearchShardTarget.*;
import static org.elasticsearch.search.internal.InternalSearchHit.*;

/**
 * @author kimchy (shay.banon)
 */
public class InternalSearchHits implements SearchHits {

    public static class StreamContext {

        public static enum ShardTargetType {
            STREAM,
            LOOKUP,
            NO_STREAM
        }

        private IdentityHashMap<SearchShardTarget, Integer> shardHandleLookup = new IdentityHashMap<SearchShardTarget, Integer>();
        private TIntObjectHashMap<SearchShardTarget> handleShardLookup = new TIntObjectHashMap<SearchShardTarget>();
        private ShardTargetType streamShardTarget = ShardTargetType.STREAM;

        public StreamContext reset() {
            shardHandleLookup.clear();
            handleShardLookup.clear();
            streamShardTarget = ShardTargetType.STREAM;
            return this;
        }

        public IdentityHashMap<SearchShardTarget, Integer> shardHandleLookup() {
            return shardHandleLookup;
        }

        public TIntObjectHashMap<SearchShardTarget> handleShardLookup() {
            return handleShardLookup;
        }

        public ShardTargetType streamShardTarget() {
            return streamShardTarget;
        }

        public StreamContext streamShardTarget(ShardTargetType streamShardTarget) {
            this.streamShardTarget = streamShardTarget;
            return this;
        }
    }

    private static final ThreadLocal<ThreadLocals.CleanableValue<StreamContext>> cache = new ThreadLocal<ThreadLocals.CleanableValue<StreamContext>>() {
        @Override protected ThreadLocals.CleanableValue<StreamContext> initialValue() {
            return new ThreadLocals.CleanableValue<StreamContext>(new StreamContext());
        }
    };

    public static StreamContext streamContext() {
        return cache.get().get().reset();
    }


    private static final InternalSearchHit[] EMPTY = new InternalSearchHit[0];

    private InternalSearchHit[] hits;

    private long totalHits;

    private float maxScore;

    InternalSearchHits() {

    }

    public InternalSearchHits(InternalSearchHit[] hits, long totalHits, float maxScore) {
        this.hits = hits;
        this.totalHits = totalHits;
        this.maxScore = maxScore;
    }

    public void shardTarget(SearchShardTarget shardTarget) {
        for (InternalSearchHit hit : hits) {
            hit.shardTarget(shardTarget);
        }
    }

    public long totalHits() {
        return totalHits;
    }

    @Override public long getTotalHits() {
        return totalHits();
    }

    @Override public float maxScore() {
        return this.maxScore;
    }

    @Override public float getMaxScore() {
        return maxScore();
    }

    public SearchHit[] hits() {
        return this.hits;
    }

    @Override public SearchHit getAt(int position) {
        return hits[position];
    }

    @Override public SearchHit[] getHits() {
        return hits();
    }

    @Override public Iterator<SearchHit> iterator() {
        return Iterators.forArray(hits());
    }

    public InternalSearchHit[] internalHits() {
        return this.hits;
    }

    static final class Fields {
        static final XContentBuilderString HITS = new XContentBuilderString("hits");
        static final XContentBuilderString TOTAL = new XContentBuilderString("total");
        static final XContentBuilderString MAX_SCORE = new XContentBuilderString("max_score");
    }

    @Override public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject(Fields.HITS);
        builder.field(Fields.TOTAL, totalHits);
        if (Float.isNaN(maxScore)) {
            builder.nullField(Fields.MAX_SCORE);
        } else {
            builder.field(Fields.MAX_SCORE, maxScore);
        }
        builder.field(Fields.HITS);
        builder.startArray();
        for (SearchHit hit : hits) {
            hit.toXContent(builder, params);
        }
        builder.endArray();
        builder.endObject();
        return builder;
    }

    public static InternalSearchHits readSearchHits(StreamInput in, StreamContext context) throws IOException {
        InternalSearchHits hits = new InternalSearchHits();
        hits.readFrom(in, context);
        return hits;
    }

    public static InternalSearchHits readSearchHits(StreamInput in) throws IOException {
        InternalSearchHits hits = new InternalSearchHits();
        hits.readFrom(in);
        return hits;
    }

    @Override public void readFrom(StreamInput in) throws IOException {
        readFrom(in, streamContext().streamShardTarget(StreamContext.ShardTargetType.LOOKUP));
    }

    public void readFrom(StreamInput in, StreamContext context) throws IOException {
        totalHits = in.readVLong();
        maxScore = in.readFloat();
        int size = in.readVInt();
        if (size == 0) {
            hits = EMPTY;
        } else {
            if (context.streamShardTarget() == StreamContext.ShardTargetType.LOOKUP) {
                // read the lookup table first
                int lookupSize = in.readVInt();
                for (int i = 0; i < lookupSize; i++) {
                    context.handleShardLookup().put(in.readVInt(), readSearchShardTarget(in));
                }
            }

            hits = new InternalSearchHit[size];
            for (int i = 0; i < hits.length; i++) {
                hits[i] = readSearchHit(in, context);
            }
        }
    }

    @Override public void writeTo(StreamOutput out) throws IOException {
        writeTo(out, streamContext().streamShardTarget(StreamContext.ShardTargetType.LOOKUP));
    }

    public void writeTo(StreamOutput out, StreamContext context) throws IOException {
        out.writeVLong(totalHits);
        out.writeFloat(maxScore);
        out.writeVInt(hits.length);
        if (hits.length > 0) {
            if (context.streamShardTarget() == StreamContext.ShardTargetType.LOOKUP) {
                // start from 1, 0 is for null!
                int counter = 1;
                for (InternalSearchHit hit : hits) {
                    if (hit.shard() != null) {
                        Integer handle = context.shardHandleLookup().get(hit.shard());
                        if (handle == null) {
                            context.shardHandleLookup().put(hit.shard(), counter++);
                        }
                    }
                }
                out.writeVInt(context.shardHandleLookup().size());
                if (!context.shardHandleLookup().isEmpty()) {
                    for (Map.Entry<SearchShardTarget, Integer> entry : context.shardHandleLookup().entrySet()) {
                        out.writeVInt(entry.getValue());
                        entry.getKey().writeTo(out);
                    }
                }
            }

            for (InternalSearchHit hit : hits) {
                hit.writeTo(out, context);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2859.java