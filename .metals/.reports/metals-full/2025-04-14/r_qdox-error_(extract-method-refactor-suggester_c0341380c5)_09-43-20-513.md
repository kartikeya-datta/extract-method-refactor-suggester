error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6948.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6948.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6948.java
text:
```scala
r@@eturn MatrixUtils.createRealMatrix(xData);

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


/**
 * Class using QR decomposition to solve A &times; X = B in least square sense
 * for any matrices A.
 * <p>This class solve A &times; X = B in least squares sense: it finds X
 * such that ||A &times; X - B|| is minimal.</p>
 *   
 * @version $Revision$ $Date$
 * @since 2.0
 */
public class QRSolver implements DecompositionSolver {

    /** Serializable version identifier. */
    private static final long serialVersionUID = -579465076068393818L;

    /** Underlying decomposition. */
    private final QRDecomposition decomposition;

    /**
     * Simple constructor.
     * @param decomposition decomposition to use
     */
    public QRSolver(final QRDecomposition decomposition) {
        this.decomposition = decomposition;
    }

    /** Solve the linear equation A &times; X = B in least square sense.
     * <p>The m&times;n matrix A may not be square, the solution X is
     * such that ||A &times; X - B|| is minimal.</p>
     * @param b right-hand side of the equation A &times; X = B
     * @return a vector X that minimizes the two norm of A &times; X - B
     * @exception IllegalArgumentException if matrices dimensions don't match
     * @exception InvalidMatrixException if decomposed matrix is singular
     */
    public double[] solve(final double[] b)
        throws IllegalArgumentException, InvalidMatrixException {

        if (decomposition.getR().getRowDimension() != b.length) {
            throw new IllegalArgumentException("constant vector has wrong length");            
        }
        if (!isNonSingular()) {
            throw new SingularMatrixException();
        }

        // solve Q.y = b, using the fact Q is orthogonal
        final double[] y = decomposition.getQT().operate(b);

        // solve triangular system R.x = y
        final RealMatrix r = decomposition.getR();
        final double[] x = new double[r.getColumnDimension()];
        System.arraycopy(y, 0, x, 0, r.getRowDimension());
        for (int i = r.getRowDimension() - 1; i >= 0; --i) {
            x[i] /= r.getEntry(i, i);
            final double lastX = x[i];
            for (int j = i - 1; j >= 0; --j) {
                x[j] -= lastX * r.getEntry(j, i);
            }
        }

        return x;

    }

    /** Solve the linear equation A &times; X = B in least square sense.
     * <p>The m&times;n matrix A may not be square, the solution X is
     * such that ||A &times; X - B|| is minimal.</p>
     * @param b right-hand side of the equation A &times; X = B
     * @return a vector X that minimizes the two norm of A &times; X - B
     * @exception IllegalArgumentException if matrices dimensions don't match
     * @exception InvalidMatrixException if decomposed matrix is singular
     */
    public RealVector solve(final RealVector b)
        throws IllegalArgumentException, InvalidMatrixException {
        return new RealVectorImpl(solve(b.getData()), false);
    }

    /** Solve the linear equation A &times; X = B in least square sense.
     * <p>The m&times;n matrix A may not be square, the solution X is
     * such that ||A &times; X - B|| is minimal.</p>
     * @param b right-hand side of the equation A &times; X = B
     * @return a matrix X that minimizes the two norm of A &times; X - B
     * @exception IllegalArgumentException if matrices dimensions don't match
     * @exception InvalidMatrixException if decomposed matrix is singular
     */
    public RealMatrix solve(final RealMatrix b)
        throws IllegalArgumentException, InvalidMatrixException {

        if (decomposition.getR().getRowDimension() != b.getRowDimension()) {
            throw new IllegalArgumentException("Incorrect row dimension");            
        }
        if (!isNonSingular()) {
            throw new SingularMatrixException();
        }

        // solve Q.y = b, using the fact Q is orthogonal
        final RealMatrix y = decomposition.getQT().multiply(b);

        // solve triangular system R.x = y
        final RealMatrix r = decomposition.getR();
        final double[][] xData =
            new double[r.getColumnDimension()][b.getColumnDimension()];
        for (int i = 0; i < r.getRowDimension(); ++i) {
            final double[] xi = xData[i];
            for (int k = 0; k < xi.length; ++k) {
                xi[k] = y.getEntry(i, k);
            }
        }
        for (int i = r.getRowDimension() - 1; i >= 0; --i) {
            final double rii = r.getEntry(i, i);
            final double[] xi = xData[i];
            for (int k = 0; k < xi.length; ++k) {
                xi[k] /= rii;
                final double lastX = xi[k];
                for (int j = i - 1; j >= 0; --j) {
                    xData[j][k] -= lastX * r.getEntry(j, i);
                }
            }
        }

        return new RealMatrixImpl(xData, false);

    }

    /**
     * Check if the decomposed matrix is non-singular.
     * @return true if the decomposed matrix is non-singular
     */
    public boolean isNonSingular() {
        final RealMatrix r = decomposition.getR();
        final int p = Math.min(r.getRowDimension(), r.getColumnDimension());
        for (int i = 0; i < p; ++i) {
            if (r.getEntry(i, i) == 0) {
                return false;
            }
        }
        return true;
    }

    /** Get the pseudo-inverse of the decomposed matrix.
     * @return inverse matrix
     * @throws InvalidMatrixException if decomposed matrix is singular
     */
    public RealMatrix getInverse()
        throws InvalidMatrixException {
        final RealMatrix r = decomposition.getR();
        final int p = Math.min(r.getRowDimension(), r.getColumnDimension());
        return solve(MatrixUtils.createRealIdentityMatrix(p));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6948.java