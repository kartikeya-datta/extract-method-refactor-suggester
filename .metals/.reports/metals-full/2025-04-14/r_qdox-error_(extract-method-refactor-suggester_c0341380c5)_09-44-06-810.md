error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9002.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9002.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9002.java
text:
```scala
r@@eturn optima.clone();

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

package org.apache.commons.math.optimization;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.analysis.DifferentiableMultivariateVectorialFunction;
import org.apache.commons.math.analysis.MultivariateRealFunction;
import org.apache.commons.math.random.RandomVectorGenerator;

/** 
 * Special implementation of the {@link DifferentiableMultivariateVectorialOptimizer} interface adding
 * multi-start features to an existing optimizer.
 * <p>
 * This class wraps a classical optimizer to use it several times in
 * turn with different starting points in order to avoid being trapped
 * into a local extremum when looking for a global one.
 * </p>
 * @version $Revision$ $Date$
 * @since 2.0
 */
public class MultiStartDifferentiableMultivariateVectorialOptimizer
    implements DifferentiableMultivariateVectorialOptimizer {

    /** Serializable version identifier. */
    private static final long serialVersionUID = 9206382258980561530L;

    /** Underlying classical optimizer. */
    private final DifferentiableMultivariateVectorialOptimizer optimizer;

    /** Maximal number of iterations allowed. */
    private int maxIterations;

    /** Number of iterations already performed for all starts. */
    private int totalIterations;

    /** Number of evaluations already performed for all starts. */
    private int totalEvaluations;

    /** Number of jacobian evaluations already performed for all starts. */
    private int totalJacobianEvaluations;

    /** Number of starts to go. */
    private int starts;

    /** Random generator for multi-start. */
    private RandomVectorGenerator generator;

    /** Found optima. */
    private VectorialPointValuePair[] optima;

    /**
     * Create a multi-start optimizer from a single-start optimizer
     * @param optimizer single-start optimizer to wrap
     * @param starts number of starts to perform (including the
     * first one), multi-start is disabled if value is less than or
     * equal to 1
     * @param generator random vector generator to use for restarts
     */
    public MultiStartDifferentiableMultivariateVectorialOptimizer(
                final DifferentiableMultivariateVectorialOptimizer optimizer,
                final int starts,
                final RandomVectorGenerator generator) {
        this.optimizer                = optimizer;
        this.maxIterations            = Integer.MAX_VALUE;
        this.totalIterations          = 0;
        this.totalEvaluations         = 0;
        this.totalJacobianEvaluations = 0;
        this.starts                   = starts;
        this.generator                = generator;
        this.optima                   = null;
    }

    /** Get all the optima found during the last call to {@link
     * #optimize(MultivariateRealFunction, GoalType, double[]) optimize}.
     * <p>The optimizer stores all the optima found during a set of
     * restarts. The {@link #optimize(MultivariateRealFunction, GoalType,
     * double[]) optimize} method returns the best point only. This
     * method returns all the points found at the end of each starts,
     * including the best one already returned by the {@link
     * #optimize(MultivariateRealFunction, GoalType, double[]) optimize}
     * method.
     * </p>
     * <p>
     * The returned array as one element for each start as specified
     * in the constructor. It is ordered with the results from the
     * runs that did converge first, sorted from best to worst
     * objective value (i.e in ascending order if minimizing and in
     * descending order if maximizing), followed by and null elements
     * corresponding to the runs that did not converge. This means all
     * elements will be null if the {@link #optimize(MultivariateRealFunction,
     * GoalType, double[]) optimize} method did throw a {@link
     * ConvergenceException ConvergenceException}). This also means that
     * if the first element is non null, it is the best point found across
     * all starts.</p>
     * @return array containing the optima
     * @exception IllegalStateException if {@link #optimize(MultivariateRealFunction,
     * GoalType, double[]) optimize} has not been called
     */
    public VectorialPointValuePair[] getOptima() throws IllegalStateException {
        if (optima == null) {
            throw MathRuntimeException.createIllegalStateException("no optimum computed yet");
        }
        return (VectorialPointValuePair[]) optima.clone();
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
        return totalIterations;
    }

    /** {@inheritDoc} */
    public int getEvaluations() {
        return totalEvaluations;
    }

    /** {@inheritDoc} */
    public int getJacobianEvaluations() {
        return totalJacobianEvaluations;
    }

    /** {@inheritDoc} */
    public void setConvergenceChecker(VectorialConvergenceChecker checker) {
        optimizer.setConvergenceChecker(checker);
    }

    /** {@inheritDoc} */
    public VectorialConvergenceChecker getConvergenceChecker() {
        return optimizer.getConvergenceChecker();
    }

    /** {@inheritDoc} */
    public VectorialPointValuePair optimize(final DifferentiableMultivariateVectorialFunction f,
                                            final double[] target, final double[] weights,
                                            final double[] startPoint)
        throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

        optima                   = new VectorialPointValuePair[starts];
        totalIterations          = 0;
        totalEvaluations         = 0;
        totalJacobianEvaluations = 0;

        // multi-start loop
        for (int i = 0; i < starts; ++i) {

            try {
                optimizer.setMaxIterations(maxIterations - totalIterations);
                optima[i] = optimizer.optimize(f, target, weights,
                                               (i == 0) ? startPoint : generator.nextVector());
            } catch (FunctionEvaluationException fee) {
                optima[i] = null;
            } catch (OptimizationException oe) {
                optima[i] = null;
            }

            totalIterations          += optimizer.getIterations();
            totalEvaluations         += optimizer.getEvaluations();
            totalJacobianEvaluations += optimizer.getJacobianEvaluations();

        }

        // sort the optima from best to worst, followed by null elements
        Arrays.sort(optima, new Comparator<VectorialPointValuePair>() {
            public int compare(final VectorialPointValuePair o1, final VectorialPointValuePair o2) {
                if (o1 == null) {
                    return (o2 == null) ? 0 : +1;
                } else if (o2 == null) {
                    return -1;
                }
                return Double.compare(weightedResidual(o1), weightedResidual(o2));
            }
            private double weightedResidual(final VectorialPointValuePair pv) {
                final double[] value = pv.getValueRef();
                double sum = 0;
                for (int i = 0; i < value.length; ++i) {
                    final double ri = value[i] - target[i];
                    sum += weights[i] * ri * ri;
                }
                return sum;
            }
        });

        if (optima[0] == null) {
            throw new OptimizationException(
                    "none of the {0} start points lead to convergence",
                    starts);
        }

        // return the found point given the best objective function value
        return optima[0];

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9002.java