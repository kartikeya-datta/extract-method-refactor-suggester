error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8773.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8773.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8773.java
text:
```scala
r@@eturn (FastMath.expm1(ss)) * FastMath.exp(2 * scale + ss);

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

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

/**
 * Implementation of the log-normal (gaussian) distribution.
 *
 * <p>
 * <strong>Parameters:</strong>
 * {@code X} is log-normally distributed if its natural logarithm {@code log(X)}
 * is normally distributed. The probability distribution function of {@code X}
 * is given by (for {@code x > 0})
 * </p>
 * <p>
 * {@code exp(-0.5 * ((ln(x) - m) / s)^2) / (s * sqrt(2 * pi) * x)}
 * </p>
 * <ul>
 * <li>{@code m} is the <em>scale</em> parameter: this is the mean of the
 * normally distributed natural logarithm of this distribution,</li>
 * <li>{@code s} is the <em>shape</em> parameter: this is the standard
 * deviation of the normally distributed natural logarithm of this
 * distribution.
 * </ul>
 *
 * @see <a href="http://en.wikipedia.org/wiki/Log-normal_distribution">
 * Log-normal distribution (Wikipedia)</a>
 * @see <a href="http://mathworld.wolfram.com/LogNormalDistribution.html">
 * Log Normal distribution (MathWorld)</a>
 *
 * @version $Id$
 * @since 3.0
 */
public class LogNormalDistribution extends AbstractRealDistribution {
    /** Default inverse cumulative probability accuracy. */
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1e-9;

    /** Serializable version identifier. */
    private static final long serialVersionUID = 20120112;

    /** &radic;(2 &pi;) */
    private static final double SQRT2PI = FastMath.sqrt(2 * FastMath.PI);

    /** &radic;(2) */
    private static final double SQRT2 = FastMath.sqrt(2.0);

    /** The scale parameter of this distribution. */
    private final double scale;

    /** The shape parameter of this distribution. */
    private final double shape;
    /** The value of {@code log(shape) + 0.5 * log(2*PI)} stored for faster computation. */
    private final double logShapePlusHalfLog2Pi;

    /** Inverse cumulative probability accuracy. */
    private final double solverAbsoluteAccuracy;

    /**
     * Create a log-normal distribution, where the mean and standard deviation
     * of the {@link NormalDistribution normally distributed} natural
     * logarithm of the log-normal distribution are equal to zero and one
     * respectively. In other words, the scale of the returned distribution is
     * {@code 0}, while its shape is {@code 1}.
     */
    public LogNormalDistribution() {
        this(0, 1);
    }

    /**
     * Create a log-normal distribution using the specified scale and shape.
     *
     * @param scale the scale parameter of this distribution
     * @param shape the shape parameter of this distribution
     * @throws NotStrictlyPositiveException if {@code shape <= 0}.
     */
    public LogNormalDistribution(double scale, double shape)
        throws NotStrictlyPositiveException {
        this(scale, shape, DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
    }

    /**
     * Create a log-normal distribution using the specified scale, shape and
     * inverse cumulative distribution accuracy.
     *
     * @param scale the scale parameter of this distribution
     * @param shape the shape parameter of this distribution
     * @param inverseCumAccuracy Inverse cumulative probability accuracy.
     * @throws NotStrictlyPositiveException if {@code shape <= 0}.
     */
    public LogNormalDistribution(double scale, double shape, double inverseCumAccuracy)
        throws NotStrictlyPositiveException {
        this(new Well19937c(), scale, shape, inverseCumAccuracy);
    }

    /**
     * Creates a log-normal distribution.
     *
     * @param rng Random number generator.
     * @param scale Scale parameter of this distribution.
     * @param shape Shape parameter of this distribution.
     * @throws NotStrictlyPositiveException if {@code shape <= 0}.
     * @since 3.3
     */
    public LogNormalDistribution(RandomGenerator rng, double scale, double shape)
        throws NotStrictlyPositiveException {
        this(rng, scale, shape, DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
    }

    /**
     * Creates a log-normal distribution.
     *
     * @param rng Random number generator.
     * @param scale Scale parameter of this distribution.
     * @param shape Shape parameter of this distribution.
     * @param inverseCumAccuracy Inverse cumulative probability accuracy.
     * @throws NotStrictlyPositiveException if {@code shape <= 0}.
     * @since 3.1
     */
    public LogNormalDistribution(RandomGenerator rng,
                                 double scale,
                                 double shape,
                                 double inverseCumAccuracy)
        throws NotStrictlyPositiveException {
        super(rng);

        if (shape <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.SHAPE, shape);
        }

        this.scale = scale;
        this.shape = shape;
        this.logShapePlusHalfLog2Pi = FastMath.log(shape) + 0.5 * FastMath.log(2 * FastMath.PI);
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    /**
     * Returns the scale parameter of this distribution.
     *
     * @return the scale parameter
     */
    public double getScale() {
        return scale;
    }

    /**
     * Returns the shape parameter of this distribution.
     *
     * @return the shape parameter
     */
    public double getShape() {
        return shape;
    }

    /**
     * {@inheritDoc}
     *
     * For scale {@code m}, and shape {@code s} of this distribution, the PDF
     * is given by
     * <ul>
     * <li>{@code 0} if {@code x <= 0},</li>
     * <li>{@code exp(-0.5 * ((ln(x) - m) / s)^2) / (s * sqrt(2 * pi) * x)}
     * otherwise.</li>
     * </ul>
     */
    public double density(double x) {
        if (x <= 0) {
            return 0;
        }
        final double x0 = FastMath.log(x) - scale;
        final double x1 = x0 / shape;
        return FastMath.exp(-0.5 * x1 * x1) / (shape * SQRT2PI * x);
    }

    /** {@inheritDoc}
     *
     * See documentation of {@link #density(double)} for computation details.
     */
    @Override
    public double logDensity(double x) {
        if (x <= 0) {
            return Double.NEGATIVE_INFINITY;
        }
        final double logX = FastMath.log(x);
        final double x0 = logX - scale;
        final double x1 = x0 / shape;
        return -0.5 * x1 * x1 - (logShapePlusHalfLog2Pi + logX);
    }

    /**
     * {@inheritDoc}
     *
     * For scale {@code m}, and shape {@code s} of this distribution, the CDF
     * is given by
     * <ul>
     * <li>{@code 0} if {@code x <= 0},</li>
     * <li>{@code 0} if {@code ln(x) - m < 0} and {@code m - ln(x) > 40 * s}, as
     * in these cases the actual value is within {@code Double.MIN_VALUE} of 0,
     * <li>{@code 1} if {@code ln(x) - m >= 0} and {@code ln(x) - m > 40 * s},
     * as in these cases the actual value is within {@code Double.MIN_VALUE} of
     * 1,</li>
     * <li>{@code 0.5 + 0.5 * erf((ln(x) - m) / (s * sqrt(2))} otherwise.</li>
     * </ul>
     */
    public double cumulativeProbability(double x)  {
        if (x <= 0) {
            return 0;
        }
        final double dev = FastMath.log(x) - scale;
        if (FastMath.abs(dev) > 40 * shape) {
            return dev < 0 ? 0.0d : 1.0d;
        }
        return 0.5 + 0.5 * Erf.erf(dev / (shape * SQRT2));
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated See {@link RealDistribution#cumulativeProbability(double,double)}
     */
    @Override@Deprecated
    public double cumulativeProbability(double x0, double x1)
        throws NumberIsTooLargeException {
        return probability(x0, x1);
    }

    /** {@inheritDoc} */
    @Override
    public double probability(double x0,
                              double x1)
        throws NumberIsTooLargeException {
        if (x0 > x1) {
            throw new NumberIsTooLargeException(LocalizedFormats.LOWER_ENDPOINT_ABOVE_UPPER_ENDPOINT,
                                                x0, x1, true);
        }
        if (x0 <= 0 || x1 <= 0) {
            return super.probability(x0, x1);
        }
        final double denom = shape * SQRT2;
        final double v0 = (FastMath.log(x0) - scale) / denom;
        final double v1 = (FastMath.log(x1) - scale) / denom;
        return 0.5 * Erf.erf(v0, v1);
    }

    /** {@inheritDoc} */
    @Override
    protected double getSolverAbsoluteAccuracy() {
        return solverAbsoluteAccuracy;
    }

    /**
     * {@inheritDoc}
     *
     * For scale {@code m} and shape {@code s}, the mean is
     * {@code exp(m + s^2 / 2)}.
     */
    public double getNumericalMean() {
        double s = shape;
        return FastMath.exp(scale + (s * s / 2));
    }

    /**
     * {@inheritDoc}
     *
     * For scale {@code m} and shape {@code s}, the variance is
     * {@code (exp(s^2) - 1) * exp(2 * m + s^2)}.
     */
    public double getNumericalVariance() {
        final double s = shape;
        final double ss = s * s;
        return (FastMath.exp(ss) - 1) * FastMath.exp(2 * scale + ss);
    }

    /**
     * {@inheritDoc}
     *
     * The lower bound of the support is always 0 no matter the parameters.
     *
     * @return lower bound of the support (always 0)
     */
    public double getSupportLowerBound() {
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * The upper bound of the support is always positive infinity
     * no matter the parameters.
     *
     * @return upper bound of the support (always
     * {@code Double.POSITIVE_INFINITY})
     */
    public double getSupportUpperBound() {
        return Double.POSITIVE_INFINITY;
    }

    /** {@inheritDoc} */
    public boolean isSupportLowerBoundInclusive() {
        return true;
    }

    /** {@inheritDoc} */
    public boolean isSupportUpperBoundInclusive() {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * The support of this distribution is connected.
     *
     * @return {@code true}
     */
    public boolean isSupportConnected() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public double sample()  {
        final double n = random.nextGaussian();
        return FastMath.exp(scale + shape * n);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8773.java