error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7496.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7496.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7496.java
text:
```scala
public final static B@@ucketCollector NO_OP_COLLECTOR = new BucketCollector() {

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
import org.apache.lucene.index.AtomicReaderContext;
import org.elasticsearch.common.lucene.ReaderContextAware;
import org.elasticsearch.search.aggregations.Aggregator.BucketAggregationMode;

import java.io.IOException;

/**
 * A Collector that can collect data in separate buckets.
 */
public abstract class BucketCollector implements ReaderContextAware {

    public static BucketCollector NO_OP_COLLECTOR = new BucketCollector() {

        @Override
        public void collect(int docId, long bucketOrdinal) throws IOException {
            // no-op
        }

        @Override
        public void setNextReader(AtomicReaderContext reader) {
            // no-op
        }

        @Override
        public void postCollection() throws IOException {
            // no-op
        }
    };

    /**
     * Wrap the given collectors into a single instance.
     */
    public static BucketCollector wrap(Iterable<? extends BucketCollector> collectorList) {
        final BucketCollector[] collectors = Iterables.toArray(collectorList, BucketCollector.class);
        switch (collectors.length) {
            case 0:
                return NO_OP_COLLECTOR;
            case 1:
                return collectors[0];
            default:
                return new BucketCollector() {

                    @Override
                    public void collect(int docId, long bucketOrdinal) throws IOException {
                        for (BucketCollector collector : collectors) {
                            collector.collect(docId, bucketOrdinal);
                        }
                    }

                    @Override
                    public void setNextReader(AtomicReaderContext reader) {
                        for (BucketCollector collector : collectors) {
                            collector.setNextReader(reader);
                        }
                    }

                    @Override
                    public void postCollection() throws IOException {
                        for (BucketCollector collector : collectors) {
                            collector.postCollection();
                        }
                    }

                };
        }
    }

    /**
     * Called during the query phase, to collect & aggregate the given document.
     *
     * @param doc                   The document to be collected/aggregated
     * @param bucketOrdinal         The ordinal of the bucket this aggregator belongs to, assuming this aggregator is not a top level aggregator.
     *                              Typically, aggregators with {@code #bucketAggregationMode} set to {@link BucketAggregationMode#MULTI_BUCKETS}
     *                              will heavily depend on this ordinal. Other aggregators may or may not use it and can see this ordinal as just
     *                              an extra information for the aggregation context. For top level aggregators, the ordinal will always be
     *                              equal to 0.
     * @throws IOException
     */
    public abstract void collect(int docId, long bucketOrdinal) throws IOException;

    /**
     * Post collection callback.
     */
    public abstract void postCollection() throws IOException;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7496.java