error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2511.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2511.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2511.java
text:
```scala
b@@uckets.close();

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
package org.elasticsearch.search.aggregations.bucket.terms;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.text.StringText;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.util.DoubleObjectPagedHashMap;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.aggregations.AggregationStreams;
import org.elasticsearch.search.aggregations.InternalAggregation;
import org.elasticsearch.search.aggregations.InternalAggregations;
import org.elasticsearch.search.aggregations.bucket.terms.support.BucketPriorityQueue;
import org.elasticsearch.search.aggregations.support.format.ValueFormatter;
import org.elasticsearch.search.aggregations.support.format.ValueFormatterStreams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class DoubleTerms extends InternalTerms {

    public static final Type TYPE = new Type("terms", "dterms");

    public static final AggregationStreams.Stream STREAM = new AggregationStreams.Stream() {
        @Override
        public DoubleTerms readResult(StreamInput in) throws IOException {
            DoubleTerms buckets = new DoubleTerms();
            buckets.readFrom(in);
            return buckets;
        }
    };

    public static void registerStreams() {
        AggregationStreams.registerStream(STREAM, TYPE.stream());
    }

    static class Bucket extends InternalTerms.Bucket {

        double term;

        public Bucket(double term, long docCount, InternalAggregations aggregations) {
            super(docCount, aggregations);
            this.term = term;
        }

        @Override
        public String getKey() {
            return String.valueOf(term);
        }

        @Override
        public Text getKeyAsText() {
            return new StringText(String.valueOf(term));
        }

        @Override
        public Number getKeyAsNumber() {
            return term;
        }

        @Override
        int compareTerm(Terms.Bucket other) {
            return Double.compare(term, other.getKeyAsNumber().doubleValue());
        }

    }

    private @Nullable ValueFormatter formatter;

    DoubleTerms() {} // for serialization

    public DoubleTerms(String name, InternalOrder order, @Nullable ValueFormatter formatter, int requiredSize, long minDocCount, Collection<InternalTerms.Bucket> buckets) {
        super(name, order, requiredSize, minDocCount, buckets);
        this.formatter = formatter;
    }

    @Override
    public Type type() {
        return TYPE;
    }

    @Override
    public InternalTerms reduce(ReduceContext reduceContext) {
        List<InternalAggregation> aggregations = reduceContext.aggregations();
        if (aggregations.size() == 1) {
            InternalTerms terms = (InternalTerms) aggregations.get(0);
            terms.trimExcessEntries(reduceContext.bigArrays());
            return terms;
        }
        InternalTerms reduced = null;

        DoubleObjectPagedHashMap<List<Bucket>> buckets = null;
        for (InternalAggregation aggregation : aggregations) {
            InternalTerms terms = (InternalTerms) aggregation;
            if (terms instanceof UnmappedTerms) {
                continue;
            }
            if (reduced == null) {
                reduced = terms;
            }
            if (buckets == null) {
                buckets = new DoubleObjectPagedHashMap<>(terms.buckets.size(), reduceContext.bigArrays());
            }
            for (Terms.Bucket bucket : terms.buckets) {
                List<Bucket> existingBuckets = buckets.get(((Bucket) bucket).term);
                if (existingBuckets == null) {
                    existingBuckets = new ArrayList<>(aggregations.size());
                    buckets.put(((Bucket) bucket).term, existingBuckets);
                }
                existingBuckets.add((Bucket) bucket);
            }
        }

        if (reduced == null) {
            // there are only unmapped terms, so we just return the first one (no need to reduce)
            return (UnmappedTerms) aggregations.get(0);
        }

        // TODO: would it be better to sort the backing array buffer of hppc map directly instead of using a PQ?
        final int size = (int) Math.min(requiredSize, buckets.size());
        BucketPriorityQueue ordered = new BucketPriorityQueue(size, order.comparator(null));
        for (DoubleObjectPagedHashMap.Cursor<List<DoubleTerms.Bucket>> cursor : buckets) {
            List<DoubleTerms.Bucket> sameTermBuckets = cursor.value;
            final InternalTerms.Bucket b = sameTermBuckets.get(0).reduce(sameTermBuckets, reduceContext.bigArrays());
            if (b.getDocCount() >= minDocCount) {
                ordered.insertWithOverflow(b);
            }
        }
        buckets.release();
        InternalTerms.Bucket[] list = new InternalTerms.Bucket[ordered.size()];
        for (int i = ordered.size() - 1; i >= 0; i--) {
            list[i] = (Bucket) ordered.pop();
        }
        reduced.buckets = Arrays.asList(list);
        return reduced;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        this.name = in.readString();
        this.order = InternalOrder.Streams.readOrder(in);
        this.formatter = ValueFormatterStreams.readOptional(in);
        this.requiredSize = readSize(in);
        this.minDocCount = in.readVLong();
        int size = in.readVInt();
        List<InternalTerms.Bucket> buckets = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            buckets.add(new Bucket(in.readDouble(), in.readVLong(), InternalAggregations.readAggregations(in)));
        }
        this.buckets = buckets;
        this.bucketMap = null;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(name);
        InternalOrder.Streams.writeOrder(order, out);
        ValueFormatterStreams.writeOptional(formatter, out);
        writeSize(requiredSize, out);
        out.writeVLong(minDocCount);
        out.writeVInt(buckets.size());
        for (InternalTerms.Bucket bucket : buckets) {
            out.writeDouble(((Bucket) bucket).term);
            out.writeVLong(bucket.getDocCount());
            ((InternalAggregations) bucket.getAggregations()).writeTo(out);
        }
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject(name);
        builder.startArray(CommonFields.BUCKETS);
        for (InternalTerms.Bucket bucket : buckets) {
            builder.startObject();
            builder.field(CommonFields.KEY, ((Bucket) bucket).term);
            if (formatter != null) {
                builder.field(CommonFields.KEY_AS_STRING, formatter.format(((Bucket) bucket).term));
            }
            builder.field(CommonFields.DOC_COUNT, bucket.getDocCount());
            ((InternalAggregations) bucket.getAggregations()).toXContentInternal(builder, params);
            builder.endObject();
        }
        builder.endArray();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2511.java