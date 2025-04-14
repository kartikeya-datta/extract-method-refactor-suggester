error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15707.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15707.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,18]

error in qdox parser
file content:
```java
offset: 18
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15707.java
text:
```scala
private volatile S@@ample currentSample;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.visualizers;

import java.util.Map;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.math.StatCalculatorLong;

/**
 * Aggegate sample data container. Just instantiate a new instance of this
 * class, and then call {@link #addSample(SampleResult)} a few times, and pull
 * the stats out with whatever methods you prefer.
 *
 */
public class SamplingStatCalculator {
    private final StatCalculatorLong calculator = new StatCalculatorLong();

    private double maxThroughput;

    private long firstTime;

    private String label;

    private Sample currentSample;

    public SamplingStatCalculator(){ // Only for use by test code
        this("");
    }

    public SamplingStatCalculator(String label) {
        this.label = label;
        init();
    }

    private void init() {
        firstTime = Long.MAX_VALUE;
        calculator.clear();
        maxThroughput = Double.MIN_VALUE;
        currentSample = new Sample();
    }

    /**
     * Clear the counters (useful for differential stats)
     *
     */
    public synchronized void clear() {
        init();
    }

    public Sample getCurrentSample() {
        return currentSample;
    }

    /**
     * Get the elapsed time for the samples
     *
     * @return how long the samples took
     */
    public long getElapsed() {
        if (getCurrentSample().getEndTime() == 0) {
            return 0;// No samples collected ...
        }
        return getCurrentSample().getEndTime() - firstTime;
    }

    /**
     * Returns the throughput associated to this sampler in requests per second.
     * May be slightly skewed because it takes the timestamps of the first and
     * last samples as the total time passed, and the test may actually have
     * started before that start time and ended after that end time.
     */
    public double getRate() {
        if (calculator.getCount() == 0) {
            return 0.0; // Better behaviour when howLong=0 or lastTime=0
        }

        return getCurrentSample().getThroughput();
    }

    /**
     * Throughput in bytes / second
     *
     * @return throughput in bytes/second
     */
    public double getBytesPerSecond() {
        // Code duplicated from getPageSize()
        double rate = 0;
        if (this.getElapsed() > 0 && calculator.getTotalBytes() > 0) {
            rate = calculator.getTotalBytes() / ((double) this.getElapsed() / 1000);
        }
        if (rate < 0) {
            rate = 0;
        }
        return rate;
    }

    /**
     * Throughput in kilobytes / second
     *
     * @return Throughput in kilobytes / second
     */
    public double getKBPerSecond() {
        return getBytesPerSecond() / 1024; // 1024=bytes per kb
    }

    /**
     * calculates the average page size, which means divide the bytes by number
     * of samples.
     *
     * @return average page size in bytes (0 if sample count is zero)
     */
    public double getAvgPageBytes() {
        long count = calculator.getCount();
        if (count == 0) {
            return 0;
        }
        return calculator.getTotalBytes() / (double) count;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Records a sample.
     *
     */
    public Sample addSample(SampleResult res) {
        long rtime, cmean, cstdv, cmedian, cpercent, eCount, endTime;
        double throughput;
        boolean rbool;
        synchronized (calculator) {
            calculator.addValue(res.getTime(), res.getSampleCount());
            calculator.addBytes(res.getBytes());
            setStartTime(res);
            eCount = getCurrentSample().getErrorCount();
            if (!res.isSuccessful()) {
                eCount++;
            }
            endTime = getEndTime(res);
            long howLongRunning = endTime - firstTime;
            throughput = ((double) calculator.getCount() / (double) howLongRunning) * 1000.0;
            if (throughput > maxThroughput) {
                maxThroughput = throughput;
            }

            rtime = res.getTime();
            cmean = (long)calculator.getMean();
            cstdv = (long)calculator.getStandardDeviation();
            cmedian = calculator.getMedian().longValue();
            cpercent = calculator.getPercentPoint( 0.500 ).longValue();
// TODO cpercent is the same as cmedian here - why? and why pass it to "distributionLine"?
            rbool = res.isSuccessful();
        }

        long count = calculator.getCount();
        Sample s =
            new Sample( null, rtime, cmean, cstdv, cmedian, cpercent, throughput, eCount, rbool, count, endTime );
        currentSample = s;
        return s;
    }

    private long getEndTime(SampleResult res) {
        long endTime = res.getEndTime();
        long lastTime = getCurrentSample().getEndTime();
        if (lastTime < endTime) {
            lastTime = endTime;
        }
        return lastTime;
    }

    /**
     * @param res
     */
    private void setStartTime(SampleResult res) {
        long startTime = res.getStartTime();
        if (firstTime > startTime) {
            // this is our first sample, set the start time to current timestamp
            firstTime = startTime;
        }
    }

    /**
     * Returns the raw double value of the percentage of samples with errors
     * that were recorded. (Between 0.0 and 1.0)
     *
     * @return the raw double value of the percentage of samples with errors
     *         that were recorded.
     */
    public double getErrorPercentage() {
        double rval = 0.0;

        if (calculator.getCount() == 0) {
            return (rval);
        }
        rval = (double) getCurrentSample().getErrorCount() / (double) calculator.getCount();
        return (rval);
    }

    /**
     * For debugging purposes, only.
     */
    @Override
    public String toString() {
        StringBuilder mySB = new StringBuilder();

        mySB.append("Samples: " + this.getCount() + "  ");
        mySB.append("Avg: " + this.getMean() + "  ");
        mySB.append("Min: " + this.getMin() + "  ");
        mySB.append("Max: " + this.getMax() + "  ");
        mySB.append("Error Rate: " + this.getErrorPercentage() + "  ");
        mySB.append("Sample Rate: " + this.getRate());
        return (mySB.toString());
    }

    /**
     * @return errorCount
     */
    public long getErrorCount() {
        return getCurrentSample().getErrorCount();
    }

    /**
     * @return Returns the maxThroughput.
     */
    public double getMaxThroughput() {
        return maxThroughput;
    }

    public Map<Number, Number[]> getDistribution() {
        return calculator.getDistribution();
    }

    public Number getPercentPoint(double percent) {
        return calculator.getPercentPoint(percent);
    }

    public long getCount() {
        return calculator.getCount();
    }

    public Number getMax() {
        return calculator.getMax();
    }

    public double getMean() {
        return calculator.getMean();
    }

    public Number getMeanAsNumber() {
        return Long.valueOf((long) calculator.getMean());
    }

    public Number getMedian() {
        return calculator.getMedian();
    }

    public Number getMin() {
        if (calculator.getMin().longValue() < 0) {
            return Long.valueOf(0);
        }
        return calculator.getMin();
    }

    public Number getPercentPoint(float percent) {
        return calculator.getPercentPoint(percent);
    }

    public double getStandardDeviation() {
        return calculator.getStandardDeviation();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15707.java