error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14390.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14390.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14390.java
text:
```scala
t@@hrow new NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());

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

package org.apache.commons.math.linear;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Class transforming a symmetrical matrix to tridiagonal shape.
 * <p>A symmetrical m &times; m matrix A can be written as the product of three matrices:
 * A = Q &times; T &times; Q<sup>T</sup> with Q an orthogonal matrix and T a symmetrical
 * tridiagonal matrix. Both Q and T are m &times; m matrices.</p>
 * <p>This implementation only uses the upper part of the matrix, the part below the
 * diagonal is not accessed at all.</p>
 * <p>Transformation to tridiagonal shape is often not a goal by itself, but it is
 * an intermediate step in more general decomposition algorithms like {@link
 * EigenDecomposition eigen decomposition}. This class is therefore intended for internal
 * use by the library and is not public. As a consequence of this explicitly limited scope,
 * many methods directly returns references to internal arrays, not copies.</p>
 * @version $Revision$ $Date$
 * @since 2.0
 */
class TriDiagonalTransformer implements Serializable {

    /** Serializable version identifier. */
    private static final long serialVersionUID = 8935390784125343332L;

    /** Householder vectors. */
    private final double householderVectors[][];

    /** Main diagonal. */
    private final double[] main;

    /** Secondary diagonal. */
    private final double[] secondary;

    /** Cached value of Q. */
    private RealMatrix cachedQ;

    /** Cached value of Qt. */
    private RealMatrix cachedQt;

    /** Cached value of T. */
    private RealMatrix cachedT;

    /**
     * Build the transformation to tridiagonal shape of a symmetrical matrix.
     * <p>The specified matrix is assumed to be symmetrical without any check.
     * Only the upper triangular part of the matrix is used.</p>
     * @param matrix the symmetrical matrix to transform.
     * @exception InvalidMatrixException if matrix is not square
     */
    public TriDiagonalTransformer(RealMatrix matrix)
        throws InvalidMatrixException {
        if (!matrix.isSquare()) {
            throw new InvalidMatrixException("transformation to tridiagonal requires that the matrix be square");
        }

        final int m = matrix.getRowDimension();
        householderVectors = matrix.getData();
        main      = new double[m];
        secondary = new double[m - 1];
        cachedQ   = null;
        cachedQt  = null;
        cachedT   = null;

        // transform matrix
        transform();

    }

    /**
     * Returns the matrix Q of the transform. 
     * <p>Q is an orthogonal matrix, i.e. its transpose is also its inverse.</p>
     * @return the Q matrix
     */
    public RealMatrix getQ() {
        if (cachedQ == null) {
            cachedQ = getQT().transpose();
        }
        return cachedQ;
    }

    /**
     * Returns the transpose of the matrix Q of the transform. 
     * <p>Q is an orthogonal matrix, i.e. its transpose is also its inverse.</p>
     * @return the Q matrix
     */
    public RealMatrix getQT() {

        if (cachedQt == null) {

            final int m = householderVectors.length;
            final double[][] qtData  = new double[m][m];

            // build up first part of the matrix by applying Householder transforms
            for (int k = m - 1; k >= 1; --k) {
                final double[] hK = householderVectors[k - 1];
                final double inv = 1.0 / (secondary[k - 1] * hK[k]);
                qtData[k][k] = 1;
                if (hK[k] != 0.0) {
                    final double[] qtK = qtData[k];
                    double beta = 1.0 / secondary[k - 1];
                    qtK[k] = 1 + beta * hK[k];
                    for (int i = k + 1; i < m; ++i) {
                        qtK[i] = beta * hK[i];
                    }
                    for (int j = k + 1; j < m; ++j) {
                        final double[] qtJ = qtData[j];
                        beta = 0;
                        for (int i = k + 1; i < m; ++i) {
                            beta += qtJ[i] * hK[i];
                        }
                        beta *= inv;
                        qtJ[k] = beta * hK[k];
                        for (int i = k + 1; i < m; ++i) {
                            qtJ[i] += beta * hK[i];
                        }
                    }
                }
            }
            qtData[0][0] = 1;

            // cache the matrix for subsequent calls
            cachedQt = new RealMatrixImpl(qtData, false);

        }

        // return the cached matrix
        return cachedQt;

    }

    /**
     * Returns the tridiagonal matrix T of the transform. 
     * @return the T matrix
     */
    public RealMatrix getT() {

        if (cachedT == null) {

            final int m = main.length;
            double[][] tData = new double[m][m];
            for (int i = 0; i < m; ++i) {
                double[] tDataI = tData[i];
                tDataI[i] = main[i];
                if (i > 0) {
                    tDataI[i - 1] = secondary[i - 1];
                }
                if (i < main.length - 1) {
                    tDataI[i + 1] = secondary[i];
                }
            }

            // cache the matrix for subsequent calls
            cachedT = new RealMatrixImpl(tData, false);

        }

        // return the cached matrix
        return cachedT;

    }

    /**
     * Get the Householder vectors of the transform.
     * <p>Note that since this class is only intended for internal use,
     * it returns directly a reference to its internal arrays, not a copy.</p>
     * @return the main diagonal elements of the B matrix
     */
    double[][] getHouseholderVectorsRef() {
        return householderVectors;
    }

    /**
     * Get the main diagonal elements of the matrix T of the transform.
     * <p>Note that since this class is only intended for internal use,
     * it returns directly a reference to its internal arrays, not a copy.</p>
     * @return the main diagonal elements of the T matrix
     */
    double[] getMainDiagonalRef() {
        return main;
    }

    /**
     * Get the secondary diagonal elements of the matrix T of the transform.
     * <p>Note that since this class is only intended for internal use,
     * it returns directly a reference to its internal arrays, not a copy.</p>
     * @return the secondary diagonal elements of the T matrix
     */
    double[] getSecondaryDiagonalRef() {
        return secondary;
    }

    /**
     * Transform original matrix to tridiagonal form.
     * <p>Transformation is done using Householder transforms.</p>
     */
    private void transform() {

        final int m = householderVectors.length;
        final double[] z = new double[m];
        for (int k = 0; k < m - 1; k++) {

            //zero-out a row and a column simultaneously
            final double[] hK = householderVectors[k];
            main[k] = hK[k];
            double xNormSqr = 0;
            for (int j = k + 1; j < m; ++j) {
                final double c = hK[j];
                xNormSqr += c * c;
            }
            final double a = (hK[k + 1] > 0) ? -Math.sqrt(xNormSqr) : Math.sqrt(xNormSqr);
            secondary[k] = a;
            if (a != 0.0) {
                // apply Householder transform from left and right simultaneously

                hK[k + 1] -= a;
                final double beta = -1 / (a * hK[k + 1]);

                // compute a = beta A v, where v is the Householder vector
                // this loop is written in such a way
                //   1) only the upper triangular part of the matrix is accessed
                //   2) access is cache-friendly for a matrix stored in rows
                Arrays.fill(z, k + 1, m, 0);
                for (int i = k + 1; i < m; ++i) {
                    final double[] hI = householderVectors[i];
                    final double hKI = hK[i];
                    double zI = hI[i] * hKI;
                    for (int j = i + 1; j < m; ++j) {
                        final double hIJ = hI[j];
                        zI   += hIJ * hK[j];
                        z[j] += hIJ * hKI;
                    }
                    z[i] = beta * (z[i] + zI);
                }

                // compute gamma = beta vT z / 2
                double gamma = 0;
                for (int i = k + 1; i < m; ++i) {
                    gamma += z[i] * hK[i];
                }
                gamma *= beta / 2;

                // compute z = z - gamma v
                for (int i = k + 1; i < m; ++i) {
                    z[i] -= gamma * hK[i];
                }

                // update matrix: A = A - v zT - z vT
                // only the upper triangular part of the matrix is updated
                for (int i = k + 1; i < m; ++i) {
                    final double[] hI = householderVectors[i];
                    for (int j = i; j < m; ++j) {
                        hI[j] -= hK[i] * z[j] + z[i] * hK[j];
                    }
                }

            }

        }
        main[m - 1] = householderVectors[m - 1][m - 1];
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14390.java