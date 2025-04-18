error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7073.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7073.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7073.java
text:
```scala
i@@f (rank > 0) {

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
package org.apache.commons.math.stat.regression;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.util.MathUtils;

/**
 * Results of a Multiple Linear Regression model fit.
 *
 * @version $Id$
 * @since 3.0
 */
public class RegressionResults implements Serializable {

    /** INDEX of Sum of Squared Errors */
    private static final int SSE_IDX = 0;
    /** INDEX of Sum of Squares of Model */
    private static final int SST_IDX = 1;
    /** INDEX of R-Squared of regression */
    private static final int RSQ_IDX = 2;
    /** INDEX of Mean Squared Error */
    private static final int MSE_IDX = 3;
    /** INDEX of Adjusted R Squared */
    private static final int ADJRSQ_IDX = 4;
    /** UID */
    private static final long serialVersionUID = 1l;
    /** regression slope parameters */
    private final double[] parameters;
    /** variance covariance matrix of parameters */
    private final double[][] varCovData;
    /** boolean flag for variance covariance matrix in symm compressed storage */
    private final boolean isSymmetricVCD;
    /** rank of the solution */
    private final int rank;
    /** number of observations on which results are based */
    private final long nobs;
    /** boolean flag indicator of whether a constant was included*/
    private final boolean containsConstant;
    /** array storing global results, SSE, MSE, RSQ, adjRSQ */
    private final double[] globalFitInfo;
    /** error message */
    private final String indexOutOfBound = "Index is outside of the 0 to number of variables - 1 range";

    /**
     *  Set the default constructor to private access
     *  to prevent inadvertent instantiation
     */
    @SuppressWarnings("unused")
    private RegressionResults() {
        this.parameters = null;
        this.varCovData = null;
        this.rank = -1;
        this.nobs = -1;
        this.containsConstant = false;
        this.isSymmetricVCD = false;
        this.globalFitInfo = null;
    }

    /**
     * Constructor for Regression Results.
     *
     * @param parameters a double array with the regression slope estimates
     * @param varcov the variance covariance matrix, stored either in a square matrix
     * or as a compressed
     * @param isSymmetricCompressed a flag which denotes that the variance covariance
     * matrix is in symmetric compressed format
     * @param nobs the number of observations of the regression estimation
     * @param rank the number of independent variables in the regression
     * @param sumy the sum of the independent variable
     * @param sumysq the sum of the squared independent variable
     * @param sse sum of squared errors
     * @param containsConstant true model has constant,  false model does not have constant
     * @param copyData if true a deep copy of all input data is made, if false only references
     * are copied and the RegressionResults become mutable
     */
    public RegressionResults(
            final double[] parameters, final double[][] varcov,
            final boolean isSymmetricCompressed,
            final long nobs, final int rank,
            final double sumy, final double sumysq, final double sse,
            final boolean containsConstant,
            final boolean copyData) {
        if (copyData) {
            this.parameters = MathUtils.copyOf(parameters);
            this.varCovData = new double[varcov.length][];
            for (int i = 0; i < varcov.length; i++) {
                this.varCovData[i] = MathUtils.copyOf(varcov[i]);
            }
        } else {
            this.parameters = parameters;
            this.varCovData = varcov;
        }
        this.isSymmetricVCD = isSymmetricCompressed;
        this.nobs = nobs;
        this.rank = rank;
        this.containsConstant = containsConstant;
        this.globalFitInfo = new double[5];
        Arrays.fill(this.globalFitInfo, Double.NaN);

        if (rank > 2) {
            this.globalFitInfo[SST_IDX] = containsConstant ?
                    (sumysq - sumy * sumy / ((double) nobs)) : sumysq;
        }

        this.globalFitInfo[SSE_IDX] = sse;
        this.globalFitInfo[MSE_IDX] = this.globalFitInfo[SSE_IDX] /
                ((double) (nobs - rank));
        this.globalFitInfo[RSQ_IDX] = 1.0 -
                this.globalFitInfo[SSE_IDX] /
                this.globalFitInfo[SST_IDX];

        if (!containsConstant) {
            this.globalFitInfo[ADJRSQ_IDX] = 1.0-
                    (1.0 - this.globalFitInfo[RSQ_IDX]) *
                    ( (double) nobs / ( (double) (nobs - rank)));
        } else {
            this.globalFitInfo[ADJRSQ_IDX] = 1.0 - (sse * (nobs - 1.0)) /
                    (globalFitInfo[SST_IDX] * (nobs - rank));
        }
    }

    /**
     * <p>Returns the parameter estimate for the regressor at the given index.</p>
     *
     * <p>A redundant regressor will have its redundancy flag set, as well as
     *  a parameters estimated equal to {@code Double.NaN}</p>
     *
     * @param index an integer index which must be in the range [0, numberOfParameters-1]
     * @return parameters estimated for regressor at index
     * @throws IndexOutOfBoundsException thrown if the index >= numberOfParameters
     */
    public double getParameterEstimate(int index) throws IndexOutOfBoundsException {
        if (parameters == null) {
            return Double.NaN;
        }
        if (index < 0 || index >= this.parameters.length) {
            throw new IndexOutOfBoundsException(indexOutOfBound);
        }
        return this.parameters[index];
    }

    /**
     * <p>Returns a copy of the regression parameters estimates.</p>
     *
     * <p>The parameter estimates are returned in the natural order of the data.</p>
     *
     * <p>A redundant regressor will have its redundancy flag set, as will
     *  a parameter estimate equal to {@code Double.NaN}.</p>
     *
     * @return array of parameter estimates, null if no estimation occurred
     */
    public double[] getParameterEstimates() {
        if (this.parameters == null) {
            return null;
        }
        return MathUtils.copyOf(parameters);
    }

    /**
     * Returns the <a href="http://www.xycoon.com/standerrorb(1).htm">standard
     * error of the parameter estimate at index</a>,
     * usually denoted s(b<sub>index</sub>).
     *
     * @param index an integer index which must be in the range [0, numberOfParameters-1]
     * @return standard errors associated with parameters estimated at index
     * @throws IndexOutOfBoundsException thrown if the index >= numberOfParameters
     */
    public double getStdErrorOfEstimate(int index) throws IndexOutOfBoundsException {
        if (parameters == null) {
            return Double.NaN;
        }
        if (index < 0 || index >= this.parameters.length) {
            throw new IndexOutOfBoundsException(indexOutOfBound);
        }
        double var = this.getVcvElement(index, index);
        if (!Double.isNaN(var) && var > Double.MIN_VALUE) {
            return FastMath.sqrt(var);
        }
        return Double.NaN;
    }

    /**
     * <p>Returns the <a href="http://www.xycoon.com/standerrorb(1).htm">standard
     * error of the parameter estimates</a>,
     * usually denoted s(b<sub>i</sub>).</p>
     *
     * <p>If there are problems with an ill conditioned design matrix then the regressor
     * which is redundant will be assigned <code>Double.NaN</code>. </p>
     *
     * @return an array standard errors associated with parameters estimates,
     *  null if no estimation occurred
     */
    public double[] getStdErrorOfEstimates() {
        if (parameters == null) {
            return null;
        }
        double[] se = new double[this.parameters.length];
        for (int i = 0; i < this.parameters.length; i++) {
            double var = this.getVcvElement(i, i);
            if (!Double.isNaN(var) && var > Double.MIN_VALUE) {
                se[i] = FastMath.sqrt(var);
                continue;
            }
            se[i] = Double.NaN;
        }
        return se;
    }

    /**
     * <p>Returns the covariance between regression parameters i and j.</p>
     *
     * <p>If there are problems with an ill conditioned design matrix then the covariance
     * which involves redundant columns will be assigned {@code Double.NaN}. </p>
     *
     * @param i - the ith regression parameter
     * @param j - the jth regression parameter
     * @return the covariance of the parameter estimates
     * @throws IndexOutOfBoundsException thrown when i,j >= number of parameters
     */
    public double getCovarianceOfParameters(int i, int j) throws IndexOutOfBoundsException {
        if (parameters == null) {
            return Double.NaN;
        }
        if (i < 0 || i >= this.parameters.length) {
            throw new IndexOutOfBoundsException(" Row index is outside of the 0 " +
                    "to number of variables - 1 range");
        }
        if (j < 0 || j >= this.parameters.length) {
            throw new IndexOutOfBoundsException(" Column index is outside of the 0" +
                    " to number of variables - 1 range");
        }
        return this.getVcvElement(i, j);
    }

    /**
     * <p>Returns the number of parameters estimated in the model.</p>
     *
     * <p>This is the maximum number of regressors, some techniques may drop
     * redundant parameters</p>
     *
     * @return number of regressors, -1 if not estimated
     */
    public int getNumberOfParameters() {
        if (this.parameters == null) {
            return -1;
        }
        return this.parameters.length;
    }

    /**
     * Returns the number of observations added to the regression model.
     *
     * @return Number of observations, -1 if an error condition prevents estimation
     */
    public long getN() {
        return this.nobs;
    }

    /**
     * <p>Returns the sum of squared deviations of the y values about their mean.</p>
     *
     * <p>This is defined as SSTO
     * <a href="http://www.xycoon.com/SumOfSquares.htm">here</a>.</p>
     *
     * <p>If {@code n < 2}, this returns {@code Double.NaN}.</p>
     *
     * @return sum of squared deviations of y values
     */
    public double getTotalSumSquares() {
        return this.globalFitInfo[SST_IDX];
    }

    /**
     * <p>Returns the sum of squared deviations of the predicted y values about
     * their mean (which equals the mean of y).</p>
     *
     * <p>This is usually abbreviated SSR or SSM.  It is defined as SSM
     * <a href="http://www.xycoon.com/SumOfSquares.htm">here</a></p>
     *
     * <p><strong>Preconditions</strong>: <ul>
     * <li>At least two observations (with at least two different x values)
     * must have been added before invoking this method. If this method is
     * invoked before a model can be estimated, <code>Double.NaN</code> is
     * returned.
     * </li></ul></p>
     *
     * @return sum of squared deviations of predicted y values
     */
    public double getRegressionSumSquares() {
        return this.globalFitInfo[SST_IDX] - this.globalFitInfo[SSE_IDX];
    }

    /**
     * <p>Returns the <a href="http://www.xycoon.com/SumOfSquares.htm">
     * sum of squared errors</a> (SSE) associated with the regression
     * model.</p>
     *
     * <p>The return value is constrained to be non-negative - i.e., if due to
     * rounding errors the computational formula returns a negative result,
     * 0 is returned.</p>
     *
     * <p><strong>Preconditions</strong>: <ul>
     * <li>numberOfParameters data pairs
     * must have been added before invoking this method. If this method is
     * invoked before a model can be estimated, <code>Double,NaN</code> is
     * returned.
     * </li></ul></p>
     *
     * @return sum of squared errors associated with the regression model
     */
    public double getErrorSumSquares() {
        return this.globalFitInfo[ SSE_IDX];
    }

    /**
     * <p>Returns the sum of squared errors divided by the degrees of freedom,
     * usually abbreviated MSE.</p>
     *
     * <p>If there are fewer than <strong>numberOfParameters + 1</strong> data pairs in the model,
     * or if there is no variation in <code>x</code>, this returns
     * <code>Double.NaN</code>.</p>
     *
     * @return sum of squared deviations of y values
     */
    public double getMeanSquareError() {
        return this.globalFitInfo[ MSE_IDX];
    }

    /**
     * <p>Returns the <a href="http://www.xycoon.com/coefficient1.htm">
     * coefficient of multiple determination</a>,
     * usually denoted r-square.</p>
     *
     * <p><strong>Preconditions</strong>: <ul>
     * <li>At least numberOfParameters observations (with at least numberOfParameters different x values)
     * must have been added before invoking this method. If this method is
     * invoked before a model can be estimated, {@code Double,NaN} is
     * returned.
     * </li></ul></p>
     *
     * @return r-square, a double in the interval [0, 1]
     */
    public double getRSquared() {
        return this.globalFitInfo[ RSQ_IDX];
    }

    /**
     * <p>Returns the adjusted R-squared statistic, defined by the formula <pre>
     * R<sup>2</sup><sub>adj</sub> = 1 - [SSR (n - 1)] / [SSTO (n - p)]
     * </pre>
     * where SSR is the sum of squared residuals},
     * SSTO is the total sum of squares}, n is the number
     * of observations and p is the number of parameters estimated (including the intercept).</p>
     *
     * <p>If the regression is estimated without an intercept term, what is returned is <pre>
     * <code> 1 - (1 - {@link #getRSquared()} ) * (n / (n - p)) </code>
     * </pre></p>
     *
     * @return adjusted R-Squared statistic
     */
    public double getAdjustedRSquared() {
        return this.globalFitInfo[ ADJRSQ_IDX];
    }

    /**
     * Returns true if the regression model has been computed including an intercept.
     * In this case, the coefficient of the intercept is the first element of the
     * {@link #getParameterEstimates() parameter estimates}.
     * @return true if the model has an intercept term
     */
    public boolean hasIntercept() {
        return this.containsConstant;
    }

    /**
     * Gets the i-jth element of the variance-covariance matrix.
     *
     * @param i first variable index
     * @param j second variable index
     * @return the requested variance-covariance matrix entry
     */
    private double getVcvElement(int i, int j) {
        if (this.isSymmetricVCD) {
            if (this.varCovData.length > 1) {
                //could be stored in upper or lower triangular
                if (i == j) {
                    return varCovData[i][i];
                } else if (i >= varCovData[j].length) {
                    return varCovData[i][j];
                } else {
                    return varCovData[j][i];
                }
            } else {//could be in single array
                if (i > j) {
                    return varCovData[0][(i + 1) * i / 2 + j];
                } else {
                    return varCovData[0][(j + 1) * j / 2 + i];
                }
            }
        } else {
            return this.varCovData[i][j];
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7073.java