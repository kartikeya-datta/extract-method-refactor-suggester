error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8531.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8531.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8531.java
text:
```scala
B@@OBYQAOptimizer optim = new BOBYQAOptimizer(2 * dim + 1);

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
package org.apache.commons.math.optimization.direct;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.math.analysis.MultivariateRealFunction;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.TooManyEvaluationsException;
import org.apache.commons.math.exception.NumberIsTooLargeException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.MultivariateRealOptimizer;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link BOBYQAOptimizer}.
 */
public class BOBYQAOptimizerTest {

    static final int DIM = 13;
   
    @Test(expected=NumberIsTooLargeException.class)
    public void testInitOutOfBounds() {
        double[] startPoint = point(DIM, 3);
        double[][] boundaries = boundaries(DIM, -1, 2);
        doTest(new Rosen(), startPoint, boundaries,
                GoalType.MINIMIZE, 
                1e-13, 1e-6, 2000, null);
    }
    
    @Test(expected=DimensionMismatchException.class)
    public void testBoundariesDimensionMismatch() {
        double[] startPoint = point(DIM, 0.5);
        double[][] boundaries = boundaries(DIM + 1, -1, 2);
        doTest(new Rosen(), startPoint, boundaries,
               GoalType.MINIMIZE, 
               1e-13, 1e-6, 2000, null);
    }

    @Test(expected=NumberIsTooSmallException.class)
    public void testProblemDimensionTooSmall() {
        double[] startPoint = point(1, 0.5);
        doTest(new Rosen(), startPoint, null,
               GoalType.MINIMIZE,
               1e-13, 1e-6, 2000, null);
    }

    @Test(expected=TooManyEvaluationsException.class)
    public void testMaxEvaluations() {
        final int lowMaxEval = 2;
        double[] startPoint = point(DIM, 0.1);
        double[][] boundaries = null;
        doTest(new Rosen(), startPoint, boundaries,
               GoalType.MINIMIZE, 
               1e-13, 1e-6, lowMaxEval, null);
     }

    @Test
    public void testRosen() {
        double[] startPoint = point(DIM,0.1);
        double[][] boundaries = null;
        RealPointValuePair expected = new RealPointValuePair(point(DIM,1.0),0.0);
        doTest(new Rosen(), startPoint, boundaries,
                GoalType.MINIMIZE, 
                1e-13, 1e-6, 2000, expected);
     }

    @Test
    public void testMaximize() {
        double[] startPoint = point(DIM,1.0);
        double[][] boundaries = null;
        RealPointValuePair expected = new RealPointValuePair(point(DIM,0.0),1.0);
        doTest(new MinusElli(), startPoint, boundaries,
                GoalType.MAXIMIZE, 
                2e-10, 5e-6, 1000, expected);
        boundaries = boundaries(DIM,-0.3,0.3); 
        startPoint = point(DIM,0.1);
        doTest(new MinusElli(), startPoint, boundaries,
                GoalType.MAXIMIZE, 
                2e-10, 5e-6, 1000, expected);
    }

    @Test
    public void testEllipse() {
        double[] startPoint = point(DIM,1.0);
        double[][] boundaries = null;
        RealPointValuePair expected =
            new RealPointValuePair(point(DIM,0.0),0.0);
        doTest(new Elli(), startPoint, boundaries,
                GoalType.MINIMIZE, 
                1e-13, 1e-6, 1000, expected);
     }

    @Test
    public void testElliRotated() {
        double[] startPoint = point(DIM,1.0);
        double[][] boundaries = null;
        RealPointValuePair expected =
            new RealPointValuePair(point(DIM,0.0),0.0);
        doTest(new ElliRotated(), startPoint, boundaries,
                GoalType.MINIMIZE, 
                1e-12, 1e-6, 10000, expected);
    }

    @Test
    public void testCigar() {
        double[] startPoint = point(DIM,1.0);
        double[][] boundaries = null;
        RealPointValuePair expected =
            new RealPointValuePair(point(DIM,0.0),0.0);
        doTest(new Cigar(), startPoint, boundaries,
                GoalType.MINIMIZE, 
                1e-13, 1e-6, 100, expected);
    }

    @Test
    public void testTwoAxes() {
        double[] startPoint = point(DIM,1.0);
        double[][] boundaries = null;
        RealPointValuePair expected =
            new RealPointValuePair(point(DIM,0.0),0.0);
        doTest(new TwoAxes(), startPoint, boundaries,
                GoalType.MINIMIZE, 2*
                1e-13, 1e-6, 100, expected);
     }

    @Test
    public void testCigTab() {
        double[] startPoint = point(DIM,1.0);
        double[][] boundaries = null;
        RealPointValuePair expected =
            new RealPointValuePair(point(DIM,0.0),0.0);
        doTest(new CigTab(), startPoint, boundaries,
                GoalType.MINIMIZE, 
                1e-13, 5e-5, 100, expected);
     }

    @Test
    public void testSphere() {
        double[] startPoint = point(DIM,1.0);
        double[][] boundaries = null;
        RealPointValuePair expected =
            new RealPointValuePair(point(DIM,0.0),0.0);
        doTest(new Sphere(), startPoint, boundaries,
                GoalType.MINIMIZE, 
                1e-13, 1e-6, 100, expected);
    }

    @Test
    public void testTablet() {
        double[] startPoint = point(DIM,1.0); 
        double[][] boundaries = null;
        RealPointValuePair expected =
            new RealPointValuePair(point(DIM,0.0),0.0);
        doTest(new Tablet(), startPoint, boundaries,
                GoalType.MINIMIZE, 
                1e-13, 1e-6, 100, expected);
    }

    @Test
    public void testDiffPow() {
        double[] startPoint = point(DIM/2,1.0);
        double[][] boundaries = null;
        RealPointValuePair expected =
            new RealPointValuePair(point(DIM/2,0.0),0.0);
        doTest(new DiffPow(), startPoint, boundaries,
                GoalType.MINIMIZE, 
                1e-8, 1e-1, 12000, expected);
    }

    @Test
    public void testSsDiffPow() {
        double[] startPoint = point(DIM/2,1.0);
        double[][] boundaries = null;
        RealPointValuePair expected =
            new RealPointValuePair(point(DIM/2,0.0),0.0);
        doTest(new SsDiffPow(), startPoint, boundaries,
                GoalType.MINIMIZE, 
                1e-2, 1.3e-1, 50000, expected);
    }

    @Test
    public void testAckley() {
        double[] startPoint = point(DIM,0.1);
        double[][] boundaries = null;
        RealPointValuePair expected =
            new RealPointValuePair(point(DIM,0.0),0.0);
        doTest(new Ackley(), startPoint, boundaries,
                GoalType.MINIMIZE,
                1e-8, 1e-5, 1000, expected);
    }

    @Test
    public void testRastrigin() {
        double[] startPoint = point(DIM,1.0);

        double[][] boundaries = null;
        RealPointValuePair expected =
            new RealPointValuePair(point(DIM,0.0),0.0);
        doTest(new Rastrigin(), startPoint, boundaries,
                GoalType.MINIMIZE, 
                1e-13, 1e-6, 1000, expected);
    }

    @Test
    public void testConstrainedRosen() {
        double[] startPoint = point(DIM,0.1);

        double[][] boundaries = boundaries(DIM,-1,2);
        RealPointValuePair expected =
            new RealPointValuePair(point(DIM,1.0),0.0);
        doTest(new Rosen(), startPoint, boundaries,
                GoalType.MINIMIZE,
                1e-13, 1e-6, 2000, expected);
    }

    /**
     * @param func Function to optimize.
     * @param startPoint Starting point.
     * @param boundaries Upper / lower point limit.
     * @param goal Minimization or maximization.
     * @param fTol Tolerance relative error on the objective function.
     * @param pointTol Tolerance for checking that the optimum is correct.
     * @param maxEvaluations Maximum number of evaluations.
     * @param expected Expected point / value.
     */
    private void doTest(MultivariateRealFunction func,
            double[] startPoint,
            double[][] boundaries,
            GoalType goal,
            double fTol,
            double pointTol,
            int maxEvaluations,
            RealPointValuePair expected) {

        System.out.println(func.getClass().getName() + " BEGIN"); // XXX

        int dim = startPoint.length;
//        MultivariateRealOptimizer optim =
//            new PowellOptimizer(1e-13, Math.ulp(1d));
//        RealPointValuePair result = optim.optimize(100000, func, goal, startPoint);
        final double[] lB = boundaries == null ? null : boundaries[0];
        final double[] uB = boundaries == null ? null : boundaries[1];
        MultivariateRealOptimizer optim = new BOBYQAOptimizer(2 * dim + 1);
        RealPointValuePair result = optim.optimize(maxEvaluations, func, goal, startPoint, lB, uB);
//        System.out.println(func.getClass().getName() + " = " 
//              + optim.getEvaluations() + " f(");
//        for (double x: result.getPoint())  System.out.print(x + " ");
//        System.out.println(") = " +  result.getValue());
        Assert.assertEquals(expected.getValue(),
                result.getValue(), fTol);
        for (int i = 0; i < dim; i++) {
            Assert.assertEquals(expected.getPoint()[i],
                    result.getPoint()[i], pointTol);
        }

        System.out.println(func.getClass().getName() + " END"); // XXX
    }

    private static double[] point(int n, double value) {
        double[] ds = new double[n];
        Arrays.fill(ds, value);
        return ds;
    }

    private static double[][] boundaries(int dim,
            double lower, double upper) {
        double[][] boundaries = new double[2][dim];
        for (int i = 0; i < dim; i++)
            boundaries[0][i] = lower;
        for (int i = 0; i < dim; i++)
            boundaries[1][i] = upper;
        return boundaries;
    }

    private static class Sphere implements MultivariateRealFunction {

        public double value(double[] x) {
            double f = 0;
            for (int i = 0; i < x.length; ++i)
                f += x[i] * x[i];
            return f;
        }
    }

    private static class Cigar implements MultivariateRealFunction {
        private double factor;

        Cigar() {
            this(1e3);
        }

        Cigar(double axisratio) {
            factor = axisratio * axisratio;
        }

        public double value(double[] x) {
            double f = x[0] * x[0];
            for (int i = 1; i < x.length; ++i)
                f += factor * x[i] * x[i];
            return f;
        }
    }

    private static class Tablet implements MultivariateRealFunction {
        private double factor;

        Tablet() {
            this(1e3);
        }

        Tablet(double axisratio) {
            factor = axisratio * axisratio;
        }

        public double value(double[] x) {
            double f = factor * x[0] * x[0];
            for (int i = 1; i < x.length; ++i)
                f += x[i] * x[i];
            return f;
        }
    }

    private static class CigTab implements MultivariateRealFunction {
        private double factor;

        CigTab() {
            this(1e4);
        }

        CigTab(double axisratio) {
            factor = axisratio;
        }

        public double value(double[] x) {
            int end = x.length - 1;
            double f = x[0] * x[0] / factor + factor * x[end] * x[end];
            for (int i = 1; i < end; ++i)
                f += x[i] * x[i];
            return f;
        }
    }

    private static class TwoAxes implements MultivariateRealFunction {

        private double factor;

        TwoAxes() {
            this(1e6);
        }

        TwoAxes(double axisratio) {
            factor = axisratio * axisratio;
        }

        public double value(double[] x) {
            double f = 0;
            for (int i = 0; i < x.length; ++i)
                f += (i < x.length / 2 ? factor : 1) * x[i] * x[i];
            return f;
        }
    }

    private static class ElliRotated implements MultivariateRealFunction {
        private Basis B = new Basis();
        private double factor;

        ElliRotated() {
            this(1e3);
        }

        ElliRotated(double axisratio) {
            factor = axisratio * axisratio;
        }

        public double value(double[] x) {
            double f = 0;
            x = B.Rotate(x);
            for (int i = 0; i < x.length; ++i)
                f += Math.pow(factor, i / (x.length - 1.)) * x[i] * x[i];
            return f;
        }
    }

    private static class Elli implements MultivariateRealFunction {

        private double factor;

        Elli() {
            this(1e3);
        }

        Elli(double axisratio) {
            factor = axisratio * axisratio;
        }

        public double value(double[] x) {
            double f = 0;
            for (int i = 0; i < x.length; ++i)
                f += Math.pow(factor, i / (x.length - 1.)) * x[i] * x[i];
            return f;
        }
    }

    private static class MinusElli implements MultivariateRealFunction {
        private final Elli elli = new Elli();
        public double value(double[] x) {
            return 1.0 - elli.value(x);
        }
    }

    private static class DiffPow implements MultivariateRealFunction {
//        private int fcount = 0;
        public double value(double[] x) {
            double f = 0;
            for (int i = 0; i < x.length; ++i)
                f += Math.pow(Math.abs(x[i]), 2. + 10 * (double) i
                        / (x.length - 1.));
//            System.out.print("" + (fcount++) + ") ");
//            for (int i = 0; i < x.length; i++)
//                System.out.print(x[i] +  " ");
//            System.out.println(" = " + f);
            return f;
        }
    }

    private static class SsDiffPow implements MultivariateRealFunction {

        public double value(double[] x) {
            double f = Math.pow(new DiffPow().value(x), 0.25);
            return f;
        }
    }

    private static class Rosen implements MultivariateRealFunction {

        public double value(double[] x) {
            double f = 0;
            for (int i = 0; i < x.length - 1; ++i)
                f += 1e2 * (x[i] * x[i] - x[i + 1]) * (x[i] * x[i] - x[i + 1])
                + (x[i] - 1.) * (x[i] - 1.);
            return f;
        }
    }

    private static class Ackley implements MultivariateRealFunction {
        private double axisratio;

        Ackley(double axra) {
            axisratio = axra;
        }

        public Ackley() {
            this(1);
        }

        public double value(double[] x) {
            double f = 0;
            double res2 = 0;
            double fac = 0;
            for (int i = 0; i < x.length; ++i) {
                fac = Math.pow(axisratio, (i - 1.) / (x.length - 1.));
                f += fac * fac * x[i] * x[i];
                res2 += Math.cos(2. * Math.PI * fac * x[i]);
            }
            f = (20. - 20. * Math.exp(-0.2 * Math.sqrt(f / x.length))
                    + Math.exp(1.) - Math.exp(res2 / x.length));
            return f;
        }
    }

    private static class Rastrigin implements MultivariateRealFunction {

        private double axisratio;
        private double amplitude;

        Rastrigin() {
            this(1, 10);
        }

        Rastrigin(double axisratio, double amplitude) {
            this.axisratio = axisratio;
            this.amplitude = amplitude;
        }

        public double value(double[] x) {
            double f = 0;
            double fac;
            for (int i = 0; i < x.length; ++i) {
                fac = Math.pow(axisratio, (i - 1.) / (x.length - 1.));
                if (i == 0 && x[i] < 0)
                    fac *= 1.;
                f += fac * fac * x[i] * x[i] + amplitude
                * (1. - Math.cos(2. * Math.PI * fac * x[i]));
            }
            return f;
        }
    }

    private static class Basis {
        double[][] basis;
        Random rand = new Random(2); // use not always the same basis

        double[] Rotate(double[] x) {
            GenBasis(x.length);
            double[] y = new double[x.length];
            for (int i = 0; i < x.length; ++i) {
                y[i] = 0;
                for (int j = 0; j < x.length; ++j)
                    y[i] += basis[i][j] * x[j];
            }
            return y;
        }

        void GenBasis(int DIM) {
            if (basis != null ? basis.length == DIM : false)
                return;

            double sp;
            int i, j, k;

            /* generate orthogonal basis */
            basis = new double[DIM][DIM];
            for (i = 0; i < DIM; ++i) {
                /* sample components gaussian */
                for (j = 0; j < DIM; ++j)
                    basis[i][j] = rand.nextGaussian();
                /* substract projection of previous vectors */
                for (j = i - 1; j >= 0; --j) {
                    for (sp = 0., k = 0; k < DIM; ++k)
                        sp += basis[i][k] * basis[j][k]; /* scalar product */
                    for (k = 0; k < DIM; ++k)
                        basis[i][k] -= sp * basis[j][k]; /* substract */
                }
                /* normalize */
                for (sp = 0., k = 0; k < DIM; ++k)
                    sp += basis[i][k] * basis[i][k]; /* squared norm */
                for (k = 0; k < DIM; ++k)
                    basis[i][k] /= Math.sqrt(sp);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8531.java