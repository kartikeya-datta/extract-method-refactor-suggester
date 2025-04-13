error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4373.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4373.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4373.java
text:
```scala
S@@tringBuilder mySB = new StringBuilder();

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

import java.text.DecimalFormat;

import org.apache.jmeter.samplers.SampleResult;

/**
 * <p>
 * Running sample data container. Just instantiate a new instance of this
 * class, and then call {@link #addSample(SampleResult)} a few times, and pull
 * the stats out with whatever methods you prefer.
 * </p>
 * <p>
 * Please note that this class is not thread-safe.
 * The calling class is responsible for ensuring thread safety if required.
 * Versions prior to 2.3.2 appeared to be thread-safe but weren't as label and index were not final.
 * Also the caller needs to synchronize access in order to enure that variables are consistent.
 * </p>
 *
 */
public class RunningSample {

    private static final DecimalFormat rateFormatter = new DecimalFormat("#.0"); // $NON-NLS-1$

    private static final DecimalFormat errorFormatter = new DecimalFormat("#0.00%"); // $NON-NLS-1$

    private long counter;

    private long runningSum;

    private long max, min;

    private long errorCount;

    private long firstTime;

    private long lastTime;

    private final String label;

    private final int index;

    /**
     * Use this constructor to create the initial instance
     */
    public RunningSample(String label, int index) {
        this.label = label;
        this.index = index;
        init();
    }

    /**
     * Copy constructor to create a duplicate of existing instance (without the
     * disadvantages of clone()
     *
     * @param src existing RunningSample to be copied
     */
    public RunningSample(RunningSample src) {
        this.counter = src.counter;
        this.errorCount = src.errorCount;
        this.firstTime = src.firstTime;
        this.index = src.index;
        this.label = src.label;
        this.lastTime = src.lastTime;
        this.max = src.max;
        this.min = src.min;
        this.runningSum = src.runningSum;
    }

    private void init() {
        counter = 0L;
        runningSum = 0L;
        max = Long.MIN_VALUE;
        min = Long.MAX_VALUE;
        errorCount = 0L;
        firstTime = Long.MAX_VALUE;
        lastTime = 0L;
    }

    /**
     * Clear the counters (useful for differential stats)
     *
     */
    public void clear() {
        init();
    }

    /**
     * Get the elapsed time for the samples
     *
     * @return how long the samples took
     */
    public long getElapsed() {
        if (lastTime == 0) {
            return 0;// No samples collected ...
        }
        return lastTime - firstTime;
    }

    /**
     * Returns the throughput associated to this sampler in requests per second.
     * May be slightly skewed because it takes the timestamps of the first and
     * last samples as the total time passed, and the test may actually have
     * started before that start time and ended after that end time.
     */
    public double getRate() {
        if (counter == 0) {
            return 0.0; // Better behaviour when howLong=0 or lastTime=0
        }

        long howLongRunning = lastTime - firstTime;

        if (howLongRunning == 0) {
            return Double.MAX_VALUE;
        }

        return (double) counter / howLongRunning * 1000.0;
    }

    /**
     * Returns the throughput associated to this sampler in requests per min.
     * May be slightly skewed because it takes the timestamps of the first and
     * last samples as the total time passed, and the test may actually have
     * started before that start time and ended after that end time.
     */
    public double getRatePerMin() {
        if (counter == 0) {
            return 0.0; // Better behaviour when howLong=0 or lastTime=0
        }

        long howLongRunning = lastTime - firstTime;

        if (howLongRunning == 0) {
            return Double.MAX_VALUE;
        }
        return (double) counter / howLongRunning * 60000.0;
    }

    /**
     * Returns a String that represents the throughput associated for this
     * sampler, in units appropriate to its dimension:
     * <p>
     * The number is represented in requests/second or requests/minute or
     * requests/hour.
     * <p>
     * Examples: "34.2/sec" "0.1/sec" "43.0/hour" "15.9/min"
     *
     * @return a String representation of the rate the samples are being taken
     *         at.
     */
    public String getRateString() {
        double rate = getRate();

        if (rate == Double.MAX_VALUE) {
            return "N/A";
        }

        String unit = "sec";

        if (rate < 1.0) {
            rate *= 60.0;
            unit = "min";
        }
        if (rate < 1.0) {
            rate *= 60.0;
            unit = "hour";
        }

        String rval = rateFormatter.format(rate) + "/" + unit;

        return (rval);
    }

    public String getLabel() {
        return label;
    }

    public int getIndex() {
        return index;
    }

    /**
     * Records a sample.
     *
     */
    public void addSample(SampleResult res) {
        long aTimeInMillis = res.getTime();
        boolean aSuccessFlag = res.isSuccessful();

        counter++;
        long startTime = res.getStartTime();
        long endTime = res.getEndTime();

        if (firstTime > startTime) {
            // this is our first sample, set the start time to current timestamp
            firstTime = startTime;
        }

        // Always update the end time
        if (lastTime < endTime) {
            lastTime = endTime;
        }
        runningSum += aTimeInMillis;

        if (aTimeInMillis > max) {
            max = aTimeInMillis;
        }

        if (aTimeInMillis < min) {
            min = aTimeInMillis;
        }

        if (!aSuccessFlag) {
            errorCount++;
        }
    }

    /**
     * Adds another RunningSample to this one.
     * Does not check if it has the same label and index.
     */
    public void addSample(RunningSample rs) {
        this.counter += rs.counter;
        this.errorCount += rs.errorCount;
        this.runningSum += rs.runningSum;
        if (this.firstTime > rs.firstTime) {
            this.firstTime = rs.firstTime;
        }
        if (this.lastTime < rs.lastTime) {
            this.lastTime = rs.lastTime;
        }
        if (this.max < rs.max) {
            this.max = rs.max;
        }
        if (this.min > rs.min) {
            this.min = rs.min;
        }
    }

    /**
     * Returns the time in milliseconds of the quickest sample.
     *
     * @return the time in milliseconds of the quickest sample.
     */
    public long getMin() {
        long rval = 0;

        if (min != Long.MAX_VALUE) {
            rval = min;
        }
        return (rval);
    }

    /**
     * Returns the time in milliseconds of the slowest sample.
     *
     * @return the time in milliseconds of the slowest sample.
     */
    public long getMax() {
        long rval = 0;

        if (max != Long.MIN_VALUE) {
            rval = max;
        }
        return (rval);
    }

    /**
     * Returns the average time in milliseconds that samples ran in.
     *
     * @return the average time in milliseconds that samples ran in.
     */
    public long getAverage() {
        if (counter == 0) {
            return (0);
        }
        return (runningSum / counter);
    }

    /**
     * Returns the number of samples that have been recorded by this instance of
     * the RunningSample class.
     *
     * @return the number of samples that have been recorded by this instance of
     *         the RunningSample class.
     */
    public long getNumSamples() {
        return (counter);
    }

    /**
     * Returns the raw double value of the percentage of samples with errors
     * that were recorded. (Between 0.0 and 1.0) If you want a nicer return
     * format, see {@link #getErrorPercentageString()}.
     *
     * @return the raw double value of the percentage of samples with errors
     *         that were recorded.
     */
    public double getErrorPercentage() {
        double rval = 0.0;

        if (counter == 0) {
            return (rval);
        }
        rval = (double) errorCount / (double) counter;
        return (rval);
    }

    /**
     * Returns a String which represents the percentage of sample errors that
     * have occurred. ("0.00%" through "100.00%")
     *
     * @return a String which represents the percentage of sample errors that
     *         have occurred.
     */
    public String getErrorPercentageString() {
        double myErrorPercentage = this.getErrorPercentage();

        return (errorFormatter.format(myErrorPercentage));
    }

    /**
     * For debugging purposes, mainly.
     */
    @Override
    public String toString() {
        StringBuffer mySB = new StringBuffer();

        mySB.append("Samples: " + this.getNumSamples() + "  ");
        mySB.append("Avg: " + this.getAverage() + "  ");
        mySB.append("Min: " + this.getMin() + "  ");
        mySB.append("Max: " + this.getMax() + "  ");
        mySB.append("Error Rate: " + this.getErrorPercentageString() + "  ");
        mySB.append("Sample Rate: " + this.getRateString());
        return (mySB.toString());
    }

    /**
     * @return errorCount
     */
    public long getErrorCount() {
        return errorCount;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4373.java