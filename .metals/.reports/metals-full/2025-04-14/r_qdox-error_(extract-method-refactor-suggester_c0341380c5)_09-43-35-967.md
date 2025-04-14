error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7751.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7751.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 885
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7751.java
text:
```scala
public class LegendreGaussIntegrator extends BaseAbstractUnivariateIntegrator {

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
p@@ackage org.apache.commons.math.analysis.integration;

import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.exception.MaxCountExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.exception.TooManyEvaluationsException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.util.FastMath;

/**
 * Implements the <a href="http://mathworld.wolfram.com/Legendre-GaussQuadrature.html">
 * Legendre-Gauss</a> quadrature formula.
 * <p>
 * Legendre-Gauss integrators are efficient integrators that can
 * accurately integrate functions with few function evaluations. A
 * Legendre-Gauss integrator using an n-points quadrature formula can
 * integrate 2n-1 degree polynomials exactly.
 * </p>
 * <p>
 * These integrators evaluate the function on n carefully chosen
 * abscissas in each step interval (mapped to the canonical [-1,1] interval).
 * The evaluation abscissas are not evenly spaced and none of them are
 * at the interval endpoints. This implies the function integrated can be
 * undefined at integration interval endpoints.
 * </p>
 * <p>
 * The evaluation abscissas x<sub>i</sub> are the roots of the degree n
 * Legendre polynomial. The weights a<sub>i</sub> of the quadrature formula
 * integrals from -1 to +1 &int; Li<sup>2</sup> where Li (x) =
 * &prod; (x-x<sub>k</sub>)/(x<sub>i</sub>-x<sub>k</sub>) for k != i.
 * </p>
 * <p>
 * @version $Id$
 * @since 1.2
 */

public class LegendreGaussIntegrator extends UnivariateRealIntegratorImpl {

    /** Abscissas for the 2 points method. */
    private static final double[] ABSCISSAS_2 = {
        -1.0 / FastMath.sqrt(3.0),
         1.0 / FastMath.sqrt(3.0)
    };

    /** Weights for the 2 points method. */
    private static final double[] WEIGHTS_2 = {
        1.0,
        1.0
    };

    /** Abscissas for the 3 points method. */
    private static final double[] ABSCISSAS_3 = {
        -FastMath.sqrt(0.6),
         0.0,
         FastMath.sqrt(0.6)
    };

    /** Weights for the 3 points method. */
    private static final double[] WEIGHTS_3 = {
        5.0 / 9.0,
        8.0 / 9.0,
        5.0 / 9.0
    };

    /** Abscissas for the 4 points method. */
    private static final double[] ABSCISSAS_4 = {
        -FastMath.sqrt((15.0 + 2.0 * FastMath.sqrt(30.0)) / 35.0),
        -FastMath.sqrt((15.0 - 2.0 * FastMath.sqrt(30.0)) / 35.0),
         FastMath.sqrt((15.0 - 2.0 * FastMath.sqrt(30.0)) / 35.0),
         FastMath.sqrt((15.0 + 2.0 * FastMath.sqrt(30.0)) / 35.0)
    };

    /** Weights for the 4 points method. */
    private static final double[] WEIGHTS_4 = {
        (90.0 - 5.0 * FastMath.sqrt(30.0)) / 180.0,
        (90.0 + 5.0 * FastMath.sqrt(30.0)) / 180.0,
        (90.0 + 5.0 * FastMath.sqrt(30.0)) / 180.0,
        (90.0 - 5.0 * FastMath.sqrt(30.0)) / 180.0
    };

    /** Abscissas for the 5 points method. */
    private static final double[] ABSCISSAS_5 = {
        -FastMath.sqrt((35.0 + 2.0 * FastMath.sqrt(70.0)) / 63.0),
        -FastMath.sqrt((35.0 - 2.0 * FastMath.sqrt(70.0)) / 63.0),
         0.0,
         FastMath.sqrt((35.0 - 2.0 * FastMath.sqrt(70.0)) / 63.0),
         FastMath.sqrt((35.0 + 2.0 * FastMath.sqrt(70.0)) / 63.0)
    };

    /** Weights for the 5 points method. */
    private static final double[] WEIGHTS_5 = {
        (322.0 - 13.0 * FastMath.sqrt(70.0)) / 900.0,
        (322.0 + 13.0 * FastMath.sqrt(70.0)) / 900.0,
        128.0 / 225.0,
        (322.0 + 13.0 * FastMath.sqrt(70.0)) / 900.0,
        (322.0 - 13.0 * FastMath.sqrt(70.0)) / 900.0
    };

    /** Abscissas for the current method. */
    private final double[] abscissas;

    /** Weights for the current method. */
    private final double[] weights;

    /**
     * Build a Legendre-Gauss integrator with given accuracies and iterations counts.
     * @param n number of points desired (must be between 2 and 5 inclusive)
     * @param relativeAccuracy relative accuracy of the result
     * @param absoluteAccuracy absolute accuracy of the result
     * @param minimalIterationCount minimum number of iterations
     * @param maximalIterationCount maximum number of iterations
     * @exception NotStrictlyPositiveException if minimal number of iterations
     * is not strictly positive
     * @exception NumberIsTooSmallException if maximal number of iterations
     * is lesser than or equal to the minimal number of iterations
     */
    public LegendreGaussIntegrator(final int n,
                                   final double relativeAccuracy,
                                   final double absoluteAccuracy,
                                   final int minimalIterationCount,
                                   final int maximalIterationCount)
        throws NotStrictlyPositiveException, NumberIsTooSmallException {
        super(relativeAccuracy, absoluteAccuracy, minimalIterationCount, maximalIterationCount);
        switch(n) {
        case 2 :
            abscissas = ABSCISSAS_2;
            weights   = WEIGHTS_2;
            break;
        case 3 :
            abscissas = ABSCISSAS_3;
            weights   = WEIGHTS_3;
            break;
        case 4 :
            abscissas = ABSCISSAS_4;
            weights   = WEIGHTS_4;
            break;
        case 5 :
            abscissas = ABSCISSAS_5;
            weights   = WEIGHTS_5;
            break;
        default :
            throw new MathIllegalArgumentException(
                    LocalizedFormats.N_POINTS_GAUSS_LEGENDRE_INTEGRATOR_NOT_SUPPORTED,
                    n, 2, 5);
        }

    }

    /**
     * Build a Legendre-Gauss integrator with given accuracies.
     * @param n number of points desired (must be between 2 and 5 inclusive)
     * @param relativeAccuracy relative accuracy of the result
     * @param absoluteAccuracy absolute accuracy of the result
     */
    public LegendreGaussIntegrator(final int n,
                                   final double relativeAccuracy,
                                   final double absoluteAccuracy) {
        this(n, relativeAccuracy, absoluteAccuracy,
             DEFAULT_MIN_ITERATIONS_COUNT, DEFAULT_MAX_ITERATIONS_COUNT);
    }

    /**
     * Build a Legendre-Gauss integrator with given iteration counts.
     * @param n number of points desired (must be between 2 and 5 inclusive)
     * @param minimalIterationCount minimum number of iterations
     * @param maximalIterationCount maximum number of iterations
     * @exception NotStrictlyPositiveException if minimal number of iterations
     * is not strictly positive
     * @exception NumberIsTooSmallException if maximal number of iterations
     * is lesser than or equal to the minimal number of iterations
     */
    public LegendreGaussIntegrator(final int n,
                                   final int minimalIterationCount,
                                   final int maximalIterationCount) {
        this(n, DEFAULT_RELATIVE_ACCURACY, DEFAULT_ABSOLUTE_ACCURACY,
             minimalIterationCount, maximalIterationCount);
    }

    /** {@inheritDoc} */
    @Override
    protected double doIntegrate()
        throws TooManyEvaluationsException, MaxCountExceededException {

        // compute first estimate with a single step
        double oldt = stage(1);

        int n = 2;
        while (true) {

            // improve integral with a larger number of steps
            final double t = stage(n);

            // estimate error
            final double delta = FastMath.abs(t - oldt);
            final double limit =
                FastMath.max(absoluteAccuracy,
                         relativeAccuracy * (FastMath.abs(oldt) + FastMath.abs(t)) * 0.5);

            // check convergence
            if ((iterations.getCount() + 1 >= minimalIterationCount) && (delta <= limit)) {
                return t;
            }

            // prepare next iteration
            double ratio = FastMath.min(4, FastMath.pow(delta / limit, 0.5 / abscissas.length));
            n = FastMath.max((int) (ratio * n), n + 1);
            oldt = t;
            iterations.incrementCount();

        }

    }

    /**
     * Compute the n-th stage integral.
     * @param n number of steps
     * @return the value of n-th stage integral
     * @throws TooManyEvaluationsException if the maximum number of evaluations
     * is exceeded.
     */
    private double stage(final int n)
        throws TooManyEvaluationsException {

        // set up the step for the current stage
        final double step     = (max - min) / n;
        final double halfStep = step / 2.0;

        // integrate over all elementary steps
        double midPoint = min + halfStep;
        double sum = 0.0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < abscissas.length; ++j) {
                sum += weights[j] * computeObjectiveValue(midPoint + halfStep * abscissas[j]);
            }
            midPoint += step;
        }

        return halfStep * sum;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7751.java