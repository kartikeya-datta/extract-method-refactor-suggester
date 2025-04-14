error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7375.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7375.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7375.java
text:
```scala
n@@ew ArrayList<>(ranges.size());

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
package org.elasticsearch.search.aggregations.bucket.range;

import com.google.common.collect.Lists;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.util.InPlaceMergeSorter;
import org.elasticsearch.index.fielddata.DoubleValues;
import org.elasticsearch.search.aggregations.Aggregator;
import org.elasticsearch.search.aggregations.AggregatorFactories;
import org.elasticsearch.search.aggregations.InternalAggregation;
import org.elasticsearch.search.aggregations.InternalAggregations;
import org.elasticsearch.search.aggregations.bucket.BucketsAggregator;
import org.elasticsearch.search.aggregations.support.AggregationContext;
import org.elasticsearch.search.aggregations.support.ValueSourceAggregatorFactory;
import org.elasticsearch.search.aggregations.support.ValuesSourceConfig;
import org.elasticsearch.search.aggregations.support.numeric.NumericValuesSource;
import org.elasticsearch.search.aggregations.support.numeric.ValueFormatter;
import org.elasticsearch.search.aggregations.support.numeric.ValueParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RangeAggregator extends BucketsAggregator {

    public static class Range {

        public String key;
        public double from = Double.NEGATIVE_INFINITY;
        String fromAsStr;
        public double to = Double.POSITIVE_INFINITY;
        String toAsStr;

        public Range(String key, double from, String fromAsStr, double to, String toAsStr) {
            this.key = key;
            this.from = from;
            this.fromAsStr = fromAsStr;
            this.to = to;
            this.toAsStr = toAsStr;
        }

        boolean matches(double value) {
            return value >= from && value < to;
        }

        @Override
        public String toString() {
            return "[" + from + " to " + to + ")";
        }

        public void process(ValueParser parser, AggregationContext aggregationContext) {
            if (fromAsStr != null) {
                from = parser != null ? parser.parseDouble(fromAsStr, aggregationContext.searchContext()) : Double.valueOf(fromAsStr);
            }
            if (toAsStr != null) {
                to = parser != null ? parser.parseDouble(toAsStr, aggregationContext.searchContext()) : Double.valueOf(toAsStr);
            }
        }
    }

    private final NumericValuesSource valuesSource;
    private final Range[] ranges;
    private final boolean keyed;
    private final InternalRange.Factory rangeFactory;
    private DoubleValues values;

    final double[] maxTo;

    public RangeAggregator(String name,
                           AggregatorFactories factories,
                           NumericValuesSource valuesSource,
                           InternalRange.Factory rangeFactory,
                           List<Range> ranges,
                           boolean keyed,
                           AggregationContext aggregationContext,
                           Aggregator parent) {

        super(name, BucketAggregationMode.MULTI_BUCKETS, factories, ranges.size() * (parent == null ? 1 : parent.estimatedBucketCount()), aggregationContext, parent);
        assert valuesSource != null;
        this.valuesSource = valuesSource;
        this.keyed = keyed;
        this.rangeFactory = rangeFactory;
        this.ranges = ranges.toArray(new Range[ranges.size()]);
        for (int i = 0; i < this.ranges.length; i++) {
            this.ranges[i].process(valuesSource.parser(), context);
        }
        sortRanges(this.ranges);

        maxTo = new double[this.ranges.length];
        maxTo[0] = this.ranges[0].to;
        for (int i = 1; i < this.ranges.length; ++i) {
            maxTo[i] = Math.max(this.ranges[i].to,maxTo[i-1]);
        }

    }

    @Override
    public boolean shouldCollect() {
        return true;
    }

    @Override
    public void setNextReader(AtomicReaderContext reader) {
        values = valuesSource.doubleValues();
    }

    private final long subBucketOrdinal(long owningBucketOrdinal, int rangeOrd) {
        return owningBucketOrdinal * ranges.length + rangeOrd;
    }

    @Override
    public void collect(int doc, long owningBucketOrdinal) throws IOException {
        final int valuesCount = values.setDocument(doc);
        for (int i = 0, lo = 0; i < valuesCount; ++i) {
            final double value = values.nextValue();
            lo = collect(doc, value, owningBucketOrdinal, lo);
        }
    }

    private int collect(int doc, double value, long owningBucketOrdinal, int lowBound) throws IOException {
        int lo = lowBound, hi = ranges.length - 1; // all candidates are between these indexes
        int mid = (lo + hi) >>> 1;
        while (lo <= hi) {
            if (value < ranges[mid].from) {
                hi = mid - 1;
            } else if (value >= maxTo[mid]) {
                lo = mid + 1;
            } else {
                break;
            }
            mid = (lo + hi) >>> 1;
        }
        if (lo > hi) return lo; // no potential candidate

        // binary search the lower bound
        int startLo = lo, startHi = mid;
        while (startLo <= startHi) {
            final int startMid = (startLo + startHi) >>> 1;
            if (value >= maxTo[startMid]) {
                startLo = startMid + 1;
            } else {
                startHi = startMid - 1;
            }
        }

        // binary search the upper bound
        int endLo = mid, endHi = hi;
        while (endLo <= endHi) {
            final int endMid = (endLo + endHi) >>> 1;
            if (value < ranges[endMid].from) {
                endHi = endMid - 1;
            } else {
                endLo = endMid + 1;
            }
        }

        assert startLo == lowBound || value >= maxTo[startLo - 1];
        assert endHi == ranges.length - 1 || value < ranges[endHi + 1].from;

        for (int i = startLo; i <= endHi; ++i) {
            if (ranges[i].matches(value)) {
                collectBucket(doc, subBucketOrdinal(owningBucketOrdinal, i));
            }
        }

        return endHi + 1;
    }

    @Override
    public InternalAggregation buildAggregation(long owningBucketOrdinal) {
        List<org.elasticsearch.search.aggregations.bucket.range.Range.Bucket> buckets = Lists.newArrayListWithCapacity(ranges.length);
        for (int i = 0; i < ranges.length; i++) {
            Range range = ranges[i];
            final long bucketOrd = subBucketOrdinal(owningBucketOrdinal, i);
            org.elasticsearch.search.aggregations.bucket.range.Range.Bucket bucket = rangeFactory.createBucket(
                    range.key, range.from, range.to, bucketDocCount(bucketOrd),bucketAggregations(bucketOrd), valuesSource.formatter());
            buckets.add(bucket);
        }
        // value source can be null in the case of unmapped fields
        ValueFormatter formatter = valuesSource != null ? valuesSource.formatter() : null;
        return rangeFactory.create(name, buckets, formatter, keyed, false);
    }

    @Override
    public InternalAggregation buildEmptyAggregation() {
        InternalAggregations subAggs = buildEmptySubAggregations();
        List<org.elasticsearch.search.aggregations.bucket.range.Range.Bucket> buckets = Lists.newArrayListWithCapacity(ranges.length);
        for (int i = 0; i < ranges.length; i++) {
            Range range = ranges[i];
            org.elasticsearch.search.aggregations.bucket.range.Range.Bucket bucket = rangeFactory.createBucket(
                    range.key, range.from, range.to, 0, subAggs, valuesSource.formatter());
            buckets.add(bucket);
        }
        // value source can be null in the case of unmapped fields
        ValueFormatter formatter = valuesSource != null ? valuesSource.formatter() : null;
        return rangeFactory.create(name, buckets, formatter, keyed, false);
    }

    private static final void sortRanges(final Range[] ranges) {
        new InPlaceMergeSorter() {

            @Override
            protected void swap(int i, int j) {
                final Range tmp = ranges[i];
                ranges[i] = ranges[j];
                ranges[j] = tmp;
            }

            @Override
            protected int compare(int i, int j) {
                int cmp = Double.compare(ranges[i].from, ranges[j].from);
                if (cmp == 0) {
                    cmp = Double.compare(ranges[i].to, ranges[j].to);
                }
                return cmp;
            }
        }.sort(0, ranges.length);
    }

    public static class Unmapped extends Aggregator {

        private final List<RangeAggregator.Range> ranges;
        private final boolean keyed;
        private final InternalRange.Factory factory;
        private final ValueFormatter formatter;
        private final ValueParser parser;

        public Unmapped(String name,
                        List<RangeAggregator.Range> ranges,
                        boolean keyed,
                        ValueFormatter formatter,
                        ValueParser parser,
                        AggregationContext aggregationContext,
                        Aggregator parent,
                        InternalRange.Factory factory) {

            super(name, BucketAggregationMode.MULTI_BUCKETS, AggregatorFactories.EMPTY, 0, aggregationContext, parent);
            this.ranges = ranges;
            for (Range range : this.ranges) {
                range.process(parser, context);
            }
            this.keyed = keyed;
            this.formatter = formatter;
            this.parser = parser;
            this.factory = factory;
        }

        @Override
        public boolean shouldCollect() {
            return false;
        }

        @Override
        public void setNextReader(AtomicReaderContext reader) {
        }

        @Override
        public void collect(int doc, long owningBucketOrdinal) throws IOException {
        }

        @Override
        public InternalRange buildAggregation(long owningBucketOrdinal) {
            return buildEmptyAggregation();
        }

        @Override
        public InternalRange buildEmptyAggregation() {
            InternalAggregations subAggs = buildEmptySubAggregations();
            List<org.elasticsearch.search.aggregations.bucket.range.Range.Bucket> buckets =
                    new ArrayList<org.elasticsearch.search.aggregations.bucket.range.Range.Bucket>(ranges.size());
            for (RangeAggregator.Range range : ranges) {
                buckets.add(factory.createBucket(range.key, range.from, range.to, 0, subAggs, formatter));
            }
            return factory.create(name, buckets, formatter, keyed, true);
        }
    }

    public static class Factory extends ValueSourceAggregatorFactory<NumericValuesSource> {

        private final InternalRange.Factory rangeFactory;
        private final List<Range> ranges;
        private final boolean keyed;

        public Factory(String name, ValuesSourceConfig<NumericValuesSource> valueSourceConfig, InternalRange.Factory rangeFactory, List<Range> ranges, boolean keyed) {
            super(name, rangeFactory.type(), valueSourceConfig);
            this.rangeFactory = rangeFactory;
            this.ranges = ranges;
            this.keyed = keyed;
        }

        @Override
        protected Aggregator createUnmapped(AggregationContext aggregationContext, Aggregator parent) {
            return new Unmapped(name, ranges, keyed, valuesSourceConfig.formatter(), valuesSourceConfig.parser(), aggregationContext, parent, rangeFactory);
        }

        @Override
        protected Aggregator create(NumericValuesSource valuesSource, long expectedBucketsCount, AggregationContext aggregationContext, Aggregator parent) {
            return new RangeAggregator(name, factories, valuesSource, rangeFactory, ranges, keyed, aggregationContext, parent);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7375.java