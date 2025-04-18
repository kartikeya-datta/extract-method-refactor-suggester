error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8658.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8658.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8658.java
text:
```scala
public v@@oid testMoments() {

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
package org.apache.commons.math.distribution;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for BinomialDistribution. Extends IntegerDistributionAbstractTest.
 * See class javadoc for IntegerDistributionAbstractTest for details.
 *
 * @version $Id$
 *          2009) $
 */
public class BinomialDistributionTest extends IntegerDistributionAbstractTest {

    // -------------- Implementations for abstract methods
    // -----------------------

    /** Creates the default discrete distribution instance to use in tests. */
    @Override
    public IntegerDistribution makeDistribution() {
        return new BinomialDistributionImpl(10, 0.70);
    }

    /** Creates the default probability density test input values */
    @Override
    public int[] makeDensityTestPoints() {
        return new int[] { -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
    }

    /** Creates the default probability density test expected values */
    @Override
    public double[] makeDensityTestValues() {
        return new double[] { 0d, 0.0000059049d, 0.000137781d, 0.0014467d,
                0.00900169d, 0.0367569d, 0.102919d, 0.200121d, 0.266828d,
                0.233474d, 0.121061d, 0.0282475d, 0d };
    }

    /** Creates the default cumulative probability density test input values */
    @Override
    public int[] makeCumulativeTestPoints() {
        return makeDensityTestPoints();
    }

    /** Creates the default cumulative probability density test expected values */
    @Override
    public double[] makeCumulativeTestValues() {
        return new double[] { 0d, 0.0000d, 0.0001d, 0.0016d, 0.0106d, 0.0473d,
                0.1503d, 0.3504d, 0.6172d, 0.8507d, 0.9718d, 1d, 1d };
    }

    /** Creates the default inverse cumulative probability test input values */
    @Override
    public double[] makeInverseCumulativeTestPoints() {
        return new double[] { 0, 0.001d, 0.010d, 0.025d, 0.050d, 0.100d,
                0.999d, 0.990d, 0.975d, 0.950d, 0.900d, 1 };
    }

    /**
     * Creates the default inverse cumulative probability density test expected
     * values
     */
    @Override
    public int[] makeInverseCumulativeTestValues() {
        return new int[] { -1, 1, 2, 3, 4, 4, 9, 9, 9, 8, 8, Integer.MAX_VALUE };
    }

    // ----------------- Additional test cases ---------------------------------

    /** Test degenerate case p = 0 */
    @Test
    public void testDegenerate0() throws Exception {
        setDistribution(new BinomialDistributionImpl(5, 0.0d));
        setCumulativeTestPoints(new int[] { -1, 0, 1, 5, 10 });
        setCumulativeTestValues(new double[] { 0d, 1d, 1d, 1d, 1d });
        setDensityTestPoints(new int[] { -1, 0, 1, 10, 11 });
        setDensityTestValues(new double[] { 0d, 1d, 0d, 0d, 0d });
        setInverseCumulativeTestPoints(new double[] { 0.1d, 0.5d });
        setInverseCumulativeTestValues(new int[] { -1, -1 });
        verifyDensities();
        verifyCumulativeProbabilities();
        verifyInverseCumulativeProbabilities();
    }

    /** Test degenerate case p = 1 */
    @Test
    public void testDegenerate1() throws Exception {
        setDistribution(new BinomialDistributionImpl(5, 1.0d));
        setCumulativeTestPoints(new int[] { -1, 0, 1, 2, 5, 10 });
        setCumulativeTestValues(new double[] { 0d, 0d, 0d, 0d, 1d, 1d });
        setDensityTestPoints(new int[] { -1, 0, 1, 2, 5, 10 });
        setDensityTestValues(new double[] { 0d, 0d, 0d, 0d, 1d, 0d });
        setInverseCumulativeTestPoints(new double[] { 0.1d, 0.5d });
        setInverseCumulativeTestValues(new int[] { 4, 4 });
        verifyDensities();
        verifyCumulativeProbabilities();
        verifyInverseCumulativeProbabilities();
    }

    @Test
    public void testMomonts() {
        final double tol = 1e-9;
        BinomialDistribution dist;
        
        dist = new BinomialDistributionImpl(10, 0.5);
        Assert.assertEquals(dist.getNumericalMean(), 10d * 0.5d, tol);
        Assert.assertEquals(dist.getNumericalVariance(), 10d * 0.5d * 0.5d, tol); 
        
        dist = new BinomialDistributionImpl(30, 0.3);
        Assert.assertEquals(dist.getNumericalMean(), 30d * 0.3d, tol);
        Assert.assertEquals(dist.getNumericalVariance(), 30d * 0.3d * (1d - 0.3d), tol);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8658.java