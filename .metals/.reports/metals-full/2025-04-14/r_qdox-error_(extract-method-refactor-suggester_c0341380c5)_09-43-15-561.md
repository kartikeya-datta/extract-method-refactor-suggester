error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6120.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6120.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6120.java
text:
```scala
p@@.evaluate(dummy);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.apache.commons.math3.fitting.leastsquares;

import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.TestUtils;
import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem.Evaluation;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DiagonalMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

/**
 * The only features tested here are utility methods defined
 * in {@link LeastSquaresProblem.Evaluation} that compute the
 * chi-square and parameters standard-deviations.
 */
public class EvaluationTest {

    /**
     * Create a {@link LeastSquaresBuilder} from a {@link StatisticalReferenceDataset}.
     *
     * @param dataset the source data
     * @return a builder for further customization.
     */
    public LeastSquaresBuilder builder(StatisticalReferenceDataset dataset) {
        StatisticalReferenceDataset.LeastSquaresProblem problem
                = dataset.getLeastSquaresProblem();
        final double[] start = dataset.getParameters();
        final double[] observed = dataset.getData()[1];
        final double[] weights = new double[observed.length];
        Arrays.fill(weights, 1d);

        return new LeastSquaresBuilder()
                .model(problem.getModelFunction(), problem.getModelFunctionJacobian())
                .target(observed)
                .weight(new DiagonalMatrix(weights))
                .start(start);
    }

    @Test
    public void testComputeResiduals() {
        //setup
        RealVector point = new ArrayRealVector(2);
        Evaluation evaluation = new LeastSquaresBuilder()
                .target(new ArrayRealVector(new double[]{3,-1}))
                .model(new MultivariateJacobianFunction() {
                    public Pair<RealVector, RealMatrix> value(RealVector point) {
                        return new Pair<RealVector, RealMatrix>(
                                new ArrayRealVector(new double[]{1, 2}),
                                MatrixUtils.createRealIdentityMatrix(2)
                        );
                    }
                })
                .weight(MatrixUtils.createRealIdentityMatrix(2))
                .build()
                .evaluate(point);

        //action + verify
        Assert.assertArrayEquals(
                evaluation.getResiduals().toArray(),
                new double[]{2, -3},
                Precision.EPSILON);
    }

    @Test
    public void testComputeCovariance() throws IOException {
        //setup
        RealVector point = new ArrayRealVector(2);
        Evaluation evaluation = new LeastSquaresBuilder()
                .model(new MultivariateJacobianFunction() {
                    public Pair<RealVector, RealMatrix> value(RealVector point) {
                        return new Pair<RealVector, RealMatrix>(
                                new ArrayRealVector(2),
                                MatrixUtils.createRealDiagonalMatrix(new double[]{1, 1e-2})
                        );
                    }
                })
                .weight(MatrixUtils.createRealDiagonalMatrix(new double[]{1, 1}))
                .target(new ArrayRealVector(2))
                .build()
                .evaluate(point);

        //action
        TestUtils.assertEquals(
                "covariance",
                evaluation.getCovariances(FastMath.nextAfter(1e-4, 0.0)),
                MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 1e4}}),
                Precision.EPSILON
        );

        //singularity fail
        try {
            evaluation.getCovariances(FastMath.nextAfter(1e-4, 1.0));
            Assert.fail("Expected Exception");
        } catch (SingularMatrixException e) {
            //expected
        }
    }

    @Test
    public void testComputeValueAndJacobian() {
        //setup
        final RealVector point = new ArrayRealVector(new double[]{1, 2});
        Evaluation evaluation = new LeastSquaresBuilder()
                .weight(new DiagonalMatrix(new double[]{16, 4}))
                .model(new MultivariateJacobianFunction() {
                    public Pair<RealVector, RealMatrix> value(RealVector actualPoint) {
                        //verify correct values passed in
                        Assert.assertArrayEquals(
                                point.toArray(), actualPoint.toArray(), Precision.EPSILON);
                        //return values
                        return new Pair<RealVector, RealMatrix>(
                                new ArrayRealVector(new double[]{3, 4}),
                                MatrixUtils.createRealMatrix(new double[][]{{5, 6}, {7, 8}})
                        );
                    }
                })
                .target(new double[2])
                .build()
                .evaluate(point);

        //action
        RealVector residuals = evaluation.getResiduals();
        RealMatrix jacobian = evaluation.getJacobian();

        //verify
        Assert.assertArrayEquals(evaluation.getPoint().toArray(), point.toArray(), 0);
        Assert.assertArrayEquals(new double[]{-12, -8}, residuals.toArray(), Precision.EPSILON);
        TestUtils.assertEquals(
                "jacobian",
                jacobian,
                MatrixUtils.createRealMatrix(new double[][]{{20, 24},{14, 16}}),
                Precision.EPSILON);
    }

    @Test
    public void testComputeCost() throws IOException {
        final StatisticalReferenceDataset dataset
            = StatisticalReferenceDatasetFactory.createKirby2();

        final LeastSquaresProblem lsp = builder(dataset).build();

        final double expected = dataset.getResidualSumOfSquares();
        final double cost = lsp.evaluate(lsp.getStart()).getCost();
        final double actual = cost * cost;
        Assert.assertEquals(dataset.getName(), expected, actual, 1e-11 * expected);
    }

    @Test
    public void testComputeRMS() throws IOException {
        final StatisticalReferenceDataset dataset
            = StatisticalReferenceDatasetFactory.createKirby2();

        final LeastSquaresProblem lsp = builder(dataset).build();

        final double expected = FastMath.sqrt(dataset.getResidualSumOfSquares() /
                                              dataset.getNumObservations());
        final double actual = lsp.evaluate(lsp.getStart()).getRMS();
        Assert.assertEquals(dataset.getName(), expected, actual, 1e-11 * expected);
    }

    @Test
    public void testComputeSigma() throws IOException {
        final StatisticalReferenceDataset dataset
            = StatisticalReferenceDatasetFactory.createKirby2();

        final LeastSquaresProblem lsp = builder(dataset).build();

        final double[] expected = dataset.getParametersStandardDeviations();

        final Evaluation evaluation = lsp.evaluate(lsp.getStart());
        final double cost = evaluation.getCost();
        final RealVector sig = evaluation.getSigma(1e-14);
        final int dof = lsp.getObservationSize() - lsp.getParameterSize();
        for (int i = 0; i < sig.getDimension(); i++) {
            final double actual = FastMath.sqrt(cost * cost / dof) * sig.getEntry(i);
            Assert.assertEquals(dataset.getName() + ", parameter #" + i,
                                expected[i], actual, 1e-6 * expected[i]);
        }
    }

    @Test
    public void testEvaluateCopiesPoint() throws IOException {
        //setup
        StatisticalReferenceDataset dataset
                = StatisticalReferenceDatasetFactory.createKirby2();
        LeastSquaresProblem lsp = builder(dataset).build();
        RealVector point = new ArrayRealVector(lsp.getParameterSize());

        //action
        Evaluation evaluation = lsp.evaluate(point);

        //verify
        Assert.assertNotSame(point, evaluation.getPoint());
        point.setEntry(0, 1);
        Assert.assertEquals(evaluation.getPoint().getEntry(0), 0, 0);
    }

    @Test
    public void testLazyEvaluation() {
        final RealVector dummy = new ArrayRealVector(new double[] { 0 });

        final LeastSquaresProblem p
            = LeastSquaresFactory.create(LeastSquaresFactory.model(dummyModel(), dummyJacobian()),
                                         dummy, dummy, null, 0, 0, true);

        // Should not throw because actual evaluation is deferred.
        final Evaluation eval = p.evaluate(dummy);

        try {
            eval.getResiduals();
            Assert.fail("Exception expected");
        } catch (RuntimeException e) {
            // Expecting exception.
            Assert.assertEquals("dummyModel", e.getMessage());
        }

        try {
            eval.getJacobian();
            Assert.fail("Exception expected");
        } catch (RuntimeException e) {
            // Expecting exception.
            Assert.assertEquals("dummyJacobian", e.getMessage());
        }
    }

    // MATH-1151
    @Test
    public void testLazyEvaluationPrecondition() {
        final RealVector dummy = new ArrayRealVector(new double[] { 0 });

        // "ValueAndJacobianFunction" is required but we implement only
        // "MultivariateJacobianFunction".
        final MultivariateJacobianFunction m1 = new MultivariateJacobianFunction() {
                public Pair<RealVector, RealMatrix> value(RealVector notUsed) {
                    return new Pair<RealVector, RealMatrix>(null, null);
                }
            };

        try {
            // Should throw.
            LeastSquaresFactory.create(m1, dummy, dummy, null, 0, 0, true);
            Assert.fail("Expecting MathIllegalStateException");
        } catch (MathIllegalStateException e) {
            // Expected.
        }

        final MultivariateJacobianFunction m2 = new ValueAndJacobianFunction() {
                public Pair<RealVector, RealMatrix> value(RealVector notUsed) {
                    return new Pair<RealVector, RealMatrix>(null, null);
                }
                public RealVector computeValue(final double[] params) {
                    return null;
                }
                public RealMatrix computeJacobian(final double[] params) {
                    return null;
                }
            };

        // Should pass.
        LeastSquaresFactory.create(m2, dummy, dummy, null, 0, 0, true);
    }

    @Test
    public void testDirectEvaluation() {
        final RealVector dummy = new ArrayRealVector(new double[] { 0 });

        final LeastSquaresProblem p
            = LeastSquaresFactory.create(LeastSquaresFactory.model(dummyModel(), dummyJacobian()),
                                         dummy, dummy, null, 0, 0, false);

        try {
            // Should throw.
            final Evaluation eval = p.evaluate(dummy);
            Assert.fail("Exception expected");
        } catch (RuntimeException e) {
            // Expecting exception.
            // Whether it is model or Jacobian that caused it is not significant.
            final String msg = e.getMessage();
            Assert.assertTrue(msg.equals("dummyModel") ||
                              msg.equals("dummyJacobian"));
        }
    }

    /** Used for testing direct vs lazy evaluation. */
    private MultivariateVectorFunction dummyModel() {
        return new MultivariateVectorFunction() {
            public double[] value(double[] p) {
                throw new RuntimeException("dummyModel");
            }
        };
    }

    /** Used for testing direct vs lazy evaluation. */
    private MultivariateMatrixFunction dummyJacobian() {
        return new MultivariateMatrixFunction() {
            public double[][] value(double[] p) {
                throw new RuntimeException("dummyJacobian");
            }
        };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6120.java