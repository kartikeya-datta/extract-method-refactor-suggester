error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14685.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14685.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14685.java
text:
```scala
private v@@oid setMultiStart(int starts, RandomVectorGenerator generator) {

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
import org.apache.commons.math.DimensionMismatchException;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.random.CorrelatedRandomVectorGenerator;
import org.apache.commons.math.random.JDKRandomGenerator;
import org.apache.commons.math.random.NotPositiveDefiniteMatrixException;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.random.RandomVectorGenerator;
import org.apache.commons.math.random.UncorrelatedRandomVectorGenerator;
import org.apache.commons.math.random.UniformRandomGenerator;
import org.apache.commons.math.stat.descriptive.moment.VectorialCovariance;
import org.apache.commons.math.stat.descriptive.moment.VectorialMean;

/** This class implements simplex-based direct search optimization
 * algorithms.

 * <p>Direct search methods only use cost function values, they don't
 * need derivatives and don't either try to compute approximation of
 * the derivatives. According to a 1996 paper by Margaret H. Wright
 * (<a href="http://cm.bell-labs.com/cm/cs/doc/96/4-02.ps.gz">Direct
 * Search Methods: Once Scorned, Now Respectable</a>), they are used
 * when either the computation of the derivative is impossible (noisy
 * functions, unpredictable dicontinuities) or difficult (complexity,
 * computation cost). In the first cases, rather than an optimum, a
 * <em>not too bad</em> point is desired. In the latter cases, an
 * optimum is desired but cannot be reasonably found. In all cases
 * direct search methods can be useful.</p>

 * <p>Simplex-based direct search methods are based on comparison of
 * the cost function values at the vertices of a simplex (which is a
 * set of n+1 points in dimension n) that is updated by the algorithms
 * steps.</p>

 * <p>The instances can be built either in single-start or in
 * multi-start mode. Multi-start is a traditional way to try to avoid
 * beeing trapped in a local minimum and miss the global minimum of a
 * function. It can also be used to verify the convergence of an
 * algorithm. In multi-start mode, the {@link #minimizes(CostFunction,
 * int, ConvergenceChecker, double[], double[]) minimizes}
 * method returns the best minimum found after all starts, and the
 * {@link #getMinima getMinima} method can be used to retrieve all
 * minima from all starts (including the one already provided by the
 * {@link #minimizes(CostFunction, int, ConvergenceChecker, double[],
 * double[]) minimizes} method).</p>

 * <p>This class is the base class performing the boilerplate simplex
 * initialization and handling. The simplex update by itself is
 * performed by the derived classes according to the implemented
 * algorithms.</p>

 * @version $Id: DirectSearchOptimizer.java 1705 2006-09-17 19:57:39Z luc $
 * @see CostFunction
 * @see NelderMead
 * @see MultiDirectional
 */
public abstract class DirectSearchOptimizer {

    /** Simple constructor.
     */
    protected DirectSearchOptimizer() {
    }

    /** Minimizes a cost function.
     * <p>The initial simplex is built from two vertices that are
     * considered to represent two opposite vertices of a box parallel
     * to the canonical axes of the space. The simplex is the subset of
     * vertices encountered while going from vertexA to vertexB
     * travelling along the box edges only. This can be seen as a scaled
     * regular simplex using the projected separation between the given
     * points as the scaling factor along each coordinate axis.</p>
     * <p>The optimization is performed in single-start mode.</p>
     * @param f cost function
     * @param maxEvaluations maximal number of function calls for each
     * start (note that the number will be checked <em>after</em>
     * complete simplices have been evaluated, this means that in some
     * cases this number will be exceeded by a few units, depending on
     * the dimension of the problem)
     * @param checker object to use to check for convergence
     * @param vertexA first vertex
     * @param vertexB last vertex
     * @return the point/cost pairs giving the minimal cost
     * @exception CostException if the cost function throws one during
     * the search
     * @exception NoConvergenceException if none of the starts did
     * converge (it is not thrown if at least one start did converge)
     */
    public PointCostPair minimizes(CostFunction f, int maxEvaluations,
                                   ConvergenceChecker checker,
                                   double[] vertexA, double[] vertexB)
    throws CostException, ConvergenceException {

        // set up optimizer
        buildSimplex(vertexA, vertexB);
        setSingleStart();

        // compute minimum
        return minimizes(f, maxEvaluations, checker);

    }

    /** Minimizes a cost function.
     * <p>The initial simplex is built from two vertices that are
     * considered to represent two opposite vertices of a box parallel
     * to the canonical axes of the space. The simplex is the subset of
     * vertices encountered while going from vertexA to vertexB
     * travelling along the box edges only. This can be seen as a scaled
     * regular simplex using the projected separation between the given
     * points as the scaling factor along each coordinate axis.</p>
     * <p>The optimization is performed in multi-start mode.</p>
     * @param f cost function
     * @param maxEvaluations maximal number of function calls for each
     * start (note that the number will be checked <em>after</em>
     * complete simplices have been evaluated, this means that in some
     * cases this number will be exceeded by a few units, depending on
     * the dimension of the problem)
     * @param checker object to use to check for convergence
     * @param vertexA first vertex
     * @param vertexB last vertex
     * @param starts number of starts to perform (including the
     * first one), multi-start is disabled if value is less than or
     * equal to 1
     * @param seed seed for the random vector generator
     * @return the point/cost pairs giving the minimal cost
     * @exception CostException if the cost function throws one during
     * the search
     * @exception NoConvergenceException if none of the starts did
     * converge (it is not thrown if at least one start did converge)
     */
    public PointCostPair minimizes(CostFunction f, int maxEvaluations,
                                   ConvergenceChecker checker,
                                   double[] vertexA, double[] vertexB,
                                   int starts, long seed)
    throws CostException, ConvergenceException {

        // set up the simplex travelling around the box
        buildSimplex(vertexA, vertexB);

        // we consider the simplex could have been produced by a generator
        // having its mean value at the center of the box, the standard
        // deviation along each axe beeing the corresponding half size
        double[] mean              = new double[vertexA.length];
        double[] standardDeviation = new double[vertexA.length];
        for (int i = 0; i < vertexA.length; ++i) {
            mean[i]              = 0.5 * (vertexA[i] + vertexB[i]);
            standardDeviation[i] = 0.5 * Math.abs(vertexA[i] - vertexB[i]);
        }

        RandomGenerator rg = new JDKRandomGenerator();
        rg.setSeed(seed);
        UniformRandomGenerator urg = new UniformRandomGenerator(rg);
        RandomVectorGenerator rvg =
            new UncorrelatedRandomVectorGenerator(mean, standardDeviation, urg);
        setMultiStart(starts, rvg);

        // compute minimum
        return minimizes(f, maxEvaluations, checker);

    }

    /** Minimizes a cost function.
     * <p>The simplex is built from all its vertices.</p>
     * <p>The optimization is performed in single-start mode.</p>
     * @param f cost function
     * @param maxEvaluations maximal number of function calls for each
     * start (note that the number will be checked <em>after</em>
     * complete simplices have been evaluated, this means that in some
     * cases this number will be exceeded by a few units, depending on
     * the dimension of the problem)
     * @param checker object to use to check for convergence
     * @param vertices array containing all vertices of the simplex
     * @return the point/cost pairs giving the minimal cost
     * @exception CostException if the cost function throws one during
     * the search
     * @exception ConvergenceException if none of the starts did
     * converge (it is not thrown if at least one start did converge)
     */
    public PointCostPair minimizes(CostFunction f, int maxEvaluations,
                                   ConvergenceChecker checker,
                                   double[][] vertices)
    throws CostException, ConvergenceException {

        // set up optimizer
        buildSimplex(vertices);
        setSingleStart();

        // compute minimum
        return minimizes(f, maxEvaluations, checker);

    }

    /** Minimizes a cost function.
     * <p>The simplex is built from all its vertices.</p>
     * <p>The optimization is performed in multi-start mode.</p>
     * @param f cost function
     * @param maxEvaluations maximal number of function calls for each
     * start (note that the number will be checked <em>after</em>
     * complete simplices have been evaluated, this means that in some
     * cases this number will be exceeded by a few units, depending on
     * the dimension of the problem)
     * @param checker object to use to check for convergence
     * @param vertices array containing all vertices of the simplex
     * @param starts number of starts to perform (including the
     * first one), multi-start is disabled if value is less than or
     * equal to 1
     * @param seed seed for the random vector generator
     * @return the point/cost pairs giving the minimal cost
     * @exception NotPositiveDefiniteMatrixException if the vertices
     * array is degenerated
     * @exception CostException if the cost function throws one during
     * the search
     * @exception ConvergenceException if none of the starts did
     * converge (it is not thrown if at least one start did converge)
     */
    public PointCostPair minimizes(CostFunction f, int maxEvaluations,
                                   ConvergenceChecker checker,
                                   double[][] vertices,
                                   int starts, long seed)
    throws NotPositiveDefiniteMatrixException,
    CostException, ConvergenceException {

        try {
            // store the points into the simplex
            buildSimplex(vertices);

            // compute the statistical properties of the simplex points
            VectorialMean meanStat = new VectorialMean(vertices[0].length);
            VectorialCovariance covStat = new VectorialCovariance(vertices[0].length);
            for (int i = 0; i < vertices.length; ++i) {
                meanStat.increment(vertices[i]);
                covStat.increment(vertices[i]);
            }
            double[] mean = meanStat.getResult();
            RealMatrix covariance = covStat.getResult();
            

            RandomGenerator rg = new JDKRandomGenerator();
            rg.setSeed(seed);
            RandomVectorGenerator rvg =
                new CorrelatedRandomVectorGenerator(mean,
                                                    covariance, 1.0e-12 * covariance.getNorm(),
                                                    new UniformRandomGenerator(rg));
            setMultiStart(starts, rvg);

            // compute minimum
            return minimizes(f, maxEvaluations, checker);

        } catch (DimensionMismatchException dme) {
            // this should not happen
            throw new RuntimeException("internal error");
        }

    }

    /** Minimizes a cost function.
     * <p>The simplex is built randomly.</p>
     * <p>The optimization is performed in single-start mode.</p>
     * @param f cost function
     * @param maxEvaluations maximal number of function calls for each
     * start (note that the number will be checked <em>after</em>
     * complete simplices have been evaluated, this means that in some
     * cases this number will be exceeded by a few units, depending on
     * the dimension of the problem)
     * @param checker object to use to check for convergence
     * @param generator random vector generator
     * @return the point/cost pairs giving the minimal cost
     * @exception CostException if the cost function throws one during
     * the search
     * @exception ConvergenceException if none of the starts did
     * converge (it is not thrown if at least one start did converge)
     */
    public PointCostPair minimizes(CostFunction f, int maxEvaluations,
                                   ConvergenceChecker checker,
                                   RandomVectorGenerator generator)
    throws CostException, ConvergenceException {

        // set up optimizer
        buildSimplex(generator);
        setSingleStart();

        // compute minimum
        return minimizes(f, maxEvaluations, checker);

    }

    /** Minimizes a cost function.
     * <p>The simplex is built randomly.</p>
     * <p>The optimization is performed in multi-start mode.</p>
     * @param f cost function
     * @param maxEvaluations maximal number of function calls for each
     * start (note that the number will be checked <em>after</em>
     * complete simplices have been evaluated, this means that in some
     * cases this number will be exceeded by a few units, depending on
     * the dimension of the problem)
     * @param checker object to use to check for convergence
     * @param generator random vector generator
     * @param starts number of starts to perform (including the
     * first one), multi-start is disabled if value is less than or
     * equal to 1
     * @return the point/cost pairs giving the minimal cost
     * @exception CostException if the cost function throws one during
     * the search
     * @exception ConvergenceException if none of the starts did
     * converge (it is not thrown if at least one start did converge)
     */
    public PointCostPair minimizes(CostFunction f, int maxEvaluations,
                                   ConvergenceChecker checker,
                                   RandomVectorGenerator generator,
                                   int starts)
    throws CostException, ConvergenceException {

        // set up optimizer
        buildSimplex(generator);
        setMultiStart(starts, generator);

        // compute minimum
        return minimizes(f, maxEvaluations, checker);

    }

    /** Build a simplex from two extreme vertices.
     * <p>The two vertices are considered to represent two opposite
     * vertices of a box parallel to the canonical axes of the
     * space. The simplex is the subset of vertices encountered while
     * going from vertexA to vertexB travelling along the box edges
     * only. This can be seen as a scaled regular simplex using the
     * projected separation between the given points as the scaling
     * factor along each coordinate axis.</p>
     * @param vertexA first vertex
     * @param vertexB last vertex
     */
    private void buildSimplex(double[] vertexA, double[] vertexB) {

        int n = vertexA.length;
        simplex = new PointCostPair[n + 1];

        // set up the simplex travelling around the box
        for (int i = 0; i <= n; ++i) {
            double[] vertex = new double[n];
            if (i > 0) {
                System.arraycopy(vertexB, 0, vertex, 0, i);
            }
            if (i < n) {
                System.arraycopy(vertexA, i, vertex, i, n - i);
            }
            simplex[i] = new PointCostPair(vertex, Double.NaN);
        }

    }

    /** Build a simplex from all its points.
     * @param vertices array containing all vertices of the simplex
     */
    private void buildSimplex(double[][] vertices) {
        int n = vertices.length - 1;
        simplex = new PointCostPair[n + 1];
        for (int i = 0; i <= n; ++i) {
            simplex[i] = new PointCostPair(vertices[i], Double.NaN);
        }
    }

    /** Build a simplex randomly.
     * @param generator random vector generator
     */
    private void buildSimplex(RandomVectorGenerator generator) {

        // use first vector size to compute the number of points
        double[] vertex = generator.nextVector();
        int n = vertex.length;
        simplex = new PointCostPair[n + 1];
        simplex[0] = new PointCostPair(vertex, Double.NaN);

        // fill up the vertex
        for (int i = 1; i <= n; ++i) {
            simplex[i] = new PointCostPair(generator.nextVector(), Double.NaN);
        }

    }

    /** Set up single-start mode.
     */
    private void setSingleStart() {
        starts    = 1;
        generator = null;
        minima    = null;
    }

    /** Set up multi-start mode.
     * @param starts number of starts to perform (including the
     * first one), multi-start is disabled if value is less than or
     * equal to 1
     * @param generator random vector generator to use for restarts
     */
    public void setMultiStart(int starts, RandomVectorGenerator generator) {
        if (starts < 2) {
            this.starts    = 1;
            this.generator = null;
            minima         = null;
        } else {
            this.starts    = starts;
            this.generator = generator;
            minima         = null;
        }
    }

    /** Get all the minima found during the last call to {@link
     * #minimizes(CostFunction, int, ConvergenceChecker, double[], double[])
     * minimizes}.
     * <p>The optimizer stores all the minima found during a set of
     * restarts when multi-start mode is enabled. The {@link
     * #minimizes(CostFunction, int, ConvergenceChecker, double[], double[])
     * minimizes} method returns the best point only. This method
     * returns all the points found at the end of each starts, including
     * the best one already returned by the {@link #minimizes(CostFunction,
     * int, ConvergenceChecker, double[], double[]) minimizes} method.
     * The array as one element for each start as specified in the constructor
     * (it has one element only if optimizer has been set up for single-start).</p>
     * <p>The array containing the minima is ordered with the results
     * from the runs that did converge first, sorted from lowest to
     * highest minimum cost, and null elements corresponding to the runs
     * that did not converge (all elements will be null if the {@link
     * #minimizes(CostFunction, int, ConvergenceChecker, double[], double[])
     * minimizes} method throwed a {@link ConvergenceException
     * ConvergenceException}).</p>
     * @return array containing the minima, or null if {@link
     * #minimizes(CostFunction, int, ConvergenceChecker, double[], double[])
     * minimizes} has not been called
     */
    public PointCostPair[] getMinima() {
        return (PointCostPair[]) minima.clone();
    }

    /** Minimizes a cost function.
     * @param f cost function
     * @param maxEvaluations maximal number of function calls for each
     * start (note that the number will be checked <em>after</em>
     * complete simplices have been evaluated, this means that in some
     * cases this number will be exceeded by a few units, depending on
     * the dimension of the problem)
     * @param checker object to use to check for convergence
     * @return the point/cost pairs giving the minimal cost
     * @exception CostException if the cost function throws one during
     * the search
     * @exception ConvergenceException if none of the starts did
     * converge (it is not thrown if at least one start did converge)
     */
    private PointCostPair minimizes(CostFunction f, int maxEvaluations,
                                    ConvergenceChecker checker)
    throws CostException, ConvergenceException {

        this.f = f;
        minima = new PointCostPair[starts];

        // multi-start loop
        for (int i = 0; i < starts; ++i) {

            evaluations = 0;
            evaluateSimplex();

            for (boolean loop = true; loop;) {
                if (checker.converged(simplex)) {
                    // we have found a minimum
                    minima[i] = simplex[0];
                    loop = false;
                } else if (evaluations >= maxEvaluations) {
                    // this start did not converge, try a new one
                    minima[i] = null;
                    loop = false;
                } else {
                    iterateSimplex();
                }
            }

            if (i < (starts - 1)) {
                // restart
                buildSimplex(generator);
            }

        }

        // sort the minima from lowest cost to highest cost, followed by
        // null elements
        Arrays.sort(minima, pointCostPairComparator);

        // return the found point given the lowest cost
        if (minima[0] == null) {
            throw new ConvergenceException("none of the {0} start points"
                    + " lead to convergence",
                    new String[] {
                            Integer.toString(starts)
                    });
        }
        return minima[0];

    }

    /** Compute the next simplex of the algorithm.
     */
    protected abstract void iterateSimplex()
    throws CostException;

    /** Evaluate the cost on one point.
     * <p>A side effect of this method is to count the number of
     * function evaluations</p>
     * @param x point on which the cost function should be evaluated
     * @return cost at the given point
     * @exception CostException if no cost can be computed for the parameters
     */
    protected double evaluateCost(double[] x)
    throws CostException {
        evaluations++;
        return f.cost(x);
    }

    /** Evaluate all the non-evaluated points of the simplex.
     * @exception CostException if no cost can be computed for the parameters
     */
    protected void evaluateSimplex()
    throws CostException {

        // evaluate the cost at all non-evaluated simplex points
        for (int i = 0; i < simplex.length; ++i) {
            PointCostPair pair = simplex[i];
            if (Double.isNaN(pair.cost)) {
                simplex[i] = new PointCostPair(pair.point, evaluateCost(pair.point));
            }
        }

        // sort the simplex from lowest cost to highest cost
        Arrays.sort(simplex, pointCostPairComparator);

    }

    /** Replace the worst point of the simplex by a new point.
     * @param pointCostPair point to insert
     */
    protected void replaceWorstPoint(PointCostPair pointCostPair) {
        int n = simplex.length - 1;
        for (int i = 0; i < n; ++i) {
            if (simplex[i].cost > pointCostPair.cost) {
                PointCostPair tmp = simplex[i];
                simplex[i]        = pointCostPair;
                pointCostPair     = tmp;
            }
        }
        simplex[n] = pointCostPair;
    }

    /** Comparator for {@link PointCostPair PointCostPair} objects. */
    private static Comparator pointCostPairComparator = new Comparator() {
        public int compare(Object o1, Object o2) {
            if (o1 == null) {
                return (o2 == null) ? 0 : +1;
            } else if (o2 == null) {
                return -1;
            } else {
                double cost1 = ((PointCostPair) o1).cost;
                double cost2 = ((PointCostPair) o2).cost;
                return (cost1 < cost2) ? -1 : ((o1 == o2) ? 0 : +1);
            }
        }
    };

    /** Simplex. */
    protected PointCostPair[] simplex;

    /** Cost function. */
    private CostFunction f;

    /** Number of evaluations already performed. */
    private int evaluations;

    /** Number of starts to go. */
    private int starts;

    /** Random generator for multi-start. */
    private RandomVectorGenerator generator;

    /** Found minima. */
    private PointCostPair[] minima;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14685.java