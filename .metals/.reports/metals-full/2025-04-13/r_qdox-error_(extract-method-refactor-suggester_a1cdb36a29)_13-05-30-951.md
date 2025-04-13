error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2620.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2620.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2620.java
text:
```scala
n@@ormal = new NormalDistribution(p, FastMath.sqrt(p));

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
package org.apache.commons.math.distribution;

import java.io.Serializable;

import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.special.Gamma;
import org.apache.commons.math.util.MathUtils;
import org.apache.commons.math.util.FastMath;

/**
 * Implementation for the {@link PoissonDistribution}.
 *
 * @version $Id$
 */
public class PoissonDistributionImpl extends AbstractIntegerDistribution
    implements PoissonDistribution, Serializable {
    /**
     * Default maximum number of iterations for cumulative probability calculations.
     * @since 2.1
     */
    public static final int DEFAULT_MAX_ITERATIONS = 10000000;
    /**
     * Default convergence criterion.
     * @since 2.1
     */
    public static final double DEFAULT_EPSILON = 1e-12;
    /** Serializable version identifier. */
    private static final long serialVersionUID = -3349935121172596109L;
    /** Distribution used to compute normal approximation. */
    private final NormalDistribution normal;
    /** Mean of the distribution. */
    private final double mean;
    /**
     * Maximum number of iterations for cumulative probability.
     *
     * Cumulative probabilities are estimated using either Lanczos series approximation of
     * Gamma#regularizedGammaP or continued fraction approximation of Gamma#regularizedGammaQ.
     */
    private final int maxIterations;
    /**
     * Convergence criterion for cumulative probability.
     */
    private final double epsilon;

    /**
     * Create a new Poisson distribution with the given the mean. The mean value
     * must be positive; otherwise an <code>IllegalArgument</code> is thrown.
     *
     * @param p the Poisson mean
     * @throws NotStrictlyPositiveException if {@code p <= 0}.
     */
    public PoissonDistributionImpl(double p) {
        this(p, DEFAULT_EPSILON, DEFAULT_MAX_ITERATIONS);
    }

    /**
     * Create a new Poisson distribution with the given mean, convergence criterion
     * and maximum number of iterations.
     *
     * @param p Poisson mean.
     * @param epsilon Convergence criterion for cumulative probabilities.
     * @param maxIterations the maximum number of iterations for cumulative
     * probabilities.
     * @since 2.1
     */
    public PoissonDistributionImpl(double p, double epsilon, int maxIterations) {
        if (p <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.MEAN, p);
        }
        mean = p;
        normal = new NormalDistributionImpl(p, FastMath.sqrt(p));
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
    }

    /**
     * Create a new Poisson distribution with the given mean and convergence criterion.
     *
     * @param p Poisson mean.
     * @param epsilon Convergence criterion for cumulative probabilities.
     * @since 2.1
     */
    public PoissonDistributionImpl(double p, double epsilon) {
        this(p, epsilon, DEFAULT_MAX_ITERATIONS);
    }

    /**
     * Create a new Poisson distribution with the given mean and maximum number of iterations.
     *
     * @param p Poisson mean.
     * @param maxIterations Maximum number of iterations for cumulative probabilities.
     * @since 2.1
     */
    public PoissonDistributionImpl(double p, int maxIterations) {
        this(p, DEFAULT_EPSILON, maxIterations);
    }

    /**
     * {@inheritDoc}
     */
    public double getMean() {
        return mean;
    }

    /**
     * The probability mass function {@code P(X = x)} for a Poisson distribution.
     *
     * @param x Value at which the probability density function is evaluated.
     * @return the value of the probability mass function at {@code x}.
     */
    public double probability(int x) {
        double ret;
        if (x < 0 || x == Integer.MAX_VALUE) {
            ret = 0.0;
        } else if (x == 0) {
            ret = FastMath.exp(-mean);
        } else {
            ret = FastMath.exp(-SaddlePointExpansion.getStirlingError(x) -
                  SaddlePointExpansion.getDeviancePart(x, mean)) /
                  FastMath.sqrt(MathUtils.TWO_PI * x);
        }
        return ret;
    }

    /**
     * The probability distribution function {@code P(X <= x)} for a Poisson
     * distribution.
     *
     * @param x Value at which the PDF is evaluated.
     * @return the Poisson distribution function evaluated at {@code x}.
     * due to convergence or other numerical errors.
     */
    @Override
    public double cumulativeProbability(int x)  {
        if (x < 0) {
            return 0;
        }
        if (x == Integer.MAX_VALUE) {
            return 1;
        }
        return Gamma.regularizedGammaQ((double) x + 1, mean, epsilon, maxIterations);
    }

    /**
     * Calculates the Poisson distribution function using a normal
     * approximation. The {@code N(mean, sqrt(mean))} distribution is used
     * to approximate the Poisson distribution.
     * The computation uses "half-correction" (evaluating the normal
     * distribution function at {@code x + 0.5}).
     *
     * @param x Upper bound, inclusive.
     * @return the distribution function value calculated using a normal
     * approximation.
     * approximation.
     */
    public double normalApproximateProbability(int x)  {
        // calculate the probability using half-correction
        return normal.cumulativeProbability(x + 0.5);
    }

    /**
     * Generates a random value sampled from this distribution.
     * <br/>
     * <strong>Algorithm Description</strong>:
     * <ul>
     *  <li>For small means, uses simulation of a Poisson process
     *   using Uniform deviates, as described
     *   <a href="http://irmi.epfl.ch/cmos/Pmmi/interactive/rng7.htm"> here</a>.
     *   The Poisson process (and hence value returned) is bounded by 1000 * mean.
     *  </li>
     *  <li>For large means, uses the rejection algorithm described in
     *   <quote>
     *    Devroye, Luc. (1981).<i>The Computer Generation of Poisson Random Variables</i>
     *    <strong>Computing</strong> vol. 26 pp. 197-207.
     *   </quote>
     *  </li>
     * </ul>
     *
     * @return a random value.
     * @since 2.2
     */
    @Override
    public int sample()  {
        return (int) FastMath.min(randomData.nextPoisson(mean), Integer.MAX_VALUE);
    }

    /**
     * Access the domain value lower bound, based on {@code p}, used to
     * bracket a CDF root. This method is used by
     * {@link #inverseCumulativeProbability(double)} to find critical values.
     *
     * @param p Desired probability for the critical value.
     * @return the domain lower bound.
     */
    @Override
    protected int getDomainLowerBound(double p) {
        return 0;
    }

    /**
     * Access the domain value upper bound, based on {@code p}, used to
     * bracket a CDF root. This method is used by
     * {@link #inverseCumulativeProbability(double)} to find critical values.
     *
     * @param p Desired probability for the critical value.
     * @return the domain upper bound.
     */
    @Override
    protected int getDomainUpperBound(double p) {
        return Integer.MAX_VALUE;
    }

    /**
     * {@inheritDoc}
     *
     * The lower bound of the support is always 0 no matter the mean parameter.
     *
     * @return lower bound of the support (always 0)
     */
    @Override
    public int getSupportLowerBound() {
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * The upper bound of the support is positive infinity,
     * regardless of the parameter values. There is no integer infinity,
     * so this method returns <code>Integer.MAX_VALUE</code> and
     * {@link #isSupportUpperBoundInclusive()} returns <code>true</code>.
     *
     * @return upper bound of the support (always <code>Integer.MAX_VALUE</code> for positive infinity)
     */
    @Override
    public int getSupportUpperBound() {
        return Integer.MAX_VALUE;
    }

    /**
     * {@inheritDoc}
     *
     * For mean parameter <code>p</code>, the mean is <code>p</code>
     *
     * @return {@inheritDoc}
     */
    @Override
    protected double calculateNumericalMean() {
        return getMean();
    }

    /**
     * {@inheritDoc}
     *
     * For mean parameter <code>p</code>, the variance is <code>p</code>
     *
     * @return {@inheritDoc}
     */
    @Override
    protected double calculateNumericalVariance() {
        return getMean();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSupportUpperBoundInclusive() {
        return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2620.java