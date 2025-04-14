error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3708.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3708.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3708.java
text:
```scala
public v@@oid testCumulativeDensityFunction() {

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

import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link KolmogorovSmirnovDistribution}.
 *
 * @version $Id$
 */
public class KolmogorovSmirnovDistributionTest {
    
    private static final double TOLERANCE = 10e-10;

    @Test
    public void testCumulativeDensityFunction() throws Exception {
        
        KolmogorovSmirnovDistribution dist;
        
        /* The code below is generated using the R-script located in
         * /src/test/R/KolmogorovSmirnovDistributionTestCases.R
         */
        
        /* R version 2.11.1 (2010-05-31) */


        /* formatC(.C("pkolmogorov2x", p = as.double(0.005), n = as.integer(200), PACKAGE = "stats")$p, 40) gives
         * 4.907829957616471622388047046469198862537e-86
         */
        dist = new KolmogorovSmirnovDistribution(200);
        Assert.assertEquals(4.907829957616471622388047046469198862537e-86, dist.cdf(0.005, false), TOLERANCE);

        /* formatC(.C("pkolmogorov2x", p = as.double(0.02), n = as.integer(200), PACKAGE = "stats")$p, 40) gives
         * 5.151982014280041957199687829849210629618e-06
         */
        dist = new KolmogorovSmirnovDistribution(200);
        Assert.assertEquals(5.151982014280041957199687829849210629618e-06, dist.cdf(0.02, false), TOLERANCE);

        /* formatC(.C("pkolmogorov2x", p = as.double(0.031111), n = as.integer(200), PACKAGE = "stats")$p, 40) gives
         * 0.01291614648162886340443389343590752105229
         */
        dist = new KolmogorovSmirnovDistribution(200);
        Assert.assertEquals(0.01291614648162886340443389343590752105229, dist.cdf(0.031111, false), TOLERANCE);

        /* formatC(.C("pkolmogorov2x", p = as.double(0.04), n = as.integer(200), PACKAGE = "stats")$p, 40) gives
         * 0.1067137011362679355208626930107129737735
         */
        dist = new KolmogorovSmirnovDistribution(200);
        Assert.assertEquals(0.1067137011362679355208626930107129737735, dist.cdf(0.04, false), TOLERANCE);

        /* formatC(.C("pkolmogorov2x", p = as.double(0.005), n = as.integer(341), PACKAGE = "stats")$p, 40) gives
         * 1.914734701559404553985102395145063418825e-53
         */
        dist = new KolmogorovSmirnovDistribution(341);
        Assert.assertEquals(1.914734701559404553985102395145063418825e-53, dist.cdf(0.005, false), TOLERANCE);

        /* formatC(.C("pkolmogorov2x", p = as.double(0.02), n = as.integer(341), PACKAGE = "stats")$p, 40) gives
         * 0.001171328985781981343872182321774744195864
         */
        dist = new KolmogorovSmirnovDistribution(341);
        Assert.assertEquals(0.001171328985781981343872182321774744195864, dist.cdf(0.02, false), TOLERANCE);

        /* formatC(.C("pkolmogorov2x", p = as.double(0.031111), n = as.integer(341), PACKAGE = "stats")$p, 40) gives
         * 0.1142955196267499418105728636874118819833
         */
        dist = new KolmogorovSmirnovDistribution(341);
        Assert.assertEquals(0.1142955196267499418105728636874118819833, dist.cdf(0.031111, false), TOLERANCE);

        /* formatC(.C("pkolmogorov2x", p = as.double(0.04), n = as.integer(341), PACKAGE = "stats")$p, 40) gives
         * 0.3685529520496805266915885113121476024389
         */
        dist = new KolmogorovSmirnovDistribution(341);
        Assert.assertEquals(0.3685529520496805266915885113121476024389, dist.cdf(0.04, false), TOLERANCE);

        /* formatC(.C("pkolmogorov2x", p = as.double(0.005), n = as.integer(389), PACKAGE = "stats")$p, 40) gives
         * 1.810657144595055888918455512707637574637e-47
         */
        dist = new KolmogorovSmirnovDistribution(389);
        Assert.assertEquals(1.810657144595055888918455512707637574637e-47, dist.cdf(0.005, false), TOLERANCE);

        /* formatC(.C("pkolmogorov2x", p = as.double(0.02), n = as.integer(389), PACKAGE = "stats")$p, 40) gives
         * 0.003068542559702356568168690742481885536108
         */
        dist = new KolmogorovSmirnovDistribution(389);
        Assert.assertEquals(0.003068542559702356568168690742481885536108, dist.cdf(0.02, false), TOLERANCE);

        /* formatC(.C("pkolmogorov2x", p = as.double(0.031111), n = as.integer(389), PACKAGE = "stats")$p, 40) gives
         * 0.1658291700122746237244797384846606291831
         */
        dist = new KolmogorovSmirnovDistribution(389);
        Assert.assertEquals(0.1658291700122746237244797384846606291831, dist.cdf(0.031111, false), TOLERANCE);

        /* formatC(.C("pkolmogorov2x", p = as.double(0.04), n = as.integer(389), PACKAGE = "stats")$p, 40) gives
         * 0.4513143712128902529379104180407011881471
         */
        dist = new KolmogorovSmirnovDistribution(389);
        Assert.assertEquals(0.4513143712128902529379104180407011881471, dist.cdf(0.04, false), TOLERANCE);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3708.java