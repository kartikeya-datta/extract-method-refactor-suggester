error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9704.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9704.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9704.java
text:
```scala
r@@eturn ((RealMatrixImpl) new RealMatrixImpl(jTj, false).inverse()).getDataRef();

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

package org.apache.commons.math.estimation;

import java.util.Arrays;

import org.apache.commons.math.linear.InvalidMatrixException;
import org.apache.commons.math.linear.RealMatrixImpl;

/**
 * Base class for implementing estimators.
 * <p>This base class handles the boilerplates methods associated to thresholds
 * settings, jacobian and error estimation.</p>
 * @version $Revision$ $Date$
 * @since 1.2
 *
 */
public abstract class AbstractEstimator implements Estimator {

    /**
     * Build an abstract estimator for least squares problems.
     */
    protected AbstractEstimator() {
    }

    /**
     * Set the maximal number of cost evaluations allowed.
     * 
     * @param maxCostEval maximal number of cost evaluations allowed
     * @see #estimate
     */
    public final void setMaxCostEval(int maxCostEval) {
        this.maxCostEval = maxCostEval;
    }

    /**
     * Get the number of cost evaluations.
     * 
     * @return number of cost evaluations
     * */
    public final int getCostEvaluations() {
        return costEvaluations;
    }

    /** 
     * Get the number of jacobian evaluations.
     * 
     * @return number of jacobian evaluations
     * */
    public final int getJacobianEvaluations() {
        return jacobianEvaluations;
    }

    /** 
     * Update the jacobian matrix.
     */
    protected void updateJacobian() {
        incrementJacobianEvaluationsCounter();
        Arrays.fill(jacobian, 0);
        for (int i = 0, index = 0; i < rows; i++) {
            WeightedMeasurement wm = measurements[i];
            double factor = -Math.sqrt(wm.getWeight());
            for (int j = 0; j < cols; ++j) {
                jacobian[index++] = factor * wm.getPartial(parameters[j]);
            }
        }
    }

    /**
     * Increment the jacobian evaluations counter.
     */
    protected final void incrementJacobianEvaluationsCounter() {
      ++jacobianEvaluations;
    }

    /** 
     * Update the residuals array and cost function value.
     * @exception EstimationException if the number of cost evaluations
     * exceeds the maximum allowed
     */
    protected void updateResidualsAndCost()
    throws EstimationException {

        if (++costEvaluations > maxCostEval) {
            throw new EstimationException("maximal number of evaluations exceeded ({0})",
                                          new Object[] { new Integer(maxCostEval) });
        }

        cost = 0;
        for (int i = 0, index = 0; i < rows; i++, index += cols) {
            WeightedMeasurement wm = measurements[i];
            double residual = wm.getResidual();
            residuals[i] = Math.sqrt(wm.getWeight()) * residual;
            cost += wm.getWeight() * residual * residual;
        }
        cost = Math.sqrt(cost);

    }

    /** 
     * Get the Root Mean Square value.
     * Get the Root Mean Square value, i.e. the root of the arithmetic
     * mean of the square of all weighted residuals. This is related to the
     * criterion that is minimized by the estimator as follows: if
     * <em>c</em> if the criterion, and <em>n</em> is the number of
     * measurements, then the RMS is <em>sqrt (c/n)</em>.
     * 
     * @param problem estimation problem
     * @return RMS value
     */
    public double getRMS(EstimationProblem problem) {
        WeightedMeasurement[] wm = problem.getMeasurements();
        double criterion = 0;
        for (int i = 0; i < wm.length; ++i) {
            double residual = wm[i].getResidual();
            criterion += wm[i].getWeight() * residual * residual;
        }
        return Math.sqrt(criterion / wm.length);
    }

    /**
     * Get the Chi-Square value.
     * @param problem estimation problem
     * @return chi-square value
     */
    public double getChiSquare(EstimationProblem problem) {
        WeightedMeasurement[] wm = problem.getMeasurements();
        double chiSquare = 0;
        for (int i = 0; i < wm.length; ++i) {
            double residual = wm[i].getResidual();
            chiSquare += residual * residual / wm[i].getWeight();
        }
        return chiSquare;
    }

    /**
     * Get the covariance matrix of unbound estimated parameters.
     * @param problem estimation problem
     * @return covariance matrix
     * @exception EstimationException if the covariance matrix
     * cannot be computed (singular problem)
     */
    public double[][] getCovariances(EstimationProblem problem)
      throws EstimationException {
 
        // set up the jacobian
        updateJacobian();

        // compute transpose(J).J, avoiding building big intermediate matrices
        final int rows = problem.getMeasurements().length;
        final int cols = problem.getUnboundParameters().length;
        final int max  = cols * rows;
        double[][] jTj = new double[cols][cols];
        for (int i = 0; i < cols; ++i) {
            for (int j = i; j < cols; ++j) {
                double sum = 0;
                for (int k = 0; k < max; k += cols) {
                    sum += jacobian[k + i] * jacobian[k + j];
                }
                jTj[i][j] = sum;
                jTj[j][i] = sum;
            }
        }

        try {
            // compute the covariances matrix
            return new RealMatrixImpl(jTj).inverse().getData();
        } catch (InvalidMatrixException ime) {
            throw new EstimationException("unable to compute covariances: singular problem",
                                          new Object[0]);
        }

    }

    /**
     * Guess the errors in unbound estimated parameters.
     * <p>Guessing is covariance-based, it only gives rough order of magnitude.</p>
     * @param problem estimation problem
     * @return errors in estimated parameters
     * @exception EstimationException if the covariances matrix cannot be computed
     * or the number of degrees of freedom is not positive (number of measurements
     * lesser or equal to number of parameters)
     */
    public double[] guessParametersErrors(EstimationProblem problem)
      throws EstimationException {
        int m = problem.getMeasurements().length;
        int p = problem.getUnboundParameters().length;
        if (m <= p) {
            throw new EstimationException("no degrees of freedom ({0} measurements, {1} parameters)",
                                          new Object[] { new Integer(m), new Integer(p)});
        }
        double[] errors = new double[problem.getUnboundParameters().length];
        final double c = Math.sqrt(getChiSquare(problem) / (m - p));
        double[][] covar = getCovariances(problem);
        for (int i = 0; i < errors.length; ++i) {
            errors[i] = Math.sqrt(covar[i][i]) * c;
        }
        return errors;
    }

    /**
     * Initialization of the common parts of the estimation.
     * <p>This method <em>must</em> be called at the start
     * of the {@link #estimate(EstimationProblem) estimate}
     * method.</p>
     * @param problem estimation problem to solve
     */
    protected void initializeEstimate(EstimationProblem problem) {

        // reset counters
        costEvaluations     = 0;
        jacobianEvaluations = 0;

        // retrieve the equations and the parameters
        measurements = problem.getMeasurements();
        parameters   = problem.getUnboundParameters();

        // arrays shared with the other private methods
        rows      = measurements.length;
        cols      = parameters.length;
        jacobian  = new double[rows * cols];
        residuals = new double[rows];

        cost = Double.POSITIVE_INFINITY;

    }

    /** 
     * Solve an estimation problem.
     *
     * <p>The method should set the parameters of the problem to several
     * trial values until it reaches convergence. If this method returns
     * normally (i.e. without throwing an exception), then the best
     * estimate of the parameters can be retrieved from the problem
     * itself, through the {@link EstimationProblem#getAllParameters
     * EstimationProblem.getAllParameters} method.</p>
     *
     * @param problem estimation problem to solve
     * @exception EstimationException if the problem cannot be solved
     *
     */
    public abstract void estimate(EstimationProblem problem)
    throws EstimationException;

    /** Array of measurements. */
    protected WeightedMeasurement[] measurements;

    /** Array of parameters. */
    protected EstimatedParameter[] parameters;

    /** 
     * Jacobian matrix.
     * <p>This matrix is in canonical form just after the calls to
     * {@link #updateJacobian()}, but may be modified by the solver
     * in the derived class (the {@link LevenbergMarquardtEstimator
     * Levenberg-Marquardt estimator} does this).</p>
     */
    protected double[] jacobian;

    /** Number of columns of the jacobian matrix. */
    protected int cols;

    /** Number of rows of the jacobian matrix. */
    protected int rows;

    /** Residuals array.
     * <p>This array is in canonical form just after the calls to
     * {@link #updateJacobian()}, but may be modified by the solver
     * in the derived class (the {@link LevenbergMarquardtEstimator
     * Levenberg-Marquardt estimator} does this).</p>
     */
    protected double[] residuals;

    /** Cost value (square root of the sum of the residuals). */
    protected double cost;

    /** Maximal allowed number of cost evaluations. */
    private int maxCostEval;

    /** Number of cost evaluations. */
    private int costEvaluations;

    /** Number of jacobian evaluations. */
    private int jacobianEvaluations;

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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9704.java