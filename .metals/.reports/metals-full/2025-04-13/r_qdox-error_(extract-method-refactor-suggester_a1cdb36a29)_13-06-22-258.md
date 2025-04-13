error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13568.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13568.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13568.java
text:
```scala
r@@eturn new Median().withEstimationType(type).withNaNStrategy(strategy);

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
package org.apache.commons.math3.stat.descriptive.rank;

import static org.apache.commons.math3.stat.descriptive.rank.Percentile.EstimationType.LEGACY;
import static org.apache.commons.math3.stat.descriptive.rank.Percentile.EstimationType.R_1;
import static org.apache.commons.math3.stat.descriptive.rank.Percentile.EstimationType.R_2;
import static org.apache.commons.math3.stat.descriptive.rank.Percentile.EstimationType.R_3;
import static org.apache.commons.math3.stat.descriptive.rank.Percentile.EstimationType.R_4;
import static org.apache.commons.math3.stat.descriptive.rank.Percentile.EstimationType.R_5;
import static org.apache.commons.math3.stat.descriptive.rank.Percentile.EstimationType.R_6;
import static org.apache.commons.math3.stat.descriptive.rank.Percentile.EstimationType.R_7;
import static org.apache.commons.math3.stat.descriptive.rank.Percentile.EstimationType.R_8;
import static org.apache.commons.math3.stat.descriptive.rank.Percentile.EstimationType.R_9;

import org.apache.commons.math3.stat.descriptive.UnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.UnivariateStatisticAbstractTest;
import org.apache.commons.math3.stat.descriptive.rank.Percentile.EstimationType;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the {@link UnivariateStatistic} class.
 * @version $Id$
 */
public class MedianTest extends UnivariateStatisticAbstractTest{

    protected Median stat;

    /**
     * {@link  org.apache.commons.math3.stat.descriptive.rank.Percentile.EstimationType type}
     *  to be used while calling
     * {@link #getUnivariateStatistic()}
     */
    protected EstimationType estimationType = LEGACY;

    /**
     * {@inheritDoc}
     */
    @Override
    public UnivariateStatistic getUnivariateStatistic() {
        return new Median();
    }

    private Median getTestMedian(EstimationType type) {
        NaNStrategy strategy = (type == LEGACY) ? NaNStrategy.FIXED : NaNStrategy.REMOVED;
        return new Median().withEstimationtype(type).withNaNStrategy(strategy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double expectedValue() {
        return this.median;
    }

    @Before
    public void before() {
        estimationType=LEGACY;
    }

    @Test
    public void testAllTechniquesSingleton() {
        double[] singletonArray = new double[] { 1d };
        for (EstimationType e : EstimationType.values()) {
            UnivariateStatistic percentile = getTestMedian(e);
            Assert.assertEquals(1d, percentile.evaluate(singletonArray), 0);
            Assert.assertEquals(1d, percentile.evaluate(singletonArray, 0, 1),
                    0);
            Assert.assertEquals(1d,
                    new Median().evaluate(singletonArray, 0, 1, 5), 0);
            Assert.assertEquals(1d,
                    new Median().evaluate(singletonArray, 0, 1, 100), 0);
            Assert.assertTrue(Double.isNaN(percentile.evaluate(singletonArray,
                    0, 0)));
        }
    }
    @Test
    public void testAllTechniquesMedian() {
        double[] d = new double[] { 1, 3, 2, 4 };
        testAssertMappedValues(d, new Object[][] { { LEGACY, 2.5d },
            { R_1, 2d }, { R_2, 2.5d }, { R_3, 2d }, { R_4, 2d }, { R_5, 2.5 },
            { R_6, 2.5 },{ R_7, 2.5 },{ R_8, 2.5 }, { R_9 , 2.5 } },  1.0e-05);

    }


    /**
     * Simple test assertion utility method
     *
     * @param d input data
     * @param map of expected result against a {@link EstimationType}
     * @param tolerance the tolerance of difference allowed
     */
    protected void testAssertMappedValues(double[] d, Object[][] map,
            Double tolerance) {
        for (Object[] o : map) {
            EstimationType e = (EstimationType) o[0];
            double expected = (Double) o[1];
            double result = getTestMedian(e).evaluate(d);
            Assert.assertEquals("expected[" + e + "] = " + expected +
                    " but was = " + result, expected, result, tolerance);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13568.java