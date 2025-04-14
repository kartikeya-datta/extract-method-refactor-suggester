error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7373.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7373.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7373.java
text:
```scala
b@@ucketsMap = new ObjectObjectOpenHashMap<>();

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

import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.index.mapper.core.DateFieldMapper;
import org.elasticsearch.search.aggregations.AggregationStreams;
import org.elasticsearch.search.aggregations.InternalAggregations;
import org.elasticsearch.search.aggregations.support.numeric.ValueFormatter;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public class InternalDateHistogram extends InternalHistogram<InternalDateHistogram.Bucket> implements DateHistogram {

    final static Type TYPE = new Type("date_histogram", "dhisto");
    final static Factory FACTORY = new Factory();

    private final static AggregationStreams.Stream STREAM = new AggregationStreams.Stream() {
        @Override
        public InternalDateHistogram readResult(StreamInput in) throws IOException {
            InternalDateHistogram histogram = new InternalDateHistogram();
            histogram.readFrom(in);
            return histogram;
        }
    };

    public static void registerStream() {
        AggregationStreams.registerStream(STREAM, TYPE.stream());
    }

    static class Bucket extends InternalHistogram.Bucket implements DateHistogram.Bucket {

        private final ValueFormatter formatter;

        Bucket(long key, long docCount, InternalAggregations aggregations, ValueFormatter formatter) {
            super(key, docCount, aggregations);
            this.formatter = formatter;
        }

        @Override
        public String getKey() {
            return formatter != null ? formatter.format(key) : DateFieldMapper.Defaults.DATE_TIME_FORMATTER.printer().print(key);
        }

        @Override
        public DateTime getKeyAsDate() {
            return new DateTime(key, DateTimeZone.UTC);
        }

        @Override
        public String toString() {
            return getKey();
        }
    }

    static class Factory extends InternalHistogram.Factory<InternalDateHistogram.Bucket> {

        private Factory() {
        }

        @Override
        public String type() {
            return TYPE.name();
        }

        @Override
        public InternalDateHistogram create(String name, List<InternalDateHistogram.Bucket> buckets, InternalOrder order,
                                            long minDocCount, EmptyBucketInfo emptyBucketInfo, ValueFormatter formatter, boolean keyed) {
            return new InternalDateHistogram(name, buckets, order, minDocCount, emptyBucketInfo, formatter, keyed);
        }

        @Override
        public InternalDateHistogram.Bucket createBucket(long key, long docCount, InternalAggregations aggregations, ValueFormatter formatter) {
            return new Bucket(key, docCount, aggregations, formatter);
        }
    }

    private ObjectObjectOpenHashMap<String, InternalDateHistogram.Bucket> bucketsMap;

    InternalDateHistogram() {} // for serialization

    InternalDateHistogram(String name, List<InternalDateHistogram.Bucket> buckets, InternalOrder order, long minDocCount,
                          EmptyBucketInfo emptyBucketInfo, ValueFormatter formatter, boolean keyed) {
        super(name, buckets, order, minDocCount, emptyBucketInfo, formatter, keyed);
    }

    @Override
    public Type type() {
        return TYPE;
    }

    @Override
    public Bucket getBucketByKey(String key) {
        try {
            long time = Long.parseLong(key);
            return super.getBucketByKey(time);
        } catch (NumberFormatException nfe) {
            // it's not a number, so lets try to parse it as a date using the formatter.
        }
        if (bucketsMap == null) {
            bucketsMap = new ObjectObjectOpenHashMap<String, InternalDateHistogram.Bucket>();
            for (InternalDateHistogram.Bucket bucket : buckets) {
                bucketsMap.put(bucket.getKey(), bucket);
            }
        }
        return bucketsMap.get(key);
    }

    @Override
    public DateHistogram.Bucket getBucketByKey(DateTime key) {
        return getBucketByKey(key.getMillis());
    }

    @Override
    protected InternalDateHistogram.Bucket createBucket(long key, long docCount, InternalAggregations aggregations, ValueFormatter formatter) {
        return new Bucket(key, docCount, aggregations, formatter);
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        bucketsMap = null; // we need to reset this on read (as it's lazily created on demand)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7373.java