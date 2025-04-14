error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1940.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1940.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1940.java
text:
```scala
final d@@ouble[] vectorData;

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
package org.apache.commons.math3.linear;

import java.io.Serializable;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

/**
 * Implementation of a diagonal matrix.
 *
 * @version $Id$
 * @since 3.1.1
 */
public class DiagonalMatrix extends AbstractRealMatrix
    implements Serializable {
    /** Serializable version identifier. */
    private static final long serialVersionUID = 20121229L;
    /** Entries of the diagonal. */
    private final double[] data;

    /**
     * Creates a matrix with the supplied dimension.
     *
     * @param dimension Number of rows and columns in the new matrix.
     * @throws NotStrictlyPositiveException if the dimension is
     * not positive.
     */
    public DiagonalMatrix(final int dimension)
        throws NotStrictlyPositiveException {
        super(dimension, dimension);
        data = new double[dimension];
    }

    /**
     * Creates a matrix using the input array as the underlying data.
     * <br/>
     * The input array is copied, not referenced.
     *
     * @param d Data for the new matrix.
     */
    public DiagonalMatrix(final double[] d) {
        this(d, true);
    }

    /**
     * Creates a matrix using the input array as the underlying data.
     * <br/>
     * If an array is created specially in order to be embedded in a
     * this instance and not used directly, the {@code copyArray} may be
     * set to {@code false}.
     * This will prevent the copying and improve performance as no new
     * array will be built and no data will be copied.
     *
     * @param d Data for new matrix.
     * @param copyArray if {@code true}, the input array will be copied,
     * otherwise it will be referenced.
     * @exception NullArgumentException if d is null
     */
    public DiagonalMatrix(final double[] d, final boolean copyArray)
        throws NullArgumentException {
        MathUtils.checkNotNull(d);
        data = copyArray ? d.clone() : d;
    }

    /**
     * {@inheritDoc}
     *
     * @throws DimensionMismatchException if the requested dimensions are not equal.
     */
    @Override
    public RealMatrix createMatrix(final int rowDimension,
                                   final int columnDimension)
        throws NotStrictlyPositiveException,
               DimensionMismatchException {
        if (rowDimension != columnDimension) {
            throw new DimensionMismatchException(rowDimension, columnDimension);
        }

        return new DiagonalMatrix(rowDimension);
    }

    /** {@inheritDoc} */
    @Override
    public RealMatrix copy() {
        return new DiagonalMatrix(data);
    }

    /**
     * Compute the sum of {@code this} and {@code m}.
     *
     * @param m Matrix to be added.
     * @return {@code this + m}.
     * @throws MatrixDimensionMismatchException if {@code m} is not the same
     * size as {@code this}.
     */
    public DiagonalMatrix add(final DiagonalMatrix m)
        throws MatrixDimensionMismatchException {
        // Safety check.
        MatrixUtils.checkAdditionCompatible(this, m);

        final int dim = getRowDimension();
        final double[] outData = new double[dim];
        for (int i = 0; i < dim; i++) {
            outData[i] = data[i] + m.data[i];
        }

        return new DiagonalMatrix(outData, false);
    }

    /**
     * Returns {@code this} minus {@code m}.
     *
     * @param m Matrix to be subtracted.
     * @return {@code this - m}
     * @throws MatrixDimensionMismatchException if {@code m} is not the same
     * size as {@code this}.
     */
    public DiagonalMatrix subtract(final DiagonalMatrix m)
        throws MatrixDimensionMismatchException {
        MatrixUtils.checkSubtractionCompatible(this, m);

        final int dim = getRowDimension();
        final double[] outData = new double[dim];
        for (int i = 0; i < dim; i++) {
            outData[i] = data[i] - m.data[i];
        }

        return new DiagonalMatrix(outData, false);
    }

    /**
     * Returns the result of postmultiplying {@code this} by {@code m}.
     *
     * @param m matrix to postmultiply by
     * @return {@code this * m}
     * @throws DimensionMismatchException if
     * {@code columnDimension(this) != rowDimension(m)}
     */
    public DiagonalMatrix multiply(final DiagonalMatrix m)
        throws DimensionMismatchException {
        MatrixUtils.checkMultiplicationCompatible(this, m);

        final int dim = getRowDimension();
        final double[] outData = new double[dim];
        for (int i = 0; i < dim; i++) {
            outData[i] = data[i] * m.data[i];
        }

        return new DiagonalMatrix(outData, false);
    }

    /**
     * Returns the result of postmultiplying {@code this} by {@code m}.
     *
     * @param m matrix to postmultiply by
     * @return {@code this * m}
     * @throws DimensionMismatchException if
     * {@code columnDimension(this) != rowDimension(m)}
     */
    @Override
    public RealMatrix multiply(final RealMatrix m)
        throws DimensionMismatchException {
        if (m instanceof DiagonalMatrix) {
            return multiply((DiagonalMatrix) m);
        } else {
            MatrixUtils.checkMultiplicationCompatible(this, m);
            final int nRows = m.getRowDimension();
            final int nCols = m.getColumnDimension();
            final double[][] product = new double[nRows][nCols];
            for (int r = 0; r < nRows; r++) {
                for (int c = 0; c < nCols; c++) {
                    product[r][c] = data[r] * m.getEntry(r, c);
                }
            }
            return new Array2DRowRealMatrix(product, false);
        }
    }

    /** {@inheritDoc} */
    @Override
    public double[][] getData() {
        final int dim = getRowDimension();
        final double[][] out = new double[dim][dim];

        for (int i = 0; i < dim; i++) {
            out[i][i] = data[i];
        }

        return out;
    }

    /**
     * Gets a reference to the underlying data array.
     *
     * @return 1-dimensional array of entries.
     */
    public double[] getDataRef() {
        return data;
    }

    /** {@inheritDoc} */
    @Override
    public double getEntry(final int row, final int column)
        throws OutOfRangeException {
        MatrixUtils.checkMatrixIndex(this, row, column);
        return row == column ? data[row] : 0;
    }

    /** {@inheritDoc}
     * @throws NumberIsTooLargeException if {@code row != column} and value is non-zero.
     */
    @Override
    public void setEntry(final int row, final int column, final double value)
        throws OutOfRangeException, NumberIsTooLargeException {
        if (row == column) {
            MatrixUtils.checkRowIndex(this, row);
            data[row] = value;
        } else {
            ensureZero(value);
        }
    }

    /** {@inheritDoc}
     * @throws NumberIsTooLargeException if {@code row != column} and increment is non-zero.
     */
    @Override
    public void addToEntry(final int row,
                           final int column,
                           final double increment)
        throws OutOfRangeException, NumberIsTooLargeException {
        if (row == column) {
            MatrixUtils.checkRowIndex(this, row);
            data[row] += increment;
        } else {
            ensureZero(increment);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void multiplyEntry(final int row,
                              final int column,
                              final double factor)
        throws OutOfRangeException {
        // we don't care about non-diagonal elements for multiplication
        if (row == column) {
            MatrixUtils.checkRowIndex(this, row);
            data[row] *= factor;
        }
    }

    /** {@inheritDoc} */
    @Override
    public int getRowDimension() {
        return data.length;
    }

    /** {@inheritDoc} */
    @Override
    public int getColumnDimension() {
        return data.length;
    }

    /** {@inheritDoc} */
    @Override
    public double[] operate(final double[] v)
        throws DimensionMismatchException {
        return multiply(new DiagonalMatrix(v, false)).getDataRef();
    }

    /** {@inheritDoc} */
    @Override
    public double[] preMultiply(final double[] v)
        throws DimensionMismatchException {
        return operate(v);
    }

    /** {@inheritDoc} */
    @Override
    public RealVector preMultiply(final RealVector v) throws DimensionMismatchException {
        double[] vectorData = null;
        if (v instanceof ArrayRealVector) {
            vectorData = ((ArrayRealVector) v).getDataRef();
        } else {
            vectorData = v.toArray();
        }
        return MatrixUtils.createRealVector(preMultiply(vectorData));
    }

    /** Ensure a value is zero.
     * @param value value to check
     * @exception NumberIsTooLargeException if value is not zero
     */
    private void ensureZero(final double value) throws NumberIsTooLargeException {
        if (!Precision.equals(0.0, value, 1)) {
            throw new NumberIsTooLargeException(FastMath.abs(value), 0, true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1940.java