error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14707.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14707.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14707.java
text:
```scala
v@@ *= v;

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
package org.apache.commons.math3.distribution;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

/**
 * <p>
 * Utility class used by various distributions to accurately compute their
 * respective probability mass functions. The implementation for this class is
 * based on the Catherine Loader's <a target="_blank"
 * href="http://www.herine.net/stat/software/dbinom.html">dbinom</a> routines.
 * </p>
 * <p>
 * This class is not intended to be called directly.
 * </p>
 * <p>
 * References:
 * <ol>
 * <li>Catherine Loader (2000). "Fast and Accurate Computation of Binomial
 * Probabilities.". <a target="_blank"
 * href="http://www.herine.net/stat/papers/dbinom.pdf">
 * http://www.herine.net/stat/papers/dbinom.pdf</a></li>
 * </ol>
 * </p>
 *
 * @since 2.1
 * @version $Id$
 */
final class SaddlePointExpansion {

    /** 1/2 * log(2 &#960;). */
    private static final double HALF_LOG_2_PI = 0.5 * FastMath.log(MathUtils.TWO_PI);

    /** exact Stirling expansion error for certain values. */
    private static final double[] EXACT_STIRLING_ERRORS = { 0.0, /* 0.0 */
    0.1534264097200273452913848, /* 0.5 */
    0.0810614667953272582196702, /* 1.0 */
    0.0548141210519176538961390, /* 1.5 */
    0.0413406959554092940938221, /* 2.0 */
    0.03316287351993628748511048, /* 2.5 */
    0.02767792568499833914878929, /* 3.0 */
    0.02374616365629749597132920, /* 3.5 */
    0.02079067210376509311152277, /* 4.0 */
    0.01848845053267318523077934, /* 4.5 */
    0.01664469118982119216319487, /* 5.0 */
    0.01513497322191737887351255, /* 5.5 */
    0.01387612882307074799874573, /* 6.0 */
    0.01281046524292022692424986, /* 6.5 */
    0.01189670994589177009505572, /* 7.0 */
    0.01110455975820691732662991, /* 7.5 */
    0.010411265261972096497478567, /* 8.0 */
    0.009799416126158803298389475, /* 8.5 */
    0.009255462182712732917728637, /* 9.0 */
    0.008768700134139385462952823, /* 9.5 */
    0.008330563433362871256469318, /* 10.0 */
    0.007934114564314020547248100, /* 10.5 */
    0.007573675487951840794972024, /* 11.0 */
    0.007244554301320383179543912, /* 11.5 */
    0.006942840107209529865664152, /* 12.0 */
    0.006665247032707682442354394, /* 12.5 */
    0.006408994188004207068439631, /* 13.0 */
    0.006171712263039457647532867, /* 13.5 */
    0.005951370112758847735624416, /* 14.0 */
    0.005746216513010115682023589, /* 14.5 */
    0.005554733551962801371038690 /* 15.0 */
    };

    /**
     * Default constructor.
     */
    private SaddlePointExpansion() {
        super();
    }

    /**
     * Compute the error of Stirling's series at the given value.
     * <p>
     * References:
     * <ol>
     * <li>Eric W. Weisstein. "Stirling's Series." From MathWorld--A Wolfram Web
     * Resource. <a target="_blank"
     * href="http://mathworld.wolfram.com/StirlingsSeries.html">
     * http://mathworld.wolfram.com/StirlingsSeries.html</a></li>
     * </ol>
     * </p>
     *
     * @param z the value.
     * @return the Striling's series error.
     */
    static double getStirlingError(double z) {
        double ret;
        if (z < 15.0) {
            double z2 = 2.0 * z;
            if (FastMath.floor(z2) == z2) {
                ret = EXACT_STIRLING_ERRORS[(int) z2];
            } else {
                ret = Gamma.logGamma(z + 1.0) - (z + 0.5) * FastMath.log(z) +
                      z - HALF_LOG_2_PI;
            }
        } else {
            double z2 = z * z;
            ret = (0.083333333333333333333 -
                    (0.00277777777777777777778 -
                            (0.00079365079365079365079365 -
                                    (0.000595238095238095238095238 -
                                            0.0008417508417508417508417508 /
                                            z2) / z2) / z2) / z2) / z;
        }
        return ret;
    }

    /**
     * A part of the deviance portion of the saddle point approximation.
     * <p>
     * References:
     * <ol>
     * <li>Catherine Loader (2000). "Fast and Accurate Computation of Binomial
     * Probabilities.". <a target="_blank"
     * href="http://www.herine.net/stat/papers/dbinom.pdf">
     * http://www.herine.net/stat/papers/dbinom.pdf</a></li>
     * </ol>
     * </p>
     *
     * @param x the x value.
     * @param mu the average.
     * @return a part of the deviance.
     */
    static double getDeviancePart(double x, double mu) {
        double ret;
        if (FastMath.abs(x - mu) < 0.1 * (x + mu)) {
            double d = x - mu;
            double v = d / (x + mu);
            double s1 = v * d;
            double s = Double.NaN;
            double ej = 2.0 * x * v;
            v = v * v;
            int j = 1;
            while (s1 != s) {
                s = s1;
                ej *= v;
                s1 = s + ej / ((j * 2) + 1);
                ++j;
            }
            ret = s1;
        } else {
            ret = x * FastMath.log(x / mu) + mu - x;
        }
        return ret;
    }

    /**
     * Compute the logarithm of the PMF for a binomial distribution
     * using the saddle point expansion.
     *
     * @param x the value at which the probability is evaluated.
     * @param n the number of trials.
     * @param p the probability of success.
     * @param q the probability of failure (1 - p).
     * @return log(p(x)).
     */
    static double logBinomialProbability(int x, int n, double p, double q) {
        double ret;
        if (x == 0) {
            if (p < 0.1) {
                ret = -getDeviancePart(n, n * q) - n * p;
            } else {
                ret = n * FastMath.log(q);
            }
        } else if (x == n) {
            if (q < 0.1) {
                ret = -getDeviancePart(n, n * p) - n * q;
            } else {
                ret = n * FastMath.log(p);
            }
        } else {
            ret = getStirlingError(n) - getStirlingError(x) -
                  getStirlingError(n - x) - getDeviancePart(x, n * p) -
                  getDeviancePart(n - x, n * q);
            double f = (MathUtils.TWO_PI * x * (n - x)) / n;
            ret = -0.5 * FastMath.log(f) + ret;
        }
        return ret;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14707.java