error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5706.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5706.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,20]

error in qdox parser
file content:
```java
offset: 20
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5706.java
text:
```scala
protected abstract V@@ectorialPointValuePair doOptimize()

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

package org.apache.commons.math.optimization.general;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxEvaluationsExceededException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.DifferentiableMultivariateVectorialFunction;
import org.apache.commons.math.analysis.MultivariateMatrixFunction;
import org.apache.commons.math.linear.InvalidMatrixException;
import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.SimpleVectorialValueChecker;
import org.apache.commons.math.optimization.VectorialConvergenceChecker;
import org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer;
import org.apache.commons.math.optimization.VectorialPointValuePair;

/**
 * Base class for implementing least squares optimizers.
 * <p>This base class handles the boilerplate methods associated to thresholds
 * settings, jacobian and error estimation.</p>
 * @version $Revision$ $Date$
 * @since 1.2
 *
 */
public abstract class AbstractLeastSquaresOptimizer implements DifferentiableMultivariateVectorialOptimizer {

    /** Default maximal number of iterations allowed. */
    public static final int DEFAULT_MAX_ITERATIONS = 100;

    /** Maximal number of iterations allowed. */
    private int maxIterations;

    /** Number of iterations already performed. */
    private int iterations;

    /** Maximal number of evaluations allowed. */
    private int maxEvaluations;

    /** Number of evaluations already performed. */
    private int objectiveEvaluations;

    /** Number of jacobian evaluations. */
    private int jacobianEvaluations;

    /** Convergence checker. */
    protected VectorialConvergenceChecker checker;

    /**
     * Jacobian matrix.
     * <p>This matrix is in canonical form just after the calls to
     * {@link #updateJacobian()}, but may be modified by the solver
     * in the derived class (the {@link LevenbergMarquardtOptimizer
     * Levenberg-Marquardt optimizer} does this).</p>
     */
    protected double[][] jacobian;

    /** Number of columns of the jacobian matrix. */
    protected int cols;

    /** Number of rows of the jacobian matrix. */
    protected int rows;

    /** Objective function. */
    private DifferentiableMultivariateVectorialFunction function;

    /** Objective function derivatives. */
    private MultivariateMatrixFunction jF;

    /** Target value for the objective functions at optimum. */
    protected double[] targetValues;

    /** Weight for the least squares cost computation. */
    protected double[] residualsWeights;

    /** Current point. */
    protected double[] point;

    /** Current objective function value. */
    protected double[] objective;

    /** Current residuals. */
    protected double[] residuals;

    /** Cost value (square root of the sum of the residuals). */
    protected double cost;

    /** Simple constructor with default settings.
     * <p>The convergence check is set to a {@link SimpleVectorialValueChecker}
     * and the maximal number of evaluation is set to its default value.</p>
     */
    protected AbstractLeastSquaresOptimizer() {
        setConvergenceChecker(new SimpleVectorialValueChecker());
        setMaxIterations(DEFAULT_MAX_ITERATIONS);
        setMaxEvaluations(Integer.MAX_VALUE);
    }

    /** {@inheritDoc} */
    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    /** {@inheritDoc} */
    public int getMaxIterations() {
        return maxIterations;
    }

    /** {@inheritDoc} */
    public int getIterations() {
        return iterations;
    }

    /** {@inheritDoc} */
    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    /** {@inheritDoc} */
    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    /** {@inheritDoc} */
    public int getEvaluations() {
        return objectiveEvaluations;
    }

    /** {@inheritDoc} */
    public int getJacobianEvaluations() {
        return jacobianEvaluations;
    }

    /** {@inheritDoc} */
    public void setConvergenceChecker(VectorialConvergenceChecker convergenceChecker) {
        this.checker = convergenceChecker;
    }

    /** {@inheritDoc} */
    public VectorialConvergenceChecker getConvergenceChecker() {
        return checker;
    }

    /** Increment the iterations counter by 1.
     * @exception OptimizationException if the maximal number
     * of iterations is exceeded
     */
    protected void incrementIterationsCounter()
        throws OptimizationException {
        if (++iterations > maxIterations) {
            throw new OptimizationException(new MaxIterationsExceededException(maxIterations));
        }
    }

    /**
     * Update the jacobian matrix.
     * @exception FunctionEvaluationException if the function jacobian
     * cannot be evaluated or its dimension doesn't match problem dimension
     */
    protected void updateJacobian() throws FunctionEvaluationException {
        ++jacobianEvaluations;
        jacobian = jF.value(point);
        if (jacobian.length != rows) {
            throw new FunctionEvaluationException(point, "dimension mismatch {0} != {1}",
                                                  jacobian.length, rows);
        }
        for (int i = 0; i < rows; i++) {
            final double[] ji = jacobian[i];
            final double factor = -Math.sqrt(residualsWeights[i]);
            for (int j = 0; j < cols; ++j) {
                ji[j] *= factor;
            }
        }
    }

