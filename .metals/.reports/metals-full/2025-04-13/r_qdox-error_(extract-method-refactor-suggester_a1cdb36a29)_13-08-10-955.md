error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6981.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6981.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6981.java
text:
```scala
protected U@@nivariateSolver getSolver() {

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

package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math.analysis.QuinticFunction;
import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.exception.TooManyEvaluationsException;
import org.apache.commons.math.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test case for {@link BracketingNthOrderBrentSolver bracketing n<sup>th</sup> order Brent} solver.
 *
 * @version $Id$
 */
public final class BracketingNthOrderBrentSolverTest extends BaseSecantSolverAbstractTest {
    /** {@inheritDoc} */
    @Override
    protected UnivariateRealSolver getSolver() {
        return new BracketingNthOrderBrentSolver();
    }

    /** {@inheritDoc} */
    @Override
    protected int[] getQuinticEvalCounts() {
        return new int[] {1, 3, 8, 1, 9, 4, 8, 1, 12, 1, 16};
    }

    @Test(expected=NumberIsTooSmallException.class)
    public void testInsufficientOrder1() {
        new BracketingNthOrderBrentSolver(1.0e-10, 1);
    }

    @Test(expected=NumberIsTooSmallException.class)
    public void testInsufficientOrder2() {
        new BracketingNthOrderBrentSolver(1.0e-10, 1.0e-10, 1);
    }

    @Test(expected=NumberIsTooSmallException.class)
    public void testInsufficientOrder3() {
        new BracketingNthOrderBrentSolver(1.0e-10, 1.0e-10, 1.0e-10, 1);
    }

    @Test
    public void testConstructorsOK() {
        Assert.assertEquals(2, new BracketingNthOrderBrentSolver(1.0e-10, 2).getMaximalOrder());
        Assert.assertEquals(2, new BracketingNthOrderBrentSolver(1.0e-10, 1.0e-10, 2).getMaximalOrder());
        Assert.assertEquals(2, new BracketingNthOrderBrentSolver(1.0e-10, 1.0e-10, 1.0e-10, 2).getMaximalOrder());
    }

    @Test
    public void testConvergenceOnFunctionAccuracy() {
        BracketingNthOrderBrentSolver solver =
                new BracketingNthOrderBrentSolver(1.0e-12, 1.0e-10, 0.001, 3);
        QuinticFunction f = new QuinticFunction();
        double result = solver.solve(20, f, 0.2, 0.9, 0.4, AllowedSolution.BELOW_SIDE);
        Assert.assertEquals(0, f.value(result), solver.getFunctionValueAccuracy());
        Assert.assertTrue(f.value(result) <= 0);
        Assert.assertTrue(result - 0.5 > solver.getAbsoluteAccuracy());
        result = solver.solve(20, f, -0.9, -0.2,  -0.4, AllowedSolution.ABOVE_SIDE);
        Assert.assertEquals(0, f.value(result), solver.getFunctionValueAccuracy());
        Assert.assertTrue(f.value(result) >= 0);
        Assert.assertTrue(result + 0.5 < -solver.getAbsoluteAccuracy());
    }

    @Test
    public void testIssue716() {
        BracketingNthOrderBrentSolver solver =
                new BracketingNthOrderBrentSolver(1.0e-12, 1.0e-10, 1.0e-22, 5);
        UnivariateFunction sharpTurn = new UnivariateFunction() {
            public double value(double x) {
                return (2 * x + 1) / (1.0e9 * (x + 1));
            }
        };
        double result = solver.solve(100, sharpTurn, -0.9999999, 30, 15, AllowedSolution.RIGHT_SIDE);
        Assert.assertEquals(0, sharpTurn.value(result), solver.getFunctionValueAccuracy());
        Assert.assertTrue(sharpTurn.value(result) >= 0);
        Assert.assertEquals(-0.5, result, 1.0e-10);
    }

    @Test
    public void testFasterThanNewton() {
        // the following test functions come from Beny Neta's paper:
        // "Several New Methods for solving Equations"
        // intern J. Computer Math Vol 23 pp 265-282
        // available here: http://www.math.nps.navy.mil/~bneta/SeveralNewMethods.PDF
        // the reference roots have been computed by the Dfp solver to more than
        // 80 digits and checked with emacs (only the first 20 digits are reproduced here)
        compare(new TestFunction(0.0, -2, 2) {
            @Override
            public double value(double x)      { return FastMath.sin(x) - 0.5 * x; }
            @Override
            public double derivative(double x) { return FastMath.cos(x) - 0.5; }
        });
        compare(new TestFunction(6.3087771299726890947, -5, 10) {
            @Override
            public double value(double x)      { return FastMath.pow(x, 5) + x - 10000; }
            @Override
            public double derivative(double x) { return 5 * FastMath.pow(x, 4) + 1; }
        });
        compare(new TestFunction(9.6335955628326951924, 0.001, 10) {
            @Override
            public double value(double x)      { return FastMath.sqrt(x) - 1 / x - 3; }
            @Override
            public double derivative(double x) { return 0.5 / FastMath.sqrt(x) + 1 / (x * x); }
        });
        compare(new TestFunction(2.8424389537844470678, -5, 5) {
            @Override
            public double value(double x)      { return FastMath.exp(x) + x - 20; }
            @Override
            public double derivative(double x) { return FastMath.exp(x) + 1; }
        });
        compare(new TestFunction(8.3094326942315717953, 0.001, 10) {
            @Override
            public double value(double x)      { return FastMath.log(x) + FastMath.sqrt(x) - 5; }
            @Override
            public double derivative(double x) { return 1 / x + 0.5 / FastMath.sqrt(x); }
        });
        compare(new TestFunction(1.4655712318767680266, -0.5, 1.5) {
            @Override
            public double value(double x)      { return (x - 1) * x * x - 1; }
            @Override
            public double derivative(double x) { return (3 * x - 2) * x; }
        });

    }

    private void compare(TestFunction f) {
        compare(f, f.getRoot(), f.getMin(), f.getMax());
    }

    private void compare(DifferentiableUnivariateFunction f,
                         double root, double min, double max) {
        NewtonSolver newton = new NewtonSolver(1.0e-12);
        BracketingNthOrderBrentSolver bracketing =
                new BracketingNthOrderBrentSolver(1.0e-12, 1.0e-12, 1.0e-18, 5);
        double resultN;
        try {
            resultN = newton.solve(100, f, min, max);
        } catch (TooManyEvaluationsException tmee) {
            resultN = Double.NaN;
        }
        double resultB;
        try {
            resultB = bracketing.solve(100, f, min, max);
        } catch (TooManyEvaluationsException tmee) {
            resultB = Double.NaN;
        }
        Assert.assertEquals(root, resultN, newton.getAbsoluteAccuracy());
        Assert.assertEquals(root, resultB, bracketing.getAbsoluteAccuracy());
        Assert.assertTrue(bracketing.getEvaluations() < newton.getEvaluations());
    }

    private static abstract class TestFunction implements DifferentiableUnivariateFunction {

        private final double root;
        private final double min;
        private final double max;

        protected TestFunction(final double root, final double min, final double max) {
            this.root = root;
            this.min  = min;
            this.max  = max;
        }

        public double getRoot() {
            return root;
        }

        public double getMin() {
            return min;
        }

        public double getMax() {
            return max;
        }

        public abstract double value(double x);

        public abstract double derivative(double x);

        public UnivariateFunction derivative() {
            return new UnivariateFunction() {
                public double value(double x) {
                     return derivative(x);
                }
            };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6981.java