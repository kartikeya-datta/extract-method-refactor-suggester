error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8611.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8611.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8611.java
text:
```scala
S@@tringBuilder outBuffer = new StringBuilder();

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.stat.descriptive.moment.GeometricMean;
import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.apache.commons.math.stat.descriptive.moment.SecondMoment;
import org.apache.commons.math.stat.descriptive.moment.Variance;
import org.apache.commons.math.stat.descriptive.rank.Max;
import org.apache.commons.math.stat.descriptive.rank.Min;
import org.apache.commons.math.stat.descriptive.summary.Sum;
import org.apache.commons.math.stat.descriptive.summary.SumOfLogs;
import org.apache.commons.math.stat.descriptive.summary.SumOfSquares;
import org.apache.commons.math.util.MathUtils;
import org.apache.commons.math.util.FastMath;

/**
 * <p>
 * Computes summary statistics for a stream of data values added using the
 * {@link #addValue(double) addValue} method. The data values are not stored in
 * memory, so this class can be used to compute statistics for very large data
 * streams.
 * </p>
 * <p>
 * The {@link StorelessUnivariateStatistic} instances used to maintain summary
 * state and compute statistics are configurable via setters. For example, the
 * default implementation for the variance can be overridden by calling
 * {@link #setVarianceImpl(StorelessUnivariateStatistic)}. Actual parameters to
 * these methods must implement the {@link StorelessUnivariateStatistic}
 * interface and configuration must be completed before <code>addValue</code>
 * is called. No configuration is necessary to use the default, commons-math
 * provided implementations.
 * </p>
 * <p>
 * Note: This class is not thread-safe. Use
 * {@link SynchronizedSummaryStatistics} if concurrent access from multiple
 * threads is required.
 * </p>
 * @version $Revision$ $Date$
 */
public class SummaryStatistics implements StatisticalSummary, Serializable {

    /** Serialization UID */
    private static final long serialVersionUID = -2021321786743555871L;

    /** count of values that have been added */
    protected long n = 0;

    /** SecondMoment is used to compute the mean and variance */
    protected SecondMoment secondMoment = new SecondMoment();

    /** sum of values that have been added */
    protected Sum sum = new Sum();

    /** sum of the square of each value that has been added */
    protected SumOfSquares sumsq = new SumOfSquares();

    /** min of values that have been added */
    protected Min min = new Min();

    /** max of values that have been added */
    protected Max max = new Max();

    /** sumLog of values that have been added */
    protected SumOfLogs sumLog = new SumOfLogs();

    /** geoMean of values that have been added */
    protected GeometricMean geoMean = new GeometricMean(sumLog);

    /** mean of values that have been added */
    protected Mean mean = new Mean();

    /** variance of values that have been added */
    protected Variance variance = new Variance();

    /** Sum statistic implementation - can be reset by setter. */
    private StorelessUnivariateStatistic sumImpl = sum;

    /** Sum of squares statistic implementation - can be reset by setter. */
    private StorelessUnivariateStatistic sumsqImpl = sumsq;

    /** Minimum statistic implementation - can be reset by setter. */
    private StorelessUnivariateStatistic minImpl = min;

    /** Maximum statistic implementation - can be reset by setter. */
    private StorelessUnivariateStatistic maxImpl = max;

    /** Sum of log statistic implementation - can be reset by setter. */
    private StorelessUnivariateStatistic sumLogImpl = sumLog;

    /** Geometric mean statistic implementation - can be reset by setter. */
    private StorelessUnivariateStatistic geoMeanImpl = geoMean;

    /** Mean statistic implementation - can be reset by setter. */
    private StorelessUnivariateStatistic meanImpl = mean;

    /** Variance statistic implementation - can be reset by setter. */
    private StorelessUnivariateStatistic varianceImpl = variance;

    /**
     * Construct a SummaryStatistics instance
     */
    public SummaryStatistics() {
    }

    /**
     * A copy constructor. Creates a deep-copy of the {@code original}.
     *
     * @param original the {@code SummaryStatistics} instance to copy
     */
    public SummaryStatistics(SummaryStatistics original) {
        copy(original, this);
    }

    /**
     * Return a {@link StatisticalSummaryValues} instance reporting current
     * statistics.
     * @return Current values of statistics
     */
    public StatisticalSummary getSummary() {
        return new StatisticalSummaryValues(getMean(), getVariance(), getN(),
                getMax(), getMin(), getSum());
    }

    /**
     * Add a value to the data
     * @param value the value to add
     */
    public void addValue(double value) {
        sumImpl.increment(value);
        sumsqImpl.increment(value);
        minImpl.increment(value);
        maxImpl.increment(value);
        sumLogImpl.increment(value);
        secondMoment.increment(value);
        // If mean, variance or geomean have been overridden,
        // need to increment these
        if (!(meanImpl instanceof Mean)) {
            meanImpl.increment(value);
        }
        if (!(varianceImpl instanceof Variance)) {
            varianceImpl.increment(value);
        }
        if (!(geoMeanImpl instanceof GeometricMean)) {
            geoMeanImpl.increment(value);
        }
        n++;
    }

    /**
     * Returns the number of available values
     * @return The number of available values
     */
    public long getN() {
        return n;
    }

    /**
     * Returns the sum of the values that have been added
     * @return The sum or <code>Double.NaN</code> if no values have been added
     */
    public double getSum() {
        return sumImpl.getResult();
    }

    /**
     * Returns the sum of the squares of the values that have been added.
     * <p>
     * Double.NaN is returned if no values have been added.
     * </p>
     * @return The sum of squares
     */
    public double getSumsq() {
        return sumsqImpl.getResult();
    }

    /**
     * Returns the mean of the values that have been added.
     * <p>
     * Double.NaN is returned if no values have been added.
     * </p>
     * @return the mean
     */
    public double getMean() {
        if (mean == meanImpl) {
            return new Mean(secondMoment).getResult();
        } else {
            return meanImpl.getResult();
        }
    }

    /**
     * Returns the standard deviation of the values that have been added.
     * <p>
     * Double.NaN is returned if no values have been added.
     * </p>
     * @return the standard deviation
     */
    public double getStandardDeviation() {
        double stdDev = Double.NaN;
        if (getN() > 0) {
            if (getN() > 1) {
                stdDev = FastMath.sqrt(getVariance());
            } else {
                stdDev = 0.0;
            }
        }
        return stdDev;
    }

    /**
     * Returns the variance of the values that have been added.
     * <p>
     * Double.NaN is returned if no values have been added.
     * </p>
     * @return the variance
     */
    public double getVariance() {
        if (varianceImpl == variance) {
            return new Variance(secondMoment).getResult();
        } else {
            return varianceImpl.getResult();
        }
    }

    /**
     * Returns the maximum of the values that have been added.
     * <p>
     * Double.NaN is returned if no values have been added.
     * </p>
     * @return the maximum
     */
    public double getMax() {
        return maxImpl.getResult();
    }

    /**
     * Returns the minimum of the values that have been added.
     * <p>
     * Double.NaN is returned if no values have been added.
     * </p>
     * @return the minimum
     */
    public double getMin() {
        return minImpl.getResult();
    }

    /**
     * Returns the geometric mean of the values that have been added.
     * <p>
     * Double.NaN is returned if no values have been added.
     * </p>
     * @return the geometric mean
     */
    public double getGeometricMean() {
        return geoMeanImpl.getResult();
    }

    /**
     * Returns the sum of the logs of the values that have been added.
     * <p>
     * Double.NaN is returned if no values have been added.
     * </p>
     * @return the sum of logs
     * @since 1.2
     */
    public double getSumOfLogs() {
        return sumLogImpl.getResult();
    }

    /**
     * Returns a statistic related to the Second Central Moment.  Specifically,
     * what is returned is the sum of squared deviations from the sample mean
     * among the values that have been added.
     * <p>
     * Returns <code>Double.NaN</code> if no data values have been added and
     * returns <code>0</code> if there is just one value in the data set.</p>
     * <p>
     * @return second central moment statistic
     * @since 2.0
     */
    public double getSecondMoment() {
        return secondMoment.getResult();
    }

    /**
     * Generates a text report displaying summary statistics from values that
     * have been added.
     * @return String with line feeds displaying statistics
     * @since 1.2
     */
    @Override
    public String toString() {
        StringBuffer outBuffer = new StringBuffer();
        String endl = "\n";
        outBuffer.append("SummaryStatistics:").append(endl);
        outBuffer.append("n: ").append(getN()).append(endl);
        outBuffer.append("min: ").append(getMin()).append(endl);
        outBuffer.append("max: ").append(getMax()).append(endl);
        outBuffer.append("mean: ").append(getMean()).append(endl);
        outBuffer.append("geometric mean: ").append(getGeometricMean())
            .append(endl);
        outBuffer.append("variance: ").append(getVariance()).append(endl);
        outBuffer.append("sum of squares: ").append(getSumsq()).append(endl);
        outBuffer.append("standard deviation: ").append(getStandardDeviation())
            .append(endl);
        outBuffer.append("sum of logs: ").append(getSumOfLogs()).append(endl);
        return outBuffer.toString();
    }

    /**
     * Resets all statistics and storage
     */
    public void clear() {
        this.n = 0;
        minImpl.clear();
        maxImpl.clear();
        sumImpl.clear();
        sumLogImpl.clear();
        sumsqImpl.clear();
        geoMeanImpl.clear();
        secondMoment.clear();
        if (meanImpl != mean) {
            meanImpl.clear();
        }
        if (varianceImpl != variance) {
            varianceImpl.clear();
        }
    }

    /**
     * Returns true iff <code>object</code> is a
     * <code>SummaryStatistics</code> instance and all statistics have the
     * same values as this.
     * @param object the object to test equality against.
     * @return true if object equals this
     */
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof SummaryStatistics == false) {
            return false;
        }
        SummaryStatistics stat = (SummaryStatistics)object;
        return MathUtils.equalsIncludingNaN(stat.getGeometricMean(), getGeometricMean()) &&
               MathUtils.equalsIncludingNaN(stat.getMax(),           getMax())           &&
               MathUtils.equalsIncludingNaN(stat.getMean(),          getMean())          &&
               MathUtils.equalsIncludingNaN(stat.getMin(),           getMin())           &&
               MathUtils.equalsIncludingNaN(stat.getN(),             getN())             &&
               MathUtils.equalsIncludingNaN(stat.getSum(),           getSum())           &&
               MathUtils.equalsIncludingNaN(stat.getSumsq(),         getSumsq())         &&
               MathUtils.equalsIncludingNaN(stat.getVariance(),      getVariance());
    }

    /**
     * Returns hash code based on values of statistics
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = 31 + MathUtils.hash(getGeometricMean());
        result = result * 31 + MathUtils.hash(getGeometricMean());
        result = result * 31 + MathUtils.hash(getMax());
        result = result * 31 + MathUtils.hash(getMean());
        result = result * 31 + MathUtils.hash(getMin());
        result = result * 31 + MathUtils.hash(getN());
        result = result * 31 + MathUtils.hash(getSum());
        result = result * 31 + MathUtils.hash(getSumsq());
        result = result * 31 + MathUtils.hash(getVariance());
        return result;
    }

    // Getters and setters for statistics implementations
    /**
     * Returns the currently configured Sum implementation
     * @return the StorelessUnivariateStatistic implementing the sum
     * @since 1.2
     */
    public StorelessUnivariateStatistic getSumImpl() {
        return sumImpl;
    }

    /**
     * <p>
     * Sets the implementation for the Sum.
     * </p>
     * <p>
     * This method must be activated before any data has been added - i.e.,
     * before {@link #addValue(double) addValue} has been used to add data;
     * otherwise an IllegalStateException will be thrown.
     * </p>
     * @param sumImpl the StorelessUnivariateStatistic instance to use for
     *        computing the Sum
     * @throws IllegalStateException if data has already been added (i.e if n >
     *         0)
     * @since 1.2
     */
    public void setSumImpl(StorelessUnivariateStatistic sumImpl) {
        checkEmpty();
        this.sumImpl = sumImpl;
    }

    /**
     * Returns the currently configured sum of squares implementation
     * @return the StorelessUnivariateStatistic implementing the sum of squares
     * @since 1.2
     */
    public StorelessUnivariateStatistic getSumsqImpl() {
        return sumsqImpl;
    }

    /**
     * <p>
     * Sets the implementation for the sum of squares.
     * </p>
     * <p>
     * This method must be activated before any data has been added - i.e.,
     * before {@link #addValue(double) addValue} has been used to add data;
     * otherwise an IllegalStateException will be thrown.
     * </p>
     * @param sumsqImpl the StorelessUnivariateStatistic instance to use for
     *        computing the sum of squares
     * @throws IllegalStateException if data has already been added (i.e if n >
     *         0)
     * @since 1.2
     */
    public void setSumsqImpl(StorelessUnivariateStatistic sumsqImpl) {
        checkEmpty();
        this.sumsqImpl = sumsqImpl;
    }

    /**
     * Returns the currently configured minimum implementation
     * @return the StorelessUnivariateStatistic implementing the minimum
     * @since 1.2
     */
    public StorelessUnivariateStatistic getMinImpl() {
        return minImpl;
    }

    /**
     * <p>
     * Sets the implementation for the minimum.
     * </p>
     * <p>
     * This method must be activated before any data has been added - i.e.,
     * before {@link #addValue(double) addValue} has been used to add data;
     * otherwise an IllegalStateException will be thrown.
     * </p>
     * @param minImpl the StorelessUnivariateStatistic instance to use for
     *        computing the minimum
     * @throws IllegalStateException if data has already been added (i.e if n >
     *         0)
     * @since 1.2
     */
    public void setMinImpl(StorelessUnivariateStatistic minImpl) {
        checkEmpty();
        this.minImpl = minImpl;
    }

    /**
     * Returns the currently configured maximum implementation
     * @return the StorelessUnivariateStatistic implementing the maximum
     * @since 1.2
     */
    public StorelessUnivariateStatistic getMaxImpl() {
        return maxImpl;
    }

    /**
     * <p>
     * Sets the implementation for the maximum.
     * </p>
     * <p>
     * This method must be activated before any data has been added - i.e.,
     * before {@link #addValue(double) addValue} has been used to add data;
     * otherwise an IllegalStateException will be thrown.
     * </p>
     * @param maxImpl the StorelessUnivariateStatistic instance to use for
     *        computing the maximum
     * @throws IllegalStateException if data has already been added (i.e if n >
     *         0)
     * @since 1.2
     */
    public void setMaxImpl(StorelessUnivariateStatistic maxImpl) {
        checkEmpty();
        this.maxImpl = maxImpl;
    }

    /**
     * Returns the currently configured sum of logs implementation
     * @return the StorelessUnivariateStatistic implementing the log sum
     * @since 1.2
     */
    public StorelessUnivariateStatistic getSumLogImpl() {
        return sumLogImpl;
    }

    /**
     * <p>
     * Sets the implementation for the sum of logs.
     * </p>
     * <p>
     * This method must be activated before any data has been added - i.e.,
     * before {@link #addValue(double) addValue} has been used to add data;
     * otherwise an IllegalStateException will be thrown.
     * </p>
     * @param sumLogImpl the StorelessUnivariateStatistic instance to use for
     *        computing the log sum
     * @throws IllegalStateException if data has already been added (i.e if n >
     *         0)
     * @since 1.2
     */
    public void setSumLogImpl(StorelessUnivariateStatistic sumLogImpl) {
        checkEmpty();
        this.sumLogImpl = sumLogImpl;
        geoMean.setSumLogImpl(sumLogImpl);
    }

    /**
     * Returns the currently configured geometric mean implementation
     * @return the StorelessUnivariateStatistic implementing the geometric mean
     * @since 1.2
     */
    public StorelessUnivariateStatistic getGeoMeanImpl() {
        return geoMeanImpl;
    }

    /**
     * <p>
     * Sets the implementation for the geometric mean.
     * </p>
     * <p>
     * This method must be activated before any data has been added - i.e.,
     * before {@link #addValue(double) addValue} has been used to add data;
     * otherwise an IllegalStateException will be thrown.
     * </p>
     * @param geoMeanImpl the StorelessUnivariateStatistic instance to use for
     *        computing the geometric mean
     * @throws IllegalStateException if data has already been added (i.e if n >
     *         0)
     * @since 1.2
     */
    public void setGeoMeanImpl(StorelessUnivariateStatistic geoMeanImpl) {
        checkEmpty();
        this.geoMeanImpl = geoMeanImpl;
    }

    /**
     * Returns the currently configured mean implementation
     * @return the StorelessUnivariateStatistic implementing the mean
     * @since 1.2
     */
    public StorelessUnivariateStatistic getMeanImpl() {
        return meanImpl;
    }

    /**
     * <p>
     * Sets the implementation for the mean.
     * </p>
     * <p>
     * This method must be activated before any data has been added - i.e.,
     * before {@link #addValue(double) addValue} has been used to add data;
     * otherwise an IllegalStateException will be thrown.
     * </p>
     * @param meanImpl the StorelessUnivariateStatistic instance to use for
     *        computing the mean
     * @throws IllegalStateException if data has already been added (i.e if n >
     *         0)
     * @since 1.2
     */
    public void setMeanImpl(StorelessUnivariateStatistic meanImpl) {
        checkEmpty();
        this.meanImpl = meanImpl;
    }

    /**
     * Returns the currently configured variance implementation
     * @return the StorelessUnivariateStatistic implementing the variance
     * @since 1.2
     */
    public StorelessUnivariateStatistic getVarianceImpl() {
        return varianceImpl;
    }

    /**
     * <p>
     * Sets the implementation for the variance.
     * </p>
     * <p>
     * This method must be activated before any data has been added - i.e.,
     * before {@link #addValue(double) addValue} has been used to add data;
     * otherwise an IllegalStateException will be thrown.
     * </p>
     * @param varianceImpl the StorelessUnivariateStatistic instance to use for
     *        computing the variance
     * @throws IllegalStateException if data has already been added (i.e if n >
     *         0)
     * @since 1.2
     */
    public void setVarianceImpl(StorelessUnivariateStatistic varianceImpl) {
        checkEmpty();
        this.varianceImpl = varianceImpl;
    }

    /**
     * Throws IllegalStateException if n > 0.
     */
    private void checkEmpty() {
        if (n > 0) {
            throw MathRuntimeException.createIllegalStateException(
                    LocalizedFormats.VALUES_ADDED_BEFORE_CONFIGURING_STATISTIC,
                    n);
        }
    }

    /**
     * Returns a copy of this SummaryStatistics instance with the same internal state.
     *
     * @return a copy of this
     */
    public SummaryStatistics copy() {
        SummaryStatistics result = new SummaryStatistics();
        copy(this, result);
        return result;
    }

    /**
     * Copies source to dest.
     * <p>Neither source nor dest can be null.</p>
     *
     * @param source SummaryStatistics to copy
     * @param dest SummaryStatistics to copy to
     * @throws NullPointerException if either source or dest is null
     */
    public static void copy(SummaryStatistics source, SummaryStatistics dest) {
        dest.maxImpl = source.maxImpl.copy();
        dest.meanImpl = source.meanImpl.copy();
        dest.minImpl = source.minImpl.copy();
        dest.sumImpl = source.sumImpl.copy();
        dest.varianceImpl = source.varianceImpl.copy();
        dest.sumLogImpl = source.sumLogImpl.copy();
        dest.sumsqImpl = source.sumsqImpl.copy();
        if (source.getGeoMeanImpl() instanceof GeometricMean) {
            // Keep geoMeanImpl, sumLogImpl in synch
            dest.geoMeanImpl = new GeometricMean((SumOfLogs) dest.sumLogImpl);
        } else {
            dest.geoMeanImpl = source.geoMeanImpl.copy();
        }
        SecondMoment.copy(source.secondMoment, dest.secondMoment);
        dest.n = source.n;

        // Make sure that if stat == statImpl in source, same
        // holds in dest; otherwise copy stat
        if (source.geoMean == source.geoMeanImpl) {
            dest.geoMean = (GeometricMean) dest.geoMeanImpl;
        } else {
            GeometricMean.copy(source.geoMean, dest.geoMean);
        }
        if (source.max == source.maxImpl) {
            dest.max = (Max) dest.maxImpl;
        } else {
            Max.copy(source.max, dest.max);
        }
        if (source.mean == source.meanImpl) {
            dest.mean = (Mean) dest.meanImpl;
        } else {
            Mean.copy(source.mean, dest.mean);
        }
        if (source.min == source.minImpl) {
            dest.min = (Min) dest.minImpl;
        } else {
            Min.copy(source.min, dest.min);
        }
        if (source.sum == source.sumImpl) {
            dest.sum = (Sum) dest.sumImpl;
        } else {
            Sum.copy(source.sum, dest.sum);
        }
        if (source.variance == source.varianceImpl) {
            dest.variance = (Variance) dest.varianceImpl;
        } else {
            Variance.copy(source.variance, dest.variance);
        }
        if (source.sumLog == source.sumLogImpl) {
            dest.sumLog = (SumOfLogs) dest.sumLogImpl;
        } else {
            SumOfLogs.copy(source.sumLog, dest.sumLog);
        }
        if (source.sumsq == source.sumsqImpl) {
            dest.sumsq = (SumOfSquares) dest.sumsqImpl;
        } else {
            SumOfSquares.copy(source.sumsq, dest.sumsq);
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8611.java