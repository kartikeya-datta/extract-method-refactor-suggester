error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4089.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4089.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4089.java
text:
```scala
protected v@@oid validateSampleData(double[][] x, double[] y) throws MathIllegalArgumentException {

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
package org.apache.commons.math3.stat.regression;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.util.FastMath;

/**
 * Abstract base class for implementations of MultipleLinearRegression.
 * @version $Id$
 * @since 2.0
 */
public abstract class AbstractMultipleLinearRegression implements
        MultipleLinearRegression {

    /** X sample data. */
    private RealMatrix xMatrix;

    /** Y sample data. */
    private RealVector yVector;

    /** Whether or not the regression model includes an intercept.  True means no intercept. */
    private boolean noIntercept = false;

    /**
     * @return the X sample data.
     */
    protected RealMatrix getX() {
        return xMatrix;
    }

    /**
     * @return the Y sample data.
     */
    protected RealVector getY() {
        return yVector;
    }

    /**
     * @return true if the model has no intercept term; false otherwise
     * @since 2.2
     */
    public boolean isNoIntercept() {
        return noIntercept;
    }

    /**
     * @param noIntercept true means the model is to be estimated without an intercept term
     * @since 2.2
     */
    public void setNoIntercept(boolean noIntercept) {
        this.noIntercept = noIntercept;
    }

    /**
     * <p>Loads model x and y sample data from a flat input array, overriding any previous sample.
     * </p>
     * <p>Assumes that rows are concatenated with y values first in each row.  For example, an input
     * <code>data</code> array containing the sequence of values (1, 2, 3, 4, 5, 6, 7, 8, 9) with
     * <code>nobs = 3</code> and <code>nvars = 2</code> creates a regression dataset with two
     * independent variables, as below:
     * <pre>
     *   y   x[0]  x[1]
     *   --------------
     *   1     2     3
     *   4     5     6
     *   7     8     9
     * </pre>
     * </p>
     * <p>Note that there is no need to add an initial unitary column (column of 1's) when
     * specifying a model including an intercept term.  If {@link #isNoIntercept()} is <code>true</code>,
     * the X matrix will be created without an initial column of "1"s; otherwise this column will
     * be added.
     * </p>
     * <p>Throws IllegalArgumentException if any of the following preconditions fail:
     * <ul><li><code>data</code> cannot be null</li>
     * <li><code>data.length = nobs * (nvars + 1)</li>
     * <li><code>nobs > nvars</code></li></ul>
     * </p>
     *
     * @param data input data array
     * @param nobs number of observations (rows)
     * @param nvars number of independent variables (columns, not counting y)
     * @throws NullArgumentException if the data array is null
     * @throws DimensionMismatchException if the length of the data array is not equal
     * to <code>nobs * (nvars + 1)</code>
     * @throws NumberIsTooSmallException if <code>nobs</code> is smaller than
     * <code>nvars</code>
     */
    public void newSampleData(double[] data, int nobs, int nvars) {
        if (data == null) {
            throw new NullArgumentException();
        }
        if (data.length != nobs * (nvars + 1)) {
            throw new DimensionMismatchException(data.length, nobs * (nvars + 1));
        }
        if (nobs <= nvars) {
            throw new NumberIsTooSmallException(nobs, nvars, false);
        }
        double[] y = new double[nobs];
        final int cols = noIntercept ? nvars: nvars + 1;
        double[][] x = new double[nobs][cols];
        int pointer = 0;
        for (int i = 0; i < nobs; i++) {
            y[i] = data[pointer++];
            if (!noIntercept) {
                x[i][0] = 1.0d;
            }
            for (int j = noIntercept ? 0 : 1; j < cols; j++) {
                x[i][j] = data[pointer++];
            }
        }
        this.xMatrix = new Array2DRowRealMatrix(x);
        this.yVector = new ArrayRealVector(y);
    }

    /**
     * Loads new y sample data, overriding any previous data.
     *
     * @param y the array representing the y sample
     * @throws NullArgumentException if y is null
     * @throws NoDataException if y is empty
     */
    protected void newYSampleData(double[] y) {
        if (y == null) {
            throw new NullArgumentException();
        }
        if (y.length == 0) {
            throw new NoDataException();
        }
        this.yVector = new ArrayRealVector(y);
    }

    /**
     * <p>Loads new x sample data, overriding any previous data.
     * </p>
     * The input <code>x</code> array should have one row for each sample
     * observation, with columns corresponding to independent variables.
     * For example, if <pre>
     * <code> x = new double[][] {{1, 2}, {3, 4}, {5, 6}} </code></pre>
     * then <code>setXSampleData(x) </code> results in a model with two independent
     * variables and 3 observations:
     * <pre>
     *   x[0]  x[1]
     *   ----------
     *     1    2
     *     3    4
     *     5    6
     * </pre>
     * </p>
     * <p>Note that there is no need to add an initial unitary column (column of 1's) when
     * specifying a model including an intercept term.
     * </p>
     * @param x the rectangular array representing the x sample
     * @throws NullArgumentException if x is null
     * @throws NoDataException if x is empty
     * @throws DimensionMismatchException if x is not rectangular
     */
    protected void newXSampleData(double[][] x) {
        if (x == null) {
            throw new NullArgumentException();
        }
        if (x.length == 0) {
            throw new NoDataException();
        }
        if (noIntercept) {
            this.xMatrix = new Array2DRowRealMatrix(x, true);
        } else { // Augment design matrix with initial unitary column
            final int nVars = x[0].length;
            final double[][] xAug = new double[x.length][nVars + 1];
            for (int i = 0; i < x.length; i++) {
                if (x[i].length != nVars) {
                    throw new DimensionMismatchException(x[i].length, nVars);
                }
                xAug[i][0] = 1.0d;
                System.arraycopy(x[i], 0, xAug[i], 1, nVars);
            }
            this.xMatrix = new Array2DRowRealMatrix(xAug, false);
        }
    }

    /**
     * Validates sample data.  Checks that
     * <ul><li>Neither x nor y is null or empty;</li>
     * <li>The length (i.e. number of rows) of x equals the length of y</li>
     * <li>x has at least one more row than it has columns (i.e. there is
     * sufficient data to estimate regression coefficients for each of the
     * columns in x plus an intercept.</li>
     * </ul>
     *
     * @param x the [n,k] array representing the x data
     * @param y the [n,1] array representing the y data
     * @throws NullArgumentException if {@code x} or {@code y} is null
     * @throws DimensionMismatchException if {@code x} and {@code y} do not
     * have the same length
     * @throws NoDataException if {@code x} or {@code y} are zero-length
     * @throws MathIllegalArgumentException if the number of rows of {@code x}
     * is not larger than the number of columns + 1
     */
    protected void validateSampleData(double[][] x, double[] y) {
        if ((x == null) || (y == null)) {
            throw new NullArgumentException();
        }
        if (x.length != y.length) {
            throw new DimensionMismatchException(y.length, x.length);
        }
        if (x.length == 0) {  // Must be no y data either
            throw new NoDataException();
        }
        if (x[0].length + 1 > x.length) {
            throw new MathIllegalArgumentException(
                    LocalizedFormats.NOT_ENOUGH_DATA_FOR_NUMBER_OF_PREDICTORS,
                    x.length, x[0].length);
        }
    }

    /**
     * Validates that the x data and covariance matrix have the same
     * number of rows and that the covariance matrix is square.
     *
     * @param x the [n,k] array representing the x sample
     * @param covariance the [n,n] array representing the covariance matrix
     * @throws DimensionMismatchException if the number of rows in x is not equal
     * to the number of rows in covariance
     * @throws NonSquareMatrixException if the covariance matrix is not square
     */
    protected void validateCovarianceData(double[][] x, double[][] covariance) {
        if (x.length != covariance.length) {
            throw new DimensionMismatchException(x.length, covariance.length);
        }
        if (covariance.length > 0 && covariance.length != covariance[0].length) {
            throw new NonSquareMatrixException(covariance.length, covariance[0].length);
        }
    }

    /**
     * {@inheritDoc}
     */
    public double[] estimateRegressionParameters() {
        RealVector b = calculateBeta();
        return b.toArray();
    }

    /**
     * {@inheritDoc}
     */
    public double[] estimateResiduals() {
        RealVector b = calculateBeta();
        RealVector e = yVector.subtract(xMatrix.operate(b));
        return e.toArray();
    }

    /**
     * {@inheritDoc}
     */
    public double[][] estimateRegressionParametersVariance() {
        return calculateBetaVariance().getData();
    }

    /**
     * {@inheritDoc}
     */
    public double[] estimateRegressionParametersStandardErrors() {
        double[][] betaVariance = estimateRegressionParametersVariance();
        double sigma = calculateErrorVariance();
        int length = betaVariance[0].length;
        double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            result[i] = FastMath.sqrt(sigma * betaVariance[i][i]);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public double estimateRegressandVariance() {
        return calculateYVariance();
    }

    /**
     * Estimates the variance of the error.
     *
     * @return estimate of the error variance
     * @since 2.2
     */
    public double estimateErrorVariance() {
        return calculateErrorVariance();

    }

    /**
     * Estimates the standard error of the regression.
     *
     * @return regression standard error
     * @since 2.2
     */
    public double estimateRegressionStandardError() {
        return Math.sqrt(estimateErrorVariance());
    }

    /**
     * Calculates the beta of multiple linear regression in matrix notation.
     *
     * @return beta
     */
    protected abstract RealVector calculateBeta();

    /**
     * Calculates the beta variance of multiple linear regression in matrix
     * notation.
     *
     * @return beta variance
     */
    protected abstract RealMatrix calculateBetaVariance();


    /**
     * Calculates the variance of the y values.
     *
     * @return Y variance
     */
    protected double calculateYVariance() {
        return new Variance().evaluate(yVector.toArray());
    }

    /**
     * <p>Calculates the variance of the error term.</p>
     * Uses the formula <pre>
     * var(u) = u &middot; u / (n - k)
     * </pre>
     * where n and k are the row and column dimensions of the design
     * matrix X.
     *
     * @return error variance estimate
     * @since 2.2
     */
    protected double calculateErrorVariance() {
        RealVector residuals = calculateResiduals();
        return residuals.dotProduct(residuals) /
               (xMatrix.getRowDimension() - xMatrix.getColumnDimension());
    }

    /**
     * Calculates the residuals of multiple linear regression in matrix
     * notation.
     *
     * <pre>
     * u = y - X * b
     * </pre>
     *
     * @return The residuals [n,1] matrix
     */
    protected RealVector calculateResiduals() {
        RealVector b = calculateBeta();
        return yVector.subtract(xMatrix.operate(b));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4089.java