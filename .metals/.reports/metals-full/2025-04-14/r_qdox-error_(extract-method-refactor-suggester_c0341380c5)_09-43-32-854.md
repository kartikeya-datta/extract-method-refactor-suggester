error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8542.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8542.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8542.java
text:
```scala
private A@@ggregatorFactory[] factories;

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
package org.elasticsearch.search.aggregations;

import com.google.common.collect.Iterables;
import com.google.common.collect.UnmodifiableIterator;
import org.apache.lucene.index.AtomicReaderContext;
import org.elasticsearch.common.lease.Releasables;
import org.elasticsearch.common.util.ObjectArray;
import org.elasticsearch.search.aggregations.Aggregator.BucketAggregationMode;
import org.elasticsearch.search.aggregations.support.AggregationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class AggregatorFactories {

    public static final AggregatorFactories EMPTY = new Empty();

    private final AggregatorFactory[] factories;

    public static Builder builder() {
        return new Builder();
    }

    private AggregatorFactories(AggregatorFactory[] factories) {
        this.factories = factories;
    }

    private static Aggregator createAndRegisterContextAware(AggregationContext context, AggregatorFactory factory, Aggregator parent, long estimatedBucketsCount) {
        final Aggregator aggregator = factory.create(context, parent, estimatedBucketsCount);
        if (aggregator.shouldCollect()) {
            context.registerReaderContextAware(aggregator);
        }
        return aggregator;
    }

    /**
     * Create all aggregators so that they can be consumed with multiple buckets.
     */
    public Aggregator[] createSubAggregators(Aggregator parent, final long estimatedBucketsCount) {
        Aggregator[] aggregators = new Aggregator[count()];
        for (int i = 0; i < factories.length; ++i) {
            final AggregatorFactory factory = factories[i];
            final Aggregator first = createAndRegisterContextAware(parent.context(), factory, parent, estimatedBucketsCount);
            if (first.bucketAggregationMode() == BucketAggregationMode.MULTI_BUCKETS) {
                // This aggregator already supports multiple bucket ordinals, can be used directly
                aggregators[i] = first;
                continue;
            }
            // the aggregator doesn't support multiple ordinals, let's wrap it so that it does.
            aggregators[i] = new Aggregator(first.name(), BucketAggregationMode.MULTI_BUCKETS, AggregatorFactories.EMPTY, 1, first.context(), first.parent()) {

                ObjectArray<Aggregator> aggregators;

                {
                    // if estimated count is zero, we at least create a single aggregator.
                    // The estimated count is just an estimation and we can't rely on how it's estimated (that is, an
                    // estimation of 0 should not imply that we'll end up without any buckets)
                    long arraySize = estimatedBucketsCount > 0 ?  estimatedBucketsCount : 1;
                    aggregators = bigArrays.newObjectArray(arraySize);
                    aggregators.set(0, first);
                    for (long i = 1; i < arraySize; ++i) {
                        aggregators.set(i, createAndRegisterContextAware(parent.context(), factory, parent, estimatedBucketsCount));
                    }
                }

                @Override
                public boolean shouldCollect() {
                    return first.shouldCollect();
                }

                @Override
                protected void doPostCollection() {
                    for (long i = 0; i < aggregators.size(); ++i) {
                        final Aggregator aggregator = aggregators.get(i);
                        if (aggregator != null) {
                            aggregator.postCollection();
                        }
                    }
                }

                @Override
                public void collect(int doc, long owningBucketOrdinal) throws IOException {
                    aggregators = bigArrays.grow(aggregators, owningBucketOrdinal + 1);
                    Aggregator aggregator = aggregators.get(owningBucketOrdinal);
                    if (aggregator == null) {
                        aggregator = createAndRegisterContextAware(parent.context(), factory, parent, estimatedBucketsCount);
                        aggregators.set(owningBucketOrdinal, aggregator);
                    }
                    aggregator.collect(doc, 0);
                }

                @Override
                public void setNextReader(AtomicReaderContext reader) {
                }

                @Override
                public InternalAggregation buildAggregation(long owningBucketOrdinal) {
                    // The bucket ordinal may be out of range in case of eg. a terms/filter/terms where
                    // the filter matches no document in the highest buckets of the first terms agg
                    if (owningBucketOrdinal >= aggregators.size() || aggregators.get(owningBucketOrdinal) == null) {
                        return first.buildEmptyAggregation();
                    } else {
                        return aggregators.get(owningBucketOrdinal).buildAggregation(0);
                    }
                }

                @Override
                public InternalAggregation buildEmptyAggregation() {
                    return first.buildEmptyAggregation();
                }

                @Override
                public void doRelease() {
                    final Iterable<Aggregator> aggregatorsIter = new Iterable<Aggregator>() {

                        @Override
                        public Iterator<Aggregator> iterator() {
                            return new UnmodifiableIterator<Aggregator>() {

                                long i = 0;

                                @Override
                                public boolean hasNext() {
                                    return i < aggregators.size();
                                }

                                @Override
                                public Aggregator next() {
                                    return aggregators.get(i++);
                                }

                            };
                        }

                    };
                    Releasables.release(Iterables.concat(aggregatorsIter, Collections.singleton(aggregators)));
                }
            };
        }
        return aggregators;
    }

    public Aggregator[] createTopLevelAggregators(AggregationContext ctx) {
        // These aggregators are going to be used with a single bucket ordinal, no need to wrap the PER_BUCKET ones
        Aggregator[] aggregators = new Aggregator[factories.length];
        for (int i = 0; i < factories.length; i++) {
            aggregators[i] = createAndRegisterContextAware(ctx, factories[i], null, 0);
        }
        return aggregators;
    }

    public int count() {
        return factories.length;
    }

    void setParent(AggregatorFactory parent) {
        for (AggregatorFactory factory : factories) {
            factory.parent = parent;
        }
    }

    public void validate() {
        for (AggregatorFactory factory : factories) {
            factory.validate();
        }
    }

    private final static class Empty extends AggregatorFactories {

        private static final AggregatorFactory[] EMPTY_FACTORIES = new AggregatorFactory[0];
        private static final Aggregator[] EMPTY_AGGREGATORS = new Aggregator[0];

        private Empty() {
            super(EMPTY_FACTORIES);
        }

        @Override
        public Aggregator[] createSubAggregators(Aggregator parent, long estimatedBucketsCount) {
            return EMPTY_AGGREGATORS;
        }

        @Override
        public Aggregator[] createTopLevelAggregators(AggregationContext ctx) {
            return EMPTY_AGGREGATORS;
        }

    }

    public static class Builder {

        private List<AggregatorFactory> factories = new ArrayList<>();

        public Builder add(AggregatorFactory factory) {
            factories.add(factory);
            return this;
        }

        public AggregatorFactories build() {
            if (factories.isEmpty()) {
                return EMPTY;
            }
            return new AggregatorFactories(factories.toArray(new AggregatorFactory[factories.size()]));
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8542.java