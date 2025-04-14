error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12579.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12579.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12579.java
text:
```scala
r@@eturn new WeibullDistributionImpl(1.2, 2.1);

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

/**
 * Test cases for WeibullDistribution.
 * Extends ContinuousDistributionAbstractTest.  See class javadoc for
 * ContinuousDistributionAbstractTest for details.
 * 
 * @version $Revision: 1.8 $ $Date: 2004-07-24 16:41:37 -0500 (Sat, 24 Jul 2004) $
 */
public class WeibullDistributionTest extends ContinuousDistributionAbstractTest  {
    
    /**
     * Constructor for CauchyDistributionTest.
     * @param arg0
     */
    public WeibullDistributionTest(String arg0) {
        super(arg0);
    }
    
    //-------------- Implementations for abstract methods -----------------------
    
    /** Creates the default continuous distribution instance to use in tests. */
    public ContinuousDistribution makeDistribution() {
        return DistributionFactory.newInstance().createWeibullDistribution(1.2, 2.1);
    }   
    
    /** Creates the default cumulative probability distribution test input values */
    public double[] makeCumulativeTestPoints() {
        // quantiles computed using Mathematica 
        return new double[] {0.00664355181d, 0.04543282833d, 0.09811627374d,
                0.1767135246d, 0.3219468654d, 4.207902826d, 5.23968437d,
                6.232056007d, 7.497630467d, 10.51154969d};
    }
    
    /** Creates the default cumulative probability density test expected values */
    public double[] makeCumulativeTestValues() {
        return new double[] {0.001d, 0.01d, 0.025d, 0.05d, 0.1d, 0.900d, 0.950d,
                0.975d, 0.990d, 0.999d};
    }
    
    //---------------------------- Additional test cases -------------------------
    
    public void testInverseCumulativeProbabilityExtremes() throws Exception {
        setInverseCumulativeTestPoints(new double[] {0.0, 1.0});
        setInverseCumulativeTestValues(
                new double[] {0.0, Double.POSITIVE_INFINITY});
        verifyInverseCumulativeProbabilities();
    }
    
    public void testAlpha() {
        WeibullDistribution distribution = (WeibullDistribution) getDistribution();
        double expected = Math.random();
        distribution.setShape(expected);
        assertEquals(expected, distribution.getShape(), 0.0);
    }
    
    public void testBeta() {
        WeibullDistribution distribution = (WeibullDistribution) getDistribution();
        double expected = Math.random();
        distribution.setScale(expected);
        assertEquals(expected, distribution.getScale(), 0.0);
    }
    
    public void testSetAlpha() {
        WeibullDistribution distribution = (WeibullDistribution) getDistribution();
        try {
            distribution.setShape(0.0);
            fail("Can not have 0.0 alpha.");
        } catch (IllegalArgumentException ex) {
            // success
        }
        
        try {
            distribution.setShape(-1.0);
            fail("Can not have negative alpha.");
        } catch (IllegalArgumentException ex) {
            // success
        }
    }
    
    public void testSetBeta() {
        WeibullDistribution distribution = (WeibullDistribution) getDistribution();
        try {
            distribution.setScale(0.0);
            fail("Can not have 0.0 beta.");
        } catch (IllegalArgumentException ex) {
            // success
        }
        
        try {
            distribution.setScale(-1.0);
            fail("Can not have negative beta.");
        } catch (IllegalArgumentException ex) {
            // success
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12579.java