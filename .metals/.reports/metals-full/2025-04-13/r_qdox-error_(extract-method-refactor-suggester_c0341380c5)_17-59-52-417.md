error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12559.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12559.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12559.java
text:
```scala
final D@@iagonalMatrix p = (DiagonalMatrix) m.copy();

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

import org.apache.commons.math3.TestUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the {@link DiagonalMatrix} class.
 */
public class DiagonalMatrixTest {
    @Test
    public void testConstructor1() {
        final int dim = 3;
        final DiagonalMatrix m = new DiagonalMatrix(dim);
        Assert.assertEquals(dim, m.getRowDimension());
        Assert.assertEquals(dim, m.getColumnDimension());
    }

    @Test
    public void testConstructor2() {
        final double[] d = { -1.2, 3.4, 5 };
        final DiagonalMatrix m = new DiagonalMatrix(d);
        for (int i = 0; i < m.getRowDimension(); i++) {
            for (int j = 0; j < m.getRowDimension(); j++) {
                if (i == j) {
                    Assert.assertEquals(d[i], m.getEntry(i, j), 0d);
                } else {
                    Assert.assertEquals(0d, m.getEntry(i, j), 0d);
                }
            }
        }

        // Check that the underlying was copied.
        d[0] = 0;
        Assert.assertFalse(d[0] == m.getEntry(0, 0));
    }

    @Test
    public void testConstructor3() {
        final double[] d = { -1.2, 3.4, 5 };
        final DiagonalMatrix m = new DiagonalMatrix(d, false);
        for (int i = 0; i < m.getRowDimension(); i++) {
            for (int j = 0; j < m.getRowDimension(); j++) {
                if (i == j) {
                    Assert.assertEquals(d[i], m.getEntry(i, j), 0d);
                } else {
                    Assert.assertEquals(0d, m.getEntry(i, j), 0d);
                }
            }
        }

        // Check that the underlying is referenced.
        d[0] = 0;
        Assert.assertTrue(d[0] == m.getEntry(0, 0));

    }

    @Test(expected=DimensionMismatchException.class)
    public void testCreateError() {
        final double[] d = { -1.2, 3.4, 5 };
        final DiagonalMatrix m = new DiagonalMatrix(d, false);
        m.createMatrix(5, 3);
    }

    @Test
    public void testCreate() {
        final double[] d = { -1.2, 3.4, 5 };
        final DiagonalMatrix m = new DiagonalMatrix(d, false);
        final RealMatrix p = m.createMatrix(5, 5);
        Assert.assertTrue(p instanceof DiagonalMatrix);
        Assert.assertEquals(5, p.getRowDimension());
        Assert.assertEquals(5, p.getColumnDimension());
    }

    @Test
    public void testCopy() {
        final double[] d = { -1.2, 3.4, 5 };
        final DiagonalMatrix m = new DiagonalMatrix(d, false);
        final DiagonalMatrix p = m.copy();
        for (int i = 0; i < m.getRowDimension(); ++i) {
            Assert.assertEquals(m.getEntry(i, i), p.getEntry(i, i), 1.0e-20);
        }
    }

    @Test
    public void testGetData() {
        final double[] data = { -1.2, 3.4, 5 };
        final int dim = 3;
        final DiagonalMatrix m = new DiagonalMatrix(dim);
        for (int i = 0; i < dim; i++) {
            m.setEntry(i, i, data[i]);
        }

        final double[][] out = m.getData();
        Assert.assertEquals(dim, out.length);
        for (int i = 0; i < m.getRowDimension(); i++) {
            Assert.assertEquals(dim, out[i].length);
            for (int j = 0; j < m.getRowDimension(); j++) {
                if (i == j) {
                    Assert.assertEquals(data[i], out[i][j], 0d);
                } else {
                    Assert.assertEquals(0d, out[i][j], 0d);
                }
            }
        }        
    }

    @Test
    public void testAdd() {
        final double[] data1 = { -1.2, 3.4, 5 };
        final DiagonalMatrix m1 = new DiagonalMatrix(data1);
 
        final double[] data2 = { 10.1, 2.3, 45 };
        final DiagonalMatrix m2 = new DiagonalMatrix(data2);

        final DiagonalMatrix result = m1.add(m2);
        Assert.assertEquals(m1.getRowDimension(), result.getRowDimension());
        for (int i = 0; i < result.getRowDimension(); i++) {
            for (int j = 0; j < result.getRowDimension(); j++) {
                if (i == j) {
                    Assert.assertEquals(data1[i] + data2[i], result.getEntry(i, j), 0d);
                } else {
                    Assert.assertEquals(0d, result.getEntry(i, j), 0d);
                }
            }
        }
    }

    @Test
    public void testSubtract() {
        final double[] data1 = { -1.2, 3.4, 5 };
        final DiagonalMatrix m1 = new DiagonalMatrix(data1);
 
        final double[] data2 = { 10.1, 2.3, 45 };
        final DiagonalMatrix m2 = new DiagonalMatrix(data2);

        final DiagonalMatrix result = m1.subtract(m2);
        Assert.assertEquals(m1.getRowDimension(), result.getRowDimension());
        for (int i = 0; i < result.getRowDimension(); i++) {
            for (int j = 0; j < result.getRowDimension(); j++) {
                if (i == j) {
                    Assert.assertEquals(data1[i] - data2[i], result.getEntry(i, j), 0d);
                } else {
                    Assert.assertEquals(0d, result.getEntry(i, j), 0d);
                }
            }
        }
    }

    @Test
    public void testAddToEntry() {
        final double[] data = { -1.2, 3.4, 5 };
        final DiagonalMatrix m = new DiagonalMatrix(data);

        for (int i = 0; i < m.getRowDimension(); i++) {
            m.addToEntry(i, i, i);
            Assert.assertEquals(data[i] + i, m.getEntry(i, i), 0d);
        }
    }

    @Test
    public void testMultiplyEntry() {
        final double[] data = { -1.2, 3.4, 5 };
        final DiagonalMatrix m = new DiagonalMatrix(data);

        for (int i = 0; i < m.getRowDimension(); i++) {
            m.multiplyEntry(i, i, i);
            Assert.assertEquals(data[i] * i, m.getEntry(i, i), 0d);
        }
    }

    @Test
    public void testMultiply1() {
        final double[] data1 = { -1.2, 3.4, 5 };
        final DiagonalMatrix m1 = new DiagonalMatrix(data1);
        final double[] data2 = { 10.1, 2.3, 45 };
        final DiagonalMatrix m2 = new DiagonalMatrix(data2);

        final DiagonalMatrix result = (DiagonalMatrix) m1.multiply((RealMatrix) m2);
        Assert.assertEquals(m1.getRowDimension(), result.getRowDimension());
        for (int i = 0; i < result.getRowDimension(); i++) {
            for (int j = 0; j < result.getRowDimension(); j++) {
                if (i == j) {
                    Assert.assertEquals(data1[i] * data2[i], result.getEntry(i, j), 0d);
                } else {
                    Assert.assertEquals(0d, result.getEntry(i, j), 0d);
                }
            }
        }
    }

    @Test
    public void testMultiply2() {
        final double[] data1 = { -1.2, 3.4, 5 };
        final DiagonalMatrix diag1 = new DiagonalMatrix(data1);

        final double[][] data2 = { { -1.2, 3.4 },
                                   { -5.6, 7.8 },
                                   {  9.1, 2.3 } };
        final RealMatrix dense2 = new Array2DRowRealMatrix(data2);
        final RealMatrix dense1 = new Array2DRowRealMatrix(diag1.getData());

        final RealMatrix diagResult = diag1.multiply(dense2);
        final RealMatrix denseResult = dense1.multiply(dense2);

        for (int i = 0; i < dense1.getRowDimension(); i++) {
            for (int j = 0; j < dense2.getColumnDimension(); j++) {
                Assert.assertEquals(denseResult.getEntry(i, j),
                                    diagResult.getEntry(i, j), 0d);
            }
        }
    }

    @Test
    public void testOperate() {
        final double[] data = { -1.2, 3.4, 5 };
        final DiagonalMatrix diag = new DiagonalMatrix(data);
        final RealMatrix dense = new Array2DRowRealMatrix(diag.getData());

        final double[] v = { 6.7, 890.1, 23.4 };
        final double[] diagResult = diag.operate(v);
        final double[] denseResult = dense.operate(v);

        TestUtils.assertEquals(diagResult, denseResult, 0d);
    }

    @Test
    public void testPreMultiply() {
        final double[] data = { -1.2, 3.4, 5 };
        final DiagonalMatrix diag = new DiagonalMatrix(data);
        final RealMatrix dense = new Array2DRowRealMatrix(diag.getData());

        final double[] v = { 6.7, 890.1, 23.4 };
        final double[] diagResult = diag.preMultiply(v);
        final double[] denseResult = dense.preMultiply(v);

        TestUtils.assertEquals(diagResult, denseResult, 0d);
    }

    @Test(expected=NumberIsTooLargeException.class)
    public void testSetNonDiagonalEntry() {
        final DiagonalMatrix diag = new DiagonalMatrix(3);
        diag.setEntry(1, 2, 3.4);
    }

    @Test
    public void testSetNonDiagonalZero() {
        final DiagonalMatrix diag = new DiagonalMatrix(3);
        diag.setEntry(1, 2, 0.0);
        Assert.assertEquals(0.0, diag.getEntry(1, 2), Precision.SAFE_MIN);
    }

    @Test(expected=NumberIsTooLargeException.class)
    public void testAddNonDiagonalEntry() {
        final DiagonalMatrix diag = new DiagonalMatrix(3);
        diag.addToEntry(1, 2, 3.4);
    }

    @Test
    public void testAddNonDiagonalZero() {
        final DiagonalMatrix diag = new DiagonalMatrix(3);
        diag.addToEntry(1, 2, 0.0);
        Assert.assertEquals(0.0, diag.getEntry(1, 2), Precision.SAFE_MIN);
    }

    @Test
    public void testMultiplyNonDiagonalEntry() {
        final DiagonalMatrix diag = new DiagonalMatrix(3);
        diag.multiplyEntry(1, 2, 3.4);
        Assert.assertEquals(0.0, diag.getEntry(1, 2), Precision.SAFE_MIN);
    }

    @Test
    public void testMultiplyNonDiagonalZero() {
        final DiagonalMatrix diag = new DiagonalMatrix(3);
        diag.multiplyEntry(1, 2, 0.0);
        Assert.assertEquals(0.0, diag.getEntry(1, 2), Precision.SAFE_MIN);
    }

    @Test(expected=OutOfRangeException.class)
    public void testSetEntryOutOfRange() {
        final DiagonalMatrix diag = new DiagonalMatrix(3);
        diag.setEntry(3, 3, 3.4);
    }

    @Test(expected=NullArgumentException.class)
    public void testNull() {
        new DiagonalMatrix(null, false);
    }

    @Test(expected=NumberIsTooLargeException.class)
    public void testSetSubMatrixError() {
        final double[] data = { -1.2, 3.4, 5 };
        final DiagonalMatrix diag = new DiagonalMatrix(data);
        diag.setSubMatrix(new double[][] { {1.0, 1.0}, {1.0, 1.0}}, 1, 1);
    }

    @Test
    public void testSetSubMatrix() {
        final double[] data = { -1.2, 3.4, 5 };
        final DiagonalMatrix diag = new DiagonalMatrix(data);
        diag.setSubMatrix(new double[][] { {0.0, 5.0, 0.0}, {0.0, 0.0, 6.0}}, 1, 0);
        Assert.assertEquals(-1.2, diag.getEntry(0, 0), 1.0e-20);
        Assert.assertEquals( 5.0, diag.getEntry(1, 1), 1.0e-20);
        Assert.assertEquals( 6.0, diag.getEntry(2, 2), 1.0e-20);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12559.java