    /**
     * Update the residuals array and cost function value.
     * @exception FunctionEvaluationException if the function cannot be evaluated
     * or its dimension doesn't match problem dimension or maximal number of
     * of evaluations is exceeded
     */
    protected void updateResidualsAndCost()
        throws FunctionEvaluationException {

        if (++objectiveEvaluations > maxEvaluations) {
            throw new FunctionEvaluationException(new MaxEvaluationsExceededException(maxEvaluations),
                                                  point);
        }
        objective = function.value(point);
        if (objective.length != rows) {
            throw new FunctionEvaluationException(point, "dimension mismatch {0} != {1}",
                                                  objective.length, rows);
        }
        cost = 0;
        for (int i = 0, index = 0; i < rows; i++, index += cols) {
            final double residual = targetValues[i] - objective[i];
            residuals[i] = residual;
            cost += residualsWeights[i] * residual * residual;
        }
        cost = Math.sqrt(cost);

    }

    /**
     * Get the Root Mean Square value.
     * Get the Root Mean Square value, i.e. the root of the arithmetic
     * mean of the square of all weighted residuals. This is related to the
     * criterion that is minimized by the optimizer as follows: if
     * <em>c</em> if the criterion, and <em>n</em> is the number of
     * measurements, then the RMS is <em>sqrt (c/n)</em>.
     *
     * @return RMS value
     */
    public double getRMS() {
        double criterion = 0;
        for (int i = 0; i < rows; ++i) {
            final double residual = residuals[i];
            criterion += residualsWeights[i] * residual * residual;
        }
        return Math.sqrt(criterion / rows);
    }

    /**
     * Get the Chi-Square value.
     * @return chi-square value
     */
    public double getChiSquare() {
        double chiSquare = 0;
        for (int i = 0; i < rows; ++i) {
            final double residual = residuals[i];
            chiSquare += residual * residual / residualsWeights[i];
        }
        return chiSquare;
    }

    /**
     * Get the covariance matrix of optimized parameters.
     * @return covariance matrix
     * @exception FunctionEvaluationException if the function jacobian cannot
     * be evaluated
     * @exception OptimizationException if the covariance matrix
     * cannot be computed (singular problem)
     */
    public double[][] getCovariances()
        throws FunctionEvaluationException, OptimizationException {

        // set up the jacobian
        updateJacobian();

        // compute transpose(J).J, avoiding building big intermediate matrices
        double[][] jTj = new double[cols][cols];
        for (int i = 0; i < cols; ++i) {
            for (int j = i; j < cols; ++j) {
                double sum = 0;
                for (int k = 0; k < rows; ++k) {
                    sum += jacobian[k][i] * jacobian[k][j];
                }
                jTj[i][j] = sum;
                jTj[j][i] = sum;
            }
        }

        try {
            // compute the covariances matrix
            RealMatrix inverse =
                new LUDecompositionImpl(MatrixUtils.createRealMatrix(jTj)).getSolver().getInverse();
            return inverse.getData();
        } catch (InvalidMatrixException ime) {
            throw new OptimizationException("unable to compute covariances: singular problem");
        }

    }

    /**
     * Guess the errors in optimized parameters.
     * <p>Guessing is covariance-based, it only gives rough order of magnitude.</p>
     * @return errors in optimized parameters
     * @exception FunctionEvaluationException if the function jacobian cannot b evaluated
     * @exception OptimizationException if the covariances matrix cannot be computed
     * or the number of degrees of freedom is not positive (number of measurements
     * lesser or equal to number of parameters)
     */
    public double[] guessParametersErrors()
        throws FunctionEvaluationException, OptimizationException {
        if (rows <= cols) {
            throw new OptimizationException(
                    "no degrees of freedom ({0} measurements, {1} parameters)",
                    rows, cols);
        }
        double[] errors = new double[cols];
        final double c = Math.sqrt(getChiSquare() / (rows - cols));
        double[][] covar = getCovariances();
        for (int i = 0; i < errors.length; ++i) {
            errors[i] = Math.sqrt(covar[i][i]) * c;
        }
        return errors;
    }

    /** {@inheritDoc} */
    public VectorialPointValuePair optimize(final DifferentiableMultivariateVectorialFunction f,
                                            final double[] target, final double[] weights,
                                            final double[] startPoint)
        throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

        if (target.length != weights.length) {
            throw new OptimizationException("dimension mismatch {0} != {1}",
                                            target.length, weights.length);
        }

        // reset counters
        iterations           = 0;
        objectiveEvaluations = 0;
        jacobianEvaluations  = 0;

        // store least squares problem characteristics
        function         = f;
        jF               = f.jacobian();
        targetValues     = target.clone();
        residualsWeights = weights.clone();
        this.point       = startPoint.clone();
        this.residuals   = new double[target.length];

        // arrays shared with the other private methods
        rows      = target.length;
        cols      = point.length;
        jacobian  = new double[rows][cols];

        cost = Double.POSITIVE_INFINITY;

        return doOptimize();

    }

    /** Perform the bulk of optimization algorithm.
     * @return the point/value pair giving the optimal value for objective function
     * @exception FunctionEvaluationException if the objective function throws one during
     * the search
     * @exception OptimizationException if the algorithm failed to converge
     * @exception IllegalArgumentException if the start point dimension is wrong
     */
    abstract protected VectorialPointValuePair doOptimize()
        throws FunctionEvaluationException, OptimizationException, IllegalArgumentException;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5706.java