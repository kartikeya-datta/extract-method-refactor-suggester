error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3709.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3709.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3709.java
text:
```scala
public v@@oid setUp() {

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

package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link TriangularDistribution}. See class javadoc for
 * {@link RealDistributionAbstractTest} for further details.
 */
public class TriangularDistributionTest extends RealDistributionAbstractTest {

    // --- Override tolerance -------------------------------------------------

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setTolerance(1e-4);
    }

    //--- Implementations for abstract methods --------------------------------

    /**
     * Creates the default triangular distribution instance to use in tests.
     */
    @Override
    public TriangularDistribution makeDistribution() {
        // Left side 5 wide, right side 10 wide.
        return new TriangularDistribution(-3, 2, 12);
    }

    /**
     * Creates the default cumulative probability distribution test input
     * values.
     */
    @Override
    public double[] makeCumulativeTestPoints() {
        return new double[] { -3.0001,                 // below lower limit
                              -3.0,                    // at lower limit
                              -2.0, -1.0, 0.0, 1.0,    // on lower side
                              2.0,                     // at mode
                              3.0, 4.0, 10.0, 11.0,    // on upper side
                              12.0,                    // at upper limit
                              12.0001                  // above upper limit
                            };
    }

    /**
     * Creates the default cumulative probability density test expected values.
     */
    @Override
    public double[] makeCumulativeTestValues() {
        // Top at 2 / (b - a) = 2 / (12 - -3) = 2 / 15 = 7.5
        // Area left  = 7.5 * 5  * 0.5 = 18.75 (1/3 of the total area)
        // Area right = 7.5 * 10 * 0.5 = 37.5  (2/3 of the total area)
        // Area total = 18.75 + 37.5 = 56.25
        // Derivative left side = 7.5 / 5 = 1.5
        // Derivative right side = -7.5 / 10 = -0.75
        double third = 1 / 3.0;
        double left = 18.75;
        double area = 56.25;
        return new double[] { 0.0,
                              0.0,
                              0.75 / area, 3 / area, 6.75 / area, 12 / area,
                              third,
                              (left + 7.125) / area, (left + 13.5) / area,
                              (left + 36) / area, (left + 37.125) / area,
                              1.0,
                              1.0
                            };
    }

    /**
     * Creates the default inverse cumulative probability distribution test
     * input values.
     */
    @Override
    public double[] makeInverseCumulativeTestPoints() {
        // Exclude the points outside the limits, as they have cumulative
        // probability of zero and one, meaning the inverse returns the
        // limits and not the points outside the limits.
        double[] points = makeCumulativeTestValues();
        double[] points2 = new double[points.length-2];
        System.arraycopy(points, 1, points2, 0, points2.length);
        return points2;
        //return Arrays.copyOfRange(points, 1, points.length - 1);
    }

    /**
     * Creates the default inverse cumulative probability density test expected
     * values.
     */
    @Override
    public double[] makeInverseCumulativeTestValues() {
        // Exclude the points outside the limits, as they have cumulative
        // probability of zero and one, meaning the inverse returns the
        // limits and not the points outside the limits.
        double[] points = makeCumulativeTestPoints();
        double[] points2 = new double[points.length-2];
        System.arraycopy(points, 1, points2, 0, points2.length);
        return points2;
        //return Arrays.copyOfRange(points, 1, points.length - 1);
    }

    /** Creates the default probability density test expected values. */
    @Override
    public double[] makeDensityTestValues() {
        return new double[] { 0,
                              0,
                              2 / 75.0, 4 / 75.0, 6 / 75.0, 8 / 75.0,
                              10 / 75.0,
                              9 / 75.0, 8 / 75.0, 2 / 75.0, 1 / 75.0,
                              0,
                              0
                            };
    }

    //--- Additional test cases -----------------------------------------------

    /** Test lower bound getter. */
    @Test
    public void testGetLowerBound() {
        TriangularDistribution distribution = makeDistribution();
        Assert.assertEquals(-3.0, distribution.getSupportLowerBound(), 0);
    }

    /** Test upper bound getter. */
    @Test
    public void testGetUpperBound() {
        TriangularDistribution distribution = makeDistribution();
        Assert.assertEquals(12.0, distribution.getSupportUpperBound(), 0);
    }

    /** Test pre-condition for equal lower/upper limit. */
    @Test(expected=NumberIsTooLargeException.class)
    public void testPreconditions1() {
        new TriangularDistribution(0, 0, 0);
    }

    /** Test pre-condition for lower limit larger than upper limit. */
    @Test(expected=NumberIsTooLargeException.class)
    public void testPreconditions2() {
        new TriangularDistribution(1, 1, 0);
    }

    /** Test pre-condition for mode larger than upper limit. */
    @Test(expected=NumberIsTooLargeException.class)
    public void testPreconditions3() {
        new TriangularDistribution(0, 2, 1);
    }

    /** Test pre-condition for mode smaller than lower limit. */
    @Test(expected=NumberIsTooSmallException.class)
    public void testPreconditions4() {
        new TriangularDistribution(2, 1, 3);
    }

    /** Test mean/variance. */
    @Test
    public void testMeanVariance() {
        TriangularDistribution dist;

        dist = new TriangularDistribution(0, 0.5, 1.0);
        Assert.assertEquals(dist.getNumericalMean(), 0.5, 0);
        Assert.assertEquals(dist.getNumericalVariance(), 1 / 24.0, 0);

        dist = new TriangularDistribution(0, 1, 1);
        Assert.assertEquals(dist.getNumericalMean(), 2 / 3.0, 0);
        Assert.assertEquals(dist.getNumericalVariance(), 1 / 18.0, 0);

        dist = new TriangularDistribution(-3, 2, 12);
        Assert.assertEquals(dist.getNumericalMean(), 3 + (2 / 3.0), 0);
        Assert.assertEquals(dist.getNumericalVariance(), 175 / 18.0, 0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3709.java