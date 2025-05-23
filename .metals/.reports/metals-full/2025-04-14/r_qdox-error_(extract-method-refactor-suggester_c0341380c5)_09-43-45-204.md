error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8662.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8662.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8662.java
text:
```scala
public v@@oid testMoments() {

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
package org.apache.commons.math.distribution;

import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for FDistribution.
 * Extends ContinuousDistributionAbstractTest.  See class javadoc for
 * ContinuousDistributionAbstractTest for details.
 *
 * @version $Id$
 */
public class FDistributionTest extends ContinuousDistributionAbstractTest {

    //-------------- Implementations for abstract methods -----------------------

    /** Creates the default continuous distribution instance to use in tests. */
    @Override
    public FDistribution makeDistribution() {
        return new FDistributionImpl(5.0, 6.0);
    }

    /** Creates the default cumulative probability distribution test input values */
    @Override
    public double[] makeCumulativeTestPoints() {
        // quantiles computed using R version 2.9.2
        return new double[] {0.0346808448626, 0.0937009113303, 0.143313661184, 0.202008445998, 0.293728320107,
                20.8026639595, 8.74589525602, 5.98756512605, 4.38737418741, 3.10751166664};
    }

    /** Creates the default cumulative probability density test expected values */
    @Override
    public double[] makeCumulativeTestValues() {
        return new double[] {0.001, 0.01, 0.025, 0.05, 0.1, 0.999, 0.990, 0.975, 0.950, 0.900};
    }

    /** Creates the default probability density test expected values */
    @Override
    public double[] makeDensityTestValues() {
        return new double[] {0.0689156576706, 0.236735653193, 0.364074131941, 0.481570789649, 0.595880479994,
                0.000133443915657, 0.00286681303403, 0.00969192007502, 0.0242883861471, 0.0605491314658};
    }

    // --------------------- Override tolerance  --------------
    @Override
    public void setUp() throws Exception {
        super.setUp();
        setTolerance(1e-9);
    }

    //---------------------------- Additional test cases -------------------------

    @Test
    public void testCumulativeProbabilityExtremes() throws Exception {
        setCumulativeTestPoints(new double[] {-2, 0});
        setCumulativeTestValues(new double[] {0, 0});
        verifyCumulativeProbabilities();
    }

    @Test
    public void testInverseCumulativeProbabilityExtremes() throws Exception {
        setInverseCumulativeTestPoints(new double[] {0, 1});
        setInverseCumulativeTestValues(new double[] {0, Double.POSITIVE_INFINITY});
        verifyInverseCumulativeProbabilities();
    }

    @Test
    public void testDfAccessors() {
        FDistribution dist = (FDistribution) getDistribution();
        Assert.assertEquals(5d, dist.getNumeratorDegreesOfFreedom(), Double.MIN_VALUE);
        Assert.assertEquals(6d, dist.getDenominatorDegreesOfFreedom(), Double.MIN_VALUE);
    }

    @Test
    public void testPreconditions() {
        try {
            new FDistributionImpl(0, 1);
            Assert.fail("Expecting NotStrictlyPositiveException for df = 0");
        } catch (NotStrictlyPositiveException ex) {
            // Expected.
        }
        try {
            new FDistributionImpl(1, 0);
            Assert.fail("Expecting NotStrictlyPositiveException for df = 0");
        } catch (NotStrictlyPositiveException ex) {
            // Expected.
        }
    }

    @Test
    public void testLargeDegreesOfFreedom() throws Exception {
        FDistributionImpl fd = new FDistributionImpl(100000, 100000);
        double p = fd.cumulativeProbability(.999);
        double x = fd.inverseCumulativeProbability(p);
        Assert.assertEquals(.999, x, 1.0e-5);
    }

    @Test
    public void testSmallDegreesOfFreedom() throws Exception {
        FDistributionImpl fd = new FDistributionImpl(1, 1);
        double p = fd.cumulativeProbability(0.975);
        double x = fd.inverseCumulativeProbability(p);
        Assert.assertEquals(0.975, x, 1.0e-5);

        fd = new FDistributionImpl(1, 2);
        p = fd.cumulativeProbability(0.975);
        x = fd.inverseCumulativeProbability(p);
        Assert.assertEquals(0.975, x, 1.0e-5);
    }

    @Test
    public void testMomonts() {
        final double tol = 1e-9;
        FDistribution dist;
        
        dist = new FDistributionImpl(1, 2);
        Assert.assertTrue(Double.isNaN(dist.getNumericalMean()));
        Assert.assertTrue(Double.isNaN(dist.getNumericalVariance()));
        
        dist = new FDistributionImpl(1, 3);
        Assert.assertEquals(dist.getNumericalMean(), 3d / (3d - 2d), tol);
        Assert.assertTrue(Double.isNaN(dist.getNumericalVariance()));
        
        dist = new FDistributionImpl(1, 5);
        Assert.assertEquals(dist.getNumericalMean(), 5d / (5d - 2d), tol);
        Assert.assertEquals(dist.getNumericalVariance(), (2d * 5d * 5d * 4d) / 9d, tol);        
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8662.java