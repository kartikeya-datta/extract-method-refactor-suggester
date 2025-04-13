error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13351.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13351.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13351.java
text:
```scala
private static final l@@ong serialVersionUID = 5188298269533339922L;

/*
 * Copyright 2003-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.math.stat.descriptive;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.discovery.tools.DiscoverClass;
import org.apache.commons.math.stat.descriptive.moment.GeometricMean;
import org.apache.commons.math.stat.descriptive.moment.Kurtosis;
import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.apache.commons.math.stat.descriptive.moment.Skewness;
import org.apache.commons.math.stat.descriptive.moment.Variance;
import org.apache.commons.math.stat.descriptive.rank.Max;
import org.apache.commons.math.stat.descriptive.rank.Min;
import org.apache.commons.math.stat.descriptive.rank.Percentile;
import org.apache.commons.math.stat.descriptive.summary.Sum;
import org.apache.commons.math.stat.descriptive.summary.SumOfSquares;


/**
 * Abstract factory class for univariate statistical summaries.
 *
 * @version $Revision$ $Date$
 */
public abstract class DescriptiveStatistics implements StatisticalSummary, Serializable {
    
    /** Serialization UID */
    static final long serialVersionUID = 5188298269533339922L;
    
    /**
     * Create an instance of a <code>DescriptiveStatistics</code>
     * @param cls the type of <code>DescriptiveStatistics</code> object to
     *        create. 
     * @return a new factory. 
     * @throws InstantiationException is thrown if the object can not be
     *            created.
     * @throws IllegalAccessException is thrown if the type's default
     *            constructor is not accessible.
     */
    public static DescriptiveStatistics newInstance(Class cls) throws InstantiationException, IllegalAccessException {
        return (DescriptiveStatistics)cls.newInstance();
    }
    
    /**
     * Create an instance of a <code>DescriptiveStatistics</code>
     * @return a new factory. 
     */
    public static DescriptiveStatistics newInstance() {
        DescriptiveStatistics factory = null;
        try {
            DiscoverClass dc = new DiscoverClass();
            factory = (DescriptiveStatistics) dc.newInstance(
                DescriptiveStatistics.class,
                "org.apache.commons.math.stat.descriptive.DescriptiveStatisticsImpl");
        } catch(Throwable t) {
            return new DescriptiveStatisticsImpl();
        }
        return factory;
    }
    
    /**
     * This constant signals that a Univariate implementation
     * takes into account the contributions of an infinite number of
     * elements.  In other words, if getWindow returns this
     * constant, there is, in effect, no "window".
     */
    public static final int INFINITE_WINDOW = -1;

    /**
     * Adds the value to the set of numbers
     * @param v the value to be added 
     */
    public abstract void addValue(double v);

    /** 
     * Returns the <a href="http://www.xycoon.com/arithmetic_mean.htm">
     * arithmetic mean </a> of the available values 
     * @return The mean or Double.NaN if no values have been added.
     */
    public double getMean() {
        return apply(new Mean());
    }

    /** 
     * Returns the <a href="http://www.xycoon.com/geometric_mean.htm">
     * geometric mean </a> of the available values
     * @return The geometricMean, Double.NaN if no values have been added, 
     * or if the productof the available values is less than or equal to 0.
     */
    public double getGeometricMean() {
        return apply(new GeometricMean());
    }

    /** 
     * Returns the variance of the available values.
     * @return The variance, Double.NaN if no values have been added 
     * or 0.0 for a single value set.  
     */
    public double getVariance() {
        return apply(new Variance());
    }

    /** 
     * Returns the standard deviation of the available values.
     * @return The standard deviation, Double.NaN if no values have been added 
     * or 0.0 for a single value set. 
     */
    public double getStandardDeviation() {
        double stdDev = Double.NaN;
        if (getN() > 0) {
            if (getN() > 1) {
                stdDev = Math.sqrt(getVariance());
            } else {
                stdDev = 0.0;
            }
        }
        return (stdDev);
    }

    /**
     * Returns the skewness of the available values. Skewness is a 
     * measure of the assymetry of a given distribution.
     * @return The skewness, Double.NaN if no values have been added 
     * or 0.0 for a value set &lt;=2. 
     */
    public double getSkewness() {
        return apply(new Skewness());
    }

    /**
     * Returns the Kurtosis of the available values. Kurtosis is a 
     * measure of the "peakedness" of a distribution
     * @return The kurtosis, Double.NaN if no values have been added, or 0.0 
     * for a value set &lt;=3. 
     */
    public double getKurtosis() {
        return apply(new Kurtosis());
    }

    /** 
     * Returns the maximum of the available values
     * @return The max or Double.NaN if no values have been added.
     */
    public double getMax() {
        return apply(new Max());
    }

    /** 
    * Returns the minimum of the available values
    * @return The min or Double.NaN if no values have been added.
    */
    public double getMin() {
        return apply(new Min());
    }

    /** 
     * Returns the number of available values
     * @return The number of available values
     */
    public abstract long getN();

    /**
     * Returns the sum of the values that have been added to Univariate.
     * @return The sum or Double.NaN if no values have been added
     */
    public double getSum() {
        return apply(new Sum());
    }

    /**
     * Returns the sum of the squares of the available values.
     * @return The sum of the squares or Double.NaN if no 
     * values have been added.
     */
    public double getSumsq() {
        return apply(new SumOfSquares());
    }

    /** 
     * Resets all statistics and storage
     */
    public abstract void clear();

    /**
     * Univariate has the ability to return only measures for the
     * last N elements added to the set of values.
     * @return The current window size or -1 if its Infinite.
     */

    public abstract int getWindowSize();

    /**
     * WindowSize controls the number of values which contribute 
     * to the values returned by Univariate.  For example, if 
     * windowSize is set to 3 and the values {1,2,3,4,5} 
     * have been added <strong> in that order</strong> 
     * then the <i>available values</i> are {3,4,5} and all
     * reported statistics will be based on these values
     * @param windowSize sets the size of the window.
     */
    public abstract void setWindowSize(int windowSize);
    
    /**
     * Returns the current set of values in an array of double primitives.  
     * The order of addition is preserved.  The returned array is a fresh
     * copy of the underlying data -- i.e., it is not a reference to the
     * stored data.
     * 
     * @return returns the current set of numbers in the order in which they 
     *         were added to this set
     */
    public abstract double[] getValues();

    /**
     * Returns the current set of values in an array of double primitives,  
     * sorted in ascending order.  The returned array is a fresh
     * copy of the underlying data -- i.e., it is not a reference to the
     * stored data.
     * @return returns the current set of 
     * numbers sorted in ascending order        
     */
    public double[] getSortedValues() {
        double[] sort = getValues();
        Arrays.sort(sort);
        return sort;
    }

    /**
     * Returns the element at the specified index
     * @param index The Index of the element
     * @return return the element at the specified index
     */
    public abstract double getElement(int index);

    /**
     * Returns an estimate for the pth percentile of the stored values. 
     * <p>
     * The implementation provided here follows the first estimation procedure presented
     * <a href="http://www.itl.nist.gov/div898/handbook/prc/section2/prc252.htm">here.</a>
     * <p>
     * <strong>Preconditions</strong>:<ul>
     * <li><code>0 &lt; p &lt; 100</code> (otherwise an 
     * <code>IllegalArgumentException</code> is thrown)</li>
     * <li>at least one value must be stored (returns <code>Double.NaN
     *     </code> otherwise)</li>
     * </ul>
     * 
     * @param p the requested percentile (scaled from 0 - 100)
     * @return An estimate for the pth percentile of the stored data 
     * values
     */
    public double getPercentile(double p) {
        return apply(new Percentile(p));
    }
    
    /**
     * Generates a text report displaying univariate statistics from values
     * that have been added.  Each statistic is displayed on a separate
     * line.
     * 
     * @return String with line feeds displaying statistics
     */
    public String toString() {
        StringBuffer outBuffer = new StringBuffer();
        outBuffer.append("DescriptiveStatistics:\n");
        outBuffer.append("n: " + getN() + "\n");
        outBuffer.append("min: " + getMin() + "\n");
        outBuffer.append("max: " + getMax() + "\n");
        outBuffer.append("mean: " + getMean() + "\n");
        outBuffer.append("std dev: " + getStandardDeviation() + "\n");
        outBuffer.append("median: " + getPercentile(50) + "\n");
        outBuffer.append("skewness: " + getSkewness() + "\n");
        outBuffer.append("kurtosis: " + getKurtosis() + "\n");
        return outBuffer.toString();
    }
    
    /**
     * Apply the given statistic to the data associated with this set of statistics.
     * @param stat the statistic to apply
     * @return the computed value of the statistic.
     */
    public abstract double apply(UnivariateStatistic stat);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13351.java