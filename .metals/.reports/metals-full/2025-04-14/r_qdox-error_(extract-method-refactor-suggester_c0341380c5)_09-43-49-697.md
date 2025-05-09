error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10008.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10008.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10008.java
text:
```scala
e@@xpected = FastMath.abs(period) - delta;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.apache.commons.math3.util;

import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomDataImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the MathUtils class.
 * @version $Id$
 *          2007) $
 */
public final class MathUtilsTest {
    @Test
    public void testHash() {
        double[] testArray = {
            Double.NaN,
            Double.POSITIVE_INFINITY,
            Double.NEGATIVE_INFINITY,
            1d,
            0d,
            1E-14,
            (1 + 1E-14),
            Double.MIN_VALUE,
            Double.MAX_VALUE };
        for (int i = 0; i < testArray.length; i++) {
            for (int j = 0; j < testArray.length; j++) {
                if (i == j) {
                    Assert.assertEquals(MathUtils.hash(testArray[i]), MathUtils.hash(testArray[j]));
                    Assert.assertEquals(MathUtils.hash(testArray[j]), MathUtils.hash(testArray[i]));
                } else {
                    Assert.assertTrue(MathUtils.hash(testArray[i]) != MathUtils.hash(testArray[j]));
                    Assert.assertTrue(MathUtils.hash(testArray[j]) != MathUtils.hash(testArray[i]));
                }
            }
        }
    }

    @Test
    public void testArrayHash() {
        Assert.assertEquals(0, MathUtils.hash((double[]) null));
        Assert.assertEquals(MathUtils.hash(new double[] {
                                      Double.NaN, Double.POSITIVE_INFINITY,
                                      Double.NEGATIVE_INFINITY, 1d, 0d
                                    }),
                     MathUtils.hash(new double[] {
                                      Double.NaN, Double.POSITIVE_INFINITY,
                                      Double.NEGATIVE_INFINITY, 1d, 0d
                                    }));
        Assert.assertFalse(MathUtils.hash(new double[] { 1d }) ==
                    MathUtils.hash(new double[] { FastMath.nextAfter(1d, 2d) }));
        Assert.assertFalse(MathUtils.hash(new double[] { 1d }) ==
                    MathUtils.hash(new double[] { 1d, 1d }));
    }

    /**
     * Make sure that permuted arrays do not hash to the same value.
     */
    @Test
    public void testPermutedArrayHash() {
        double[] original = new double[10];
        double[] permuted = new double[10];
        RandomDataImpl random = new RandomDataImpl();

        // Generate 10 distinct random values
        for (int i = 0; i < 10; i++) {
            final RealDistribution u = new UniformRealDistribution(i + 0.5, i + 0.75);
            original[i] = u.sample();
        }

        // Generate a random permutation, making sure it is not the identity
        boolean isIdentity = true;
        do {
            int[] permutation = random.nextPermutation(10, 10);
            for (int i = 0; i < 10; i++) {
                if (i != permutation[i]) {
                    isIdentity = false;
                }
                permuted[i] = original[permutation[i]];
            }
        } while (isIdentity);

        // Verify that permuted array has different hash
        Assert.assertFalse(MathUtils.hash(original) == MathUtils.hash(permuted));
    }

    @Test
    public void testIndicatorByte() {
        Assert.assertEquals((byte)1, MathUtils.copySign((byte)1, (byte)2));
        Assert.assertEquals((byte)1, MathUtils.copySign((byte)1, (byte)0));
        Assert.assertEquals((byte)(-1), MathUtils.copySign((byte)1, (byte)(-2)));
    }

    @Test
    public void testIndicatorInt() {
        Assert.assertEquals(1, MathUtils.copySign(1, 2));
        Assert.assertEquals(1, MathUtils.copySign(1, 0));
        Assert.assertEquals((-1), MathUtils.copySign(1, -2));
    }

    @Test
    public void testIndicatorLong() {
        Assert.assertEquals(1L, MathUtils.copySign(1L, 2L));
        Assert.assertEquals(1L, MathUtils.copySign(1L, 0L));
        Assert.assertEquals(-1L, MathUtils.copySign(1L, -2L));
    }

    @Test
    public void testIndicatorShort() {
        Assert.assertEquals((short)1, MathUtils.copySign((short)1, (short)2));
        Assert.assertEquals((short)1, MathUtils.copySign((short)1, (short)0));
        Assert.assertEquals((short)(-1), MathUtils.copySign((short)1, (short)(-2)));
    }

    @Test
    public void testNormalizeAngle() {
        for (double a = -15.0; a <= 15.0; a += 0.1) {
            for (double b = -15.0; b <= 15.0; b += 0.2) {
                double c = MathUtils.normalizeAngle(a, b);
                Assert.assertTrue((b - FastMath.PI) <= c);
                Assert.assertTrue(c <= (b + FastMath.PI));
                double twoK = FastMath.rint((a - c) / FastMath.PI);
                Assert.assertEquals(c, a - twoK * FastMath.PI, 1.0e-14);
            }
        }
    }

    @Test
    public void testReduce() {
        final double period = -12.222;
        final double offset = 13;

        final double delta = 1.5;

        double orig = offset + 122456789 * period + delta;
        double expected = delta;
        Assert.assertEquals(expected,
                            MathUtils.reduce(orig, period, offset),
                            1e-7);
        Assert.assertEquals(expected,
                            MathUtils.reduce(orig, -period, offset),
                            1e-7);

        orig = offset - 123356789 * period - delta;
        expected = Math.abs(period) - delta;
        Assert.assertEquals(expected,
                            MathUtils.reduce(orig, period, offset),
                            1e-6);
        Assert.assertEquals(expected,
                            MathUtils.reduce(orig, -period, offset),
                            1e-6);

        orig = offset - 123446789 * period + delta;
        expected = delta;
        Assert.assertEquals(expected,
                            MathUtils.reduce(orig, period, offset),
                            1e-6);
        Assert.assertEquals(expected,
                            MathUtils.reduce(orig, -period, offset),
                            1e-6);

        Assert.assertTrue(Double.isNaN(MathUtils.reduce(orig, Double.NaN, offset)));
        Assert.assertTrue(Double.isNaN(MathUtils.reduce(Double.NaN, period, offset)));
        Assert.assertTrue(Double.isNaN(MathUtils.reduce(orig, period, Double.NaN)));
        Assert.assertTrue(Double.isNaN(MathUtils.reduce(orig, period,
                Double.POSITIVE_INFINITY)));
        Assert.assertTrue(Double.isNaN(MathUtils.reduce(Double.POSITIVE_INFINITY,
                period, offset)));
        Assert.assertTrue(Double.isNaN(MathUtils.reduce(orig,
                Double.POSITIVE_INFINITY, offset)));
        Assert.assertTrue(Double.isNaN(MathUtils.reduce(orig,
                Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)));
        Assert.assertTrue(Double.isNaN(MathUtils.reduce(Double.POSITIVE_INFINITY,
                period, Double.POSITIVE_INFINITY)));
        Assert.assertTrue(Double.isNaN(MathUtils.reduce(Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY, offset)));
        Assert.assertTrue(Double.isNaN(MathUtils.reduce(Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY,  Double.POSITIVE_INFINITY)));
    }

    @Test
    public void testReduceComparedWithNormalizeAngle() {
        final double tol = Math.ulp(1d);
        final double period = 2 * Math.PI;
        for (double a = -15; a <= 15; a += 0.5) {
            for (double center = -15; center <= 15; center += 1) {
                final double nA = MathUtils.normalizeAngle(a, center);
                final double offset = center - Math.PI;
                final double r = MathUtils.reduce(a, period, offset);
                Assert.assertEquals(nA, r + offset, tol);
            }
        }
    }

    @Test
    public void testSignByte() {
        final byte one = (byte) 1;
        Assert.assertEquals((byte) 1, MathUtils.copySign(one, (byte) 2));
        Assert.assertEquals((byte) (-1), MathUtils.copySign(one, (byte) (-2)));
    }

    @Test
    public void testSignInt() {
        final int one = 1;
        Assert.assertEquals(1, MathUtils.copySign(one, 2));
        Assert.assertEquals((-1), MathUtils.copySign(one, -2));
    }

    @Test
    public void testSignLong() {
        final long one = 1L;
        Assert.assertEquals(1L, MathUtils.copySign(one, 2L));
        Assert.assertEquals(-1L, MathUtils.copySign(one, -2L));
    }

    @Test
    public void testSignShort() {
        final short one = (short) 1;
        Assert.assertEquals((short) 1, MathUtils.copySign(one, (short) 2));
        Assert.assertEquals((short) (-1), MathUtils.copySign(one, (short) (-2)));
    }

    @Test
    public void testCheckFinite() {
        try {
            MathUtils.checkFinite(Double.POSITIVE_INFINITY);
            Assert.fail("an exception should have been thrown");
        } catch (NotFiniteNumberException e) {
            // Expected
        }
        try {
            MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
            Assert.fail("an exception should have been thrown");
        } catch (NotFiniteNumberException e) {
            // Expected
        }
        try {
            MathUtils.checkFinite(Double.NaN);
            Assert.fail("an exception should have been thrown");
        } catch (NotFiniteNumberException e) {
            // Expected
        }

        try {
            MathUtils.checkFinite(new double[] {0, -1, Double.POSITIVE_INFINITY, -2, 3});
            Assert.fail("an exception should have been thrown");
        } catch (NotFiniteNumberException e) {
            // Expected
        }
        try {
            MathUtils.checkFinite(new double[] {1, Double.NEGATIVE_INFINITY, -2, 3});
            Assert.fail("an exception should have been thrown");
        } catch (NotFiniteNumberException e) {
            // Expected
        }
        try {
            MathUtils.checkFinite(new double[] {4, 3, -1, Double.NaN, -2, 1});
            Assert.fail("an exception should have been thrown");
        } catch (NotFiniteNumberException e) {
            // Expected
        }
    }

    @Test
    public void testCheckNotNull1() {
        try {
            Object obj = null;
            MathUtils.checkNotNull(obj);
        } catch (NullArgumentException e) {
            // Expected.
        }
    }

    @Test
    public void testCheckNotNull2() {
        try {
            double[] array = null;
            MathUtils.checkNotNull(array, LocalizedFormats.INPUT_ARRAY);
        } catch (NullArgumentException e) {
            // Expected.
        }
    }

    @Test
    public void testCopySignByte() {
        byte a = MathUtils.copySign(Byte.MIN_VALUE, (byte) -1);
        Assert.assertEquals(Byte.MIN_VALUE, a);

        final byte minValuePlusOne = Byte.MIN_VALUE + (byte) 1;
        a = MathUtils.copySign(minValuePlusOne, (byte) 1);
        Assert.assertEquals(Byte.MAX_VALUE, a);

        a = MathUtils.copySign(Byte.MAX_VALUE, (byte) -1);
        Assert.assertEquals(minValuePlusOne, a);

        final byte one = 1;
        byte val = -2;
        a = MathUtils.copySign(val, one);
        Assert.assertEquals(-val, a);

        final byte minusOne = -one;
        val = 2;
        a = MathUtils.copySign(val, minusOne);
        Assert.assertEquals(-val, a);

        val = 0;
        a = MathUtils.copySign(val, minusOne);
        Assert.assertEquals(val, a);

        val = 0;
        a = MathUtils.copySign(val, one);
        Assert.assertEquals(val, a);
    }

    @Test(expected=MathArithmeticException.class)
    public void testCopySignByte2() {
        MathUtils.copySign(Byte.MIN_VALUE, (byte) 1);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10008.java