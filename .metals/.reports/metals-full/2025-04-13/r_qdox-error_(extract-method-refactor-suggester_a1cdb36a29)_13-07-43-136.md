error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3728.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3728.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3728.java
text:
```scala
t@@hrow new SearchSourceBuilderException("[interval] must be defined for histogram aggregation [" + name + "]");

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

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.aggregations.ValuesSourceAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilderException;

import java.io.IOException;

/**
 * A builder for a histogram aggregation.
 */
public class HistogramBuilder extends ValuesSourceAggregationBuilder<HistogramBuilder> {

    private Long interval;
    private Histogram.Order order;
    private Long minDocCount;
    private Long extendedBoundsMin;
    private Long extendedBoundsMax;
    private Long preOffset;
    private Long postOffset;

    /**
     * Constructs a new histogram aggregation builder.
     *
     * @param name  The name of the aggregation (will serve as the unique identifier for the aggregation result in the response)
     */
    public HistogramBuilder(String name) {
        super(name, InternalHistogram.TYPE.name());
    }

    /**
     * Sets the interval for the histogram.
     *
     * @param interval  The interval for the histogram
     * @return          This builder
     */
    public HistogramBuilder interval(long interval) {
        this.interval = interval;
        return this;
    }

    /**
     * Sets the order by which the buckets will be returned.
     *
     * @param order The order by which the buckets will be returned
     * @return      This builder
     */
    public HistogramBuilder order(Histogram.Order order) {
        this.order = order;
        return this;
    }

    /**
     * Sets the minimum document count per bucket. Buckets with less documents than this min value will not be returned.
     *
     * @param minDocCount   The minimum document count per bucket
     * @return              This builder
     */
    public HistogramBuilder minDocCount(long minDocCount) {
        this.minDocCount = minDocCount;
        return this;
    }

    public HistogramBuilder extendedBounds(Long min, Long max) {
        extendedBoundsMin = min;
        extendedBoundsMax = max;
        return this;
    }
    
    public HistogramBuilder preOffset(long preOffset) {
        this.preOffset = preOffset;
        return this;
    }
    
    public HistogramBuilder postOffset(long postOffset) {
        this.postOffset = postOffset;
        return this;
    }

    @Override
    protected XContentBuilder doInternalXContent(XContentBuilder builder, Params params) throws IOException {
        if (interval == null) {
            throw new SearchSourceBuilderException("[interval] must be defined for histogram aggregation [" + getName() + "]");
        }
        builder.field("interval", interval);

        if (order != null) {
            builder.field("order");
            order.toXContent(builder, params);
        }

        if (preOffset != null) {
            builder.field("pre_offset", preOffset);
        }

        if (postOffset != null) {
            builder.field("post_offset", postOffset);
        }


        if (minDocCount != null) {
            builder.field("min_doc_count", minDocCount);
        }

        if (extendedBoundsMin != null || extendedBoundsMax != null) {
            builder.startObject(HistogramParser.EXTENDED_BOUNDS.getPreferredName());
            if (extendedBoundsMin != null) {
                builder.field("min", extendedBoundsMin);
            }
            if (extendedBoundsMax != null) {
                builder.field("max", extendedBoundsMax);
            }
            builder.endObject();
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3728.java