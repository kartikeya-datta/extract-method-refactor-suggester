error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9638.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9638.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9638.java
text:
```scala
T@@erms.Order order, BucketCountThresholds bucketCountThresholds, AggregationContext aggregationContext, Aggregator parent, SubAggCollectionMode subAggCollectMode, boolean showTermDocCountError) {

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

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.SortedNumericDocValues;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.lease.Releasables;
import org.elasticsearch.common.util.LongHash;
import org.elasticsearch.search.aggregations.Aggregator;
import org.elasticsearch.search.aggregations.AggregatorFactories;
import org.elasticsearch.search.aggregations.InternalAggregation;
import org.elasticsearch.search.aggregations.bucket.terms.support.BucketPriorityQueue;
import org.elasticsearch.search.aggregations.support.AggregationContext;
import org.elasticsearch.search.aggregations.support.ValuesSource;
import org.elasticsearch.search.aggregations.support.format.ValueFormat;
import org.elasticsearch.search.aggregations.support.format.ValueFormatter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 */
public class LongTermsAggregator extends TermsAggregator {

    protected final ValuesSource.Numeric valuesSource;
    protected final @Nullable ValueFormatter formatter;
    protected final LongHash bucketOrds;
    private boolean showTermDocCountError;
    private SortedNumericDocValues values;

    public LongTermsAggregator(String name, AggregatorFactories factories, ValuesSource.Numeric valuesSource, @Nullable ValueFormat format, long estimatedBucketCount,
                               InternalOrder order, BucketCountThresholds bucketCountThresholds, AggregationContext aggregationContext, Aggregator parent, SubAggCollectionMode subAggCollectMode, boolean showTermDocCountError) {
        super(name, BucketAggregationMode.PER_BUCKET, factories, estimatedBucketCount, aggregationContext, parent, bucketCountThresholds, order, subAggCollectMode);
        this.valuesSource = valuesSource;
        this.showTermDocCountError = showTermDocCountError;
        this.formatter = format != null ? format.formatter() : null;
        bucketOrds = new LongHash(estimatedBucketCount, aggregationContext.bigArrays());
    }
    
    

    @Override
    public boolean shouldCollect() {
        return true;
    }

    protected SortedNumericDocValues getValues(ValuesSource.Numeric valuesSource) {
        return valuesSource.longValues();
    }

    @Override
    public void setNextReader(AtomicReaderContext reader) {
        values = getValues(valuesSource);
    }

    @Override
    public void collect(int doc, long owningBucketOrdinal) throws IOException {
        assert owningBucketOrdinal == 0;
        values.setDocument(doc);
        final int valuesCount = values.count();

        long previous = Long.MAX_VALUE;
        for (int i = 0; i < valuesCount; ++i) {
            final long val = values.valueAt(i);
            if (previous != val || i == 0) {
                long bucketOrdinal = bucketOrds.add(val);
                if (bucketOrdinal < 0) { // already seen
                    bucketOrdinal = - 1 - bucketOrdinal;
                    collectExistingBucket(doc, bucketOrdinal);
                } else {
                    collectBucket(doc, bucketOrdinal);
                }
                previous = val;
            }
        }
    }

    @Override
    public InternalAggregation buildAggregation(long owningBucketOrdinal) {
        assert owningBucketOrdinal == 0;

        if (bucketCountThresholds.getMinDocCount() == 0 && (order != InternalOrder.COUNT_DESC || bucketOrds.size() < bucketCountThresholds.getRequiredSize())) {
            // we need to fill-in the blanks
            for (AtomicReaderContext ctx : context.searchContext().searcher().getTopReaderContext().leaves()) {
                context.setNextReader(ctx);
                final SortedNumericDocValues values = getValues(valuesSource);
                for (int docId = 0; docId < ctx.reader().maxDoc(); ++docId) {
                    values.setDocument(docId);
                    final int valueCount = values.count();
                    for (int i = 0; i < valueCount; ++i) {
                        bucketOrds.add(values.valueAt(i));
                    }
                }
            }
        }

        final int size = (int) Math.min(bucketOrds.size(), bucketCountThresholds.getShardSize());

        BucketPriorityQueue ordered = new BucketPriorityQueue(size, order.comparator(this));
        LongTerms.Bucket spare = null;
        for (long i = 0; i < bucketOrds.size(); i++) {
            if (spare == null) {
                spare = new LongTerms.Bucket(0, 0, null, showTermDocCountError, 0);
            }
            spare.term = bucketOrds.get(i);
            spare.docCount = bucketDocCount(i);
            spare.bucketOrd = i;
            if (bucketCountThresholds.getShardMinDocCount() <= spare.docCount) {
                spare = (LongTerms.Bucket) ordered.insertWithOverflow(spare);
            }
        }

        // Get the top buckets
        final InternalTerms.Bucket[] list = new InternalTerms.Bucket[ordered.size()];
        long survivingBucketOrds[] = new long[ordered.size()];
        for (int i = ordered.size() - 1; i >= 0; --i) {
            final LongTerms.Bucket bucket = (LongTerms.Bucket) ordered.pop();
            survivingBucketOrds[i] = bucket.bucketOrd;
            list[i] = bucket;
        }
      
        runDeferredCollections(survivingBucketOrds);

        //Now build the aggs
        for (int i = 0; i < list.length; i++) {
          list[i].aggregations = bucketAggregations(list[i].bucketOrd);
          list[i].docCountError = 0;
        }
        
        return new LongTerms(name, order, formatter, bucketCountThresholds.getRequiredSize(), bucketCountThresholds.getShardSize(), bucketCountThresholds.getMinDocCount(), Arrays.asList(list), showTermDocCountError, 0);
    }
    
    
    @Override
    public InternalAggregation buildEmptyAggregation() {
        return new LongTerms(name, order, formatter, bucketCountThresholds.getRequiredSize(), bucketCountThresholds.getShardSize(), bucketCountThresholds.getMinDocCount(), Collections.<InternalTerms.Bucket>emptyList(), showTermDocCountError, 0);
    }

    @Override
    public void doClose() {
        Releasables.close(bucketOrds);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9638.java