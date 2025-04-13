error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7776.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7776.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7776.java
text:
```scala
final S@@inglePassStatistics single = new SinglePassStatistics();

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

package org.elasticsearch.action.bench;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentBuilderString;
import org.elasticsearch.common.xcontent.XContentHelper;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a single iteration of a search benchmark competition
 */
public class CompetitionIteration implements Streamable {

    private long numQueries;
    private SlowRequest[] slowRequests;
    private long totalTime;
    private long sum;
    private long sumTotalHits;
    private double stddev;
    private long min;
    private long max;
    private double mean;
    private double qps;
    private double millisPerHit;
    private double[] percentiles = new double[0];
    private Map<Double, Double> percentileValues = new TreeMap<>();

    private CompetitionIterationData iterationData;

    public CompetitionIteration() { }

    public CompetitionIteration(SlowRequest[] slowestRequests, long totalTime, long numQueries, long sumTotalHits,
                                CompetitionIterationData iterationData) {
        this.totalTime = totalTime;
        this.sumTotalHits = sumTotalHits;
        this.slowRequests = slowestRequests;
        this.numQueries = numQueries;
        this.iterationData = iterationData;
        this.millisPerHit = totalTime / (double)sumTotalHits;
    }

    public void computeStatistics() {

        SinglePassStatistics single = new SinglePassStatistics();

        for (long datum : iterationData.data()) {
            if (datum > -1) {   // ignore unset values in the underlying array
                single.push(datum);
            }
        }

        sum = single.sum();
        stddev = single.stddev();
        min = single.min();
        max = single.max();
        mean = single.mean();
        qps = numQueries * (1000.d / (double) sum);

        for (double percentile : percentiles) {
            percentileValues.put(percentile, single.percentile(percentile / 100));
        }
    }

    public CompetitionIterationData competitionIterationData() {
        return iterationData;
    }

    public long numQueries() {
        return numQueries;
    }

    public long totalTime() {
        return totalTime;
    }

    public long sumTotalHits() {
        return sumTotalHits;
    }

    public double millisPerHit() {
        return millisPerHit;
    }

    public double queriesPerSecond() {
        return qps;
    }

    public SlowRequest[] slowRequests() {
        return slowRequests;
    }

    public long min() {
        return min;
    }

    public long max() {
        return max;
    }

    public double mean() {
        return mean;
    }

    public Map<Double, Double> percentileValues() {
        return percentileValues;
    }

    public void percentiles(double[] percentiles) {
        this.percentiles = percentiles;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        totalTime = in.readVLong();
        sumTotalHits = in.readVLong();
        numQueries = in.readVLong();
        millisPerHit = in.readDouble();
        iterationData = in.readOptionalStreamable(new CompetitionIterationData());
        int size = in.readVInt();
        slowRequests = new SlowRequest[size];
        for (int i = 0; i < size; i++) {
            slowRequests[i] = new SlowRequest();
            slowRequests[i].readFrom(in);
        }
        percentiles = in.readDoubleArray();
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeVLong(totalTime);
        out.writeVLong(sumTotalHits);
        out.writeVLong(numQueries);
        out.writeDouble(millisPerHit);
        out.writeOptionalStreamable(iterationData);
        out.writeVInt(slowRequests == null ? 0 : slowRequests.length);
        if (slowRequests != null) {
            for (SlowRequest slowRequest : slowRequests) {
                slowRequest.writeTo(out);
            }
        }
        out.writeDoubleArray(percentiles);
    }

    /**
     * Represents a 'slow' search request
     */
    public static class SlowRequest implements ToXContent, Streamable {

        private long maxTimeTaken;
        private long avgTimeTaken;
        private SearchRequest searchRequest;

        public SlowRequest() { }

        public SlowRequest(long avgTimeTaken, long maxTimeTaken, SearchRequest searchRequest) {
            this.avgTimeTaken = avgTimeTaken;
            this.maxTimeTaken = maxTimeTaken;
            this.searchRequest = searchRequest;
        }

        public long avgTimeTaken() {
            return avgTimeTaken;
        }

        public long maxTimeTaken() {
            return maxTimeTaken;
        }

        public SearchRequest searchRequest() {
            return searchRequest;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            avgTimeTaken = in.readVLong();
            maxTimeTaken = in.readVLong();
            searchRequest = new SearchRequest();
            searchRequest.readFrom(in);
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            out.writeVLong(avgTimeTaken);
            out.writeVLong(maxTimeTaken);
            searchRequest.writeTo(out);
        }

        @Override
        public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
            builder.field(Fields.MAX_TIME, maxTimeTaken);
            builder.field(Fields.AVG_TIME, avgTimeTaken);
            XContentHelper.writeRawField("request", searchRequest.source(), builder, params);
            return builder;
        }
    }

    static final class Fields {
        static final XContentBuilderString MAX_TIME = new XContentBuilderString("max_time");
        static final XContentBuilderString AVG_TIME = new XContentBuilderString("avg_time");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7776.java