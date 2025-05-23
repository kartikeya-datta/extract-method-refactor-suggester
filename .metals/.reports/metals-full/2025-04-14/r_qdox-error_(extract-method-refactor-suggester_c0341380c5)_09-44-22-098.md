error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4384.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4384.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4384.java
text:
```scala
i@@mplements TrivariateGridInterpolator {

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
package org.apache.commons.math.analysis.interpolation;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NoDataException;
import org.apache.commons.math.util.MathArrays;

/**
 * Generates a tricubic interpolating function.
 *
 * @version $Id$
 * @since 2.2
 */
public class TricubicSplineInterpolator
    implements TrivariateRealGridInterpolator {
    /**
     * {@inheritDoc}
     */
    public TricubicSplineInterpolatingFunction interpolate(final double[] xval,
                                                           final double[] yval,
                                                           final double[] zval,
                                                           final double[][][] fval) {
        if (xval.length == 0 || yval.length == 0 || zval.length == 0 || fval.length == 0) {
            throw new NoDataException();
        }
        if (xval.length != fval.length) {
            throw new DimensionMismatchException(xval.length, fval.length);
        }

        MathArrays.checkOrder(xval);
        MathArrays.checkOrder(yval);
        MathArrays.checkOrder(zval);

        final int xLen = xval.length;
        final int yLen = yval.length;
        final int zLen = zval.length;

        // Samples, re-ordered as (z, x, y) and (y, z, x) tuplets
        // fvalXY[k][i][j] = f(xval[i], yval[j], zval[k])
        // fvalZX[j][k][i] = f(xval[i], yval[j], zval[k])
        final double[][][] fvalXY = new double[zLen][xLen][yLen];
        final double[][][] fvalZX = new double[yLen][zLen][xLen];
        for (int i = 0; i < xLen; i++) {
            if (fval[i].length != yLen) {
                throw new DimensionMismatchException(fval[i].length, yLen);
            }

            for (int j = 0; j < yLen; j++) {
                if (fval[i][j].length != zLen) {
                    throw new DimensionMismatchException(fval[i][j].length, zLen);
                }

                for (int k = 0; k < zLen; k++) {
                    final double v = fval[i][j][k];
                    fvalXY[k][i][j] = v;
                    fvalZX[j][k][i] = v;
                }
            }
        }

        final BicubicSplineInterpolator bsi = new BicubicSplineInterpolator();

        // For each line x[i] (0 <= i < xLen), construct a 2D spline in y and z
        final BicubicSplineInterpolatingFunction[] xSplineYZ
            = new BicubicSplineInterpolatingFunction[xLen];
        for (int i = 0; i < xLen; i++) {
            xSplineYZ[i] = bsi.interpolate(yval, zval, fval[i]);
        }

        // For each line y[j] (0 <= j < yLen), construct a 2D spline in z and x
        final BicubicSplineInterpolatingFunction[] ySplineZX
            = new BicubicSplineInterpolatingFunction[yLen];
        for (int j = 0; j < yLen; j++) {
            ySplineZX[j] = bsi.interpolate(zval, xval, fvalZX[j]);
        }

        // For each line z[k] (0 <= k < zLen), construct a 2D spline in x and y
        final BicubicSplineInterpolatingFunction[] zSplineXY
            = new BicubicSplineInterpolatingFunction[zLen];
        for (int k = 0; k < zLen; k++) {
            zSplineXY[k] = bsi.interpolate(xval, yval, fvalXY[k]);
        }

        // Partial derivatives wrt x and wrt y
        final double[][][] dFdX = new double[xLen][yLen][zLen];
        final double[][][] dFdY = new double[xLen][yLen][zLen];
        final double[][][] d2FdXdY = new double[xLen][yLen][zLen];
        for (int k = 0; k < zLen; k++) {
            final BicubicSplineInterpolatingFunction f = zSplineXY[k];
            for (int i = 0; i < xLen; i++) {
                final double x = xval[i];
                for (int j = 0; j < yLen; j++) {
                    final double y = yval[j];
                    dFdX[i][j][k] = f.partialDerivativeX(x, y);
                    dFdY[i][j][k] = f.partialDerivativeY(x, y);
                    d2FdXdY[i][j][k] = f.partialDerivativeXY(x, y);
                }
            }
        }

        // Partial derivatives wrt y and wrt z
        final double[][][] dFdZ = new double[xLen][yLen][zLen];
        final double[][][] d2FdYdZ = new double[xLen][yLen][zLen];
        for (int i = 0; i < xLen; i++) {
            final BicubicSplineInterpolatingFunction f = xSplineYZ[i];
            for (int j = 0; j < yLen; j++) {
                final double y = yval[j];
                for (int k = 0; k < zLen; k++) {
                    final double z = zval[k];
                    dFdZ[i][j][k] = f.partialDerivativeY(y, z);
                    d2FdYdZ[i][j][k] = f.partialDerivativeXY(y, z);
                }
            }
        }

        // Partial derivatives wrt x and wrt z
        final double[][][] d2FdZdX = new double[xLen][yLen][zLen];
        for (int j = 0; j < yLen; j++) {
            final BicubicSplineInterpolatingFunction f = ySplineZX[j];
            for (int k = 0; k < zLen; k++) {
                final double z = zval[k];
                for (int i = 0; i < xLen; i++) {
                    final double x = xval[i];
                    d2FdZdX[i][j][k] = f.partialDerivativeXY(z, x);
                }
            }
        }

        // Third partial cross-derivatives
        final double[][][] d3FdXdYdZ = new double[xLen][yLen][zLen];
        for (int i = 0; i < xLen ; i++) {
            final int nI = nextIndex(i, xLen);
            final int pI = previousIndex(i);
            for (int j = 0; j < yLen; j++) {
                final int nJ = nextIndex(j, yLen);
                final int pJ = previousIndex(j);
                for (int k = 0; k < zLen; k++) {
                    final int nK = nextIndex(k, zLen);
                    final int pK = previousIndex(k);

                    // XXX Not sure about this formula
                    d3FdXdYdZ[i][j][k] = (fval[nI][nJ][nK] - fval[nI][pJ][nK] -
                                          fval[pI][nJ][nK] + fval[pI][pJ][nK] -
                                          fval[nI][nJ][pK] + fval[nI][pJ][pK] +
                                          fval[pI][nJ][pK] - fval[pI][pJ][pK]) /
                        ((xval[nI] - xval[pI]) * (yval[nJ] - yval[pJ]) * (zval[nK] - zval[pK])) ;
                }
            }
        }

        // Create the interpolating splines
        return new TricubicSplineInterpolatingFunction(xval, yval, zval, fval,
                                                       dFdX, dFdY, dFdZ,
                                                       d2FdXdY, d2FdZdX, d2FdYdZ,
                                                       d3FdXdYdZ);
    }

    /**
     * Compute the next index of an array, clipping if necessary.
     * It is assumed (but not checked) that {@code i} is larger than or equal to 0}.
     *
     * @param i Index
     * @param max Upper limit of the array
     * @return the next index
     */
    private int nextIndex(int i, int max) {
        final int index = i + 1;
        return index < max ? index : index - 1;
    }
    /**
     * Compute the previous index of an array, clipping if necessary.
     * It is assumed (but not checked) that {@code i} is smaller than the size of the array.
     *
     * @param i Index
     * @return the previous index
     */
    private int previousIndex(int i) {
        final int index = i - 1;
        return index >= 0 ? index : 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4384.java