error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7499.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7499.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7499.java
text:
```scala
a@@ggUsedForSorting = path.resolveTopmostAggregator(this);

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

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.Explicit;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.aggregations.Aggregator;
import org.elasticsearch.search.aggregations.AggregatorFactories;
import org.elasticsearch.search.aggregations.bucket.BucketsAggregator;
import org.elasticsearch.search.aggregations.bucket.terms.InternalOrder.Aggregation;
import org.elasticsearch.search.aggregations.support.AggregationContext;
import org.elasticsearch.search.aggregations.support.OrderPath;

import java.io.IOException;

public abstract class TermsAggregator extends BucketsAggregator {

    public static class BucketCountThresholds {
        private Explicit<Long> minDocCount;
        private Explicit<Long> shardMinDocCount;
        private Explicit<Integer> requiredSize;
        private Explicit<Integer> shardSize;
        
        public BucketCountThresholds(long minDocCount, long shardMinDocCount, int requiredSize, int shardSize) {
            this.minDocCount = new Explicit<>(minDocCount, false);
            this.shardMinDocCount =  new Explicit<>(shardMinDocCount, false);
            this.requiredSize = new Explicit<>(requiredSize, false);
            this.shardSize = new Explicit<>(shardSize, false);
        }
        public BucketCountThresholds() {
            this(-1, -1, -1, -1);
        }

        public BucketCountThresholds(BucketCountThresholds bucketCountThresholds) {
            this(bucketCountThresholds.minDocCount.value(), bucketCountThresholds.shardMinDocCount.value(), bucketCountThresholds.requiredSize.value(), bucketCountThresholds.shardSize.value());
        }

        public void ensureValidity() {

            if (shardSize.value() == 0) {
                setShardSize(Integer.MAX_VALUE);
            }

            if (requiredSize.value() == 0) {
                setRequiredSize(Integer.MAX_VALUE);
            }
            // shard_size cannot be smaller than size as we need to at least fetch <size> entries from every shards in order to return <size>
            if (shardSize.value() < requiredSize.value()) {
                setShardSize(requiredSize.value());
            }

            // shard_min_doc_count should not be larger than min_doc_count because this can cause buckets to be removed that would match the min_doc_count criteria
            if (shardMinDocCount.value() > minDocCount.value()) {
                setShardMinDocCount(minDocCount.value());
            }

            if (requiredSize.value() < 0 || minDocCount.value() < 0) {
                throw new ElasticsearchException("parameters [requiredSize] and [minDocCount] must be >=0 in terms aggregation.");
            }
        }

        public long getShardMinDocCount() {
            return shardMinDocCount.value();
        }

        public void setShardMinDocCount(long shardMinDocCount) {
            this.shardMinDocCount = new Explicit<>(shardMinDocCount, true);
        }

        public long getMinDocCount() {
            return minDocCount.value();
        }

        public void setMinDocCount(long minDocCount) {
            this.minDocCount = new Explicit<>(minDocCount, true);
        }

        public int getRequiredSize() {
            return requiredSize.value();
        }

        public void setRequiredSize(int requiredSize) {
            this.requiredSize = new Explicit<>(requiredSize, true);
        }

        public int getShardSize() {
            return shardSize.value();
        }

        public void setShardSize(int shardSize) {
            this.shardSize = new Explicit<>(shardSize, true);
        }

        public void toXContent(XContentBuilder builder) throws IOException {
            if (requiredSize.explicit()) {
                builder.field(AbstractTermsParametersParser.REQUIRED_SIZE_FIELD_NAME.getPreferredName(), requiredSize.value());
            }
            if (shardSize.explicit()) {
                builder.field(AbstractTermsParametersParser.SHARD_SIZE_FIELD_NAME.getPreferredName(), shardSize.value());
            }
            if (minDocCount.explicit()) {
                builder.field(AbstractTermsParametersParser.MIN_DOC_COUNT_FIELD_NAME.getPreferredName(), minDocCount.value());
            }
            if (shardMinDocCount.explicit()) {
                builder.field(AbstractTermsParametersParser.SHARD_MIN_DOC_COUNT_FIELD_NAME.getPreferredName(), shardMinDocCount.value());
            }
        }
    }

    protected final BucketCountThresholds bucketCountThresholds;
    protected InternalOrder order;
    protected Aggregator aggUsedForSorting;
    protected SubAggCollectionMode subAggCollectMode;

    public TermsAggregator(String name, BucketAggregationMode bucketAggregationMode, AggregatorFactories factories, long estimatedBucketsCount, AggregationContext context, Aggregator parent, BucketCountThresholds bucketCountThresholds, InternalOrder order, SubAggCollectionMode subAggCollectMode) {
        super(name, bucketAggregationMode, factories, estimatedBucketsCount, context, parent);
        this.bucketCountThresholds = bucketCountThresholds;
        this.order = InternalOrder.validate(order, this);
        this.subAggCollectMode = subAggCollectMode;
        // Don't defer any child agg if we are dependent on it for pruning results
        if (order instanceof Aggregation){
            OrderPath path = ((Aggregation) order).path();
            aggUsedForSorting = path.resolveTopmostAggregator(this, false);
        }
    }

    @Override
    protected boolean shouldDefer(Aggregator aggregator) {
        return (subAggCollectMode == SubAggCollectionMode.BREADTH_FIRST) && (aggregator != aggUsedForSorting);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7499.java