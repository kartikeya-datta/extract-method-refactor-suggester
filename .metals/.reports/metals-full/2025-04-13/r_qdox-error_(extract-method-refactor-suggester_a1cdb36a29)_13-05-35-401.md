error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9459.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9459.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9459.java
text:
```scala
public A@@novaStats(int dfbg, int dfwg, double F) {

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
package org.apache.commons.math.stat.inference;

import java.util.Collection;

import org.apache.commons.math.MathException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.distribution.FDistribution;
import org.apache.commons.math.distribution.FDistributionImpl;
import org.apache.commons.math.stat.descriptive.summary.Sum;
import org.apache.commons.math.stat.descriptive.summary.SumOfSquares;


/**
 * Implements one-way ANOVA statistics defined in the {@link OneWayAnovaImpl}
 * interface.
 *
 * <p>Uses the
 * {@link org.apache.commons.math.distribution.FDistribution
 *  commons-math F Distribution implementation} to estimate exact p-values.</p>
 *
 * <p>This implementation is based on a description at
 * http://faculty.vassar.edu/lowry/ch13pt1.html</p>
 * <pre>
 * Abbreviations: bg = between groups,
 *                wg = within groups,
 *                ss = sum squared deviations
 * </pre>
 *
 * @since 1.2
 * @version $Revision$ $Date$
 */
public class OneWayAnovaImpl implements OneWayAnova  {

    /**
     * Default constructor.
     */
    public OneWayAnovaImpl() {
    }

    /**
     * {@inheritDoc}<p>
     * This implementation computes the F statistic using the definitional
     * formula<pre>
     *   F = msbg/mswg</pre>
     * where<pre>
     *  msbg = between group mean square
     *  mswg = within group mean square</pre>
     * are as defined <a href="http://faculty.vassar.edu/lowry/ch13pt1.html">
     * here</a></p>
     */
    public double anovaFValue(Collection<double[]> categoryData)
        throws IllegalArgumentException, MathException {
        AnovaStats a = anovaStats(categoryData);
        return a.F;
    }

    /**
     * {@inheritDoc}<p>
     * This implementation uses the
     * {@link org.apache.commons.math.distribution.FDistribution
     * commons-math F Distribution implementation} to estimate the exact
     * p-value, using the formula<pre>
     *   p = 1 - cumulativeProbability(F)</pre>
     * where <code>F</code> is the F value and <code>cumulativeProbability</code>
     * is the commons-math implementation of the F distribution.</p>
     */
    public double anovaPValue(Collection<double[]> categoryData)
        throws IllegalArgumentException, MathException {
        AnovaStats a = anovaStats(categoryData);
        FDistribution fdist = new FDistributionImpl(a.dfbg, a.dfwg);
        return 1.0 - fdist.cumulativeProbability(a.F);
    }

    /**
     * {@inheritDoc}<p>
     * This implementation uses the
     * {@link org.apache.commons.math.distribution.FDistribution
     * commons-math F Distribution implementation} to estimate the exact
     * p-value, using the formula<pre>
     *   p = 1 - cumulativeProbability(F)</pre>
     * where <code>F</code> is the F value and <code>cumulativeProbability</code>
     * is the commons-math implementation of the F distribution.</p>
     * <p>True is returned iff the estimated p-value is less than alpha.</p>
     */
    public boolean anovaTest(Collection<double[]> categoryData, double alpha)
        throws IllegalArgumentException, MathException {
        if ((alpha <= 0) || (alpha > 0.5)) {
            throw MathRuntimeException.createIllegalArgumentException(
                  "out of bounds significance level {0}, must be between {1} and {2}",
                  alpha, 0, 0.5);
        }
        return (anovaPValue(categoryData) < alpha);
    }


    /**
     * This method actually does the calculations (except P-value).
     *
     * @param categoryData <code>Collection</code> of <code>double[]</code>
     * arrays each containing data for one category
     * @return computed AnovaStats
     * @throws IllegalArgumentException if categoryData does not meet
     * preconditions specified in the interface definition
     * @throws MathException if an error occurs computing the Anova stats
     */
    private AnovaStats anovaStats(Collection<double[]> categoryData)
        throws IllegalArgumentException, MathException {

        // check if we have enough categories
        if (categoryData.size() < 2) {
            throw MathRuntimeException.createIllegalArgumentException(
                  "two or more categories required, got {0}",
                  categoryData.size());
        }

        // check if each category has enough data and all is double[]
        for (double[] array : categoryData) {
            if (array.length <= 1) {
                throw MathRuntimeException.createIllegalArgumentException(
                      "two or more values required in each category, one has {0}",
                      array.length);
            }
        }

        int dfwg = 0;
        double sswg = 0;
        Sum totsum = new Sum();
        SumOfSquares totsumsq = new SumOfSquares();
        int totnum = 0;

        for (double[] data : categoryData) {

            Sum sum = new Sum();
            SumOfSquares sumsq = new SumOfSquares();
            int num = 0;

            for (int i = 0; i < data.length; i++) {
                double val = data[i];

                // within category
                num++;
                sum.increment(val);
                sumsq.increment(val);

                // for all categories
                totnum++;
                totsum.increment(val);
                totsumsq.increment(val);
            }
            dfwg += num - 1;
            double ss = sumsq.getResult() - sum.getResult() * sum.getResult() / num;
            sswg += ss;
        }
        double sst = totsumsq.getResult() - totsum.getResult() *
            totsum.getResult()/totnum;
        double ssbg = sst - sswg;
        int dfbg = categoryData.size() - 1;
        double msbg = ssbg/dfbg;
        double mswg = sswg/dfwg;
        double F = msbg/mswg;

        return new AnovaStats(dfbg, dfwg, F);
    }

    /**
        Convenience class to pass dfbg,dfwg,F values around within AnovaImpl.
        No get/set methods provided.
    */
    private static class AnovaStats {

        /** Degrees of freedom in numerator (between groups). */
        private int dfbg;

        /** Degrees of freedom in denominator (within groups). */
        private int dfwg;

        /** Statistic. */
        private double F;

        /**
         * Constructor
         * @param dfbg degrees of freedom in numerator (between groups)
         * @param dfwg degrees of freedom in denominator (within groups)
         * @param F statistic
         */
        AnovaStats(int dfbg, int dfwg, double F) {
            this.dfbg = dfbg;
            this.dfwg = dfwg;
            this.F = F;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9459.java