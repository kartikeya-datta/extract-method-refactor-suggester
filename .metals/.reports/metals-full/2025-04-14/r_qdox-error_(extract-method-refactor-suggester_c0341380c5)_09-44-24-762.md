error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13976.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13976.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13976.java
text:
```scala
(@@double) (binUpperBounds[k] - binUpperBounds[k - 1]) / n;

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
package org.apache.commons.math3.random;

import java.util.Arrays;

import org.apache.commons.math3.TestUtils;
import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.MathIllegalArgumentException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Base class for RandomGenerator tests.
 *
 * Tests RandomGenerator methods directly and also executes RandomDataTest
 * test cases against a RandomDataImpl created using the provided generator.
 *
 * RandomGenerator test classes should extend this class, implementing
 * makeGenerator() to provide a concrete generator to test. The generator
 * returned by makeGenerator should be seeded with a fixed seed.
 *
 * @version $Id$
 */

public abstract class RandomGeneratorAbstractTest extends RandomDataGeneratorTest {

    /** RandomGenerator under test */
    protected RandomGenerator generator;

    /**
     * Override this method in subclasses to provide a concrete generator to test.
     * Return a generator seeded with a fixed seed.
     */
    protected abstract RandomGenerator makeGenerator();

    /**
     * Initialize generator and randomData instance in superclass.
     */
    public RandomGeneratorAbstractTest() {
        generator = makeGenerator();
        randomData = new RandomDataGenerator(generator);
    }

    /**
     * Set a fixed seed for the tests
     */
    @Before
    public void setUp() {
        generator = makeGenerator();
    }
    
    // Omit secureXxx tests, since they do not use the provided generator
    @Override
    public void testNextSecureLongIAE() {}
    @Override
    public void testNextSecureLongNegativeToPositiveRange() {}
    @Override
    public void testNextSecureLongNegativeRange() {}
    @Override
    public void testNextSecureLongPositiveRange() {}
    @Override
    public void testNextSecureIntIAE() {}
    @Override
    public void testNextSecureIntNegativeToPositiveRange() {}
    @Override
    public void testNextSecureIntNegativeRange() {}
    @Override
    public void testNextSecureIntPositiveRange() {}
    @Override
    public void testNextSecureHex() {}

    @Test
    /**
     * Tests uniformity of nextInt(int) distribution by generating 1000
     * samples for each of 10 test values and for each sample performing
     * a chi-square test of homogeneity of the observed distribution with
     * the expected uniform distribution.  Tests are performed at the .01
     * level and an average failure rate higher than 2% (i.e. more than 20
     * null hypothesis rejections) causes the test case to fail.
     *
     * All random values are generated using the generator instance used by
     * other tests and the generator is not reseeded, so this is a fixed seed
     * test.
     */
    public void testNextIntDirect() {
        // Set up test values - end of the array filled randomly
        int[] testValues = new int[] {4, 10, 12, 32, 100, 10000, 0, 0, 0, 0};
        for (int i = 6; i < 10; i++) {
            final int val = generator.nextInt();
            testValues[i] = val < 0 ? -val : val + 1;
        }

        final int numTests = 1000;
        for (int i = 0; i < testValues.length; i++) {
            final int n = testValues[i];
            // Set up bins
            int[] binUpperBounds;
            if (n < 32) {
                binUpperBounds = new int[n];
                for (int k = 0; k < n; k++) {
                    binUpperBounds[k] = k;
                }
            } else {
                binUpperBounds = new int[10];
                final int step = n / 10;
                for (int k = 0; k < 9; k++) {
                    binUpperBounds[k] = (k + 1) * step;
                }
                binUpperBounds[9] = n - 1;
            }
            // Run the tests
            int numFailures = 0;
            final int binCount = binUpperBounds.length;
            final long[] observed = new long[binCount];
            final double[] expected = new double[binCount];
            expected[0] = binUpperBounds[0] == 0 ? (double) smallSampleSize / (double) n :
                (double) ((binUpperBounds[0] + 1) * smallSampleSize) / (double) n;
            for (int k = 1; k < binCount; k++) {
                expected[k] = (double) smallSampleSize *
                (double) (binUpperBounds[k] - binUpperBounds[k - 1]) / (double) n;
            }
            for (int j = 0; j < numTests; j++) {
                Arrays.fill(observed, 0);
                for (int k = 0; k < smallSampleSize; k++) {
                    final int value = generator.nextInt(n);
                    Assert.assertTrue("nextInt range",(value >= 0) && (value < n));
                    for (int l = 0; l < binCount; l++) {
                        if (binUpperBounds[l] >= value) {
                            observed[l]++;
                            break;
                        }
                    }
                }
                if (testStatistic.chiSquareTest(expected, observed) < 0.01) {
                    numFailures++;
                }
            }
            if ((double) numFailures / (double) numTests > 0.02) {
                Assert.fail("Too many failures for n = " + n +
                " " + numFailures + " out of " + numTests + " tests failed.");
            }
        }
    }

    @Test
    public void testNextIntIAE2() {
        try {
            generator.nextInt(-1);
            Assert.fail("MathIllegalArgumentException expected");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
        try {
            generator.nextInt(0);
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
    }

    @Test
    public void testNextLongDirect() {
        long q1 = Long.MAX_VALUE/4;
        long q2 = 2 *  q1;
        long q3 = 3 * q1;

        Frequency freq = new Frequency();
        long val = 0;
        int value = 0;
        for (int i=0; i<smallSampleSize; i++) {
            val = generator.nextLong();
            val = val < 0 ? -val : val;
            if (val < q1) {
                value = 0;
            } else if (val < q2) {
                value = 1;
            } else if (val < q3) {
                value = 2;
            } else {
                value = 3;
            }
            freq.addValue(value);
        }
        long[] observed = new long[4];
        for (int i=0; i<4; i++) {
            observed[i] = freq.getCount(i);
        }

        /* Use ChiSquare dist with df = 4-1 = 3, alpha = .001
         * Change to 11.34 for alpha = .01
         */
        Assert.assertTrue("chi-square test -- will fail about 1 in 1000 times",
                testStatistic.chiSquare(expected,observed) < 16.27);
    }

    @Test
    public void testNextBooleanDirect() {
        long halfSampleSize = smallSampleSize / 2;
        double[] expected = {halfSampleSize, halfSampleSize};
        long[] observed = new long[2];
        for (int i=0; i<smallSampleSize; i++) {
            if (generator.nextBoolean()) {
                observed[0]++;
            } else {
                observed[1]++;
            }
        }
        /* Use ChiSquare dist with df = 2-1 = 1, alpha = .001
         * Change to 6.635 for alpha = .01
         */
        Assert.assertTrue("chi-square test -- will fail about 1 in 1000 times",
                testStatistic.chiSquare(expected,observed) < 10.828);
    }

    @Test
    public void testNextFloatDirect() {
        Frequency freq = new Frequency();
        float val = 0;
        int value = 0;
        for (int i=0; i<smallSampleSize; i++) {
            val = generator.nextFloat();
            if (val < 0.25) {
                value = 0;
            } else if (val < 0.5) {
                value = 1;
            } else if (val < 0.75) {
                value = 2;
            } else {
                value = 3;
            }
            freq.addValue(value);
        }
        long[] observed = new long[4];
        for (int i=0; i<4; i++) {
            observed[i] = freq.getCount(i);
        }

        /* Use ChiSquare dist with df = 4-1 = 3, alpha = .001
         * Change to 11.34 for alpha = .01
         */
        Assert.assertTrue("chi-square test -- will fail about 1 in 1000 times",
                testStatistic.chiSquare(expected,observed) < 16.27);
    }

    @Test
    public void testDoubleDirect() {
        SummaryStatistics sample = new SummaryStatistics();
        final int N = 10000;
        for (int i = 0; i < N; ++i) {
            sample.addValue(generator.nextDouble());
        }
        Assert.assertEquals("Note: This test will fail randomly about 1 in 100 times.",
                0.5, sample.getMean(), FastMath.sqrt(N/12.0) * 2.576);
        Assert.assertEquals(1.0 / (2.0 * FastMath.sqrt(3.0)),
                     sample.getStandardDeviation(), 0.01);
    }

    @Test
    public void testFloatDirect() {
        SummaryStatistics sample = new SummaryStatistics();
        final int N = 1000;
        for (int i = 0; i < N; ++i) {
            sample.addValue(generator.nextFloat());
        }
        Assert.assertEquals("Note: This test will fail randomly about 1 in 100 times.",
                0.5, sample.getMean(), FastMath.sqrt(N/12.0) * 2.576);
        Assert.assertEquals(1.0 / (2.0 * FastMath.sqrt(3.0)),
                     sample.getStandardDeviation(), 0.01);
    }

    @Test(expected=MathIllegalArgumentException.class)
    public void testNextIntNeg() {
        generator.nextInt(-1);
    }

    @Test
    public void testNextInt2() {
        int walk = 0;
        final int N = 10000;
        for (int k = 0; k < N; ++k) {
           if (generator.nextInt() >= 0) {
               ++walk;
           } else {
               --walk;
           }
        }
        Assert.assertTrue("Walked too far astray: " + walk + "\nNote: This " +
                "test will fail randomly about 1 in 100 times.",
                FastMath.abs(walk) < FastMath.sqrt(N) * 2.576);
    }

    @Test
    public void testNextLong2() {
        int walk = 0;
        final int N = 1000;
        for (int k = 0; k < N; ++k) {
           if (generator.nextLong() >= 0) {
               ++walk;
           } else {
               --walk;
           }
        }
        Assert.assertTrue("Walked too far astray: " + walk + "\nNote: This " +
                "test will fail randomly about 1 in 100 times.",
                FastMath.abs(walk) < FastMath.sqrt(N) * 2.576);
    }

    @Test
    public void testNexBoolean2() {
        int walk = 0;
        final int N = 10000;
        for (int k = 0; k < N; ++k) {
           if (generator.nextBoolean()) {
               ++walk;
           } else {
               --walk;
           }
        }
        Assert.assertTrue("Walked too far astray: " + walk + "\nNote: This " +
                "test will fail randomly about 1 in 100 times.",
                FastMath.abs(walk) < FastMath.sqrt(N) * 2.576);
    }

    @Test
    public void testNexBytes() {
        long[] count = new long[256];
        byte[] bytes = new byte[10];
        double[] expected = new double[256];
        final int sampleSize = 100000;

        for (int i = 0; i < 256; i++) {
            expected[i] = (double) sampleSize / 265f;
        }

        for (int k = 0; k < sampleSize; ++k) {
           generator.nextBytes(bytes);
           for (byte b : bytes) {
               ++count[b + 128];
           }
        }

        TestUtils.assertChiSquareAccept(expected, count, 0.001);

    }

    @Test
    public void testSeeding() {
        // makeGenerator initializes with fixed seed
        RandomGenerator gen = makeGenerator();
        RandomGenerator gen1 = makeGenerator();
        checkSameSequence(gen, gen1);
        // reseed, but recreate the second one
        // verifies MATH-723
        gen.setSeed(100);
        gen1 = makeGenerator();
        gen1.setSeed(100);
        checkSameSequence(gen, gen1);
    }

    private void checkSameSequence(RandomGenerator gen1, RandomGenerator gen2) {
        final int len = 11;  // Needs to be an odd number to check MATH-723
        final double[][] values = new double[2][len];
        for (int i = 0; i < len; i++) {
            values[0][i] = gen1.nextDouble();
        }
        for (int i = 0; i < len; i++) {
            values[1][i] = gen2.nextDouble();
        }
        Assert.assertTrue(Arrays.equals(values[0], values[1]));
        for (int i = 0; i < len; i++) {
            values[0][i] = gen1.nextFloat();
        }
        for (int i = 0; i < len; i++) {
            values[1][i] = gen2.nextFloat();
        }
        Assert.assertTrue(Arrays.equals(values[0], values[1]));
        for (int i = 0; i < len; i++) {
            values[0][i] = gen1.nextInt();
        }
        for (int i = 0; i < len; i++) {
            values[1][i] = gen2.nextInt();
        }
        Assert.assertTrue(Arrays.equals(values[0], values[1]));
        for (int i = 0; i < len; i++) {
            values[0][i] = gen1.nextLong();
        }
        for (int i = 0; i < len; i++) {
            values[1][i] = gen2.nextLong();
        }
        Assert.assertTrue(Arrays.equals(values[0], values[1]));
        for (int i = 0; i < len; i++) {
            values[0][i] = gen1.nextInt(len);
        }
        for (int i = 0; i < len; i++) {
            values[1][i] = gen2.nextInt(len);
        }
        Assert.assertTrue(Arrays.equals(values[0], values[1]));
        for (int i = 0; i < len; i++) {
            values[0][i] = gen1.nextBoolean() ? 1 : 0;
        }
        for (int i = 0; i < len; i++) {
            values[1][i] = gen2.nextBoolean() ? 1 : 0;
        }
        Assert.assertTrue(Arrays.equals(values[0], values[1]));
        for (int i = 0; i < len; i++) {
            values[0][i] = gen1.nextGaussian();
        }
        for (int i = 0; i < len; i++) {
            values[1][i] = gen2.nextGaussian();
        }
        Assert.assertTrue(Arrays.equals(values[0], values[1]));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13976.java