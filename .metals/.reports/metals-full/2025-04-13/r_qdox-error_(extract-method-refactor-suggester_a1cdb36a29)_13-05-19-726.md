error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7371.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7371.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7371.java
text:
```scala
L@@ist<InternalHistogram.Bucket> buckets = new ArrayList<>((int) bucketOrds.size());

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
package org.elasticsearch.search.aggregations.bucket.histogram;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.util.CollectionUtil;
import org.elasticsearch.common.inject.internal.Nullable;
import org.elasticsearch.common.lease.Releasables;
import org.elasticsearch.common.rounding.Rounding;
import org.elasticsearch.common.util.LongHash;
import org.elasticsearch.index.fielddata.LongValues;
import org.elasticsearch.search.aggregations.Aggregator;
import org.elasticsearch.search.aggregations.AggregatorFactories;
import org.elasticsearch.search.aggregations.InternalAggregation;
import org.elasticsearch.search.aggregations.bucket.BucketsAggregator;
import org.elasticsearch.search.aggregations.support.AggregationContext;
import org.elasticsearch.search.aggregations.support.ValueSourceAggregatorFactory;
import org.elasticsearch.search.aggregations.support.ValuesSourceConfig;
import org.elasticsearch.search.aggregations.support.numeric.NumericValuesSource;
import org.elasticsearch.search.aggregations.support.numeric.ValueFormatter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistogramAggregator extends BucketsAggregator {

    private final NumericValuesSource valuesSource;
    private final Rounding rounding;
    private final InternalOrder order;
    private final boolean keyed;
    private final long minDocCount;
    private final ExtendedBounds extendedBounds;
    private final InternalHistogram.Factory histogramFactory;

    private final LongHash bucketOrds;
    private LongValues values;

    public HistogramAggregator(String name, AggregatorFactories factories, Rounding rounding, InternalOrder order,
                               boolean keyed, long minDocCount, @Nullable ExtendedBounds extendedBounds,
                               @Nullable NumericValuesSource valuesSource, long initialCapacity, InternalHistogram.Factory<?> histogramFactory,
                               AggregationContext aggregationContext, Aggregator parent) {

        super(name, BucketAggregationMode.PER_BUCKET, factories, initialCapacity, aggregationContext, parent);
        this.rounding = rounding;
        this.order = order;
        this.keyed = keyed;
        this.minDocCount = minDocCount;
        this.extendedBounds = extendedBounds;
        this.valuesSource = valuesSource;
        this.histogramFactory = histogramFactory;

        bucketOrds = new LongHash(initialCapacity, aggregationContext.bigArrays());
    }

    @Override
    public boolean shouldCollect() {
        return valuesSource != null;
    }

    @Override
    public void setNextReader(AtomicReaderContext reader) {
        values = valuesSource.longValues();
    }

    @Override
    public void collect(int doc, long owningBucketOrdinal) throws IOException {
        assert owningBucketOrdinal == 0;
        final int valuesCount = values.setDocument(doc);

        long previousKey = Long.MIN_VALUE;
        for (int i = 0; i < valuesCount; ++i) {
            long value = values.nextValue();
            long key = rounding.roundKey(value);
            assert key >= previousKey;
            if (key == previousKey) {
                continue;
            }
            long bucketOrd = bucketOrds.add(key);
            if (bucketOrd < 0) { // already seen
                bucketOrd = -1 - bucketOrd;
            }
            collectBucket(doc, bucketOrd);
            previousKey = key;
        }
    }

    @Override
    public InternalAggregation buildAggregation(long owningBucketOrdinal) {
        assert owningBucketOrdinal == 0;
        List<InternalHistogram.Bucket> buckets = new ArrayList<InternalHistogram.Bucket>((int) bucketOrds.size());
        for (long i = 0; i < bucketOrds.capacity(); ++i) {
            final long ord = bucketOrds.id(i);
            if (ord < 0) {
                continue; // slot is not allocated
            }
            buckets.add(histogramFactory.createBucket(rounding.valueForKey(bucketOrds.key(i)), bucketDocCount(ord), bucketAggregations(ord), valuesSource.formatter()));
        }

        CollectionUtil.introSort(buckets, order.comparator());

        // value source will be null for unmapped fields
        ValueFormatter formatter = valuesSource != null ? valuesSource.formatter() : null;
        InternalHistogram.EmptyBucketInfo emptyBucketInfo = minDocCount == 0 ? new InternalHistogram.EmptyBucketInfo(rounding, buildEmptySubAggregations(), extendedBounds) : null;
        return histogramFactory.create(name, buckets, order, minDocCount, emptyBucketInfo, formatter, keyed);
    }

    @Override
    public InternalAggregation buildEmptyAggregation() {
        ValueFormatter formatter = valuesSource != null ? valuesSource.formatter() : null;
        InternalHistogram.EmptyBucketInfo emptyBucketInfo = minDocCount == 0 ? new InternalHistogram.EmptyBucketInfo(rounding, buildEmptySubAggregations(), extendedBounds) : null;
        return histogramFactory.create(name, Collections.emptyList(), order, minDocCount, emptyBucketInfo, formatter, keyed);
    }

    @Override
    public void doRelease() {
        Releasables.release(bucketOrds);
    }

    public static class Factory extends ValueSourceAggregatorFactory<NumericValuesSource> {

        private final Rounding rounding;
        private final InternalOrder order;
        private final boolean keyed;
        private final long minDocCount;
        private final ExtendedBounds extendedBounds;
        private final InternalHistogram.Factory<?> histogramFactory;

        public Factory(String name, ValuesSourceConfig<NumericValuesSource> valueSourceConfig,
                       Rounding rounding, InternalOrder order, boolean keyed, long minDocCount,
                       ExtendedBounds extendedBounds, InternalHistogram.Factory<?> histogramFactory) {

            super(name, histogramFactory.type(), valueSourceConfig);
            this.rounding = rounding;
            this.order = order;
            this.keyed = keyed;
            this.minDocCount = minDocCount;
            this.extendedBounds = extendedBounds;
            this.histogramFactory = histogramFactory;
        }

        @Override
        protected Aggregator createUnmapped(AggregationContext aggregationContext, Aggregator parent) {
            return new HistogramAggregator(name, factories, rounding, order, keyed, minDocCount, null, null, 0, histogramFactory, aggregationContext, parent);
        }

        @Override
        protected Aggregator create(NumericValuesSource valuesSource, long expectedBucketsCount, AggregationContext aggregationContext, Aggregator parent) {
            // todo if we'll keep track of min/max values in IndexFieldData, we could use the max here to come up with a better estimation for the buckets count

            // we need to round the bounds given by the user and we have to do it for every aggregator we crate
            // as the rounding is not necessarily an idempotent operation.
            // todo we need to think of a better structure to the factory/agtor code so we won't need to do that
            ExtendedBounds roundedBounds = null;
            if (extendedBounds != null) {
                // we need to process & validate here using the parser
                extendedBounds.processAndValidate(name, aggregationContext.searchContext(), valuesSource != null ? valuesSource.parser() : null);
                roundedBounds = extendedBounds.round(rounding);
            }
            return new HistogramAggregator(name, factories, rounding, order, keyed, minDocCount, roundedBounds, valuesSource, 50, histogramFactory, aggregationContext, parent);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7371.java