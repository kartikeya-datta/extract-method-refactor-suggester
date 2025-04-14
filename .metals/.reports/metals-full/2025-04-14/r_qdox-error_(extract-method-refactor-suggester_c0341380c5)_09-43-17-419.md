error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8708.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8708.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8708.java
text:
```scala
S@@ystem.arraycopy(b[i], 0, root.getDataRef()[index[i]], 0, rank);

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

package org.apache.commons.math.random;

import org.apache.commons.math.DimensionMismatchException;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

/** 
 * A {@link RandomVectorGenerator} that generates vectors with with 
 * correlated components.
 * <p>Random vectors with correlated components are built by combining
 * the uncorrelated components of another random vector in such a way that
 * the resulting correlations are the ones specified by a positive
 * definite covariance matrix.</p>
 * <p>The main use for correlated random vector generation is for Monte-Carlo
 * simulation of physical problems with several variables, for example to
 * generate error vectors to be added to a nominal vector. A particularly
 * interesting case is when the generated vector should be drawn from a <a
 * href="http://en.wikipedia.org/wiki/Multivariate_normal_distribution">
 * Multivariate Normal Distribution</a>. The approach using a Cholesky
 * decomposition is quite usual in this case. However, it cas be extended
 * to other cases as long as the underlying random generator provides
 * {@link NormalizedRandomGenerator normalized values} like {@link
 * GaussianRandomGenerator} or {@link UniformRandomGenerator}.</p>
 * <p>Sometimes, the covariance matrix for a given simulation is not
 * strictly positive definite. This means that the correlations are
 * not all independent from each other. In this case, however, the non
 * strictly positive elements found during the Cholesky decomposition
 * of the covariance matrix should not be negative either, they
 * should be null. Another non-conventional extension handling this case
 * is used here. Rather than computing <code>C = U<sup>T</sup>.U</code>
 * where <code>C</code> is the covariance matrix and <code>U</code>
 * is an uppertriangular matrix, we compute <code>C = B.B<sup>T</sup></code>
 * where <code>B</code> is a rectangular matrix having
 * more rows than columns. The number of columns of <code>B</code> is
 * the rank of the covariance matrix, and it is the dimension of the
 * uncorrelated random vector that is needed to compute the component
 * of the correlated vector. This class handles this situation
 * automatically.</p>
 *
 * @version $Revision$ $Date$
 * @since 1.2
 */

public class CorrelatedRandomVectorGenerator
implements RandomVectorGenerator {

    /** Simple constructor.
     * <p>Build a correlated random vector generator from its mean
     * vector and covariance matrix.</p>
     * @param mean expected mean values for all components
     * @param covariance covariance matrix
     * @param small diagonal elements threshold under which  column are
     * considered to be dependent on previous ones and are discarded
     * @param generator underlying generator for uncorrelated normalized
     * components
     * @exception IllegalArgumentException if there is a dimension
     * mismatch between the mean vector and the covariance matrix
     * @exception NotPositiveDefiniteMatrixException if the
     * covariance matrix is not strictly positive definite
     * @exception DimensionMismatchException if the mean and covariance
     * arrays dimensions don't match
     */
    public CorrelatedRandomVectorGenerator(double[] mean,
                                           RealMatrix covariance, double small,
                                           NormalizedRandomGenerator generator)
    throws NotPositiveDefiniteMatrixException, DimensionMismatchException {

        int order = covariance.getRowDimension();
        if (mean.length != order) {
            throw new DimensionMismatchException(mean.length, order);
        }
        this.mean = (double[]) mean.clone();

        decompose(covariance, small);

        this.generator = generator;
        normalized = new double[rank];

    }

    /** Simple constructor.
     * <p>Build a null mean random correlated vector generator from its
     * covariance matrix.</p>
     * @param covariance covariance matrix
     * @param small diagonal elements threshold under which  column are
     * considered to be dependent on previous ones and are discarded
     * @param generator underlying generator for uncorrelated normalized
     * components
     * @exception NotPositiveDefiniteMatrixException if the
     * covariance matrix is not strictly positive definite
     */
    public CorrelatedRandomVectorGenerator(RealMatrix covariance, double small,
                                           NormalizedRandomGenerator generator)
    throws NotPositiveDefiniteMatrixException {

        int order = covariance.getRowDimension();
        mean = new double[order];
        for (int i = 0; i < order; ++i) {
            mean[i] = 0;
        }

        decompose(covariance, small);

        this.generator = generator;
        normalized = new double[rank];

    }

    /** Get the underlying normalized components generator.
     * @return underlying uncorrelated components generator
     */
    public NormalizedRandomGenerator getGenerator() {
        return generator;
    }

    /** Get the root of the covariance matrix.
     * The root is the rectangular matrix <code>B</code> such that
     * the covariance matrix is equal to <code>B.B<sup>T</sup></code>
     * @return root of the square matrix
     * @see #getRank()
     */
    public RealMatrix getRootMatrix() {
        return root;
    }

    /** Get the rank of the covariance matrix.
     * The rank is the number of independent rows in the covariance
     * matrix, it is also the number of columns of the rectangular
     * matrix of the decomposition.
     * @return rank of the square matrix.
     * @see #getRootMatrix()
     */
    public int getRank() {
        return rank;
    }

    /** Decompose the original square matrix.
     * <p>The decomposition is based on a Choleski decomposition
     * where additional transforms are performed:
     * <ul>
     *   <li>the rows of the decomposed matrix are permuted</li>
     *   <li>columns with the too small diagonal element are discarded</li>
     *   <li>the matrix is permuted</li>
     * </ul>
     * This means that rather than computing M = U<sup>T</sup>.U where U
     * is an upper triangular matrix, this method computed M=B.B<sup>T</sup>
     * where B is a rectangular matrix.
     * @param covariance covariance matrix
     * @param small diagonal elements threshold under which  column are
     * considered to be dependent on previous ones and are discarded
     * @exception NotPositiveDefiniteMatrixException if the
     * covariance matrix is not strictly positive definite
     */
    private void decompose(RealMatrix covariance, double small)
    throws NotPositiveDefiniteMatrixException {

        int order = covariance.getRowDimension();
        double[][] c = covariance.getData();
        double[][] b = new double[order][order];

        int[] swap  = new int[order];
        int[] index = new int[order];
        for (int i = 0; i < order; ++i) {
            index[i] = i;
        }

        rank = 0;
        for (boolean loop = true; loop;) {

            // find maximal diagonal element
            swap[rank] = rank;
            for (int i = rank + 1; i < order; ++i) {
                int ii  = index[i];
                int isi = index[swap[i]];
                if (c[ii][ii] > c[isi][isi]) {
                    swap[rank] = i;
                }
            }


            // swap elements
            if (swap[rank] != rank) {
                int tmp = index[rank];
                index[rank] = index[swap[rank]];
                index[swap[rank]] = tmp;
            }

            // check diagonal element
            int ir = index[rank];
            if (c[ir][ir] < small) {

                if (rank == 0) {
                    throw new NotPositiveDefiniteMatrixException();
                }

                // check remaining diagonal elements
                for (int i = rank; i < order; ++i) {
                    if (c[index[i]][index[i]] < -small) {
                        // there is at least one sufficiently negative diagonal element,
                        // the covariance matrix is wrong
                        throw new NotPositiveDefiniteMatrixException();
                    }
                }

                // all remaining diagonal elements are close to zero,
                // we consider we have found the rank of the covariance matrix
                ++rank;
                loop = false;

            } else {

                // transform the matrix
                double sqrt = Math.sqrt(c[ir][ir]);
                b[rank][rank] = sqrt;
                double inverse = 1 / sqrt;
                for (int i = rank + 1; i < order; ++i) {
                    int ii = index[i];
                    double e = inverse * c[ii][ir];
                    b[i][rank] = e;
                    c[ii][ii] -= e * e;
                    for (int j = rank + 1; j < i; ++j) {
                        int ij = index[j];
                        double f = c[ii][ij] - e * b[j][rank];
                        c[ii][ij] = f;
                        c[ij][ii] = f;
                    }
                }

                // prepare next iteration
                loop = ++rank < order;

            }

        }

        // build the root matrix
        root = new RealMatrixImpl(order, rank);
        for (int i = 0; i < order; ++i) {
            System.arraycopy(b[i], 0, root.getDataRef()[swap[i]], 0, rank);
        }

    }

    /** Generate a correlated random vector.
     * @return a random vector as an array of double. The returned array
     * is created at each call, the caller can do what it wants with it.
     */
    public double[] nextVector() {

        // generate uncorrelated vector
        for (int i = 0; i < rank; ++i) {
            normalized[i] = generator.nextNormalizedDouble();
        }

        // compute correlated vector
        double[] correlated = new double[mean.length];
        for (int i = 0; i < correlated.length; ++i) {
            correlated[i] = mean[i];
            for (int j = 0; j < rank; ++j) {
                correlated[i] += root.getEntry(i, j) * normalized[j];
            }
        }

        return correlated;

    }

    /** Mean vector. */
    private double[] mean;

    /** Permutated Cholesky root of the covariance matrix. */
    private RealMatrixImpl root;

    /** Rank of the covariance matrix. */
    private int rank;

    /** Underlying generator. */
    private NormalizedRandomGenerator generator;

    /** Storage for the normalized vector. */
    private double[] normalized;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8708.java