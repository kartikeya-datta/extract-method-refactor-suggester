error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13359.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13359.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13359.java
text:
```scala
private static final l@@ong serialVersionUID = 2784465764798260919L;

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
package org.apache.commons.math.stat.descriptive.moment;

import org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic;

/**
 * Computes the Kurtosis of the available values.
 * <p>
 * We use the following (unbiased) formula to define kurtosis:
 *  <p>
 *  kurtosis = { [n(n+1) / (n -1)(n - 2)(n-3)] sum[(x_i - mean)^4] / std^4 } - [3(n-1)^2 / (n-2)(n-3)]
 *  <p>
 *  where n is the number of values, mean is the {@link Mean} and std is the
 * {@link StandardDeviation}
 * <p>
 *  Note that this statistic is undefined for n < 4.  <code>Double.Nan</code>
 *  is returned when there is not sufficient data to compute the statistic.
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong> If 
 * multiple threads access an instance of this class concurrently, and at least
 * one of the threads invokes the <code>increment()</code> or 
 * <code>clear()</code> method, it must be synchronized externally.
 * 
 * @version $Revision$ $Date$
 */
public class Kurtosis extends AbstractStorelessUnivariateStatistic  {

    /** Serializable version identifier */
    static final long serialVersionUID = 2784465764798260919L;  
      
    /**Fourth Moment on which this statistic is based */
    protected FourthMoment moment;

    /** 
     * Determines whether or not this statistic can be incremented or cleared.
     * <p>
     * Statistics based on (constructed from) external moments cannot
     * be incremented or cleared.
    */
    protected boolean incMoment;

    /**
     * Construct a Kurtosis
     */
    public Kurtosis() {
        incMoment = true;
        moment = new FourthMoment();
    }

    /**
     * Construct a Kurtosis from an external moment
     * 
     * @param m4 external Moment
     */
    public Kurtosis(final FourthMoment m4) {
        incMoment = false;
        this.moment = m4;
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic#increment(double)
     */
    public void increment(final double d) {
        if (incMoment) {
            moment.increment(d);
        }  else  {
            throw new IllegalStateException
            ("Statistics constructed from external moments cannot be incremented");
        }
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic#getResult()
     */
    public double getResult() {
        double kurtosis = Double.NaN;
        if (moment.getN() > 3) {
            double variance = moment.m2 / (double) (moment.n - 1);
                if (moment.n <= 3 || variance < 10E-20) {
                    kurtosis = 0.0;
                } else {
                    double n = (double) moment.n;
                    kurtosis =
                        (n * (n + 1) * moment.m4 -
                                3 * moment.m2 * moment.m2 * (n - 1)) /
                                ((n - 1) * (n -2) * (n -3) * variance * variance);
                }
        }
        return kurtosis;
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic#clear()
     */
    public void clear() {
        if (incMoment) {
            moment.clear();
        } else  {
            throw new IllegalStateException
                ("Statistics constructed from external moments cannot be cleared");
        }
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic#getN()
     */
    public long getN() {
        return moment.getN();
    }
    
    /* UnvariateStatistic Approach  */

    /**
     * Returns the kurtosis of the entries in the specified portion of the
     * input array.  
     * <p>
     * See {@link Kurtosis} for details on the computing algorithm.
     * <p>
     * Throws <code>IllegalArgumentException</code> if the array is null.
     * 
     * @param values the input array
     * @param begin index of the first array element to include
     * @param length the number of elements to include
     * @return the kurtosis of the values or Double.NaN if length is less than
     * 4
     * @throws IllegalArgumentException if the input array is null or the array
     * index parameters are not valid
     */
    public double evaluate(final double[] values,final int begin, final int length) {
        // Initialize the kurtosis  
        double kurt = Double.NaN;   
        
        if (test(values, begin, length) && length > 3) {       
            
            // Compute the mean and standard deviation
            Variance variance = new Variance();
            variance.incrementAll(values, begin, length);
            double mean = variance.moment.m1;
            double stdDev = Math.sqrt(variance.getResult());
            
            // Sum the ^4 of the distance from the mean divided by the
            // standard deviation
            double accum3 = 0.0;
            for (int i = begin; i < begin + length; i++) {
                accum3 += Math.pow((values[i] - mean), 4.0);
            }
            accum3 /= Math.pow(stdDev, 4.0d);
            
            // Get N
            double n0 = length;
            
            double coefficientOne =
                (n0 * (n0 + 1)) / ((n0 - 1) * (n0 - 2) * (n0 - 3));
            double termTwo =
                ((3 * Math.pow(n0 - 1, 2.0)) / ((n0 - 2) * (n0 - 3)));
            
            // Calculate kurtosis
            kurt = (coefficientOne * accum3) - termTwo;
        }       
        return kurt;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13359.java