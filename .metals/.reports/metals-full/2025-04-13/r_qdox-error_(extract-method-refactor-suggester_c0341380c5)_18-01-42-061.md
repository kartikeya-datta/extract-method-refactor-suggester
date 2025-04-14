error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5520.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5520.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5520.java
text:
```scala
r@@eturn Long.valueOf((long) mean);

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

package org.apache.jmeter.util;

import org.apache.jmeter.samplers.SampleResult;

/**
 * Class to calculate various items that don't require all previous results to be saved:
 * - mean = average
 * - standard deviation
 * - minimum
 * - maximum
 */
public class Calculator {

    private double sum = 0;

    private double sumOfSquares = 0;

    private double mean = 0;

    private double deviation = 0;

    private int count = 0;

    private long bytes = 0;

    private long maximum = Long.MIN_VALUE;

    private long minimum = Long.MAX_VALUE;

    private int errors = 0;

    private final String label;

    public Calculator() {
        this("");
    }

    public Calculator(String label) {
        this.label = label;
    }

    public void clear() {
        maximum = Long.MIN_VALUE;
        minimum = Long.MAX_VALUE;
        sum = 0;
        sumOfSquares = 0;
        mean = 0;
        deviation = 0;
        count = 0;
    }

    public void addValue(long newValue) {
        addValue(newValue,1);
    }

    private void addValue(long newValue, int sampleCount) {
        count += sampleCount;
        double currentVal = newValue;
        sum += currentVal;
        if (sampleCount > 1){
            minimum=Math.min(newValue/sampleCount, minimum);
            maximum=Math.max(newValue/sampleCount, maximum);
            // For n values in an aggregate sample the average value = (val/n)
            // So need to add n * (val/n) * (val/n) = val * val / n
            sumOfSquares += (currentVal * currentVal) / (sampleCount);
        } else {
            minimum=Math.min(newValue, minimum);
            maximum=Math.max(newValue, maximum);
            sumOfSquares += currentVal * currentVal;
        }
        // Calculate each time, as likely to be called for each add
        mean = sum / count;
        deviation = Math.sqrt((sumOfSquares / count) - (mean * mean));
    }


    public void addBytes(long newValue) {
        bytes += newValue;
    }

    private long startTime = 0;
    private long elapsedTime = 0;

    public void addSample(SampleResult res) {
        addBytes(res.getBytes());
        addValue(res.getTime(),res.getSampleCount());
        errors+=res.getErrorCount(); // account for multiple samples
        if (startTime == 0){
            startTime=res.getStartTime();
        }
        startTime = Math.min(startTime, res.getStartTime());
        elapsedTime = Math.max(elapsedTime, res.getEndTime()-startTime);
    }


    public long getTotalBytes() {
        return bytes;
    }


    public double getMean() {
        return mean;
    }

    public Number getMeanAsNumber() {
        return new Long((long) mean);
    }

    public double getStandardDeviation() {
        return deviation;
    }

    public long getMin() {
        return minimum;
    }

    public long getMax() {
        return maximum;
    }

    public int getCount() {
        return count;
    }

    public String getLabel() {
        return label;
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

        if (count == 0) {
            return (rval);
        }
        rval = (double) errors / (double) count;
        return (rval);
    }

    /**
     * Returns the throughput associated to this sampler in requests per second.
     * May be slightly skewed because it takes the timestamps of the first and
     * last samples as the total time passed, and the test may actually have
     * started before that start time and ended after that end time.
     */
    public double getRate() {
        if (elapsedTime == 0) {
            return 0.0;
        }

        return ((double) count / (double) elapsedTime ) * 1000;
    }

    /**
     * calculates the average page size, which means divide the bytes by number
     * of samples.
     *
     * @return average page size in bytes
     */
    public double getAvgPageBytes() {
        if (count > 0 && bytes > 0) {
            return (double) bytes / count;
        }
        return 0.0;
    }

    /**
     * Throughput in bytes / second
     *
     * @return throughput in bytes/second
     */
    public double getBytesPerSecond() {
        if (elapsedTime > 0) {
            return bytes / ((double) elapsedTime / 1000); // 1000 = millisecs/sec
        }
        return 0.0;
    }

    /**
     * Throughput in kilobytes / second
     *
     * @return Throughput in kilobytes / second
     */
    public double getKBPerSecond() {
        return getBytesPerSecond() / 1024; // 1024=bytes per kb
    }

}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5520.java