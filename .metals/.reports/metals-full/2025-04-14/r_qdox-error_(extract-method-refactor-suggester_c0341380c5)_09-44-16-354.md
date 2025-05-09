error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4193.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4193.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4193.java
text:
```scala
final i@@nt uniDimIndex = iter.next().intValue();

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

package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class MultidimensionalCounterTest {
    @Test
    public void testPreconditions() {
        MultidimensionalCounter c;

        try {
            c = new MultidimensionalCounter(0, 1);
            Assert.fail("NotStrictlyPositiveException expected");
        } catch (NotStrictlyPositiveException e) {
            // Expected.
        }
        try {
            c = new MultidimensionalCounter(2, 0);
            Assert.fail("NotStrictlyPositiveException expected");
        } catch (NotStrictlyPositiveException e) {
            // Expected.
        }
        try {
            c = new MultidimensionalCounter(-1, 1);
            Assert.fail("NotStrictlyPositiveException expected");
        } catch (NotStrictlyPositiveException e) {
            // Expected.
        }

        c = new MultidimensionalCounter(2, 3);
        try {
            c.getCount(1, 1, 1);
            Assert.fail("DimensionMismatchException expected");
        } catch (DimensionMismatchException e) {
            // Expected.
        }
        try {
            c.getCount(3, 1);
            Assert.fail("OutOfRangeException expected");
        } catch (OutOfRangeException e) {
            // Expected.
        }
        try {
            c.getCount(0, -1);
            Assert.fail("OutOfRangeException expected");
        } catch (OutOfRangeException e) {
            // Expected.
        }
        try {
            c.getCounts(-1);
            Assert.fail("OutOfRangeException expected");
        } catch (OutOfRangeException e) {
            // Expected.
        }
        try {
            c.getCounts(6);
            Assert.fail("OutOfRangeException expected");
        } catch (OutOfRangeException e) {
            // Expected.
        }
    }

    @Test
    public void testIteratorPreconditions() {
        MultidimensionalCounter.Iterator iter = (new MultidimensionalCounter(2, 3)).iterator();
        try {
            iter.getCount(-1);
            Assert.fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // Expected.
        }
        try {
            iter.getCount(2);
            Assert.fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // Expected.
        }
    }

    @Test
    public void testMulti2UniConversion() {
        final MultidimensionalCounter c = new MultidimensionalCounter(2, 4, 5);
        Assert.assertEquals(c.getCount(1, 2, 3), 33);
    }

    @Test
    public void testAccessors() {
        final int[] originalSize = new int[] {2, 6, 5};
        final MultidimensionalCounter c = new MultidimensionalCounter(originalSize);
        final int nDim = c.getDimension();
        Assert.assertEquals(nDim, originalSize.length);

        final int[] size = c.getSizes();
        for (int i = 0; i < nDim; i++) {
            Assert.assertEquals(originalSize[i], size[i]);
        }
    }

    @Test
    public void testIterationConsistency() {
        final MultidimensionalCounter c = new MultidimensionalCounter(2, 3, 4);
        final int[][] expected = new int[][] {
            { 0, 0, 0 },
            { 0, 0, 1 },
            { 0, 0, 2 },
            { 0, 0, 3 },
            { 0, 1, 0 },
            { 0, 1, 1 },
            { 0, 1, 2 },
            { 0, 1, 3 },
            { 0, 2, 0 },
            { 0, 2, 1 },
            { 0, 2, 2 },
            { 0, 2, 3 },
            { 1, 0, 0 },
            { 1, 0, 1 },
            { 1, 0, 2 },
            { 1, 0, 3 },
            { 1, 1, 0 },
            { 1, 1, 1 },
            { 1, 1, 2 },
            { 1, 1, 3 },
            { 1, 2, 0 },
            { 1, 2, 1 },
            { 1, 2, 2 },
            { 1, 2, 3 }
        };

        final int totalSize = c.getSize();
        final int nDim = c.getDimension();
        final MultidimensionalCounter.Iterator iter = c.iterator();
        for (int i = 0; i < totalSize; i++) {
            if (!iter.hasNext()) {
                Assert.fail("Too short");
            }
            final int uniDimIndex = iter.next();
            Assert.assertEquals("Wrong iteration at " + i, i, uniDimIndex);

            for (int dimIndex = 0; dimIndex < nDim; dimIndex++) {
                Assert.assertEquals("Wrong multidimensional index for [" + i + "][" + dimIndex + "]",
                                    expected[i][dimIndex], iter.getCount(dimIndex));
            }

            Assert.assertEquals("Wrong unidimensional index for [" + i + "]",
                                c.getCount(expected[i]), uniDimIndex);

            final int[] indices = c.getCounts(uniDimIndex);
            for (int dimIndex = 0; dimIndex < nDim; dimIndex++) {
                Assert.assertEquals("Wrong multidimensional index for [" + i + "][" + dimIndex + "]",
                                    expected[i][dimIndex], indices[dimIndex]);
            }
        }

        if (iter.hasNext()) {
            Assert.fail("Too long");
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4193.java