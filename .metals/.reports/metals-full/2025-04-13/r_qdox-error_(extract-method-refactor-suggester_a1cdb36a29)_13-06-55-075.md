error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6362.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6362.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6362.java
text:
```scala
c@@ount += sampleCount;

/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import java.text.DecimalFormat;

import org.apache.jmeter.samplers.SampleResult;

/**
 * Class to calculate various items that don't require all previous results to be saved:
 * - mean = average
 * - standard deviation
 * - minimum
 * - maximum
 */
public class Calculator {

    private static DecimalFormat rateFormatter = new DecimalFormat("#.0");

    private static DecimalFormat errorFormatter = new DecimalFormat("#0.00%");

    private static DecimalFormat kbFormatter = new DecimalFormat("#0.00");

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
        this.label = "";
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
        count =+ sampleCount;
        minimum=Math.min(newValue, minimum);
        maximum=Math.max(newValue, maximum);
        double currentVal = newValue;
        sum += currentVal;
        sumOfSquares += currentVal * currentVal;
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
        if (!res.isSuccessful()) errors++;
        if (startTime == 0){
            startTime=res.getStartTime();
        }
        elapsedTime=res.getEndTime()-startTime;
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
     * that were recorded. (Between 0.0 and 1.0) If you want a nicer return
     * format, see {@link #getErrorPercentageString()}.
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
     * Returns a String which represents the percentage of sample errors that
     * have occurred. ("0.00%" through "100.00%")
     * 
     * @return a String which represents the percentage of sample errors that
     *         have occurred.
     */
    public String getErrorPercentageString() {
        double myErrorPercentage = this.getErrorPercentage();
        if (myErrorPercentage < 0) {
            myErrorPercentage = 0.0;
        }

        return (errorFormatter.format(myErrorPercentage));
    }

    /**
     * Returns the throughput associated to this sampler in requests per second.
     * May be slightly skewed because it takes the timestamps of the first and
     * last samples as the total time passed, and the test may actually have
     * started before that start time and ended after that end time.
     */
    public double getRate() {
        if (elapsedTime == 0)
            return 0.0;

        return ((double) count / (double) elapsedTime ) * 1000;
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

    /**
     * calculates the average page size, which means divide the bytes by number
     * of samples.
     * 
     * @return
     */
    public double getPageSize() {
        if (count > 0 && bytes > 0) {
            return bytes / count;
        }
        return 0.0;
    }

    /**
     * formats the rate
     * 
     * @return
     */
    public String getPageSizeString() {
        double rate = getPageSize() / 1024;
        return kbFormatter.format(rate);
    }

    /**
     * Throughput in bytes / second
     * 
     * @return
     */
    public double getBytesPerSecond() {
        if (elapsedTime > 0) {
            return bytes / ((double) elapsedTime / 1000);
        }
        return 0.0;
    }

    /**
     * formats the Page Size
     * 
     * @return
     */
    public String getKBPerSecondString() {
        double rate = getBytesPerSecond() / 1024;
        return kbFormatter.format(rate);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6362.java