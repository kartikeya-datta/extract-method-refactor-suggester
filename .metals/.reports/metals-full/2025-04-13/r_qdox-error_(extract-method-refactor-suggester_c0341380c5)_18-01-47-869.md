error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12906.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12906.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12906.java
text:
```scala
r@@eturn new LUDecompositionImpl(XTX).getSolver().getInverse();

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

import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.LUSolver;
import org.apache.commons.math.linear.QRDecomposition;
import org.apache.commons.math.linear.QRDecompositionImpl;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

/**
 * <p>Implements ordinary least squares (OLS) to estimate the parameters of a 
 * multiple linear regression model.</p>
 * 
 * <p>OLS assumes the covariance matrix of the error to be diagonal and with
 * equal variance.
 * <pre>
 * u ~ N(0, sigma^2*I)
 * </pre></p>
 * 
 * <p>The regression coefficients, b, satisfy the normal equations:
 * <pre>
 * X^T X b = X^T y
 * </pre></p>
 * 
 * <p>To solve the normal equations, this implementation uses QR decomposition
 * of the X matrix. (See {@link QRDecompositionImpl} for details on the
 * decomposition algorithm.)
 * <pre>
 * X^T X b = X^T y
 * (QR)^T (QR) b = (QR)^T y
 * R^T (Q^T Q) R b = R^T Q^T y
 * R^T R b = R^T Q^T y
 * (R^T)^{-1} R^T R b = (R^T)^{-1} R^T Q^T y
 * R b = Q^T y
 * </pre>
 * Given Q and R, the last equation is solved by back-subsitution.</p>
 * 
 * @version $Revision$ $Date$
 * @since 2.0
 */
public class OLSMultipleLinearRegression extends AbstractMultipleLinearRegression {
    
    /** Cached QR decomposition of X matrix */
    private QRDecomposition qr = null;

    /**
     * {@inheritDoc}
     * 
     * Computes and caches QR decomposition of the X matrix.
     */
    public void newSampleData(double[] y, double[][] x) {
        validateSampleData(x, y);
        newYSampleData(y);
        newXSampleData(x);
    }
    
    /**
     * {@inheritDoc}
     * 
     * Computes and caches QR decomposition of the X matrix
     */
    public void newSampleData(double[] data, int nobs, int nvars) {
        super.newSampleData(data, nobs, nvars);
        qr = new QRDecompositionImpl(X);
    }
    
    /**
     * Loads new x sample data, overriding any previous sample
     * 
     * @param x the [n,k] array representing the x sample
     */
    protected void newXSampleData(double[][] x) {
        this.X = new RealMatrixImpl(x);
        qr = new QRDecompositionImpl(X);
    }
    
    /**
     * Calculates regression coefficients using OLS.
     * 
     * @return beta
     */
    protected RealMatrix calculateBeta() {
        return solveUpperTriangular(qr.getR(), qr.getQ().transpose().multiply(Y));
    }

    /**
     * Calculates the variance on the beta by OLS.
     * <pre>
     *  Var(b)=(X'X)^-1
     * </pre>
     * @return The beta variance
     */
    protected RealMatrix calculateBetaVariance() {
        RealMatrix XTX = X.transpose().multiply(X);
        return new LUSolver(new LUDecompositionImpl(XTX)).getInverse();
    }
    

    /**
     * Calculates the variance on the Y by OLS.
     * <pre>
     *  Var(y)=Tr(u'u)/(n-k)
     * </pre>
     * @return The Y variance
     */
    protected double calculateYVariance() {
        RealMatrix u = calculateResiduals();
        RealMatrix sse = u.transpose().multiply(u);
        return sse.getTrace()/(X.getRowDimension()-X.getColumnDimension());
    }
    
    /** TODO:  Find a home for the following methods in the linear package */   
    
    /**
     * <p>Uses back substitution to solve the system</p>
     * 
     * <p>coefficients X = constants</p>
     * 
     * <p>coefficients must upper-triangular and constants must be a column 
     * matrix.  The solution is returned as a column matrix.</p>
     * 
     * <p>The number of columns in coefficients determines the length
     * of the returned solution vector (column matrix).  If constants
     * has more rows than coefficients has columns, excess rows are ignored.
     * Similarly, extra (zero) rows in coefficients are ignored</p>
     * 
     * @param coefficients upper-triangular coefficients matrix
     * @param constants column RHS constants matrix
     * @return solution matrix as a column matrix
     * 
     */
    private static RealMatrix solveUpperTriangular(RealMatrix coefficients,
            RealMatrix constants) {
        if (!isUpperTriangular(coefficients, 1E-12)) {
            throw new IllegalArgumentException(
                   "Coefficients is not upper-triangular");
        }
        if (constants.getColumnDimension() != 1) {
            throw new IllegalArgumentException(
                    "Constants not a column matrix.");
        }
        int length = coefficients.getColumnDimension();
        double x[] = new double[length];
        for (int i = 0; i < length; i++) {
            int index = length - 1 - i;
            double sum = 0;
            for (int j = index + 1; j < length; j++) {
                sum += coefficients.getEntry(index, j) * x[j];
            }
            x[index] = (constants.getEntry(index, 0) - sum) / coefficients.getEntry(index, index);
        } 
        return new RealMatrixImpl(x);
    }
    
    /**
     * <p>Returns true iff m is an upper-triangular matrix.</p>
     * 
     * <p>Makes sure all below-diagonal elements are within epsilon of 0.</p>
     * 
     * @param m matrix to check
     * @param epsilon maximum allowable absolute value for elements below
     * the main diagonal
     * 
     * @return true if m is upper-triangular; false otherwise
     * @throws NullPointerException if m is null
     */
    private static boolean isUpperTriangular(RealMatrix m, double epsilon) {
        int nCols = m.getColumnDimension();
        int nRows = m.getRowDimension();
        for (int r = 0; r < nRows; r++) {
            int bound = Math.min(r, nCols);
            for (int c = 0; c < bound; c++) {
                if (Math.abs(m.getEntry(r, c)) > epsilon) {
                    return false;
                }
            }
        }
        return true;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12906.java