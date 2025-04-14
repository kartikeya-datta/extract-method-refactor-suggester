error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2879.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2879.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2879.java
text:
```scala
n@@, 2, 5);

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
package org.apache.commons.math.analysis.integration;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

/**
 * Implements the <a href="http://mathworld.wolfram.com/Legendre-GaussQuadrature.html">
 * Legendre-Gauss</a> quadrature formula.
 * <p>
 * Legendre-Gauss integrators are efficient integrators that can
 * accurately integrate functions with few functions evaluations. A
 * Legendre-Gauss integrator using an n-points quadrature formula can
 * integrate exactly 2n-1 degree polynomialss.
 * </p>
 * <p>
 * These integrators evaluate the function on n carefully chosen
 * abscissas in each step interval (mapped to the canonical [-1  1] interval).
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
 * @version $Revision$ $Date$
 * @since 1.2
 */

public class LegendreGaussIntegrator extends UnivariateRealIntegratorImpl {

    /** Serializable version identifier. */
    private static final long serialVersionUID = -331962723352824098L;

    /** Abscissas for the 2 points method. */
    private static final double[] ABSCISSAS_2 = {
        -1.0 / Math.sqrt(3.0),
         1.0 / Math.sqrt(3.0)
    };

    /** Weights for the 2 points method. */
    private static final double[] WEIGHTS_2 = {
        1.0,
        1.0
    };

    /** Abscissas for the 3 points method. */
    private static final double[] ABSCISSAS_3 = {
        -Math.sqrt(0.6),
         0.0,
         Math.sqrt(0.6)
    };

    /** Weights for the 3 points method. */
    private static final double[] WEIGHTS_3 = {
        5.0 / 9.0,
        8.0 / 9.0,
        5.0 / 9.0
    };

    /** Abscissas for the 4 points method. */
    private static final double[] ABSCISSAS_4 = {
        -Math.sqrt((15.0 + 2.0 * Math.sqrt(30.0)) / 35.0),
        -Math.sqrt((15.0 - 2.0 * Math.sqrt(30.0)) / 35.0),
         Math.sqrt((15.0 - 2.0 * Math.sqrt(30.0)) / 35.0),
         Math.sqrt((15.0 + 2.0 * Math.sqrt(30.0)) / 35.0)
    };

    /** Weights for the 4 points method. */
    private static final double[] WEIGHTS_4 = {
        (90.0 - 5.0 * Math.sqrt(30.0)) / 180.0,
        (90.0 + 5.0 * Math.sqrt(30.0)) / 180.0,
        (90.0 + 5.0 * Math.sqrt(30.0)) / 180.0,
        (90.0 - 5.0 * Math.sqrt(30.0)) / 180.0
    };

    /** Abscissas for the 5 points method. */
    private static final double[] ABSCISSAS_5 = {
        -Math.sqrt((35.0 + 2.0 * Math.sqrt(70.0)) / 63.0),
        -Math.sqrt((35.0 - 2.0 * Math.sqrt(70.0)) / 63.0),
         0.0,
         Math.sqrt((35.0 - 2.0 * Math.sqrt(70.0)) / 63.0),
         Math.sqrt((35.0 + 2.0 * Math.sqrt(70.0)) / 63.0)
    };

    /** Weights for the 5 points method. */
    private static final double[] WEIGHTS_5 = {
        (322.0 - 13.0 * Math.sqrt(70.0)) / 900.0,
        (322.0 + 13.0 * Math.sqrt(70.0)) / 900.0,
        128.0 / 225.0,
        (322.0 + 13.0 * Math.sqrt(70.0)) / 900.0,
        (322.0 - 13.0 * Math.sqrt(70.0)) / 900.0
    };

    /** Abscissas for the current method. */
    private final double[] abscissas;

    /** Weights for the current method. */
    private final double[] weights;

    /** Build a Legendre-Gauss integrator.
     * @param n number of points desired (must be between 2 and 5 inclusive)
     * @param defaultMaximalIterationCount maximum number of iterations
     * @exception IllegalArgumentException if the number of points is not
     * in the supported range
     */
    public LegendreGaussIntegrator(final int n, final int defaultMaximalIterationCount)
        throws IllegalArgumentException {
        super(defaultMaximalIterationCount);
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
            throw MathRuntimeException.createIllegalArgumentException(
                    "{0} points Legendre-Gauss integrator not supported, " +
                    "number of points must be in the {1}-{2} range",
                    new Object[] { n, 2, 5 });
        }

    }

    /** {@inheritDoc} */
    @Deprecated
    public double integrate(final double min, final double max)
        throws ConvergenceException,  FunctionEvaluationException, IllegalArgumentException {
        return integrate(f, min, max);
    }

    /** {@inheritDoc} */
    public double integrate(final UnivariateRealFunction f,
            final double min, final double max)
        throws ConvergenceException,  FunctionEvaluationException, IllegalArgumentException {
        
        clearResult();
        verifyInterval(min, max);
        verifyIterationCount();

        // compute first estimate with a single step
        double oldt = stage(f, min, max, 1);

        int n = 2;
        for (int i = 0; i < maximalIterationCount; ++i) {

            // improve integral with a larger number of steps
            final double t = stage(f, min, max, n);

            // estimate error
            final double delta = Math.abs(t - oldt);
            final double limit =
                Math.max(absoluteAccuracy,
                         relativeAccuracy * (Math.abs(oldt) + Math.abs(t)) * 0.5);

            // check convergence
            if ((i + 1 >= minimalIterationCount) && (delta <= limit)) {
                setResult(t, i);
                return result;
            }

            // prepare next iteration
            double ratio = Math.min(4, Math.pow(delta / limit, 0.5 / abscissas.length));
            n = Math.max((int) (ratio * n), n + 1);
            oldt = t;

        }

        throw new MaxIterationsExceededException(maximalIterationCount);

    }

    /**
     * Compute the n-th stage integral.
     * @param f the integrand function
     * @param min the lower bound for the interval
     * @param max the upper bound for the interval
     * @param n number of steps
     * @return the value of n-th stage integral
     * @throws FunctionEvaluationException if an error occurs evaluating the
     * function
     */
    private double stage(final UnivariateRealFunction f,
                         final double min, final double max, final int n)
        throws FunctionEvaluationException {

        // set up the step for the current stage
        final double step     = (max - min) / n;
        final double halfStep = step / 2.0;

        // integrate over all elementary steps
        double midPoint = min + halfStep;
        double sum = 0.0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < abscissas.length; ++j) {
                sum += weights[j] * f.value(midPoint + halfStep * abscissas[j]);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2879.java