error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/764.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/764.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/764.java
text:
```scala
t@@hrow new EstimationException("unable to solve: singular problem", null);

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

import java.io.Serializable;

import org.apache.commons.math.linear.InvalidMatrixException;
import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;
import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.linear.RealVectorImpl;

/** 
 * This class implements a solver for estimation problems.
 *
 * <p>This class solves estimation problems using a weighted least
 * squares criterion on the measurement residuals. It uses a
 * Gauss-Newton algorithm.</p>
 *
 * @version $Revision$ $Date$
 * @since 1.2
 *
 */

public class GaussNewtonEstimator extends AbstractEstimator implements Serializable {

    /** 
     * Simple constructor.
     *
     * <p>This constructor builds an estimator and stores its convergence
     * characteristics.</p>
     *
     * <p>An estimator is considered to have converged whenever either
     * the criterion goes below a physical threshold under which
     * improvements are considered useless or when the algorithm is
     * unable to improve it (even if it is still high). The first
     * condition that is met stops the iterations.</p>
     *
     * <p>The fact an estimator has converged does not mean that the
     * model accurately fits the measurements. It only means no better
     * solution can be found, it does not mean this one is good. Such an
     * analysis is left to the caller.</p>
     *
     * <p>If neither conditions are fulfilled before a given number of
     * iterations, the algorithm is considered to have failed and an
     * {@link EstimationException} is thrown.</p>
     *
     * @param maxCostEval maximal number of cost evaluations allowed
     * @param convergence criterion threshold below which we do not need
     * to improve the criterion anymore
     * @param steadyStateThreshold steady state detection threshold, the
     * problem has converged has reached a steady state if
     * <code>Math.abs (Jn - Jn-1) < Jn * convergence</code>, where
     * <code>Jn</code> and <code>Jn-1</code> are the current and
     * preceding criterion value (square sum of the weighted residuals
     * of considered measurements).
     */
    public GaussNewtonEstimator(int maxCostEval,
            double convergence,
            double steadyStateThreshold) {
        setMaxCostEval(maxCostEval);
        this.steadyStateThreshold = steadyStateThreshold;
        this.convergence          = convergence;
    }

    /** 
     * Solve an estimation problem using a least squares criterion.
     *
     * <p>This method set the unbound parameters of the given problem
     * starting from their current values through several iterations. At
     * each step, the unbound parameters are changed in order to
     * minimize a weighted least square criterion based on the
     * measurements of the problem.</p>
     *
     * <p>The iterations are stopped either when the criterion goes
     * below a physical threshold under which improvement are considered
     * useless or when the algorithm is unable to improve it (even if it
     * is still high). The first condition that is met stops the
     * iterations. If the convergence it nos reached before the maximum
     * number of iterations, an {@link EstimationException} is
     * thrown.</p>
     *
     * @param problem estimation problem to solve
     * @exception EstimationException if the problem cannot be solved
     *
     * @see EstimationProblem
     *
     */
    public void estimate(EstimationProblem problem)
    throws EstimationException {

        initializeEstimate(problem);

        // work matrices
        double[] grad             = new double[parameters.length];
        RealVectorImpl bDecrement = new RealVectorImpl(parameters.length);
        double[] bDecrementData   = bDecrement.getDataRef();
        RealMatrixImpl wGradGradT = new RealMatrixImpl(parameters.length, parameters.length);
        double[][] wggData        = wGradGradT.getDataRef();

        // iterate until convergence is reached
        double previous = Double.POSITIVE_INFINITY;
        do {

            // build the linear problem
            incrementJacobianEvaluationsCounter();
            RealVector b = new RealVectorImpl(parameters.length);
            RealMatrix a = new RealMatrixImpl(parameters.length, parameters.length);
            for (int i = 0; i < measurements.length; ++i) {
                if (! measurements [i].isIgnored()) {

                    double weight   = measurements[i].getWeight();
                    double residual = measurements[i].getResidual();

                    // compute the normal equation
                    for (int j = 0; j < parameters.length; ++j) {
                        grad[j] = measurements[i].getPartial(parameters[j]);
                        bDecrementData[j] = weight * residual * grad[j];
                    }

                    // build the contribution matrix for measurement i
                    for (int k = 0; k < parameters.length; ++k) {
                        double[] wggRow = wggData[k];
                        double gk = grad[k];
                        for (int l = 0; l < parameters.length; ++l) {
                            wggRow[l] =  weight * gk * grad[l];
                        }
                    }

                    // update the matrices
                    a = a.add(wGradGradT);
                    b = b.add(bDecrement);

                }
            }

            try {

                // solve the linearized least squares problem
                RealVector dX = new LUDecompositionImpl(a).solve(b);

                // update the estimated parameters
                for (int i = 0; i < parameters.length; ++i) {
                    parameters[i].setEstimate(parameters[i].getEstimate() + dX.getEntry(i));
                }

            } catch(InvalidMatrixException e) {
                throw new EstimationException("unable to solve: singular problem", new Object[0]);
            }


            previous = cost;
            updateResidualsAndCost();

        } while ((getCostEvaluations() < 2) ||
                 (Math.abs(previous - cost) > (cost * steadyStateThreshold) &&
                  (Math.abs(cost) > convergence)));

    }

    /** Threshold for cost steady state detection. */
    private double steadyStateThreshold;

    /** Threshold for cost convergence. */
    private double convergence;

    /** Serializable version identifier */
     private static final long serialVersionUID = 5485001826076289109L;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/764.java