error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10024.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10024.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10024.java
text:
```scala
t@@his.topDocs = Lucene.EMPTY_TOP_DOCS;

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
package org.elasticsearch.search.aggregations.bucket.tophits;

import org.apache.lucene.search.*;
import org.elasticsearch.ExceptionsHelper;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationStreams;
import org.elasticsearch.search.aggregations.InternalAggregation;
import org.elasticsearch.search.internal.InternalSearchHit;
import org.elasticsearch.search.internal.InternalSearchHits;

import java.io.IOException;
import java.util.List;

/**
 */
public class InternalTopHits extends InternalAggregation implements TopHits, ToXContent, Streamable {

    public static final InternalAggregation.Type TYPE = new Type("top_hits");

    public static final AggregationStreams.Stream STREAM = new AggregationStreams.Stream() {
        @Override
        public InternalTopHits readResult(StreamInput in) throws IOException {
            InternalTopHits buckets = new InternalTopHits();
            buckets.readFrom(in);
            return buckets;
        }
    };

    public static void registerStreams() {
        AggregationStreams.registerStream(STREAM, TYPE.stream());
    }

    private int size;
    private Sort sort;
    private TopDocs topDocs;
    private InternalSearchHits searchHits;

    InternalTopHits() {
    }

    public InternalTopHits(String name, int size, Sort sort, TopDocs topDocs, InternalSearchHits searchHits) {
        this.name = name;
        this.size = size;
        this.sort = sort;
        this.topDocs = topDocs;
        this.searchHits = searchHits;
    }

    public InternalTopHits(String name, InternalSearchHits searchHits) {
        this.name = name;
        this.searchHits = searchHits;
        this.topDocs = new TopDocs(0, Lucene.EMPTY_SCORE_DOCS, 0);
    }


    @Override
    public Type type() {
        return TYPE;
    }

    @Override
    public SearchHits getHits() {
        return searchHits;
    }

    @Override
    public InternalAggregation reduce(ReduceContext reduceContext) {
        List<InternalAggregation> aggregations = reduceContext.aggregations();
        if (aggregations.size() == 1) {
            return aggregations.get(0);
        }

        TopDocs[] shardDocs = new TopDocs[aggregations.size()];
        InternalSearchHits[] shardHits = new InternalSearchHits[aggregations.size()];
        for (int i = 0; i < shardDocs.length; i++) {
            InternalTopHits topHitsAgg = (InternalTopHits) aggregations.get(i);
            shardDocs[i] = topHitsAgg.topDocs;
            shardHits[i] = topHitsAgg.searchHits;
        }

        try {
            int[] tracker = new int[shardHits.length];
            TopDocs reducedTopDocs = TopDocs.merge(sort, size, shardDocs);
            InternalSearchHit[] hits = new InternalSearchHit[reducedTopDocs.scoreDocs.length];
            for (int i = 0; i < reducedTopDocs.scoreDocs.length; i++) {
                ScoreDoc scoreDoc = reducedTopDocs.scoreDocs[i];
                hits[i] = (InternalSearchHit) shardHits[scoreDoc.shardIndex].getAt(tracker[scoreDoc.shardIndex]++);
            }
            return new InternalTopHits(name, new InternalSearchHits(hits, reducedTopDocs.totalHits, reducedTopDocs.getMaxScore()));
        } catch (IOException e) {
            throw ExceptionsHelper.convertToElastic(e);
        }
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        name = in.readString();
        size = in.readVInt();
        topDocs = Lucene.readTopDocs(in);
        if (topDocs instanceof TopFieldDocs) {
            sort = new Sort(((TopFieldDocs) topDocs).fields);
        }
        searchHits = InternalSearchHits.readSearchHits(in);
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(name);
        out.writeVInt(size);
        Lucene.writeTopDocs(out, topDocs, 0);
        searchHits.writeTo(out);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject(name);
        searchHits.toXContent(builder, params);
        builder.endObject();
        return builder;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10024.java