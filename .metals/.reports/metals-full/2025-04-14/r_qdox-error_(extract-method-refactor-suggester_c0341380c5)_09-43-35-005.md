error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6976.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6976.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 866
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6976.java
text:
```scala
public class MullerSolver extends AbstractUnivariateSolver {

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
p@@ackage org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.util.FastMath;

/**
 * This class implements the <a href="http://mathworld.wolfram.com/MullersMethod.html">
 * Muller's Method</a> for root finding of real univariate functions. For
 * reference, see <b>Elementary Numerical Analysis</b>, ISBN 0070124477,
 * chapter 3.
 * <p>
 * Muller's method applies to both real and complex functions, but here we
 * restrict ourselves to real functions.
 * This class differs from {@link MullerSolver} in the way it avoids complex
 * operations.</p>
 * Muller's original method would have function evaluation at complex point.
 * Since our f(x) is real, we have to find ways to avoid that. Bracketing
 * condition is one way to go: by requiring bracketing in every iteration,
 * the newly computed approximation is guaranteed to be real.</p>
 * <p>
 * Normally Muller's method converges quadratically in the vicinity of a
 * zero, however it may be very slow in regions far away from zeros. For
 * example, f(x) = exp(x) - 1, min = -50, max = 100. In such case we use
 * bisection as a safety backup if it performs very poorly.</p>
 * <p>
 * The formulas here use divided differences directly.</p>
 *
 * @version $Id$
 * @since 1.2
 * @see MullerSolver2
 */
public class MullerSolver extends AbstractUnivariateRealSolver {

    /** Default absolute accuracy. */
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1e-6;

    /**
     * Construct a solver with default accuracy (1e-6).
     */
    public MullerSolver() {
        this(DEFAULT_ABSOLUTE_ACCURACY);
    }
    /**
     * Construct a solver.
     *
     * @param absoluteAccuracy Absolute accuracy.
     */
    public MullerSolver(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }
    /**
     * Construct a solver.
     *
     * @param relativeAccuracy Relative accuracy.
     * @param absoluteAccuracy Absolute accuracy.
     */
    public MullerSolver(double relativeAccuracy,
                        double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double doSolve() {
        final double min = getMin();
        final double max = getMax();
        final double initial = getStartValue();

        final double functionValueAccuracy = getFunctionValueAccuracy();

        verifySequence(min, initial, max);

        // check for zeros before verifying bracketing
        final double fMin = computeObjectiveValue(min);
        if (FastMath.abs(fMin) < functionValueAccuracy) {
            return min;
        }
        final double fMax = computeObjectiveValue(max);
        if (FastMath.abs(fMax) < functionValueAccuracy) {
            return max;
        }
        final double fInitial = computeObjectiveValue(initial);
        if (FastMath.abs(fInitial) <  functionValueAccuracy) {
            return initial;
        }

        verifyBracketing(min, max);

        if (isBracketing(min, initial)) {
            return solve(min, initial, fMin, fInitial);
        } else {
            return solve(initial, max, fInitial, fMax);
        }
    }

    /**
     * Find a real root in the given interval.
     *
     * @param min Lower bound for the interval.
     * @param max Upper bound for the interval.
     * @param fMin function value at the lower bound.
     * @param fMax function value at the upper bound.
     * @return the point at which the function value is zero.
     */
    private double solve(double min, double max,
                         double fMin, double fMax) {
        final double relativeAccuracy = getRelativeAccuracy();
        final double absoluteAccuracy = getAbsoluteAccuracy();
        final double functionValueAccuracy = getFunctionValueAccuracy();

        // [x0, x2] is the bracketing interval in each iteration
        // x1 is the last approximation and an interpolation point in (x0, x2)
        // x is the new root approximation and new x1 for next round
        // d01, d12, d012 are divided differences

        double x0 = min;
        double y0 = fMin;
        double x2 = max;
        double y2 = fMax;
        double x1 = 0.5 * (x0 + x2);
        double y1 = computeObjectiveValue(x1);

        double oldx = Double.POSITIVE_INFINITY;
        while (true) {
            // Muller's method employs quadratic interpolation through
            // x0, x1, x2 and x is the zero of the interpolating parabola.
            // Due to bracketing condition, this parabola must have two
            // real roots and we choose one in [x0, x2] to be x.
            final double d01 = (y1 - y0) / (x1 - x0);
            final double d12 = (y2 - y1) / (x2 - x1);
            final double d012 = (d12 - d01) / (x2 - x0);
            final double c1 = d01 + (x1 - x0) * d012;
            final double delta = c1 * c1 - 4 * y1 * d012;
            final double xplus = x1 + (-2.0 * y1) / (c1 + FastMath.sqrt(delta));
            final double xminus = x1 + (-2.0 * y1) / (c1 - FastMath.sqrt(delta));
            // xplus and xminus are two roots of parabola and at least
            // one of them should lie in (x0, x2)
            final double x = isSequence(x0, xplus, x2) ? xplus : xminus;
            final double y = computeObjectiveValue(x);

            // check for convergence
            final double tolerance = FastMath.max(relativeAccuracy * FastMath.abs(x), absoluteAccuracy);
            if (FastMath.abs(x - oldx) <= tolerance ||
                FastMath.abs(y) <= functionValueAccuracy) {
                return x;
            }

            // Bisect if convergence is too slow. Bisection would waste
            // our calculation of x, hopefully it won't happen often.
            // the real number equality test x == x1 is intentional and
            // completes the proximity tests above it
            boolean bisect = (x < x1 && (x1 - x0) > 0.95 * (x2 - x0)) ||
                             (x > x1 && (x2 - x1) > 0.95 * (x2 - x0)) ||
                             (x == x1);
            // prepare the new bracketing interval for next iteration
            if (!bisect) {
                x0 = x < x1 ? x0 : x1;
                y0 = x < x1 ? y0 : y1;
                x2 = x > x1 ? x2 : x1;
                y2 = x > x1 ? y2 : y1;
                x1 = x; y1 = y;
                oldx = x;
            } else {
                double xm = 0.5 * (x0 + x2);
                double ym = computeObjectiveValue(xm);
                if (FastMath.signum(y0) + FastMath.signum(ym) == 0.0) {
                    x2 = xm; y2 = ym;
                } else {
                    x0 = xm; y0 = ym;
                }
                x1 = 0.5 * (x0 + x2);
                y1 = computeObjectiveValue(x1);
                oldx = Double.POSITIVE_INFINITY;
            }
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6976.java